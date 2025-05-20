package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Data for cancelling booking for user")
public class BookingCancelDto {

    @JsonPropertyDescription("Id of booking for cancel. Type UUID")
    private final String bookingId;

    public BookingCancelDto(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }
}
