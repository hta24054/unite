package com.hta2405.unite.controller.api;

import com.hta2405.unite.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetails user) {
        String message = request.get("message");
        return aiService.findService(message, user.getUsername());
    }
}
