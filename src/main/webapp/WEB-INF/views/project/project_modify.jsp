<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Insert title here</title>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/css/view.css" type="text/css">
	<script src="${pageContext.request.contextPath }/js/modifyform.js"></script>
</head>
<body>
	<div class="container">
		<form action="modifyProcess" method="post" enctype="multipart/form-data" name="modifyform">
			<input type="hidden" name="userid" value="${task.memberId }">
			<input type="hidden" name="num" value="${task.taskNum}">
			<h1>MVC 게시판 - 수정</h1>
			<div class="form-group">
				<label for="board_name">글쓴이</label>
				<input value="${task.memberName}" readOnly type="text" class="form-control">
			</div>
			<div class="form-group">
				<label for="board_subject">제목</label>
				<input name="board_subject" id="board_subject" type="text" maxlength="100" class="form-control" value="${task.projectTitle}">
			</div>
			<div class="form-group">
				<label for="board_name">내용</label>
				<textarea name="board_content" id="board_content" class="form-control" rows="10"  >${task.projectContent}</textarea>
			</div>
			
			<c:if test="${boarddata.board_re_lev == 0}">
				<div class="form-group">
					<label>파일 첨부
						<img src="${pageContext.request.contextPath }/img/attach.png" alt="파일첨부" width="10px">
						<input type="file" id="upfile" name="board_file">
					</label>
					<span id="filevalue">${boarddata.board_file}</span>
					<img src="${pageContext.request.contextPath }/img/remove.png" alt="파일삭제" width="10px" class="remove">
				</div>
			</c:if>
			
			<div class="form-group">
				<button type=submit class="btn btn-primary">수정</button>
				<button type=reset class="btn btn-danger" onClick="history.go(-1)">취소</button>
			</div>
		</form>
	</div>
</body>
</html>
























