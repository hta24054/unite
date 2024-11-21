<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>진행과정</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath }/js/project_task_list.js"></script> 
    <script>
    	const contextPath = "${pageContext.request.contextPath}";
    </script>
    <jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
    <style>
        .table { width: 70%; margin: auto; }
        table, td, th { border-collapse: collapse; text-align: center;}
        h2 { text-align: left; color: black; margin: 0; }
        caption { caption-side: top; margin-bottom: 30px; }
        select.form-control{
			width: auto;
			margin-bottom: 2em;
			display: inline-block;
		}
		.rows{text-align: right; margin-right: 130px;}
		.gray{color: gray;}
		th:nth-child(1), td:nth-child(1) {width: 60px;}
		th:nth-child(2), td:nth-child(2) {width: 180px;}
		<%--th:nth-child(3), td:nth-child(3) {width: 360px;}--%>
		th:nth-child(4), td:nth-child(4) {width: 160px;}
		th:nth-child(5), td:nth-child(5) {width: 160px;}
		th:nth-child(6), td:nth-child(6) {width: 100px;}
		th:nth-child(7), td:nth-child(7) {width: 10px;}
    </style>
    
</head>
<body>
   <input type="hidden" id="listType" value="list">
   <c:if test="${listcount > 0}">  
		<jsp:include page="limit.jsp"/>
	    <input type="hidden" class="memberId" value="${memberId}">
	    <table class="table">
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
