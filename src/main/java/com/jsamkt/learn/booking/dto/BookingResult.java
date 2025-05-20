package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Booking result")
public class BookingResult {
    @JsonPropertyDescription("Is function invoked successfully")
    private final boolean success;
    @JsonPropertyDescription("General message. If success = true then message is Success. Is success = false then message consists error details")
    private final String message;

    public BookingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
