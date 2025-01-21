package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.Contact;
import com.hta2405.unite.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contact/outer")
public class ContactApiController {
    private final ContactService contactService;

    @GetMapping
    public Map<String, Object> getAllResource() {
        List<Contact> contactList = contactService.getAllOuterContact();

        // DataTables 가 요구하는 JSON 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("data", contactList);
        return response;
    }

    @PostMapping
    public int insertContact(Contact contact) {
        return contactService.addContact(contact);
    }

    @PatchMapping
    public int updateContact(Contact contact) {
        return contactService.updateContact(contact);
    }

    @DeleteMapping
    public int deleteResource(@RequestParam("selectedIds[]") long[] selectedIds) {
        List<Long> list = Arrays.stream(selectedIds).boxed().toList();
        return contactService.deleteContact(list);
    }
}