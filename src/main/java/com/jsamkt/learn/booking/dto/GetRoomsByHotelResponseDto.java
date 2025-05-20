package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.jsamkt.learn.booking.model.Room;

import java.util.List;

@JsonClassDescription("Response from function to get rooms in specific hotel. Room also has id that can be used in any other functions requires room id")
public class GetRoomsByHotelResponseDto extends BookingResult{

    private final List<Room> rooms;

    public GetRoomsByHotelResponseDto(boolean success, String message, List<Room> rooms) {
        super(success, message);
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
