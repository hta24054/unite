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
   <c:if test="${listcount > 0}">  
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
	        <caption><h2><c:out value="${left}"/> - <c:out value="${user}"/></h2></caption>
	        <thead>
	       		<tr>
					<th colspan="4">MVC 게시판 - list</th>
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
	       		<c:set var="num" value="${listcount-(page-1)*limit}"/>
				<c:forEach var="t" items="${boardlist }" varStatus="status"> 
					<tr>
						<td>
							<c:out value="${num }"/> <%--num출력--%>
							<c:set var="num" value="${num-1}"/>
						</td>
						<td>
							<div>
								<c:if test="${t.board_re_lev > 0 }"> 
									<c:forEach var="a" begin="0" end="${t.board_re_lev*2 }" step="1">
										&nbsp;
									</c:forEach>
									<img src = '${pageContext.request.contextPath }/image/line.gif'>
								</c:if>
								<a href="detail?num=${t.projectId }">
									<c:if test="${t.projectTitle.length()>=20 }">
										<c:out value="${t.projectTitle.substring(0,20) }..."/>
									</c:if>
									<c:if test="${t.projectTitle.length()<20 }">
										<c:out value="${t.projectTitle}"/>
									</c:if>
								</a>[${t.board_cnt }] 
							</div>
						</td>
						<td><div>${t.projectContent}</div></td>
						<td><div>${t.projectDate }</div></td>
						<td><div>${t.projectUpdateDate }</div></td>
						<td><div>${t.board_file }</div></td>
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
	</c:if> <%--<c:if test="${listcount > 0 }"> end --%>
	<c:if test="${listcount == 0  && empty search_word}">
		<h1>등록된 글이 없습니다</h1>
	</c:if>
	<c:if test="${listcount == 0  && !empty search_word}">
		<h1>검색 결과가 없습니다</h1>
	</c:if>
</body>
</html>
