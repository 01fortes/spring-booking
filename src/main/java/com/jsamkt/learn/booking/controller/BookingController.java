package com.jsamkt.learn.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @GetMapping
    public String chatPage(Model model) {
        model.addAttribute("pageTitle", "Awesome Booking Service");
        return "landing";
    }
}
