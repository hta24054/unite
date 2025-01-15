package com.hta2405.unite.controller;

import com.hta2405.unite.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {
    private final AiService aiService;

    @GetMapping("/addSchedule")
    public int chatSchedule(String message, @AuthenticationPrincipal UserDetails user) {
        return aiService.addSchedule(message, user.getUsername());
    }
}
