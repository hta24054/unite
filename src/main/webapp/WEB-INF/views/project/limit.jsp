<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/project_left.css">
	<meta charset="EUC-KR">
	<title>Insert title here</title>
</head>
<body>
	<div class="rows">
		<span>줄보기</span>
		<select class="form-control" id="viewcount">
			<option value="1">1</option>
			<option value="3">3</option>
			<option value="5">5</option>
			<option value="7">7</option>
			<option value="10" selected>10</option>
		</select>
	</div>
</body>
</html>