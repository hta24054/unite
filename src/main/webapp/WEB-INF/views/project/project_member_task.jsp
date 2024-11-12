<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>진행과정</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
    <style>
        .table { width: 70%; margin: auto; }
        table, td, th { border-collapse: collapse; }
        h2 { text-align: left; color: black; margin: 0; }
        caption { caption-side: top; margin-bottom: 30px; }
    </style>
</head>
<body>
    <table class="table">
        <caption><h2><c:out value="${left}"/> - <c:out value="${user}"/></h2></caption>
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>내용</th>
                <th>작성일</th>
                <th>수정일</th>
                <th>첨부파일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${task}" varStatus="status">
            <tr>
                <td>${fn:escapeXml(status.index + 1)}</td> 
                <td>${task.projectTitle}</td>
                <td>${task.projectContent}</td>
                <td>${task.projectDate}</td>
                <td>${task.projectUpdateDate}</td>
                <td>${task.board_file}</td>
            </tr>
       		</c:forEach>
        </tbody>
    </table>
</body>
</html>
