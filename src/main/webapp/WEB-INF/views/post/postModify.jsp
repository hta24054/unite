<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	    justify-content: flex-end;
	    height: 70px;
	    align-items: center;
	    margin-top: 10px;
	    gap: 20px;
	    margin-right: 3px;
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
var deletedFiles = [];
var maxFileCnt = 5;   // 첨부파일 최대 개수

var companyBulletinBoards = ['공지사항', '주간식단표', 'FAQ'];

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
function fileListAppendHtml(file) {
    // 파일 객체에 고유 fileNo 값을 추가해 배열에 저장
    file.fileNo = fileNo;
    filesArr.push(file);

    // 목록 추가
    let htmlData = '';
    htmlData += '<div id="file' + fileNo + '" class="filebox">';
    htmlData += '   <p class="name">' + file.name + '</p>';
    
    htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo;
    if (file.isExisting) {// isExisting 조건에 따라 추가
        htmlData += ', true';
    }
    htmlData += ');"><img src="${pageContext.request.contextPath}/image/delete.png"/></a>';
    
    htmlData += '</div>';
    $('.file-list').append(htmlData);
    fileNo++;
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
function deleteFile(deleteNo, isExisting = false) {
	if (isExisting) {
        // 기존 파일인 경우 deletedFiles 배열에 추가
        const deletedFile = filesArr.find(file => file.fileNo === deleteNo);
        deletedFiles.push(deletedFile);
    }
	
    // filesArr 배열에서 삭제할 파일을 fileNo로 찾음
    filesArr = filesArr.filter(file => file.fileNo !== deleteNo);

    // 파일 목록에서 삭제
    $("#file" + deleteNo).remove();

    // 파일 목록이 비어 있으면 숨김 처리
    updateFileListVisibility();
}

/* 파일 목록이 비어있으면 숨김 처리 */
function updateFileListVisibility() {
    $('.file-list').toggleClass('hidden', filesArr.length !== 0);
	updateDividerHeight();
}

/* 폼 전송 */
function submitForm() {
    var form = document.querySelector("form[name='boardform']");
    var formData = new FormData(form);
    var j = 0;

    // 삭제되지 않은 파일만 폼데이터에 담기
    filesArr.forEach(file => {
    	if (!file.isExisting) {
            // 새로 추가된 파일만 처리
            formData.append("attach_file" + j++, file);
        }
    });
    
 	// 삭제된 기존 파일 데이터 전달
    deletedFiles.forEach(file => {
        formData.append("deleted_files", JSON.stringify(file));
    });

    $.ajax({
        method: 'POST',
        url: '../board/post/modifyProcess',
        dataType: 'json',
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            alert(data.message);
            console.log('data.boardName2=',data.boardName2)
            console.log('data.postId=',data.postId)
            
            $('.boardName2').each(function() {//left메뉴 이벤트 주기
			    if ($(this).val() === data.boardName2) {
			    	$(this).addClass('menuActive');
					$(this).parent().parent().prev('a').addClass('menuActive');
			    }
			});
            $('.boardContent').css('padding','0');
			loadBoardPost(data.boardName2,data.postId);
        },
        error: function (xhr, desc, err) {
            alert('에러가 발생하였습니다.');
            location.href = "home";
        }
    });
}

//boardName2를 boardName1에 맞게 바꿈
function changeBoardName2(boardName1Value, departmentBoards){
	let $boardName2 = $('#boardName2'); // 두 번째 select 초기화
    $boardName2.empty();

	if (boardName1Value === '전사게시판') {
		$.each(companyBulletinBoards, function(index, companyBulletinBoard) {
			$boardName2.append('<option value="' + companyBulletinBoard + '">' + companyBulletinBoard + '</option>');
		});
	}else if (boardName1Value === '일반게시판') {
		$boardName2.append('<option value="' + '일반게시판' + '">' + '일반게시판' + '</option>');
	}else if (boardName1Value === '부서게시판') {
		$.each(departmentBoards, function(index, departmentBoard) {
			$boardName2.append('<option value="' + departmentBoard + '">' + departmentBoard + '</option>');
		});
	}
	
}

function filterBoardName2() {
    const excludedValues = ['공지사항', '주간식단표', 'FAQ', '일반게시판']; // 제외할 값들
    let filteredArray = []; // 결과를 담을 배열

    $('.boardName2').each(function() {
        const value = $(this).text(); // .boardName2 요소의 값 가져오기
        if (!excludedValues.includes(value)) { // 제외할 값에 포함되지 않으면 추가
            filteredArray.push(value);
        }
    });

    return filteredArray; // 필터링된 배열 반환
}

$(function(){
	var departmentBoards = filterBoardName2();
	console.log(departmentBoards);
	
	var BoardName2Value = $('.boardName2').filter(function() {
	    return $(this).css('font-weight') === 'bold' || $(this).css('font-weight') === '700';
	}).text();
	
	let boardName1Value;
	
	// BoardName2Text의 따라 BoardName1 구하기
	if (companyBulletinBoards.includes(BoardName2Value)) {
		boardName1Value='전사게시판';
	} else if(departmentBoards.includes(BoardName2Value)){
		boardName1Value='부서게시판';
	} else{
		boardName1Value='일반게시판';
	}
	
	changeBoardName2(boardName1Value, departmentBoards);//boardName2를 boardName1에 맞게 바꿈
	$('#boardName1').val(boardName1Value);
	$('#boardName2').val(BoardName2Value);
	
	$(".boardName2,.left a").removeClass('menuActive');
	
	
	// 게시글에 올려둔 첨부파일 배열에 추가 및 출력
    $('.filesHidden').each(function() {
		
        const file = {
        	'fileNo'	: null,
            name: $(this).data('post-file-original'),
            postFilePath: $(this).data('post-file-path'),
            postFileUUID: $(this).data('post-file-uuid'),
            postFileType: $(this).data('post-file-type'),
            isExisting: true // 기존 파일임을 표시
        };
        
        fileListAppendHtml(file);
    });
    updateFileListVisibility()
	
	
	
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
 			<input type="hidden" name="postId" value='${list[0].postId}'/>
 			<input type="hidden" id="boardName2Hidden" name="boardName2Hidden" value='${boardName2}'/>
 			<label for="target_board" class="labelName">
 				To.
 				<select id="boardName1" name="boardName1" class="boardName">
 					<option value="전사게시판">전사게시판</option>
 					<option value="일반게시판">일반게시판</option>
 					<option value="부서게시판">부서게시판</option>
 				</select>
 				<select id="boardName2" name="boardName2" class="boardName">
 					<option value="공지사항">공지사항</option>
 					<option value="주간식단표">주간식단표</option>
 					<option value="FAQ">FAQ</option>
 				</select>
 			</label>
 		</div>
 		<div class="form-group2">
 			<label for="board_subject" class="labelName">제목</label>
 			<input name="board_subject" id="board_subject" type="text" maxlength="100"
 					class="form-control2" placeholder="Enter board_subject" value="${list[0].postSubject}" required>
 		</div>
 		<div class="form-group2-file">
 			<label for="board_attachFile" class="labelName">파일첨부</label>
 			<div class="attachFile">
 				<img src="${pageContext.request.contextPath}/image/attach.png" alt="파일첨부">
 				이 곳에 파일을 드래그 하세요. 또는 
	 			<label class="fileLabel">
	 				파일선택
	 				<c:if test="${list[2] != null and not empty list[2]}">
	    				<c:forEach var="postFile" items="${list[2]}">
			 				<input type="hidden" class="filesHidden"
			 						data-post-file-path="${postFile.postFilePath}"  data-post-file-uuid="${postFile.postFileUUID}"
			 						data-post-file-type="${postFile.postFileType}"  data-post-file-original= "${postFile.postFileOriginal}"/>
		 				</c:forEach>
	 				</c:if>
	 				<input type="file" id="upfile" onchange="addFile(this);" multiple/>
	 			</label>
 			</div>
 		</div>
 		<div class="file-list"></div>
		<textarea class="summernote form-control2" id="board_content" name="board_content" required>${list[0].postContent}</textarea>
		<div class="form-group-btn">
	 		<button type="submit" class="btn registerBtn">수정</button>
	 		<button type="reset" id="registerBtn" class="btn registerBtn tr-post" data-page="${list[0].postId}" data-name="${boardName2}">돌아가기</button>
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