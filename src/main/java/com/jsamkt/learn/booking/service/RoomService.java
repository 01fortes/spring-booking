package com.jsamkt.learn.booking.service;

import com.jsamkt.learn.booking.dto.GetRoomsByHotelRequestDto;
import com.jsamkt.learn.booking.dto.GetRoomsByHotelResponseDto;
import com.jsamkt.learn.booking.model.Room;
import com.jsamkt.learn.booking.repository.RoomRepository;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomService {
    public static final Class<GetRoomsByHotelRequestDto> getRoomsInputType = GetRoomsByHotelRequestDto.class;
    public static final String getRoomsDescription = "Retrieves detailed information about all rooms in a specified hotel. Required parameter: hotelId (UUID).\n" +
            "Room information includes:\n" +
            "- Room identifier (UUID) for booking operations\n" +
            "- Room number\n" +
            "- Amenities:\n" +
            "  * WiFi availability (boolean)\n" +
            "  * Parking availability (boolean)\n" +
            "  * Pet-friendly status (boolean)\n" +
            "- Maximum guest capacity (integer)\n" +
            "Returns a structured response with room details if successful.";

    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    
    @Autowired
    public RoomService(RoomRepository roomRepository, HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.hotelService = hotelService;
    }

    public GetRoomsByHotelResponseDto findByHotel(GetRoomsByHotelRequestDto dto, ToolContext ctx) {
        String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
        if (userId == null) {
            return new GetRoomsByHotelResponseDto(false, "User not found", null);
        }
        if (dto.getHotelId() == null) {
            return new GetRoomsByHotelResponseDto(false, "Hotel id is missing", null);
        }
        UUID hotelId;
        try {
            hotelId = UUID.fromString(dto.getHotelId());
        } catch (Exception e) {
            return new GetRoomsByHotelResponseDto(false, String.format("Hotel id must be UUID. Provided %s is not valid UUID", dto.getHotelId()), null);
        }

        var hotel = hotelService.getById(hotelId);
        if (hotel == null) {
            return new GetRoomsByHotelResponseDto(false, "Hotel not found", null);
        }

        var rooms = roomRepository.findByHotel(hotel);
        if (rooms.isEmpty()) {
            return new GetRoomsByHotelResponseDto(false, "Hotel does not contain any rooms", null);
        }
        return new GetRoomsByHotelResponseDto(true, "Success", rooms);
    }

    public Room getById(UUID id) {
        return roomRepository.findById(id).orElseGet(() -> null);
    }

}



















