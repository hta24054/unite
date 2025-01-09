package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocSaveRequestDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.factory.DocReaderFactory;
import com.hta2405.unite.factory.DocSaverFactory;
import com.hta2405.unite.factory.DocWriterFactory;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.strategy.DocReader;
import com.hta2405.unite.strategy.DocSaver;
import com.hta2405.unite.strategy.DocWriter;
import jakarta.servlet.http.HttpServletResponse;
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
    private final DocReaderFactory docReaderFactory;

    @GetMapping("/write/{type}")
    public String showWritePage(@PathVariable String type,
                                @AuthenticationPrincipal UserDetails user,
                                Model model) {
        DocWriter writer = docWriterFactory.getWriter(DocType.fromString(type));
        writer.prepareWriter(user.getUsername(), model);

        return writer.getView();
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
        Map<String, Object> formData = objectMapper.readValue(formDataJson, new TypeReference<>() {
        });

        // DTO 생성
        DocSaveRequestDTO docSaveRequestDTO = new DocSaveRequestDTO(formData, files);

        // 저장 처리
        DocSaver saver = docSaverFactory.getSaver(DocType.fromString(type));
        saver.save(user.getUsername(), docSaveRequestDTO);

        return ResponseEntity.ok("문서 작성(저장) 완료");
    }

    @GetMapping("/inProgress")
    public String showInProgress(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("list", docService.getInProgressDTO(user.getUsername()));
        return "/doc/inProgress";
    }

    @GetMapping(value = "/read")
    public String readDoc(@AuthenticationPrincipal UserDetails user, Long docId, Model model) {
        DocRole docRole = docService.checkRole(user.getUsername(), docId);
        if (docRole.equals(DocRole.INVALID)) {
            model.addAttribute("errorMessage", "문서 조회 권한이 없습니다.");
            return "error/error";
        }

        Doc doc = docService.getDocById(docId);
        DocReader reader = docReaderFactory.getReader(doc.getDocType());
        reader.prepareRead(doc, docRole, model);

        return reader.getView();
    }

    @GetMapping("/download")
    public void downloadFile(String fileUUID, String fileName, HttpServletResponse response) {
        docService.downloadFile(fileUUID, fileName, response);

    }
}
