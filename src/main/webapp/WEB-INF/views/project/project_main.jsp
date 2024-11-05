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
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<style>
		.table{width: 70%; margin: auto;}
		table, td, th{border-collapse : collapse;}
		h2 { text-align: left; color: black; margin: 0; } /* h2의 기본 여백 제거 */
        caption { caption-side: top; margin-bottom: 30px; } /* 캡션과 테이블 간격 설정 */
        .progress {height: 30px;width: 100%;}
        .progress-bar {color: white;font-weight: bold;}
	</style>
	<script>
	$(document).ready(function() {
	    // 페이지 로딩 시 진행 중인 프로젝트 데이터 가져오기
	    loadOngoingProjects();

	    // 상태 변경 시 저장 버튼 표시
	    $(document).on('change', '.project-status', function() {
	        var selectedValue = $(this).val();
	        var saveButton = $(this).closest('tr').find('.save-status');
	        if (selectedValue === 'completed' || selectedValue === 'cancelled') {
	            saveButton.show(); // 완료 또는 취소 시 저장 버튼 보이기
	        } else {
	            saveButton.hide(); // 진행 중일 경우 저장 버튼 숨기기
	        }
	    });

	    // 저장 버튼 클릭 이벤트
	    $(document).on('click', '.save-status', function() {
	        var projectId = $(this).data('project-id');
	        var status = $(this).closest('tr').find('.project-status').val();

	        var confirmationMessage = status === 'completed' ? 
	            "정말 프로젝트를 종료하시겠습니까?" : 
	            "정말 이 프로젝트를 취소하시겠습니까?";

	        if (confirm(confirmationMessage)) {
	            // AJAX 요청으로 상태 업데이트
	            $.ajax({
	                url: "${pageContext.request.contextPath}/project/main",
	                type: "POST",
	                data: {
	                    projectId: projectId,
	                    status: status,
	                    action: 'updateStatus' // 추가된 action 파라미터
	                },
	                success: function(response) {
	                    console.log(response); // 서버 응답 확인
	                    if (response.success) {
	                        alert("상태가 업데이트되었습니다.");
	                        // 상태 업데이트 후 진행 중인 프로젝트 목록 다시 불러오기
	                        loadOngoingProjects();
	                    } else {
	                        alert("상태 업데이트에 실패했습니다.");
	                    }
	                },
	                error: function() {
	                    alert("서버 오류 발생.");
	                }
	            });
	        }
	    });
	});

	// 진행 중인 프로젝트 데이터 로드 함수
	function loadOngoingProjects() {
	    $.ajax({
	        url: "${pageContext.request.contextPath}/project/getOngoingProjects",
	        type: "GET",
	        success: function(data) {
	            // 테이블에 데이터 추가
	            var tbody = $(".table tbody");
	            tbody.empty(); // 기존 데이터 제거
	            $.each(data, function(index, project) {
	                tbody.append("<tr>" +
	                    "<td>" + project.projectId + "</td>" +
	                    "<td><a href='" + "${pageContext.request.contextPath}/project/detail?projectId=" + project.projectId + "'>" + project.projectName + "</a></td>" +
	                    "<td>" + project.memberCount + "명</td>" +
	                    "<td>" +
	                        "<div class='progress'>" +
	                            "<div class='progress-bar' role='progressbar' style='width: " + project.progressRate + "%;'>" +
	                                project.progressRate + "%" +
	                            "</div>" +
	                        "</div>" + 
	                    "</td>" +
	                    "<td>" + project.endDate + "</td>" +
	                    "<td>" +
	                    "<select class='form-control project-status' data-project-id='" + project.projectId + "'>" +
	                    "<option value='ongoing'>진행 중</option>" +
	                    "<option value='completed'>완료</option>" +
	                    "<option value='cancelled'>취소</option>" +
	                    "</select>" +
	                    "</td>" +
	                    "<td><button class='btn btn-success save-status' data-project-id='" + project.projectId + "' style='display:none;'>저장</button></td>" +
	                    "</tr>");
	            });

	            // 상태 변경 시 저장 버튼 표시
	            $('.project-status').on('change', function() {
	                var selectedValue = $(this).val();
	                var saveButton = $(this).closest('tr').find('.save-status');
	                if (selectedValue === 'completed' || selectedValue === 'cancelled') {
	                    saveButton.show(); // 완료 또는 취소 시 저장 버튼 보이기
	                } else {
	                    saveButton.hide(); // 진행 중일 경우 저장 버튼 숨기기
	                }
	            });
	        },
	        error: function() {
	            alert("프로젝트 데이터를 불러오는 데 실패했습니다.");
	        }
	    });
	}

	</script>
</head>
<body>
	<table class="table">
		<caption><h2>진행 중 프로젝트</h2></caption>
		<thead>
			<tr>
				<th>프로젝트 코드</th>
				<th>프로젝트 이름</th>
				<th>참여자</th>
				<th>진행률</th>
				<th>마감일</th>
				<th>관리</th>
			</tr>
		</thead>
		<tbody>
		   
		</tbody> 
	</table>
	
	<!-- 페이지네이션 추가 -->
	<div class="center-block">
		<ul class="pagination justify-content-center">
			<li class="page-item">
				<a ${page > 1 ? 'href=list?page=' += (page-1) : '' } class="page-link ${page <= 1 ? 'gray' : '' }">이전&nbsp;</a>
			</li>
			<c:forEach var="a" begin="${startpage }" end="${endpage }">
				<li class="page-item ${a == page ? 'active' : '' }">
					<a ${a == page ? '' : 'href=list?page=' += a } class="page-link">${a }</a>
				</li>
			</c:forEach>
			<li class="page-item">
				<a ${page < maxpage ? 'href=list?page=' += (page+1) : '' } class="page-link ${page >= maxpage ? 'gray' : '' }">&nbsp;다음</a>
			</li>
		</ul>
	</div>

	<%-- 게시글이 없는 경우 --%>
	<c:if test="${listcount == 0 }">
		<h3 style="text-align:center">등록된 글이 없습니다</h3>
	</c:if>
</body>
</html>