package com.hta2405.unite.controller;

import com.hta2405.unite.service.MessengerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessengerController {

    private final MessengerService messengerService;

    public MessengerController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    @GetMapping("/messenger")
    public String showMessengerPage(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("empId", user.getUsername());
        model.addAttribute("empMap", messengerService.getIdToENameMap());
        return "messenger/messenger";
    }
}