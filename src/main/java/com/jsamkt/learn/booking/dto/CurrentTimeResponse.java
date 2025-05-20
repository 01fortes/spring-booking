package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Current time response")
public class CurrentTimeResponse {
    @JsonPropertyDescription("Current date time")
    private final String now;

    public CurrentTimeResponse(String now) {
        this.now = now;
    }

    public String getNow() {
        return now;
    }
}
