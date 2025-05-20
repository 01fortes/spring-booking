package com.jsamkt.learn.booking.service;

import com.jsamkt.learn.booking.dto.BookingDataDto;
import com.jsamkt.learn.booking.dto.CurrentTimeRequest;
import com.jsamkt.learn.booking.dto.CurrentTimeResponse;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateTimeService {
    public static final Class<CurrentTimeRequest> inputType = CurrentTimeRequest.class;
    public static final String description = "Retrieves the current server date and time in a standardized format.\n" +
            "Used for validation and synchronization of booking times.";


    public CurrentTimeResponse getCurrentTime(CurrentTimeRequest request) {
        System.out.println("Current date time called");
        return new CurrentTimeResponse(
                SimpleDateFormat.getDateTimeInstance().format(new Date())
        );
    }
}
