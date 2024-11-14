<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.js"></script>

    <meta charset="UTF-8">
    <title>일반 문서 작성</title>
    <style>
        .header-cell {
            font-size: 36px;
            font-weight: bold;
            padding: 20px 0;
        }

        .title-input, .form-control {
            font-size: 16px;
        }

        /* 테두리 색상을 검정색으로 설정 */
        .table-bordered, .table-bordered td, .table-bordered th {
            align-content: center;
            border-color: black !important;
        }
    </style>
</head>
<body>
<form id="doc_form" action="${pageContext.request.contextPath}/doc/general_write" method="POST">
    <div class="container mt-4">
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
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                        <td>${dept.deptName}</td>
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">기 안 자</td>
                        <td>${emp.ename}</td>
                        <input type="hidden" name="writer" value="${emp.empId}">
                    </tr>
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${today}</td>
                    </tr>
                </table>
            </div>
            <!-- 결재자 테이블 -->
            <div class="col-md-6">
                <jsp:include page="sign_write.jsp"/>
            </div>
        </div>

        <!-- 기타 정보 테이블 -->
        <table class="table table-bordered mt-4">
            <tr>
                <td class="table-secondary font-weight-bold text-center">제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목</td>
                <td><input type="text" class="form-control title-input" name="title" placeholder="제목을 입력하세요." required>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="table-secondary font-weight-bold text-center">상&nbsp;&nbsp;세&nbsp;&nbsp;내&nbsp;&nbsp;용</td>
            </tr>
            <tr>
                <td colspan="2">
                    <textarea class="summernote form-control" id="content" name="content" required></textarea>
                </td>
            </tr>
        </table>
    </div>
    <!-- 버튼 영역 -->
    <div class="text-right mt-3">
        <button type="submit" form="doc_form" class="btn btn-success">결재 상신</button>
        <button type="reset" form="doc_form" class="btn btn-secondary">초기화</button>
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
        $('#doc_form').on('submit', function (event) {
            const confirmSubmission = confirm("문서를 작성하시겠습니까?");
            if (!confirmSubmission) {
                event.preventDefault(); // "취소"를 누르면 제출 취소
            }
        });
    });
</script>
</body>
</html>
