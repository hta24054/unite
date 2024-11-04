<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>취소 프로젝트</title>
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
        <caption><h2>취소 프로젝트</h2></caption>
        <thead>
            <tr>
                <th>코드명</th>
                <th>프로젝트명</th>
                <th>책임자</th>
                <th>참여자</th>
                <th>시작일</th>
                <th>취소일</th>
                <th>첨부파일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="project" items="${cancelProjects}">
            <tr>
                <td>${project.projectId}</td>
                <td>${project.projectName}</td>
                <td>${project.empName}</td>
                <td>
                    <c:set var="numParticipants" value="${fn:length(project.participantNames)}" />
                    <c:if test="${numParticipants > 0}">
                        ${project.participantNames[0]}
                        <c:if test="${numParticipants > 1}">
                            외 ${numParticipants - 1}명
                        </c:if>
                    </c:if>
                </td>
                <td>${project.projectStartDate}</td>
                <td>${project.projectEndDate}</td>
                <td>${project.projectFilePath}</td>
            </tr>
       		</c:forEach>
        </tbody>
    </table>
</body>
</html>
