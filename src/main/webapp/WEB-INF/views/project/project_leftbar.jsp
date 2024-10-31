<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<style>
		.left{font-size: 20px; line-height: 50px;}
		h3{font-size: 30px; font-weight: bold;}
		.sidebar{
			height: calc(100vh - 50px); 
			border-right: 2px solid rgb(51, 68, 102); 
			padding: 30px 100px 30px 50px;
			float: left;
			margin-top: -50px;
		}
		.left a{color: black;}
	</style>
</head>
<body>
	<div class="sidebar">
		<br><br>
	    <h3 style="color:rgb(51, 68, 102)">프로젝트</h3><br>
	    <ul class="list-group" style="list-style-type: disc; padding-left: 20px;">
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/main">진행 중 프로젝트</a></li>
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/complete">완료 프로젝트</a></li>
	        <li class="left" style="border: none;"><a href="${pageContext.request.contextPath}/project/create">프로젝트 추가</a></li>
	    </ul>
	</div>
</body>
</html>