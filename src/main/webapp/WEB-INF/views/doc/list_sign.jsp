<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>문서등록대장 - 내가 결재한 문서</title>
    <jsp:include page="../common/header.jsp"/>
    <jsp:include page="doc_leftbar.jsp"/>
    <style>
        #main_title {
            color: #334466;
            margin-left: 500px;
            margin-bottom: 20px;
            font-weight: bold;
        }

        #waiting_table td, th {
            text-align: center;
        }
    </style>
<body>
<h2 id="main_title">문서등록대장 - 내가 결재한 문서</h2>
<div class="container text-center">
    <table class="table table-striped table-bordered" id="waiting_table">
        <thead>
        <tr>
            <th style="width: 10%">문서번호</th>
            <th style="width: 15%">기안일</th>
            <th style="width: 15%">문서구분</th>
            <th style="width: 40%">문서제목</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty list}">
            <td colspan="5">내가 결재한 문서가 없습니다.</td>
        </c:if>
        <c:if test="${!empty list}">
            <c:forEach var="doc" items="${list}">
                <tr>
                    <td>${doc.docId}</td>
                    <td class="create-date" data-date="${doc.docCreateDate}"></td>
                    <td>${doc.docType.getType()}</td>
                    <td><a href="${pageContext.request.contextPath}/doc/read?docId=${doc.docId}">${doc.docTitle}</a></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
<script>
    $(document).ready(function () {
        // 날짜 포맷 함수
        function formatDate(isoDate) {
            const date = new Date(isoDate);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return year + '-' + month + '-' + day;
        }

        // 각 셀의 날짜를 포맷팅
        $(".create-date").each(function () {
            const isoDate = $(this).data("date");
            $(this).text(formatDate(isoDate));
        });
    });
</script>
</body>
</html>
