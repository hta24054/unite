package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.dto.DocSaveRequestDTO;
import com.hta2405.unite.factory.DocSaverFactory;
import com.hta2405.unite.factory.DocWriterFactory;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.strategy.DocSaver;
import com.hta2405.unite.strategy.DocWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doc")
@Slf4j
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;
    private final DocWriterFactory docWriterFactory;
    private final DocSaverFactory docSaverFactory;

    @GetMapping("/write/{type}")
    public String showWritePage(@PathVariable String type,
                                @AuthenticationPrincipal UserDetails user,
                                Model model) {
        DocWriter writer = docWriterFactory.getWriter(type);
        writer.prepareWriter(user.getUsername(), model);

        return "/doc/" + type + "_write";
    }

    @GetMapping("/countVacation")
    @ResponseBody
    public int countVacation(String startDate, String endDate) {
        return docService.countVacation(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @PostMapping(value = "/write/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> writeDoc(@PathVariable String type,
                                           @RequestPart("formData") String formDataJson,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                           @AuthenticationPrincipal UserDetails user) throws JsonProcessingException {
        // JSON 데이터를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> formData = objectMapper.readValue(formDataJson, new TypeReference<>() {});

        // DTO 생성
        DocSaveRequestDTO docSaveRequestDTO = new DocSaveRequestDTO(formData, files);

        // 저장 처리
        DocSaver saver = docSaverFactory.getSaver(type);
        saver.save(user.getUsername(), docSaveRequestDTO);

        return ResponseEntity.ok("문서 작성(저장) 완료");
    }
}
