<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>

    <meta charset="UTF-8">
    <title>일반 문서</title>
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
<div class="container mt-4">
    <!-- 기안용지 제목 -->
    <div class="text-center mb-4">
        <h1 class="header-cell">일반 문서</h1>
    </div>

    <!-- 문서 정보 및 결재자 테이블을 좌우로 배치 -->
    <div class="row">
        <!-- 문서 정보 테이블 -->
        <div class="col-md-6 mb-3">
            <table class="table table-bordered">
                <tr>
                    <td class="table-secondary font-weight-bold text-center">문서번호</td>
                    <td>${doc.docId}</td>
                </tr>
                <tr>
                    <td class="table-secondary font-weight-bold text-center">부&nbsp;&nbsp;&nbsp;서</td>
                    <td>${dept.deptName}</td>
                </tr>
                <tr>
                    <td class="table-secondary font-weight-bold text-center">기 안 자</td>
                    <td>${writer.ename}</td>
                </tr>
            </table>
        </div>
        <!-- 결재자 테이블 -->
        <div class="col-md-6">
            <jsp:include page="sign_read.jsp"/>
        </div>
    </div>

    <!-- 기타 정보 테이블 -->
    <table class="table table-bordered mt-4">
        <tr>
            <td class="table-secondary font-weight-bold text-center">제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목</td>
            <td>${doc.docTitle}</td>
        </tr>
        <tr>
            <td colspan="2" class="table-secondary font-weight-bold text-center">상&nbsp;&nbsp;세&nbsp;&nbsp;내&nbsp;&nbsp;용</td>
        </tr>
        <tr>
            <td colspan="2">${doc.docContent}</td>
        </tr>
    </table>
</div>
<!-- 버튼 영역 -->
<div class="text-right mt-3">
    <c:choose>
        <c:when test="${role == 'signer'}">
            <button type="button" class="btn btn-success">결재</button>
            <button type="button" class="btn btn-danger">반려</button>
        </c:when>
        <c:when test="${role == 'writer'}">
            <button type="button" class="btn btn-warning">회수</button>
            <button type="button" class="btn btn-danger">삭제</button>
        </c:when>
    </c:choose>
    <button type="button" class="btn btn-secondary">목록으로</button>
</div>
</body>
</html>
