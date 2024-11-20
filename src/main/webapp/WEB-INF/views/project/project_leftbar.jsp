<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/leftbar.css">
	<script src="${pageContext.request.contextPath}/js/leftbar.js"></script>
	<title>Insert title here</title>
</head>
<body>
	<div class="sidebar">
		<br><br>
	    <h3 style="color:rgb(51, 68, 102)">프로젝트</h3><br>
	    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/main">진행 중 프로젝트</a></li>
		        <c:if test="${not empty left}"> <!-- 진행 중 프로젝트 밑에 프로젝트 이름 뜨도록 -->
			        <p id="currentProjectName" style="font-weight: bold; color: rgb(51, 68, 102);">
			        	<a href="${pageContext.request.contextPath}/project/detail?projectId=${projectId}" style="color: rgb(51, 68, 102)">${left}</a>
			        </p>
			    </c:if>
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/complete">완료 프로젝트</a></li>
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/cancel">취소 프로젝트</a></li>
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/create">프로젝트 추가</a></li>
	    </ul>
	</div>
</body>
</html>