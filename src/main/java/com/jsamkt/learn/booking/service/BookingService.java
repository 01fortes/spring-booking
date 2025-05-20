package com.jsamkt.learn.booking.service;

import com.jsamkt.learn.booking.dto.*;
import com.jsamkt.learn.booking.model.Booking;
import com.jsamkt.learn.booking.model.Room;
import com.jsamkt.learn.booking.repository.BookingRepository;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    public static final Class<BookingDataDto> bookInputType = BookingDataDto.class;
    public static final String bookDescription = "Creates a new room booking with the following required parameters:\n" +
            "- userName: Guest's full name (String)\n" +
            "- userPhone: Contact phone number (String)\n" +
            "- startDateTime: Check-in date and time (LocalDateTime)\n" +
            "- endDateTime: Check-out date and time (LocalDateTime)\n" +
            "- roomId: Unique room identifier (UUID)\n" +
            "The function performs validation checks for date conflicts and room availability before confirming the booking.";

    public static final Class<BookingCancelDto> cancelInputType = BookingCancelDto.class;
    public static final String cancelDescription = "Cancels an existing booking using the booking identifier (UUID).\n" +
            "Verifies user authorization and booking existence before processing the cancellation.";

    public static final Class<GetBookingsDto> getBookingsInputType = GetBookingsDto.class;
    public static final String getBookingsDescription = "Retrieves all bookings within a specified date range.\n" +
            "Required parameters:\n" +
            "- startDateTime: Period start (LocalDateTime)\n" +
            "- endDateTime: Period end (LocalDateTime)\n" +
            "Returns a list of bookings with full details including booking IDs that can be used for modifications or cancellations.";

    public static final Class<GetRoomAvailableDatesRequestsDto> getRoomAvailableDatesInputType = GetRoomAvailableDatesRequestsDto.class;
    public static final String getRoomAvailableDatesDescription = "Retrieves available time slots for a specific room within a given period.\n" +
            "Required parameters:\n" +
            "- roomId: Unique room identifier (UUID)\n" +
            "- startSearchPeriod: Beginning of search period (LocalDateTime)\n" +
            "- endDatePeriod: End of search period (LocalDateTime)\n" +
            "The function analyzes existing bookings and returns:\n" +
            "- List of available time periods that can be used for booking\n" +
            "- Each period includes precise start and end times\n" +
            "- Automatically excludes any overlapping bookings\n" +
            "This information is essential for showing users when they can book the room and preventing double bookings.";

    private final BookingRepository repository;
    private final RoomService roomService;

    public BookingService(BookingRepository repository, RoomService roomService) {
        this.repository = repository;
        this.roomService = roomService;
    }

    public BookingResult book(BookingDataDto dto, ToolContext ctx) {
        String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
        if (userId == null) {
            return new BookingResult(false, "User not found");
        }
        var validationResult = validate(dto);
        if (validationResult != null) {
            return validationResult;
        }

        Booking booking = new Booking();
        if (booking.getId() == null) {
            booking.setId(UUID.randomUUID());
        }

        booking.setUserId(userId);
        booking.setUserName(dto.getUserName());
        booking.setUserPhone(dto.getUserPhone());
        booking.setRoom(roomService.getById(UUID.fromString(dto.getRoomId())));
        booking.setStartDate(dto.getStartDateTime());
        booking.setEndDate(dto.getEndDateTime());

        repository.save(booking);
        return new BookingResult(true, "Success");
    }

    public BookingResult cancel(BookingCancelDto dto, ToolContext ctx) {
        String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
        if (userId == null) {
            return new BookingResult(false, "User not found");
        }
        var validationResult = validate(dto, userId);
        if (validationResult != null) {
            return validationResult;
        }

        repository.deleteById(UUID.fromString(dto.getBookingId()));
        return new BookingResult(true, "Success");
    }

    public GetBookingsResult getBookings(GetBookingsDto dto, ToolContext ctx) {
        String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
        if (userId == null) {
            return new GetBookingsResult(false, "User not found", null);
        }
        var validationResult = validate(dto);
        if (validationResult != null) {
            return validationResult;
        }

        var bookings = repository.findByUserIdAndStartDateIsAfterAndEndDateIsBefore(userId, dto.getStartDateTime(), dto.getEndDateTime());
        if (bookings.isEmpty()) {
            return new GetBookingsResult(false, "User has not bookings for this period", null);
        }

        return new GetBookingsResult(true, "Success", bookings);
    }

    public GetRoomAvailableDatesResponseDto getRoomAvailableDates(GetRoomAvailableDatesRequestsDto dto, ToolContext ctx) {
        try {
            String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
            if (userId == null) {
                return new GetRoomAvailableDatesResponseDto(false, "User not found", null);
            }

            var validationResult = validate(dto);
            if (validationResult != null) {
                return validationResult;
            }

            var room = roomService.getById(UUID.fromString(dto.getRoomId()));
            var bookings = repository.findByRoomAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(room, dto.getStartSearchPeriod(), dto.getEndDatePeriod());
            List<GetRoomAvailableDatesResponseDto.AvailablePeriod> unavailableIntervals = bookings.stream()
                    .map(b -> new GetRoomAvailableDatesResponseDto.AvailablePeriod(b.getStartDate(), b.getEndDate()))
                    .sorted(Comparator.comparing(GetRoomAvailableDatesResponseDto.AvailablePeriod::getStartDate))
                    .toList();

            if (unavailableIntervals.isEmpty()) {
                return new GetRoomAvailableDatesResponseDto(true, "Success", List.of(
                        new GetRoomAvailableDatesResponseDto.AvailablePeriod(dto.getStartSearchPeriod(), dto.getEndDatePeriod())
                ));
            }

            List<GetRoomAvailableDatesResponseDto.AvailablePeriod> availablePeriods = new ArrayList<>();

            var startDate = dto.getStartSearchPeriod();
            for (GetRoomAvailableDatesResponseDto.AvailablePeriod busy : unavailableIntervals) {
                var duration = Duration.between(startDate, busy.getStartDate());
                if (duration.toHours() >= 3) {
                    availablePeriods.add(
                            new GetRoomAvailableDatesResponseDto.AvailablePeriod(
                                    startDate,
                                    busy.getStartDate()
                            )
                    );
                    startDate = busy.getEndDate();
                }
            }
            var duration = Duration.between(startDate, dto.getEndDatePeriod());
            if (duration.toHours() >= 3) {
                availablePeriods.add(
                        new GetRoomAvailableDatesResponseDto.AvailablePeriod(
                                startDate,
                                dto.getEndDatePeriod()
                        )
                );
            }

            return new GetRoomAvailableDatesResponseDto(true, "Success", availablePeriods);
        }catch (Exception e) {
            return new GetRoomAvailableDatesResponseDto(false, "Unexpected error: " + e.getMessage(), null);
        }
    }


    public List<Booking> findByRoom(Room room) {
        return repository.findByRoom(room);
    }

    public boolean isRoomAvailable(Room room, LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> overlappingBookings = repository.findOverlappingBookings(room, startDate, endDate);
        return overlappingBookings.isEmpty();
    }


    private BookingResult validate(BookingDataDto dto) {
        if (dto.getUserName() == null) {
            return new BookingResult(false, "User name is missing");
        }
        if (dto.getUserPhone() == null) {
            return new BookingResult(false, "User phone number is missing");
        }
        if (dto.getRoomId() == null) {
            return new BookingResult(false, "Room ID is missing");
        }
        if (dto.getStartDateTime() == null) {
            return new BookingResult(false, "Start date is missing");
        }
        if (dto.getStartDateTime().isBefore(LocalDateTime.now())) {
            return new BookingResult(false, "Start date cannot be in the past");
        }
        if (dto.getEndDateTime() == null) {
            return new BookingResult(false, "End date is missing");
        }
        if (dto.getEndDateTime().isBefore(LocalDateTime.now())) {
            return new BookingResult(false, "End date cannot be in the past");
        }
        if (dto.getEndDateTime().isBefore(dto.getStartDateTime())) {
            return new BookingResult(false, "End date cannot be before the start date");
        }
        UUID roomId;
        try {
            roomId = UUID.fromString(dto.getRoomId());
        } catch (Exception e) {
            return new BookingResult(false, String.format("Room Id must be real UUID. Provided %s is not valid UUID", dto.getRoomId()));
        }

        var room = roomService.getById(roomId);
        if (room == null) {
            return new BookingResult(false, "Room is not exist");
        }

        if (!isRoomAvailable(room, dto.getStartDateTime(), dto.getEndDateTime())) {
            return new BookingResult(false, "Room is not available for the requested dates");
        }

        return null;
    }

    private BookingResult validate(BookingCancelDto dto, String userId) {
        if (dto.getBookingId() == null) {
            return new BookingResult(false, "Booking ID is missing");
        }
        UUID bookingId;
        try {
            bookingId = UUID.fromString(dto.getBookingId());
        } catch (Exception e) {
            return new BookingResult(false, String.format("Booking Id must be UUID. Provided value %s is not valid UUID", dto.getBookingId()));
        }
        var booking = repository.findById(bookingId).orElseGet(() -> null);
        if (booking == null) {
            return new BookingResult(false, "Booking is not exist");
        }
        if (!booking.getUserId().equals(userId)) {
            return new BookingResult(false, "Booking is not exist");
        }

        return null;
    }

    private GetBookingsResult validate(GetBookingsDto dto) {
        if (dto.getStartDateTime() == null) {
            return new GetBookingsResult(false, "Start date is missing", null);
        }
        if (dto.getEndDateTime() == null) {
            return new GetBookingsResult(false, "End date is missing", null);
        }
        return null;
    }

    private GetRoomAvailableDatesResponseDto validate(GetRoomAvailableDatesRequestsDto dto) {
        if (dto.getRoomId() == null) {
            return new GetRoomAvailableDatesResponseDto(false, "Room ID can not be null", null);
        }
        UUID roomId;
        try {
            roomId = UUID.fromString(dto.getRoomId());
        } catch (Exception e) {
            return new GetRoomAvailableDatesResponseDto(false, String.format("Room ID must be UUID. Provided value %s is not UUID", dto.getRoomId()), null);
        }

        var room = roomService.getById(roomId);
        if (room == null) {
            return new GetRoomAvailableDatesResponseDto(false, String.format("Room by roomId %s not found", roomId), null);
        }

        return null;
    }
}
