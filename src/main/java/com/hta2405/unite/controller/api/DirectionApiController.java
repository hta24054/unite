package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.DirectionResponseDTO;
import com.hta2405.unite.service.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direction")
@RequiredArgsConstructor
public class DirectionApiController {
    private final DirectionService directionService;

    @GetMapping
    public ResponseEntity<DirectionResponseDTO> getDirection(@RequestParam String origin,
                                                             @RequestParam String destination) {
        System.out.println("origin" + origin + " " + destination);
        DirectionResponseDTO directionResponseDTO = directionService.getCachedDirection(origin, destination);
        return ResponseEntity.ok(directionResponseDTO);
    }
}