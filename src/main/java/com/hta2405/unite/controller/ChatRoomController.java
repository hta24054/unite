package com.hta2405.unite.controller;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.service.MessengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messenger")
public class ChatRoomController {
    private final MessengerService messengerService;

    public ChatRoomController(MessengerService messengerService) {
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
    public ResponseEntity<String> createRoom(@RequestBody ChatRoom chatRoom) {
        messengerService.createChatRoom(chatRoom);
        return ResponseEntity.ok("Chat room created successfully!");
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