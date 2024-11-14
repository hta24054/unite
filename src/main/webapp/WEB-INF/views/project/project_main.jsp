<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/project_left.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/project_main.js"></script> 
	<script>
    	const contextPath = "${pageContext.request.contextPath}";
    </script>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
</head>
<body>
	<jsp:include page="limit.jsp"/>
	<table class="table">
		<caption><h2>진행 중 프로젝트</h2></caption>
		<thead>
			<tr>
				<th>프로젝트 코드</th>
				<th>프로젝트 이름</th>
				<th>책임자</th>
				<th>참여자</th>
				<th>열람자</th>
				<th>진행률</th>
				<th>마감일</th>
				<th>관리</th>
			</tr>
		</thead>
		<tbody>
		   
		</tbody> 
	</table>
	<jsp:include page="page.jsp"/>
	<%-- 게시글이 없는 경우 --%>
	<c:if test="${listcount == 0 }">
		<h3 style="text-align:center">등록된 글이 없습니다</h3>
	</c:if>
</body>
</html>