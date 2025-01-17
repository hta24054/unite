package com.hta2405.unite.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.factory.DocEditorFactory;
import com.hta2405.unite.factory.DocWriterFactory;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.strategy.doc.DocEditor;
import com.hta2405.unite.strategy.doc.DocWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/doc")
public class DocApiController {
    private final DocService docService;
    private final DocWriterFactory docWriterFactory;
    private final DocEditorFactory docEditorFactory;

    @PostMapping(value = "/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> writeDoc(@PathVariable String type,
                                           @RequestPart("formData") String formDataJson,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                           @AuthenticationPrincipal UserDetails user) throws JsonProcessingException {
        // JSON 데이터를 Map으로 변환
        Map<String, Object> formData = makeMapFromJson(formDataJson);

        // DTO 생성
        DocRequestDTO docRequestDTO = new DocRequestDTO(formData, files);

        // 저장 처리
        DocWriter writer = docWriterFactory.getWriter(DocType.fromString(type));
        writer.write(user.getUsername(), docRequestDTO);

        return ResponseEntity.ok("문서 작성(저장) 완료");
    }

    @PatchMapping
    public ResponseEntity<String> editDoc(@RequestPart("formData") String formDataJson,
                                          @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                          @AuthenticationPrincipal UserDetails user) throws JsonProcessingException {
        Map<String, Object> formData = makeMapFromJson(formDataJson);
        long docId = Long.parseLong(formData.get("docId").toString());

        DocRole docRole = docService.checkRole(user.getUsername(), docId);
        if (!docRole.equals(DocRole.PRE_SIGNED_WRITER)) {
            return ResponseEntity.badRequest().body("문서 수정 불가");
        }

        Doc doc = docService.getDocById(docId);

        DocRequestDTO docRequestDTO = new DocRequestDTO(formData, files);

        DocEditor editor = docEditorFactory.getEditor(doc.getDocType());
        editor.edit(doc, docRequestDTO);

        return ResponseEntity.ok("문서 수정 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.deleteDoc(docId, user.getUsername())
                ? ResponseEntity.ok("삭제 성공") : ResponseEntity.badRequest().body("삭제 실패");
    }

    private static Map<String, Object> makeMapFromJson(String formDataJson) throws JsonProcessingException {
        // JSON 데이터를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(formDataJson, new TypeReference<>() {
        });
    }

    @GetMapping("/countVacation")
    public int countVacation(String startDate, String endDate) {
        return docService.countVacation(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(String fileUUID, String fileName) {
        return docService.downloadFile(fileUUID, fileName);
    }

    @PostMapping("/sign")
    public ResponseEntity<String> signDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.signDoc(docId, user.getUsername())
                ? ResponseEntity.ok("결재 성공") : ResponseEntity.badRequest().body("결재 실패");
    }

    @PostMapping("/revoke")
    public ResponseEntity<String> revokeDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.revokeDoc(docId, user.getUsername())
                ? ResponseEntity.ok("회수 성공") : ResponseEntity.badRequest().body("회수 실패");
    }

    @GetMapping(value = "/waitingCount")
    public int getWaitingCount(@AuthenticationPrincipal UserDetails user) {
        return docService.getWaitingDocs(user.getUsername()).size();
    }

    @GetMapping(value = "/inProgressCount")
    public int getInProgressCount(@AuthenticationPrincipal UserDetails user) {
        return docService.getInProgressDTO(user.getUsername()).size();
    }
}
