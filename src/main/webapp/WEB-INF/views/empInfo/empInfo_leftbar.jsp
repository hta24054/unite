<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="sidebar">
		<br> <br>
		<h3 style="color: rgb(51, 68, 102)">인사정보</h3>
		<br>
		<ul class="list-group"
			style="list-style-type: disc; padding-left: 20px;">
			<li class="left" style="border: none;"><a
				href="${pageContext.request.contextPath}/empInfo/view">나의 인사정보</a></li>

			<li class="left" style="border: none;"><a
				href="${pageContext.request.contextPath}/empInfo/viewdept?empId=${sessionScope.id}">부서
					인사정보</a></li>

			<li class="left" style="border: none;"><a
				href="${pageContext.request.contextPath}/empInfo/viewotherdeptinfo">타부서
					인사정보</a></li>
		</ul>
	</div>
</body>
</html>
