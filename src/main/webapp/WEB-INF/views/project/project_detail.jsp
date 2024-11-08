<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<script>
	    var contextPath = "${pageContext.request.contextPath}";
	    var projectName = "${left}";
	</script>
	<script src="${pageContext.request.contextPath }/js/project_detail.js"></script>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<style>
		.notification { height: 100%; }
		caption { caption-side: top; }
		.notification-content { border: 1px solid #ccc; border-radius: 8px; padding: 10px; height: 480px;}
		.progress {height: 30px;width: 100%;}
        .progress-bar {color: white;font-weight: bold;}
        .table { 
	        width: 100%; 
	        margin-bottom: 30px; 
	        table-layout: fixed; 
	    }
	    table, td, th {border-collapse: collapse;}
	    th, td {padding: 10px; text-align: left;}
	    th:nth-child(1), td:nth-child(1) {width: 20%; }
	    th:nth-child(2), td:nth-child(2) {width: 60%; }
	    th:nth-child(3), td:nth-child(3) {width: 20%; }
	    /* 전체 컨테이너 스타일 */
.container {
    max-width: 1200px;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 제목 스타일 */
h3, h5 {
    font-weight: 600;
    color: #343a40;
    margin-bottom: 20px;
}

/* 테이블 스타일 */
.table {
    width: 100%;
    margin-bottom: 30px;
    background-color: #ffffff;
    border: 1px solid #dee2e6;
    border-radius: 8px;
}

.table caption {
    caption-side: top;
    font-size: 1.2em;
    font-weight: bold;
    color: #495057;
    padding-bottom: 15px;
}

.table th, .table td {
    padding: 15px;
    text-align: left;
    border-top: 1px solid #dee2e6;
}

.table th {
    background-color: #e9ecef;
    font-weight: 600;
    color: #495057;
}

/* 진행률 바 스타일 */
.progress {
    height: 24px;
    border-radius: 8px;
    background-color: #e9ecef;
}

.progress-bar {
    font-weight: 600;
    color: #ffffff;
}

/* 알림 스타일 */
.notification {
    background-color: #ffffff;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.notification h5 {
    font-size: 1.1em;
    font-weight: 600;
    color: #343a40;
}

/* 모달 스타일 */
.modal-content {
    border-radius: 10px;
}

.modal-header, .modal-footer {
    border: none;
}

.modal-header h5 {
    font-weight: 600;
    color: #343a40;
}

.modal-body input, .modal-body textarea {
    border-radius: 8px;
}

.modal-footer .btn {
    padding: 8px 16px;
    font-weight: 600;
}
	    
	</style>
</head>
<body>
	<div class="container">
		<div class="d-flex justify-content-between align-items-center">
			<h3><c:out value="${left}"/></h3>
		</div>
		<hr style="margin-top: 40px;"> <!-- 진행률과의 간격 추가 -->
		<div class="row">
			<div class="col-md-8">
				<table class="table">
					<caption><h5>진행률</h5></caption>
					<thead>
						<tr>
							<th>참여자</th>
							<th>업무내용</th>
							<th>진행률</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="project" items="${project}">
						    <tr>
						        <td>${project.participantNames}</td>
						        <td><span class="task-content <c:if test='${project.isManager}'>clickable</c:if>" 
								          data-id="${project.projectId}" 
								          data-memberid="${project.memberId}" 
								          data-content="${project.memberDesign}">
								        ${empty project.memberDesign ? '업무을 지정해주세요' : project.memberDesign}
								    </span>
				                </td>
						        <td><span class="progress-rate <c:if test='${project.memberId == sessionScope.id}'>clickable</c:if> progress" 
								          data-id="${project.projectId}" data-memberid="${project.memberId}"
								          data-rate="${project.memberProgressRate}">
								    <span class="progress-bar" role="progressbar" style="width:${project.memberProgressRate}%">${project.memberProgressRate}%</span></span>
					            </td>
						    </tr>
						</c:forEach>
					</tbody>
				</table>
				<table class="table">
					<caption>
				        <div class="d-flex justify-content-between align-items-center">
				            <h5>진행 과정</h5>
				            <button type="button" class="btn btn-primary btn-sm float-right" data-toggle="modal" data-target="#writeModal">글 작성</button>
				        </div>
				    </caption>
					<thead>
						<tr>
							<th>작성자</th>
							<th>제목</th>
							<th>작성일자</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="project" items="${project2}"> 
						    <tr>
								<td>${project.taskWriter}</td>
								<td>${project.taskTitle}</td>
								<td>${project.taskUpdateDate}</td>
							</tr>
						</c:forEach> 
					</tbody>
				</table>
				<a href="${pageContext.request.contextPath}/project/progress" class="btn btn-info btn-sm float-right">상세보기</a> <!-- 제일 우측 하단에 링크 -->
			</div>
			<div class="col-md-4 notification">
				<h5>알림</h5>
				<div class="notification-content">
					<p>알림 내용이 여기에 표시됩니다.</p>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 업무 내용 입력 모달 -->
	<div class="modal fade" id="taskContentModal" tabindex="-1" role="dialog" aria-labelledby="taskContentModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="taskContentModalLabel">업무 내용 수정</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
	                <input type="text" id="taskContentInput" class="form-control" placeholder="업무 내용을 입력하세요" />
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	                <button type="button" class="btn btn-primary" id="saveTaskContentBtn">저장</button>
	            </div>
	        </div>
	    </div>
	</div>
		
	<!-- 진행률 입력 모달 -->
	<div class="modal fade" id="progressModal" tabindex="-1" role="dialog" aria-labelledby="progressModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="progressModalLabel">진행률 수정</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
	                <input type="number" id="progressInput" class="form-control" min="0" max="100" placeholder="진행률을 입력하세요 (0-100)" />
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	                <button type="button" class="btn btn-primary" id="saveProgressBtn">저장</button>
	            </div>
	        </div>
	    </div>
	</div>
	
	<!-- 글 작성 모달 -->
	<div class="modal fade" id="writeModal" tabindex="-1" role="dialog" aria-labelledby="writeModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="writeModalLabel">글 작성</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
	                <form id="writeForm" action="${pageContext.request.contextPath}/projectb/write" method="post" enctype="multipart/form-data">
	                    <div class="form-group">
	                        <label for="postTitle">제목</label>
	                        <input type="text" class="form-control" id="postTitle" name="title" placeholder="제목을 입력하세요" required>
	                    </div>
	                    <div class="form-group">
	                        <label for="postContent">내용</label>
	                        <textarea class="form-control" id="postContent" name="content" rows="4" placeholder="내용을 입력하세요" required></textarea>
	                    </div>
	                    <div class="form-group">
	                        <label for="postFile">첨부파일</label>
	                        <input type="file" class="form-control-file" id="postFile" name="file">
	                    </div>
	                    <div class="modal-footer">
	                        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	                        <button type="submit" class="btn btn-primary">저장</button>
	                    </div>
	                </form>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>