package com.jsamkt.learn.booking.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonClassDescription("Booking request data structure containing all necessary information for room reservation:\n" +
        "- userName: Guest's full name\n" +
        "- userPhone: Contact phone number\n" +
        "- startDateTime: Check-in date and time (LocalDateTime format)\n" +
        "- endDateTime: Check-out date and time (LocalDateTime format)\n" +
        "- roomId: Unique room identifier (UUID format)")
public class BookingDataDto {
    @JsonPropertyDescription("Name of user")
    private final String userName;
    @JsonPropertyDescription("User's phone number")
    private final String userPhone;
    @JsonPropertyDescription("Booking start date time. Type is java.time.LocalDateTime")
    private final LocalDateTime startDateTime;
    @JsonPropertyDescription("Booking finish date time. Type is java.time.LocalDateTime")
    private final LocalDateTime endDateTime;
    @JsonPropertyDescription("Id of room for booking. Type UUID")
    private final String roomId;

    public BookingDataDto(String userName, String userPhone, LocalDateTime bookingTime, LocalDateTime endDateTime, String roomId) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.startDateTime = bookingTime;
        this.endDateTime = endDateTime;
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "BookingDataDto{" +
                "userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", bookingTime=" + startDateTime +
                '}';
    }
}
