package com.jsamkt.learn.booking.configuration;

import com.jsamkt.learn.booking.service.BookingService;
import com.jsamkt.learn.booking.service.DateTimeService;
import com.jsamkt.learn.booking.service.HotelService;
import com.jsamkt.learn.booking.service.RoomService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class OpenAiConfiguration {

    @Bean
    @Qualifier("openAiRunScheduler")
    public Scheduler openAiRunScheduler() {
        return Schedulers.fromExecutor(Executors.newSingleThreadExecutor());
    }

    @Bean
    public ToolCallback getAllHotelsCallback(HotelService service) {
        return FunctionToolCallback.builder("get_all_hotels", service::findAllHotels)
                .inputType(HotelService.getHotelsInputType)
                .description(HotelService.getHotelsDescription)
                .build();
    }

    @Bean
    public ToolCallback getRooms(RoomService service) {
        return FunctionToolCallback.builder("get_rooms_for_hotel", service::findByHotel)
                .inputType(RoomService.getRoomsInputType)
                .description(RoomService.getRoomsDescription)
                .build();
    }

    @Bean
    public ToolCallback getRoomAvailablePeriod(BookingService service) {
        return FunctionToolCallback.builder("get_room_available_period", service::getRoomAvailableDates)
                .inputType(BookingService.getRoomAvailableDatesInputType)
                .description(BookingService.getRoomAvailableDatesDescription)
                .build();
    }

    @Bean
    public ToolCallback getUsersBookingsCallback(BookingService service) {
        return FunctionToolCallback.builder("get_users_bookings_in_period", service::getBookings)
                .inputType(BookingService.getBookingsInputType)
                .description(BookingService.getBookingsDescription)
                .build();
    }

    @Bean
    public ToolCallback cancelCallback(BookingService service) {
        return FunctionToolCallback.builder("cancel_booking", service::cancel)
                .inputType(BookingService.cancelInputType)
                .description(BookingService.cancelDescription)
                .build();
    }

    @Bean
    public ToolCallback bookingCallback(BookingService service) {
        return FunctionToolCallback.builder("booking_room", service::book)
                .inputType(BookingService.bookInputType)
                .description(BookingService.bookDescription)
                .build();
    }

    @Bean
    public ToolCallback currentTime(DateTimeService service) {
        return FunctionToolCallback.builder("current_date_time", service::getCurrentTime)
                .inputType(DateTimeService.inputType)
                .description(DateTimeService.description)
                .build();
    }


    @Bean
    public OpenAiChatOptions options() {
        return OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 @Value("${app.system.prompt}") String systemPrompt,
                                 ChatMemory chatMemory,
                                 OpenAiChatOptions options,
                                 List<ToolCallback> tools) {
        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultOptions(options)
                .defaultTools(tools)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }
}


















