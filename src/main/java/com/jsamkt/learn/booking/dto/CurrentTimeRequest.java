package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Empty request to get current date")
public class CurrentTimeRequest {
    @JsonPropertyDescription("Always must be true")
    private final Boolean realDateTime;

    public CurrentTimeRequest(Boolean realDateTime) {
        this.realDateTime = realDateTime;
    }

    public Boolean getRealDateTime() {
        return realDateTime;
    }
}
