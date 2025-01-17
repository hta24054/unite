package com.hta2405.unite.controller;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.service.MessengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/messenger")
public class MessengerApiController {
    private final MessengerService messengerService;

    public MessengerApiController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    @GetMapping("/rooms")
    public List<ChatRoom> getAllRooms() {
        return messengerService.getAllChatRooms();
    }

    @GetMapping("/rooms/{id}")
    public ChatRoom getRoomById(@PathVariable Long id) {
        return messengerService.getChatRoomById(id);
    }

    @PostMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> createRoom(@RequestBody List<String> userIds,
                                                              @AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        if (!Objects.equals(empId, "admin")) {
            userIds.add(empId);
        }
        boolean result = messengerService.createChatRoom(userIds, empId);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", result);
        return ResponseEntity.ok(response); // JSON 형식으로 반환
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        messengerService.deleteChatRoom(id);
        return ResponseEntity.ok("Chat room deleted successfully!");
    }

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message) {
        messengerService.saveMessage(message);
        return ResponseEntity.ok("Message sent successfully!");
    }
}