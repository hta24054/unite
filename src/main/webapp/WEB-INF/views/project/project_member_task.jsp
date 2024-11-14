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

        function toggleOptions(index) {
            // 각 행의 숨겨진 optionsRow의 표시 상태를 전환
            const row = document.getElementById(`optionsRow-${index}`);
		    if (row) { // 요소가 존재할 때만 스타일 변경
		        if (row.style.display === "none") {
		            row.style.display = "table-row";
		        } else {
		            row.style.display = "none";
		        }
		    } else {
		        console.warn(`Element with ID optionsRow-${index} not found.`);
		    }
        }

        function editPost(projectId) {
            // 수정 기능
            alert("수정 기능입니다. Project ID: " + projectId);
            // 필요한 로직 추가
        }

        function deletePost(projectId) {
            // 삭제 기능
            alert("삭제 기능입니다. Project ID: " + projectId);
            // 필요한 로직 추가
        }

        function goToList() {
            // 목록 페이지로 이동
            alert("목록으로 이동합니다.");
            // 필요한 로직 추가
        }

        function replyPost(projectId) {
            // 답변 기능
            alert("답변 기능입니다. Project ID: " + projectId);
            // 필요한 로직 추가
        }
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
		            <th></th>
		        </tr>
		    </thead>
		    <tbody id="boardContent">
		        <c:set var="num" value="${listcount-(page-1)*limit}"/>
		        <c:forEach var="t" items="${boardlist}" varStatus="status"> 
				    <tr>
				        <td>
				            <c:out value="${num}"/>
				            <c:set var="num" value="${num - 1}"/>
				        </td>
				        <td>
				            <div>
				                <c:if test="${t.board_re_lev > 0}"> 
				                    <c:forEach var="a" begin="0" end="${t.board_re_lev * 2}" step="1">
				                        &nbsp;
				                    </c:forEach>
				                    <img src='${pageContext.request.contextPath}/image/line.gif'>
				                </c:if>
				                <a href="detail?num=${num}">
				                    <c:if test="${t.projectTitle.length() >= 20}">
				                        <c:out value="${t.projectTitle.substring(0, 20)}..."/>
				                    </c:if>
				                    <c:if test="${t.projectTitle.length() < 20}">
				                        <c:out value="${t.projectTitle}"/>
				                    </c:if>
				                </a>[${t.board_cnt}]
				            </div>
				        </td>
				        <td><div>${t.projectContent}</div></td>
				        <td><div>${t.projectDate}</div></td>
				        <td><div>${t.projectUpdateDate}</div></td>
				        <td><div><a href="down?filename=${t.task_file_uuid }${t.task_file_type}&originalFilename=${t.task_file_original}">${t.task_file_original}</a></div></td>
				        <td>
				        	
				            	<img src="${pageContext.request.contextPath}/image/plus.png" width="20px" onclick="toggleOptions(${status.index})">
				            
				        </td>
				    </tr>
				    <!-- 숨겨진 버튼 행 -->
				    <tr id="optionsRow-${status.index}" style="display: none;">
				        <td colspan="7" style="text-align: center;">
				            <c:if test="${memberId == user}">
				                <button onclick="editPost(${t.projectId})">수정</button>
				                <button onclick="deletePost(${t.projectId})">삭제</button>
				            </c:if>
				            <button onclick="goToList()">목록</button>
				            <button onclick="replyPost(${t.projectId})">답변</button>
				        </td>
				    </tr>
				</c:forEach>
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
