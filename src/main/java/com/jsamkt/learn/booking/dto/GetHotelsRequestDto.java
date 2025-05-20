package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Request object for retrieving the complete list of available hotels.\n" +
        "Parameter: realData (boolean) - must be true to get actual hotel data")
public class GetHotelsRequestDto {
    @JsonPropertyDescription("Always must be true")
    private final boolean realData;

    public GetHotelsRequestDto(boolean realData) {
        this.realData = realData;
    }

    public boolean isRealData() {
        return realData;
    }
}
