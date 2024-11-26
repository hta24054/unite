<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardView.css" type="text/css">
<script src="${pageContext.request.contextPath}/js/boardView.js"></script>
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
	display: flex;
    align-items: center;
    gap: 5px;
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
	margin-left: 20px;
    margin-bottom: 1px;
}

.board_view_content{
	min-height: 250px;
    padding: 20px 20px 30px;
    font-size: 22px;
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

.boardViewCommentscontainer ,.comment-order-list{
	padding-right: 40px;
}
.boardViewCommentscontainer{
	border-top: 1px solid #ccc;
}
	
.viewIcon{
	border: 1px solid gray;
}

.page-link{
	display:inline-block; 
	color:#212529; 
	line-height:1.5;
}

.count{
	color: #a9a9a9;
}

.layer-item{
	padding: 0;
}

.LayerMore{
	padding: 10px;
}

.layer-list{
	display: flex;
    justify-content: space-around;
}

#upfile{
	display: none;
}

.text-comment{
	word-break: break-all;
}
</style>
</head>
<body>
	<input type="hidden" id="empId" value="${id}" name="empId"><%--view.js에서 사용하기위해 추가합니다. --%>
	<div class="boardViewcontainer">
	    <table class="table">
    		<tr>
    			<td colspan="3" class="board_view_header" style="padding: 12px 25px 0px;">
    				<span class="left_subeject" style="word-break: break-all;">
	    				<c:out value="${postDataAndFile[0].postSubject}"/>
	    				<span class="count" style="font-size: 25px;"><c:out value="[${postDataAndFile[0].postCommentCnt}]"/></span>
    				</span>
    				
    				<span class="right_btns">
    					<c:if test ="${postDataAndFile[0].postWriter == id||id == 'admin'}">
	    					<button class="btn board_view_btn postModify-link" 
	    							data-page="post/modify?id=${postDataAndFile[0].postId}">
	    							수정
	    					</button>
	    					
	    					<%-- href의 주소를 #으로 설정합니다. --%>
    						<button class="btn board_view_btn" data-toggle="modal"
    							data-target="#myModal">삭제</button>
	    				</c:if>
   						<button class="btn board_view_btn postReply-link"
   								data-page="post/reply?num=${postDataAndFile[0].postId}">답글</button>
   								
	   					<button class="btn board_view_btn page-link" data-page='1'
	   							data-name='${boardMap[postDataAndFile[0].boardId]}'>목록</button>
	   					
    				</span>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3" style="padding-top: 10px;">
    				<img src="${pageContext.request.contextPath}/emp/profile-image?UUID=${postDataAndFile[1].imgUUID}" alt="프로필 이미지" class="icon viewIcon">
				    <div class="username">${empMap[postDataAndFile[0].postWriter]}</div>
				    <div class="date"> ${postDataAndFile[0].getFormattedPostDate()} </div>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3">
    				<div class="board_view_content" style="word-break: break-all;">${postDataAndFile[0].postContent}</div>
    			</td>
    		</tr>
    		
    		<%-- 파일을 첨부한 경우 --%>
   			<c:if test="${postDataAndFile[2] != null}">
    			<c:forEach var="postFile" items="${postDataAndFile[2]}">
    				<tr>
		    			<td colspan="3" style="padding: 0px 12px 0px 12px;">
		    				<img src="${pageContext.request.contextPath}/image/attach.png" style="width:13px; margin: 0px 5px 0px 20px;">
		    				<a href="post/down?postFileId=${postFile.postFileId}">${postFile.postFileOriginal}</a>
		    			</td>
   					</tr>
	    		</c:forEach>
   			</c:if>
    		<tr>
    			<td colspan="3" style="padding-bottom:12px;">
					<div class="file-meta">
						<img src='/unite/image/comments.png' alt="프로필 이미지" class="icon" style="margin: -1px -4px 0px 15px; width: 30px;height: 30px;"/>
						<span class="comments">
							댓글
							<sup class="count" id="count" style="font-family: arial, sans-serif;"></sup>개</span>
						<span style="color:#ccc;">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
						조회<span class="count"> ${postDataAndFile[0].postView}</span>
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
    					<form name="deleteForm" action="post/delete" method="post">
    						<%-- http://localhost:8088/Board_Ajax/boards/detail?num=22
    							 주소를 보면 num을 파라미터로 넘기고 있습니다.
    							 이 값을 가져와서 ${param.num}를 사용 또는 ${boarddata.board_num}
    						 --%>
    						 <input type="hidden" name="num" value="${param.num}"
    						 		id="postId">
    						 <div class="form-group">
    						 	<label>정말 삭제하시겠습니까? <br> 복구가 불가능합니다.</label>
    						 </div>
    						 <button type="submit" class="btn btn-primary">삭제</button>
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
			<div class="comment-write-container">
				<span class="comment-photo">
					<img src="${pageContext.request.contextPath}/emp/profile-image?UUID=${sessionScope.profileUUID}" class="profileImg" width=36 height=36>
				</span>
				
			 	<div class="comment-write">
					<div class="comment-write-area">
						<b class="comment-write-area-name">${empMap[id]}</b> 
						<span class="comment-write-area-count">0/200</span>
						<textarea placeholder="댓글을 남겨보세요" rows="1" class="comment-write-area-text" maxlength="200"></textarea>
							
					</div>
					<div class="comment-attachFile"></div>
					<div class="register-box">
						<span class="emoji_btn" id="emoji_btn">
							<img src="${pageContext.request.contextPath}/image/insert_emoticon.png" width=20 height=20 style="opacity: 0.5">
						</span>
						<label class="emoji_btn" id="emoji_btn2" style="margin:0;">
							<img src="${pageContext.request.contextPath}/image/attach.png" width=20 height=20 style="opacity: 0.5">
							<input type="file" id="upfile"/>
						</label>
						<div class="button btn-cancel">취소</div><%-- 댓글의 취소는 display:none, 등록만 보이도록 한다. --%>
						<div class="button btn-register">등록</div>
					</div>
			 	</div><%-- comment-write end --%>
		 	</div>
		</div><%-- comment-area end --%>
	</div>
	
</body>
</html>