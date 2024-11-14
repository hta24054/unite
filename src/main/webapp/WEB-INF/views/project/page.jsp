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