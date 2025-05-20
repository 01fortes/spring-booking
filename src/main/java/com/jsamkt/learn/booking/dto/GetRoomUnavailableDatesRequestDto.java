package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;

@JsonClassDescription("Request object for retrieving unavailable dates for a specific room.\n" +
        "This endpoint helps to check room availability for planning purposes.\n" +
        "Required parameter:\n" +
        "- roomId: Unique identifier of the room (UUID format)\n" +
        "Optional parameters:\n" +
        "- startDate: Start of the period to check (LocalDateTime, defaults to current date)\n" +
        "- endDate: End of the period to check (LocalDateTime, defaults to current date + 30 days)")
public class GetRoomUnavailableDatesRequestDto {
    @JsonPropertyDescription("Unique identifier of the room (UUID format)")
    private final String roomId;

    @JsonPropertyDescription("Start date of the period to check (LocalDateTime format)")
    private final LocalDateTime startDate;

    @JsonPropertyDescription("End date of the period to check (LocalDateTime format)")
    private final LocalDateTime endDate;

    public GetRoomUnavailableDatesRequestDto(String roomId, LocalDateTime startDate, LocalDateTime endDate) {
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
} 