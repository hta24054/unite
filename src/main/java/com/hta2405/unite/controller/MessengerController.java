package com.hta2405.unite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessengerController {

    @GetMapping("/messenger")
    public String showMessengerPage() {
        return "messenger/messenger";
    }
}