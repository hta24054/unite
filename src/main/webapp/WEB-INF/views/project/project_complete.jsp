<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>완료 프로젝트</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath }/js/project_list.js"></script> 
    <script>
    	const contextPath = "${pageContext.request.contextPath}";
    </script>
    <jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
    <style>
        .table { width: 70%; margin: auto; }
        table, td, th { border-collapse: collapse; }
        h2 { text-align: left; color: black; margin: 0; }
        caption { caption-side: top; margin-bottom: 30px; }
        select.form-control{
			width: auto;
			margin-bottom: 2em;
			display: inline-block;
		}
		.rows{text-align: right; margin-right: 130px;}
		.gray{color: gray;}
    </style>
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
    <input type="hidden" class="memberId" value="${memberId}">
    <table class="table">
        <caption><h2>완료 프로젝트</h2></caption>
        <thead>
            <tr>
                <th>코드명</th>
                <th>프로젝트명</th>
                <th>책임자</th>
                <th>참여자</th>
                <th>시작일</th>
                <th>마감일</th>
                <th>첨부파일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="project" items="${completedProjects}">
            <tr>
                <td>${project.projectId}</td>
                <td>${project.projectName}</td>
                <td>${project.empName}</td>
                <td>
                    <c:set var="numParticipants" value="${fn:length(project.participantNames)}" />
                    <c:if test="${numParticipants > 0}">
                        ${project.participantNames[0]}
                        <c:if test="${numParticipants > 1}">
                            외 ${numParticipants - 1}명
                        </c:if>
                    </c:if>
                </td>
                <td>${project.projectStartDate}</td>
                <td>${project.projectEndDate}</td>
                <td>${project.projectFilePath}</td>
            </tr>
       		</c:forEach>
        </tbody>
    </table>
    <div class="center-block">
		  <ul class="pagination justify-content-center">
		    <!-- 이전 버튼 -->
		    <li class="page-item">
		      <c:choose>
		        <c:when test="${page > 1}">
		          <a href="list?page=${page - 1}&memberId=${memberId}" class="page-link">이전&nbsp;</a>
		        </c:when>
		        <c:otherwise>
		          <a class="page-link gray">이전&nbsp;</a>
		        </c:otherwise>
		      </c:choose>
		    </li>
		
		    <!-- 페이지 번호 -->
		    <c:forEach var="a" begin="${startpage}" end="${endpage}">
		      <li class="page-item ${a == page ? 'active' : ''}">
		        <c:choose>
		          <c:when test="${a != page}">
		            <a href="list?page=${a}&memberId=${memberId}" class="page-link">${a}</a>
		          </c:when>
		          <c:otherwise>
		            <a class="page-link">${a}</a>
		          </c:otherwise>
		        </c:choose>
		      </li>
		    </c:forEach>
		
		    <!-- 다음 버튼 -->
		    <li class="page-item">
		      <c:choose>
		        <c:when test="${page < maxpage}">
		          <a href="list?page=${page + 1}&memberId=${memberId}" class="page-link">다음&nbsp;</a>
		        </c:when>
		        <c:otherwise>
		          <a class="page-link gray">다음&nbsp;</a>
		        </c:otherwise>
		      </c:choose>
		    </li>
		  </ul>
		</div>
</body>
</html>
