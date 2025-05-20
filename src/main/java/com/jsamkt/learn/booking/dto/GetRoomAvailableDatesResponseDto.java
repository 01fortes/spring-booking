package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;
import java.util.List;

@JsonClassDescription("Response object containing information about periods when a room is available for booking.\n" +
        "Returns:\n" +
        "- success: Operation status (boolean)\n" +
        "- message: Status description or error details\n" +
        "- availablePeriods: List of time slots when the room can be booked, each containing:\n" +
        "  * startDate: Beginning of the available period (LocalDateTime)\n" +
        "  * endDate: End of the available period (LocalDateTime)\n" +
        "This information can be used to present booking options to users or to validate booking requests.")
public class GetRoomAvailableDatesResponseDto extends BookingResult {
    
    @JsonPropertyDescription("List of time periods when the room is available for booking")
    private final List<AvailablePeriod> availablePeriods;

    public GetRoomAvailableDatesResponseDto(boolean success, String message, List<AvailablePeriod> availablePeriods) {
        super(success, message);
        this.availablePeriods = availablePeriods;
    }

    public List<AvailablePeriod> getAvailablePeriods() {
        return availablePeriods;
    }

    public static class AvailablePeriod {
        @JsonPropertyDescription("Start date and time when the room becomes available (LocalDateTime format)")
        private final LocalDateTime startDate;

        @JsonPropertyDescription("End date and time until which the room is available (LocalDateTime format)")
        private final LocalDateTime endDate;

        public AvailablePeriod(LocalDateTime startDate, LocalDateTime endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }
    }
}
