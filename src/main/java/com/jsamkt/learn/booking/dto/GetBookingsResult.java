package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.jsamkt.learn.booking.model.Booking;

import java.util.List;

@JsonClassDescription("Response for get bookings function")
public class GetBookingsResult extends BookingResult{
    @JsonPropertyDescription("List of bookings")
    private final List<Booking> bookings;

    public GetBookingsResult(boolean success, String message, List<Booking> bookings) {
        super(success, message);
        this.bookings = bookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
