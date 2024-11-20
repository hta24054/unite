<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="${pageContext.request.contextPath}/js/writeform.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.css" rel="stylesheet">
<style>
	h1{font-size: 1.5rem; text-align: center; color:#1a92b9}
	.container{width:60%}
	label{font-weight: bold}
	#upfile{display: none}
	img{width: 20px;}
	.attachFile{
		border: 1px dashed;
		display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 80px;
	    width: 85%;
	    border-radius: 10px;
	}
	
	.fileLabel{
		margin:0px 3px;
		cursor: pointer;
	}
	
	.file-list {
	    display: none;
	    height: 130px;
	    overflow: auto;
	    border: 1px solid #989898;
	    padding: 10px;
	    margin: 0px 0px 1rem 15%;
	    border-radius: 10px;
	}
	.file-list .filebox p {
	    font-size: 14px;
	    margin-top: 10px;
	    display: inline-block;
	}
	.file-list .filebox .delete i{
	    color: #ff5353;
	    margin-left: 5px;
	}
	.form-group2,.form-group2-file{
		margin-bottom: 1rem;
	    display: flex;
	    align-items: center;
	    justify-content: space-between;
	}
	.boardName{
		word-wrap: normal;
	    padding: 5px 15vh 5px 3px;
	    margin: 0px 5px;
	    font-size: 18px;
	    width: 300px;
	    border: 1px solid #ccc;
	}
	.form-control2{
		width: 85%;
	}
	.form-group-btn{
		display: flex;
	    justify-content: center;
	    height: 70px;
	    align-items: center;
	    margin-top: 10px;
	}
	.registerBtn{
		width: 100px;
	    background: white;
	    color: #7f7f7f;
	    border: 1.5px solid #9d9c9c;
	    font-weight: bolder;
	}
	.labelName{
		margin: 0px;
	    font-size: 18px;
	    font-weight: 600;
	    padding: 0px 0px 0px 10px;
	}
	.dragoverFile{
        border:3px solid #334466;
        z-index : 1;
	}
	.hidden {
    	display: block;
	}
	
</style>
<script>
var fileNo = 0;
var filesArr = [];
var maxFileCnt = 5;   // 첨부파일 최대 개수

var companyBulletinBoards = ['공지사항', '주간식단표', 'FAQ'];
var DepartmentBoards = ['솔루션영업팀'];

/* 첨부파일 추가 */
function addFiles(files) {
    var attFileCnt = $('.filebox').length;
    var remainFileCnt = maxFileCnt - attFileCnt;
    var curFileCnt = files.length;

    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
        return;
    }

    for (const file of files) {
    	if (validation(file)) {
            fileListAppendHtml(file);// fileList 추가
        }
    }

    updateFileListVisibility();
    $("input:file").val(""); // 파일 입력 필드 초기화
}

//addFile과 handleFiles에서 공통된 파일 처리 로직
function addFile(obj) {
    addFiles(obj.files);
}

function handleFiles(dragFiles) {
    addFiles(dragFiles);
}

/* fileList html 추가 */
function fileListAppendHtml(file){
	// 파일 객체에 고유 fileNo 값을 추가해 배열에 저장
	file.fileNo = fileNo;
	filesArr.push(file);
	
	// 목록 추가
	let htmlData = '';
	htmlData += '<div id="file' + fileNo + '" class="filebox">';
	htmlData += '   <p class="name">' + file.name + '</p>';
	htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><img src="${pageContext.request.contextPath}/image/delete.png"/></a>';
	htmlData += '</div>';
	$('.file-list').append(htmlData);
	
	fileNo++; // 값 증가
}

/* 첨부파일 검증 */
function validation(file) {
    const maxSize = 100 * 1024 * 1024; // 100MB
    if (file.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
    } else if (file.size > maxSize) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
    } else if (!file.name.includes('.')) {
        alert("확장자가 없는 파일은 제외되었습니다.");
    } else {
        return true;
    }
    return false;
}

/* 첨부파일 삭제 */
function deleteFile(deleteNo) {

    // filesArr 배열에서 삭제할 파일을 fileNo로 찾음
    filesArr = filesArr.filter(file => file.fileNo !== deleteNo);

    // 파일 목록에서 삭제
    $("#file" + deleteNo).remove();

    // 파일 목록이 비어 있으면 숨김 처리
    updateFileListVisibility();
}

/* 파일 목록이 비어있으면 숨김 처리 */
function updateFileListVisibility() {
	console.log(filesArr.length)
    $('.file-list').toggleClass('hidden', filesArr.length !== 0);
}


/* 폼 전송 */
function submitForm() {
    var form = document.querySelector("form[name='boardform']");
    var formData = new FormData(form);
    var j = 0;

    // 삭제되지 않은 파일만 폼데이터에 담기
    filesArr.forEach(file => {
        formData.append("attach_file" + j++, file);
    });

    $.ajax({
        method: 'POST',
        url: '../board/post/add',
        dataType: 'json',
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            alert(data.message);
            location.href = "home";
        },
        error: function (xhr, desc, err) {
            alert('에러가 발생하였습니다.');
            location.href = "home";
        }
    });
}

//boardName2를 boardName1에 맞게 바꿈
function changeBoardName2(boardName1Value){
	let $boardName2 = $('#boardName2'); // 두 번째 select 초기화
    $boardName2.empty();

	if (boardName1Value === '전사게시판') {
		$.each(companyBulletinBoards, function(index, companyBulletinBoard) {
			$boardName2.append('<option value="' + companyBulletinBoard + '">' + companyBulletinBoard + '</option>');
		});
	}else if (boardName1Value === '일반게시판') {
		$boardName2.append('<option value="' + '일반게시판' + '">' + '일반게시판' + '</option>');
	}else if (boardName1Value === '부서게시판') {
		$.each(DepartmentBoards, function(index, DepartmentBoard) {
			$boardName2.append('<option value="' + DepartmentBoard + '">' + DepartmentBoard + '</option>');
		});
	}
	
}

$(function(){
	var BoardName2Value = $('#boardName2Hidden').val();
	
	// BoardName2Text의 따라 BoardName1 구하기
	if (companyBulletinBoards.includes(BoardName2Value)) {
		boardName1Value='전사게시판';
	} else if(DepartmentBoards.includes(BoardName2Value)){
		boardName1Value='부서게시판';
	} else{
		boardName1Value='일반게시판';
	}
	
	changeBoardName2(boardName1Value);//boardName2를 boardName1에 맞게 바꿈
	$('#boardName1').val(boardName1Value);
	$('#boardName2').val(BoardName2Value);
	
	$(".boardName2").css({
		'font-weight':'normal',
		'color':'#333',
		'font-size':'14px'
	});
	
	//boardName1 select를 바꿀때마다 boardName2가 같이 바뀜
	$('#boardName1').change(function() {
		let boardName1Value = $(this).val();  // 첫 번째 select의 선택 값
		
		changeBoardName2(boardName1Value);//boardName2를 boardName1에 맞게 바꿈
	});
	
	
	// dragover 이벤트: 드래그한 파일이 attachFlie 영역에 있을 때
    $('.attachFile').on('dragover', function(event) {
	    event.preventDefault();
	    $(this).addClass('dragoverFile');
	});
	
	$('.attachFile').on('dragleave', function() {
	    $(this).removeClass('dragoverFile');
	});
	
    $('.attachFile').on('drop', function(event) {
        event.preventDefault();  // 기본 동작을 취소
        $(this).removeClass('dragoverFile');
	
        var dragFiles = event.originalEvent.dataTransfer.files;  // 드롭된 파일들
        handleFiles(dragFiles);  // 드래그된 파일 처리
    });

});

//Drop 영역외에 파일 끌어다 놓았을 때 브라우져 동작막깅
document.addEventListener("dragover", function (event) {
    event.preventDefault();
});

document.addEventListener("drop", function (event) {
    event.preventDefault();
});
</script>
</head>
<body>
 	<form method="post" enctype="multipart/form-data"
      name="boardform" onsubmit="event.preventDefault(); submitForm();">
 		<div class="form-group2">
 			<input type="hidden" name="postId" value='${postData.postId}'/>
 			<input type="hidden" id="boardName2Hidden" name="boardName2Hidden" value='${boardMap[postData.boardId]}'/>
 			<label for="target_board" class="labelName">
 				To.
 				<select id="boardName1" name="boardName1" class="boardName" disabled>
 					<option value="전사게시판" >전사게시판</option>
 					<option value="일반게시판" >일반게시판</option>
 					<option value="부서게시판" >부서게시판</option>
 				</select>
 				<select id="boardName2" name="boardName2" class="boardName" disabled>
 					<option value="공지사항">공지사항</option>
 					<option value="주간식단표">주간식단표</option>
 					<option value="FAQ">FAQ</option>
 				</select>
 			</label>
 		</div>
 		<div class="form-group2">
 			<label for="board_subject" class="labelName">제목</label>
 			<input name="board_subject" id="board_subject" type="text" maxlength="100"
 					class="form-control2" placeholder="Enter board_subject" value="Re:${postData.postSubject}" required>
 		</div>
 		<div class="form-group2-file">
 			<label for="board_attachFile" class="labelName">파일첨부</label>
 			<div class="attachFile">
 				<img src="${pageContext.request.contextPath}/image/attach.png" alt="파일첨부">
 				이 곳에 파일을 드래그 하세요. 또는 
	 			<label class="fileLabel">
	 				파일선택
 					<input type="file" id="upfile" onchange="addFile(this);" multiple />
	 			</label>
 			</div>
 		</div>
 		<div class="file-list"></div>
		<textarea class="summernote form-control2" id="board_content" name="board_content" required></textarea>
		<div class="form-group-btn">
	 		<button type="submit" class="btn registerBtn">등록</button>
	 		<button type="reset" class="btn registerBtn tr-post" data-page="${postData.postId}" data-name="${boardMap[postData.boardId]}">돌아가기</button>
 		</div>
 	</form>
 	
 	<script>
	 	$.getScript('https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.js', function() {
	        $('.summernote').summernote({
	            height: 400,
	            minHeight: 400,
	            maxHeight: null,
	            focus: true
	        });

			$('.boardContent .active').removeClass('active');
	    });
 	</script>
</body>
</html>