<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/doc.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>일반 문서 작성</title>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/general_write" method="POST">
    <div class="container mt-4">
        <div class="document-wrapper">
            <!-- 기안용지 제목 -->
            <div class="text-center mb-4">
                <h1 class="header-cell">기안용지</h1>
            </div>

            <!-- 문서 정보 및 결재자 테이블을 좌우로 배치 -->
            <div class="row">
                <!-- 문서 정보 테이블 -->
                <div class="col-md-6 mb-3">
                    <table class="table table-bordered">
                        <tr>
                            <td class="table-secondary font-weight-bold text-center">문서번호</td>
                            <td><input type="text" name="docId" value="${doc.docId}" readonly></td>
                        </tr>
                        <tr>
                            <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                            <td>${dept.deptName}</td>
                        </tr>
                        <tr>
                            <td class="table-secondary font-weight-bold text-center">기 안 자</td>
                            <td>${writer.ename}</td>
                            <input type="hidden" name="writer" value="${writer.empId}">
                        </tr>
                        <tr>
                            <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                            <td>${doc.docCreateDate.toLocalDate()}</td>
                        </tr>
                    </table>
                </div>
                <!-- 결재자 테이블 -->
                <div class="col-md-6">
                    <jsp:include page="sign_edit.jsp"/>
                </div>
            </div>

            <!-- 기타 정보 테이블 -->
            <table class="table table-bordered mt-4">
                <tr>
                    <td class="table-secondary font-weight-bold text-center">제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목</td>
                    <td><input type="text" class="form-control title-input" name="title" placeholder="제목을 입력하세요."
                               data-name="제목" value="${doc.docTitle}" required>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="table-secondary font-weight-bold text-center">상&nbsp;&nbsp;세&nbsp;&nbsp;내&nbsp;&nbsp;용</td>
                </tr>
                <tr>
                    <td colspan="2">
                    <textarea class="summernote form-control" id="content" name="content" data-name="상세내용"
                              required>${doc.docContent}</textarea>
                    </td>
                </tr>
            </table>
            <!-- 버튼 영역 -->
        </div>
        <div class="button-container">
            <button type="button" form="doc_form" class="btn btn-success" id="submit-button">수정완료</button>
            <button type="reset" form="doc_form" class="btn btn-secondary">초기화</button>
        </div>
    </div>
</form>
<script>
    $(document).ready(function () {
        $('.summernote').summernote({
            height: 500, // set editor height
            minHeight: 500, // set minimum height of editor
            maxHeight: null, // set maximum height of editor
            focus: true // set focus to editable area after initializing summernote
        });

        //제출버튼 눌리는 경우
        $('#submit-button').on('click', function (event) {
            // 모든 required 필드가 채워졌는지 검사
            let isValid = true;
            $('#doc_form [required]').each(function () {
                if ($(this).val() === '') {
                    const errorMessage = $(this).data('name'); // data-error 속성 값 가져오기
                    alert(errorMessage + '을(를) 입력해 주세요');
                    $(this).focus();
                    isValid = false;
                    return false;
                }
            });

            if (!isValid) return;

            event.preventDefault();

            const additionalSigners = $('input[name="sign[]"]').length - 1;
            if (additionalSigners < 1) {
                alert("본인 이외에 최소 1명의 결재자를 추가해야 합니다.");
                return;
            }

            const confirmSubmission = confirm("문서를 수정하시겠습니까?");
            if (!confirmSubmission) return;

            // 폼 데이터 수집
            const formData = {
                docId: $('input[name="docId"]').val(),
                title: $('input[name="title"]').val(),
                content: $('textarea[name="content"]').val(),
                writer: $('input[name="writer"]').val(),
                signers: []
            }

            // 결재자 정보 추가
            $('input[name="sign[]"]').each(function () {
                formData.signers.push($(this).val());
            });

            $.ajax({
                url: "${pageContext.request.contextPath}/doc/general_edit",
                type: "POST",
                data: JSON.stringify(formData),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    if (response.status === 'success') {
                        alert("문서 수정이 완료되었습니다.");
                        window.location.href = "${pageContext.request.contextPath}/doc/in-progress";
                    } else {
                        alert("문서 수정 중 오류가 발생했습니다.");
                    }
                },
                error: function () {
                    alert("문서 수정 중 오류가 발생했습니다.");
                }
            });
        });
    });
</script>
</body>
</html>
