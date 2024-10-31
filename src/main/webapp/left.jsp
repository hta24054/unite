<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="icon" href="${pageContext.request.contextPath}/img/house-icon.svg">
	<jsp:include page="header.jsp"/>
	
		
</head>
<body>
	<div class="container" OnClick="location.href ='${pageContext.request.contextPath }/boards/list'" style="cursor:pointer;">
		<h1>전자문서</h1>
		<ul>
			<li>결제대기</li>
			<li>문서기안</li>
			<li>결재 진행 문서</li>
		</ul>
	</div>
</body>
</html>