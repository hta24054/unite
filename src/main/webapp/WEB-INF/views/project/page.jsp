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
	    <!-- ���� ��ư -->
	    <li class="page-item">
	      <c:choose>
	        <c:when test="${page > 1}">
	          <a href="list?page=${page - 1}&memberId=${memberId}" class="page-link">����&nbsp;</a>
	        </c:when>
	        <c:otherwise>
	          <a class="page-link gray">����&nbsp;</a>
	        </c:otherwise>
	      </c:choose>
	    </li>
	
	    <!-- ������ ��ȣ -->
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
	
	    <!-- ���� ��ư -->
	    <li class="page-item">
	      <c:choose>
	        <c:when test="${page < maxpage}">
	          <a href="list?page=${page + 1}&memberId=${memberId}" class="page-link">����&nbsp;</a>
	        </c:when>
	        <c:otherwise>
	          <a class="page-link gray">����&nbsp;</a>
	        </c:otherwise>
	      </c:choose>
	    </li>
	  </ul>
	</div>
</body>
</html>