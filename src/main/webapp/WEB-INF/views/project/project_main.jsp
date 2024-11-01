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
	</style>
	<script>
		$(document).ready(function() {
		    // 페이지 로딩 시 진행 중인 프로젝트 데이터 가져오기
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
		                    //"<td><a href='${pageContext.request.contextPath}/project/detail/id=?" + project.projectName + "'>" + project.projectName + "</a></td>" +
		                    "<td><a href='${pageContext.request.contextPath}/project/detail'>" + project.projectName + "</a></td>" +
		                    "<td>" + project.memberCount + "명</td>" +
		                    "<td>" + project.progressRate + "</td>" +
		                    "<td>" + project.endDate + "</td>" +
		                    "</tr>");
		            });
		        },
		        error: function() {
		            alert("프로젝트 데이터를 불러오는 데 실패했습니다.");
		        }
		    });
		});
	</script>
</head>
<body>
	<table class="table">
		<caption><h2>진행 중 프로젝트<h2></caption>
		<thead>
			<tr>
				<th>프로젝트 코드</th>
				<th>프로젝트 이름</th>
				<th>참여자</th>
				<th>진행률</th>
				<th>마감일</th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach var="project" items="${ongoingProjects}">
		        <tr>
		            <td>${project.projectId}</td>
		            <td><a href="${pageContext.request.contextPath}/project/detail/${project.projectId}">${project.projectName}</a></td>
		            <td>${project.memberCount}명</td>
		            <td>${project.progressRate}</td>
		            <td>${project.endDate}</td>
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
	
	<!-- 수정 -->
	<div class = "center-block">
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
</body>
</html>