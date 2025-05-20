package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.UUID;

@JsonClassDescription("Request object for retrieving room information for a specific hotel.\n" +
        "Required parameter: hotelId (UUID) - unique identifier of the target hotel")
public class GetRoomsByHotelRequestDto {

    @JsonPropertyDescription("Id of hotel. Type UUID")
    private final String hotelId;

    public GetRoomsByHotelRequestDto(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}
