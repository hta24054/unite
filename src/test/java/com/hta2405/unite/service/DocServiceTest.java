package com.hta2405.unite.service;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Sign;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.mybatis.mapper.DocMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocServiceTest {

    @InjectMocks
    private DocService docService;

    @Mock
    private DocMapper docMapper;

    @Mock
    private EmpService empService;

    @Test
    @DisplayName("작성자 = 로그인 사용자 일 때 사용자가 문서를 결재한 경우 POST_SIGNED_WRITER 반환해야 한다.")
    void checkRole_shouldReturnPostSignedWriter_whenLoginEmpIsWriterAndSigned() {
        // given
        String loginEmpId = "writer123";
        Long docId = 1L;
        LocalDateTime writeTime = LocalDateTime.now().minusHours(1);

        Doc doc = new Doc(docId, "writer123",
                DocType.GENERAL,
                "제목1",
                "내용1",
                writeTime,
                false); // 결재 미완료

        Emp writer = Emp.builder().empId("writer123").deptId(1100L).build();
        Emp loginEmp = Emp.builder().empId("writer123").deptId(1100L).build();
        List<Sign> signList = List.of(
                new Sign(1L, "writer123", docId, 1, writeTime),
                new Sign(2L, "writer456", docId, 2, null));

        when(docMapper.getDocById(docId)).thenReturn(doc);
        when(empService.getEmpById("writer123")).thenReturn(writer);
        when(empService.getEmpById(loginEmpId)).thenReturn(loginEmp);
        when(docMapper.getSignListByDocId(docId)).thenReturn(signList);

        // when
        DocRole result = docService.checkRole(loginEmpId, docId);

        // then
        assertEquals(DocRole.POST_SIGNED_WRITER, result);
    }

    @Test
    @DisplayName("작성자 != 로그인 사용자이고, 결재선에 포함된 경우, 사용자가 문서를 결재하기 전 PRE_SIGNER 반환해야 한다.")
    void checkRole_shouldReturnPreSigner_whenSignIsUnFinished() {
        // given
        String loginEmpId = "randomUser";
        Long docId = 1L;
        LocalDateTime writeTime = LocalDateTime.now().minusHours(1);

        Emp writer = Emp.builder().empId("writer123").deptId(1100L).build();
        Emp loginEmp = Emp.builder().empId("randomUser").deptId(1100L).build(); //작성자와 로그인 사용자가 다름

        Doc doc = new Doc(docId, "writer123",
                DocType.GENERAL,
                "제목1",
                "내용1",
                writeTime,
                false); // 결재 완료

        List<Sign> signList = List.of(
                new Sign(1L, "writer123", docId, 1, writeTime),
                new Sign(2L, "randomUser", docId, 2, null));

        when(docMapper.getDocById(docId)).thenReturn(doc);
        when(empService.getEmpById("writer123")).thenReturn(writer);
        when(empService.getEmpById(loginEmpId)).thenReturn(loginEmp);
        when(docMapper.getSignListByDocId(docId)).thenReturn(signList);

        // when
        DocRole result = docService.checkRole(loginEmpId, docId);

        // then
        assertEquals(DocRole.PRE_SIGNER, result);
    }

    @Test
    @DisplayName("작성자 != 로그인 사용자이고, 결재선에 포함되지 않고, 부서가 다른경우 INVALID 반환해야 한다.")
    void checkRole_shouldReturnInvalid_whenUserHasNoPermission() {
        // given
        String loginEmpId = "randomUser";
        Long docId = 1L;
        LocalDateTime writeTime = LocalDateTime.now().minusHours(1);

        Doc doc = new Doc(docId, "writer123",
                DocType.GENERAL,
                "제목1",
                "내용1",
                writeTime,
                false); // 결재 미완료

        Emp writer = Emp.builder().empId("writer123").deptId(1100L).build();
        Emp loginEmp = Emp.builder().empId("randomUser").deptId(1200L).build(); //부서 다름

        List<Sign> signList = List.of(
                new Sign(1L, "writer123", docId, 1, writeTime),
                new Sign(2L, "signer1", docId, 2, null)); //결재선에 없음

        when(docMapper.getDocById(docId)).thenReturn(doc);
        when(empService.getEmpById("writer123")).thenReturn(writer);
        when(empService.getEmpById(loginEmpId)).thenReturn(loginEmp);
        when(docMapper.getSignListByDocId(docId)).thenReturn(signList);

        // when
        DocRole result = docService.checkRole(loginEmpId, docId);

        // then
        assertEquals(DocRole.INVALID, result);
    }
}
