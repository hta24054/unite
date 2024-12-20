<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/leftbar.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/project.css">
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
	<div class="rows">
		<span>줄보기</span>
		<select class="form-control" id="viewcount">
			<option value="1">1</option>
			<option value="3">3</option>
			<option value="5" selected>5</option>
		</select>
	</div>
	<table class="table">
		<caption><h2>진행 중 프로젝트</h2></caption>
		<thead>
			<tr>
				<th>코드명</th>
				<th>프로젝트명</th>
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
	
	<!-- 첨부파일 모달 -->
	<div id="fileModal" class="modal fade" tabindex="-1" role="dialog">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title">첨부파일 등록</h5>
	                <button type="button" class="close" data-dismiss="modal">&times;</button>
	            </div>
	            <div class="modal-body">
	                <p>최종 첨부파일을 등록하시겠습니까?</p>
	                <input type="file" id="fileInput" name="file" multiple>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-primary" id="confirmFileUpload">저장</button>
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	            </div>
	        </div>
	    </div>
	</div>
		
</body>
</html>