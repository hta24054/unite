package com.hta2405.unite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocRequestDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.factory.*;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.strategy.doc.*;
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
    private final DocWriterViewFactory docWriterViewFactory;
    private final DocWriterFactory docWriterFactory;
    private final DocReaderFactory docReaderFactory;
    private final DocEditorViewFactory docEditorViewFactory;
    private final DocEditorFactory docEditorFactory;

    @GetMapping
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

    @GetMapping("/write/{type}")
    public String showWritePage(@PathVariable String type,
                                @AuthenticationPrincipal UserDetails user,
                                Model model) {
        DocWriteViewer writeViewer = docWriterViewFactory.getWriteViewer(DocType.fromString(type));
        writeViewer.prepareWriteView(user.getUsername(), model);

        return writeViewer.getView();
    }

    @PostMapping(value = "/{type}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
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

    @GetMapping("/edit")
    public String showEditPage(Long docId,
                               @AuthenticationPrincipal UserDetails user,
                               Model model) {
        DocRole docRole = docService.checkRole(user.getUsername(), docId);
        if (!docRole.equals(DocRole.PRE_SIGNED_WRITER)) {
            model.addAttribute("errorMessage", "문서 수정 권한이 없습니다.");
            return "error/error";
        }

        Doc doc = docService.getDocById(docId);
        DocEditViewer editViewer = docEditorViewFactory.getEditViewer(doc.getDocType());
        editViewer.prepareEditView(doc, docRole, model);
        return editViewer.getView();
    }

    @PatchMapping
    @ResponseBody
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

    private static Map<String, Object> makeMapFromJson(String formDataJson) throws JsonProcessingException {
        // JSON 데이터를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> formData = objectMapper.readValue(formDataJson, new TypeReference<>() {
        });
        return formData;
    }

    @GetMapping("/countVacation")
    @ResponseBody
    public int countVacation(String startDate, String endDate) {
        return docService.countVacation(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/inProgress")
    public String showInProgress(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("list", docService.getInProgressDTO(user.getUsername()));
        return "/doc/inProgress";
    }

    @GetMapping(value = "/waiting")
    public String showWaitingList(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("list", docService.getWaitingDocs(user.getUsername()));
        return "/doc/waiting";
    }

    @GetMapping("/download")
    public void downloadFile(String fileUUID, String fileName, HttpServletResponse response) {
        docService.downloadFile(fileUUID, fileName, response);
    }

    @PostMapping("/sign")
    @ResponseBody
    public ResponseEntity<String> signDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.signDoc(docId, user.getUsername())
                ? ResponseEntity.ok("결재 성공") : ResponseEntity.badRequest().body("결재 실패");
    }

    @PostMapping("/revoke")
    @ResponseBody
    public ResponseEntity<String> revokeDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.revokeDoc(docId, user.getUsername())
                ? ResponseEntity.ok("회수 성공") : ResponseEntity.badRequest().body("회수 실패");
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteDoc(Long docId, @AuthenticationPrincipal UserDetails user) {
        return docService.deleteDoc(docId, user.getUsername())
                ? ResponseEntity.ok("삭제 성공") : ResponseEntity.badRequest().body("삭제 실패");
    }
}
