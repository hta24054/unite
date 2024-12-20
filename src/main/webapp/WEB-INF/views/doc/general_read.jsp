<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <link href="${pageContext.request.contextPath}/css/doc.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>일반 문서</title>
</head>
<body>
<div class="container mt-4">
    <div class="document-wrapper">
        <!-- 기안용지 제목 -->
        <div class="text-center mb-4">
            <h1 class="header-cell">일반 문서</h1>
        </div>

        <!-- 테이블 묶음 컨테이너 -->
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
                    <tr>
                        <td class="table-secondary font-weight-bold text-center">작 성 일</td>
                        <td>${doc.docCreateDate.toLocalDate()}</td>
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
                <td colspan="2" style="min-height: 400px;">${doc.docContent}</td>
            </tr>
        </table>
    </div>
    <jsp:include page="read_button.jsp"/>
</div>

</body>
</html>
