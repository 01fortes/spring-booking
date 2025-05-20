package com.jsamkt.learn.booking.service;

import com.jsamkt.learn.booking.dto.GetHotelsRequestDto;
import com.jsamkt.learn.booking.dto.GetHotelsResponseDto;
import com.jsamkt.learn.booking.model.Hotel;
import com.jsamkt.learn.booking.repository.HotelRepository;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HotelService {
    public static final Class<GetHotelsRequestDto> getHotelsInputType = GetHotelsRequestDto.class;
    public static final String getHotelsDescription = "Retrieves a comprehensive list of all available hotels in our system. Each hotel entry includes:\n" +
            "- Unique identifier (UUID) for use in other API calls\n" +
            "- Hotel name\n" +
            "- Detailed location information\n" +
            "The function returns a structured response containing success status, message, and the list of hotels if successful.";

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public GetHotelsResponseDto findAllHotels(GetHotelsRequestDto dto, ToolContext ctx) {
        String userId = (String) ctx.getContext().get(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY);
        if (userId == null) {
            return new GetHotelsResponseDto(false, "User not found", null);
        }
        return new GetHotelsResponseDto(
                true,
                "Success",
                hotelRepository.findAll());
    }

    public Hotel getById(UUID id) {
        return hotelRepository.findById(id).orElseGet(() -> null);
    }


















}
