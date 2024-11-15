<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>취소 프로젝트</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/project_left.css">
    
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath }/js/project_cancel_list.js"></script> 
    <script>	
    	const contextPath = "${pageContext.request.contextPath}";    
   	</script>
    <jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>

</head>
<body>
	<jsp:include page="limit.jsp"/>
    <table class="table">
        <caption><h2>취소 프로젝트</h2></caption>
        <thead>
            <tr>
                <th>코드명</th>
                <th>프로젝트명</th>
                <th>책임자</th>
                <th>참여자</th>
                <th>열람자</th>
                <th>시작일</th>
                <th>취소일</th>
                <th>첨부파일</th>
            </tr>
        </thead>
        <tbody>
           
        </tbody>
    </table>
    <jsp:include page="page.jsp"/>
</body>
</html>
