package com.hta2405.unite.controller;

import com.hta2405.unite.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attend")
public class AttendApiController {
    private final AttendService attendService;

    @GetMapping("/record")
    public Map<String, Object> getAttendRecord(@AuthenticationPrincipal UserDetails user) {
        return attendService.getTodayRecord(user.getUsername(), LocalDate.now());
    }

    @PostMapping("/in")
    public ResponseEntity<Object> attendIn(@AuthenticationPrincipal UserDetails user, String attendType) {
        return attendService.attendIn(user, attendType);
    }

    @PostMapping("/out")
    public ResponseEntity<Object> attendOut(@AuthenticationPrincipal UserDetails user) {
        return attendService.attendOut(user);
    }
}
