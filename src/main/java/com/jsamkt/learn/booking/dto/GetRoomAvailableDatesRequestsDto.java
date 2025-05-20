package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

@JsonClassDescription("Request object for retrieving available time slots for a specific room.\n" +
        "This endpoint helps users find periods when a room can be booked.\n" +
        "Required parameters:\n" +
        "- roomId: Unique identifier of the room (UUID format)\n" +
        "- startSearchPeriod: Beginning of the search period (LocalDateTime)\n" +
        "- endDatePeriod: End of the search period (LocalDateTime)\n" +
        "The response will contain all time slots when the room is available for booking within the specified period.")
public class GetRoomAvailableDatesRequestsDto {
    @JsonPropertyDescription("Unique identifier of the room (UUID format)")
    private final String roomId;

    @JsonPropertyDescription("Start of the period to search for available slots (LocalDateTime format)")
    private final LocalDateTime startSearchPeriod;

    @JsonPropertyDescription("End of the period to search for available slots (LocalDateTime format)")
    private final LocalDateTime endDatePeriod;

    public GetRoomAvailableDatesRequestsDto(String roomId, LocalDateTime startSearchPeriod, LocalDateTime endDatePeriod) {
        this.roomId = roomId;
        this.startSearchPeriod = startSearchPeriod;
        this.endDatePeriod = endDatePeriod;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartSearchPeriod() {
        return startSearchPeriod;
    }

    public LocalDateTime getEndDatePeriod() {
        return endDatePeriod;
    }
}
