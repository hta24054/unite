<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>진행과정</title>
    <jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/css/project.css" type="text/css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath }/js/project_task_list.js"></script> 
    <script>
    	const contextPath = "${pageContext.request.contextPath}";
    </script>
    
</head>
<body>
   <input type="hidden" id="listType" value="list">
   <c:if test="${listcount > 0}">  
		<jsp:include page="limit.jsp"/>
	    <input type="hidden" class="memberId" value="${memberId}">
	    <table class="table member">
	        <caption><h2><c:out value="${left}"/> - <c:out value="${user}"/></h2></caption>
		    <thead>
		        <tr>
		            <th colspan="4" style="text-align:left"><c:out value="${user}"/> - 진행 과정 </th>
		            <th colspan="2">
		                <span>글 개수 : ${listcount }</span>
		            </th>
		        </tr>
		        <tr>
		            <th>번호</th>
		            <th>제목</th>
		            <th>내용</th>
		            <th>작성일</th>
		            <th>수정일</th>
		            <th>첨부파일</th>
		        </tr>
		    </thead>
		    <tbody id="boardContent">
		       
		    </tbody>
		</table>
    <jsp:include page="page.jsp"/>
	</c:if> <%--<c:if test="${listcount > 0 }"> end --%>
	<c:if test="${listcount == 0  && empty search_word}">
		<h1>등록된 글이 없습니다</h1>
	</c:if>
	<c:if test="${listcount == 0  && !empty search_word}">
		<h1>검색 결과가 없습니다</h1>
	</c:if>
</body>
</html>
