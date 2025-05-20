package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.jsamkt.learn.booking.model.Hotel;

import java.util.List;

@JsonClassDescription("Response of get all hotels function")
public class GetHotelsResponseDto extends BookingResult {

    @JsonPropertyDescription("List of hotels")
    private final List<Hotel> hotels;

    public GetHotelsResponseDto(boolean success, String message, List<Hotel> hotels) {
        super(success, message);
        this.hotels = hotels;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }
}
