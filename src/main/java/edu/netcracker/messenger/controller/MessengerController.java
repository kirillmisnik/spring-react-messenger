package edu.netcracker.messenger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessengerController {
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
