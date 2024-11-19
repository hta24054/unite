<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardView.css" type="text/css">
<style>
.boardViewcontainer{
	width:100%;
	margin: 0;
	padding: 0px 10px;
}
.board_view_header{
	display: flex;
    justify-content: space-between;
    align-items: center;
}
.left_subeject{
	font-size: 2rem;
	text-align: left;
}
.right_btns{
    padding-right: 15px;
    text-align: right;
}
.board_view_btn{
	border: 1px solid #afafaf;
    padding: 5px 20px;
    margin-left: 5px;
}

.table td{
	border: none;
	padding: 12px 12px 0px 12px;
}

.icon , .username, .date{
	display: inline;
}

.username{
	font-weight: 600;
    padding-right: 15px;
}

.icon{
	margin: 0px;
}

.board_view_content{
	min-height: 250px;
	padding: 20px 0px 30px;
}

.preview-btn,.download-btn{
	font-size: 11px;
	padding: 0 4px;
	border: 1px solid #ccc;
	margin-left:5px; 
}

sup {
    font-size: 16px;
    top: 0px;
}

.comment-head{
	display: none;
}

.boardViewCommentscontainer{
	padding: 0px 40px;
	border-top: 1px solid #ccc;
}
</style>
</head>
<body>
	<input type="hidden" id="loginid" value="${id}" name="loginid"><%--view.js에서 사용하기위해 추가합니다. --%>
	<div class="boardViewcontainer">
	    <table class="table">
    		<tr>
    			<td colspan="3" class="board_view_header">
    				<span class="left_subeject">
	    				<c:out value="${postDataAndFile[0].postSubject}"/>
	    				<c:out value="[${postDataAndFile[0].postCommentCnt}]"/>
    				</span>
    				
    				<span class="right_btns">
    					<c:if test ="${postDataAndFile[0].postWriter == id||id == 'admin'}">
	    					<button class="btn board_view_btn postModify-link" 
	    							data-page="post/modify?id=${postDataAndFile[0].postId}">
	    							수정
	    					</button>
	    					
	    					<%-- href의 주소를 #으로 설정합니다. --%>
	    					<a href="#">
	    						<button class="btn board_view_btn" data-toggle="modal"
	    							data-target="#myModal">삭제</button>
	    					</a>
	    				</c:if>
	   					<a href="reply?num=${boarddata.board_num}">
	   						<button class="btn board_view_btn">답글</button>
	   					</a>
	    				<a href="list">
	   						<button class="btn board_view_btn">목록</button>
	   					</a>
    				</span>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3" style="padding-top: 10px;">
    				
    				<img src=${'/unite/image/profile_black.png'} alt="프로필 이미지" class="icon">
				    <div class="username">${empMap[postDataAndFile[0].postWriter]}</div>
				    <div class="date"> ${postDataAndFile[0].getFormattedPostDate()} </div>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3">
    				<div class="board_view_content">${postDataAndFile[0].postContent}</div>
    			</td>
    		</tr>
    		
	    	<%--원문글인 경우에만 첨부파일을 추가 할 수 있습니다. --%>
    		<c:if test="${postDataAndFile[0].postReLev==0}">
	    		<%-- 파일을 첨부한 경우 --%>
    			<c:if test="${postDataAndFile[2] != null}">
	    			<c:forEach var="postFile" items="${postDataAndFile[2]}">
	    				<tr>
			    			<td colspan="3" style="padding: 0px 12px 0px 12px;"><img src="${pageContext.request.contextPath}/image/attach.png" style="width:13px; margin: 0px 5px 0px 20px;">
			    				<a href="down?postFileId=${postFile.postFileId}">${postFile.postFileOriginal}</a>
			    				<button class="preview-btn" onclick="previewFile('${postFile.postFileId}')">미리보기</button>
      							<button class="download-btn" onclick="location.href='down?postFileId=${postFile.postFileId}'">다운로드</button>
			    			</td>
    					</tr>
		    		</c:forEach>
    			</c:if>
    		</c:if>
    		<tr>
    			<td colspan="3" style="padding-bottom:12px;">
					<div class="file-meta">
						<img src='/unite/image/comments.png' alt="프로필 이미지" class="icon" style="margin-left: 15px;margin-bottom: 1px;"/>
						<span class="comments">
							댓글&nbsp;
							<sup id="count" style="font-family: arial, sans-serif;"></sup>
							개</span>
						<span style="color:#ccc;">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
						<span class="views">조회 ${postDataAndFile[0].postView}</span>
					</div>
    			</td>
    		</tr>
    	</table>
    	<%-- 게시판 view end --%>
    	
    	<%-- modal 시작 --%>
    	<div class="modal" id="myModal">
    		<div class="modal-dialog">
    			<div class="modal-content">
    				<div class="modal-body">
    					<form name="deleteForm" action="delete" method="post">
    						<%-- http://localhost:8088/Board_Ajax/boards/detail?num=22
    							 주소를 보면 num을 파라미터로 넘기고 있습니다.
    							 이 값을 가져와서 ${param.num}를 사용 또는 ${boarddata.board_num}
    						 --%>
    						 <input type="hidden" name="num" value="${param.num}"
    						 		id="comment_board_num">
    						 <div class="form-group">
    						 	<label for="board_pass">비밀번호</label>
    						 	<input type="password" class="form-control"
    						 			placeholder="Enter password"
    						 			name="board_pass" id="board_pass">
    						 </div>
    						 <button type="submit" class="btn btn-primary">전송</button>
    						 <button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
    					</form>
    				</div>
    			</div>
    		</div>
    	</div>
    </div><%-- <div class="container"> end --%>
    
    <div class="boardViewCommentscontainer">
		<%--댓글창 시작 --%>
		<div class="comment-area">
			<div class="comment-head">
				<div class="comment-order">
					<ul class="comment-order-list">
					</ul>
				</div>
			</div><!-- comment-head end-->
			<ul class="comment-list">
			</ul>
		 	<div class="comment-write">
				<div class="comment-write-area">
					<b class="comment-write-area-name">${empMap[postDataAndFile[0].postWriter]}</b> 
					<span class="comment-write-area-count">0/200</span>
					<textarea placeholder="댓글을 남겨보세요" rows="1" class="comment-write-area-text" maxlength="200"></textarea>
						
				</div>
				<div class="register-box">
					<div class="button btn-cancel">취소</div><%-- 댓글의 취소는 display:none, 등록만 보이도록 한다. --%>
					<div class="button btn-register">등록</div>
				</div>
		 	</div><%-- comment-write end --%>
		</div><%-- comment-area end --%>
	</div>
</body>
</html>