package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.Birthday;
import com.hta2405.unite.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/birthday")
@RequiredArgsConstructor
@Slf4j
public class BirthdayApiController {
    private final BirthdayService BirthdayService;

    @GetMapping
    public ResponseEntity<List<Birthday>> getBirthday() {
        log.info("컨트롤러");
        return ResponseEntity.ok(BirthdayService.getTodayBirthdays());
    }
}
