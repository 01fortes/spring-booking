package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

@JsonClassDescription("Request object for retrieving bookings within a specified time period.\n" +
        "Parameters:\n" +
        "- startDateTime: Beginning of the search period (LocalDateTime)\n" +
        "- endDateTime: End of the search period (LocalDateTime)")
public class GetBookingsDto {
    @JsonPropertyDescription("booking start period")
    private final LocalDateTime startDateTime;
    @JsonPropertyDescription("booking end period")
    private final LocalDateTime endDateTime;

    public GetBookingsDto(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
