<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="${pageContext.request.contextPath}/js/writeform.js"></script>
<style>
	h1{font-size: 1.5rem; text-align: center; color:#1a92b9}
	.container{width:60%}
	label{font-weight: bold}
	#upfile{display: none}
	img{width: 20px;}
	.attachFlie{border: 2px dotted;}
</style>
</head>
<body>
 	<form action="add" method="post" enctype="multipart/form-data"
 			name="boardform">
 		<div class="form-group">
 			<label for="target_board">
 				To.
 				<select id="boardName1">
 					<option value="전사게시판">전사게시판</option>
 					<option value="일반게시판">일반게시판</option>
 					<option value="부서게시판">부서게시판</option>
 				</select>
 				<select id="boardName2">
 					<option value="공지사항">공지사항</option>
 					<option value="주간식단표">주간식단표</option>
 					<option value="FAQ">FAQ</option>
 				</select>
 			</label>
 		</div>
 		<div class="form-group">
 			<label for="board_subject">제목</label>
 			<input name="board_subject" id="board_subject" type="text" maxlength="100"
 					class="form-control" placeholder="Enter board_subject">
 		</div>
 		<div class="form-group">
 			<label for="board_attachFile">파일첨부</label>
 			<div class="attachFlie">
 				<img src="${pageContext.request.contextPath}/image/attach.png" alt="파일첨부">
 				이 곳에 파일을 드래그 하세요. 또는 
	 			<label>
	 				파일선택
	 				<input type="file" id="upfile" name="board_file">
	 			</label>
	 			<span id="filevalue"></span>
 			</div>
 		</div>
 		<div class="form-group">
 			<label for="board_content">내용</label>
 			<textarea name="board_content" id="board_content"
 					rows="10" class="form-control"></textarea>
 		</div>
 		<div class="form-group">
 			<button type="submit" class="btn btn-primary">등록</button>
 		</div>
 	</form>
  
</body>
</html>