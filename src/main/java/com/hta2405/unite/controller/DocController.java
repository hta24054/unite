package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.dto.DocListDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.factory.DocEditorViewFactory;
import com.hta2405.unite.factory.DocReaderFactory;
import com.hta2405.unite.factory.DocWriterViewFactory;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.strategy.doc.DocEditViewer;
import com.hta2405.unite.strategy.doc.DocReader;
import com.hta2405.unite.strategy.doc.DocWriteViewer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doc")
@Slf4j
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;
    private final DocWriterViewFactory docWriterViewFactory;
    private final DocReaderFactory docReaderFactory;
    private final DocEditorViewFactory docEditorViewFactory;

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

    @GetMapping(value = "/list/dept")
    public ModelAndView showDeptDocList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<DocListDTO> docList = docService.getDeptDocs(user.getUsername());
        String message = "부서 문서함입니다. 제목을 클릭하면 문서 상세조회가 가능합니다.";
        return docService.showDocList(mv, "부서 문서함", message, docList);
    }

    @GetMapping("/list/sign")
    public ModelAndView showMySignedDocList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<DocListDTO> docList = docService.getSignedDocs(user.getUsername());
        String message = "내가 결재한 문서입니다. 제목을 클릭하면 문서 상세조회가 가능합니다.";
        return docService.showDocList(mv, "내가 결재한 문서", message, docList);
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
    public ModelAndView showWaitingList(@AuthenticationPrincipal UserDetails user, ModelAndView mv) {
        List<DocListDTO> docList = docService.getWaitingDocs(user.getUsername());
        String message = "결재 대기문서 조회 페이지입니다. 제목을 클릭하면 문서 상세조회가 가능합니다.";
        return docService.showDocList(mv, "결재 대기문서", message, docList);
    }
}
