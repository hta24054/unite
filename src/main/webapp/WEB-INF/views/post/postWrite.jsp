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
	.attachFlie{
		border: 2px dotted;
		display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 60px;
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
	.form-group{
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
	.form-control{
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
</style>
<script>
var fileNo = 0;
var filesArr = new Array();

/* 첨부파일 추가 */
function addFile(obj){
    var maxFileCnt = 5;   // 첨부파일 최대 개수
    var attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일을 넣을시 list를 보이게 함
    if(curFileCnt>0){
    	$('.file-list').css('display','block');
    }
    
    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    } else {
        for (const file of obj.files) {
            // 첨부파일 검증
            if (validation(file)) {
                // 파일 배열에 담기
                var reader = new FileReader();
                reader.onload = function () {
                    filesArr.push(file);
                };
                reader.readAsDataURL(file);

                // 목록 추가
                let htmlData = '';
                htmlData += '<div id="file' + fileNo + '" class="filebox">';
                htmlData += '   <p class="name">' + file.name + '</p>';
                htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><i class="far fa-minus-square"><img src="${pageContext.request.contextPath}/image/delete.png"/></i></a>';
                htmlData += '</div>';
                $('.file-list').append(htmlData);
                fileNo++;
            } else {
                continue;
            }
        }
    }
    // 초기화
    document.querySelector("input[type=file]").value = "";
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

/* 첨부파일 삭제 */
function deleteFile(num) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
    
    var j = 0;
    for (var i = 0; i < filesArr.length; i++) {
    	if (filesArr[i] && !filesArr[i].is_delete) {
    	    j++;
    	}
    }
    
    if(j==0){
    	$('.file-list').css('display','none');
    }
}

/* 폼 전송 */
function submitForm() {
	console.log(filesArr.length)
    var form = document.querySelector("form[name='boardform']");
    var formData = new FormData(form);
	var j=0;
    // 삭제되지 않은 파일만 폼데이터에 담기
    for (var i = 0; i < filesArr.length; i++) {
    	if (filesArr[i] && !filesArr[i].is_delete) {
    	    formData.append("attach_file"+j++, filesArr[i]);
    	}
    }
	console.log(filesArr.length)
	formData.forEach((value, key) => {
    	console.log(key + ": " + value);
	});
	
    $.ajax({
        method: 'POST',
        url: '../board/post/add',
        dataType: 'json',
        data: formData,
        processData: false, // 데이터를 문자열로 변환하지 않음
        contentType: false, // Content-Type 헤더를 multipart/form-data로 설정
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

$(function(){
	$('#boardName1').change(function() {
		let boardName1Value = $(this).val();  // 첫 번째 select의 선택 값
		let $boardName2 = $('#boardName2'); // 두 번째 select

	    // 두 번째 select 초기화
	    $boardName2.empty();

		if (boardName1Value === '전사게시판') {
			var companyBulletinBoards = ['공지사항', '주간식단표', 'FAQ'];
			$.each(companyBulletinBoards, function(index, companyBulletinBoard) {
				$boardName2.append('<option value="' + companyBulletinBoard.toLowerCase() + '">' + companyBulletinBoard + '</option>');
			});
		}else if (boardName1Value === '일반게시판') {
			$boardName2.append('<option value="' + '일반게시판'.toLowerCase() + '">' + '일반게시판' + '</option>');
		}else if (boardName1Value === '부서게시판') {
			var DepartmentBoards = ['솔루션영업팀'];
			$.each(DepartmentBoards, function(index, DepartmentBoard) {
				$boardName2.append('<option value="' + DepartmentBoard.toLowerCase() + '">' + DepartmentBoard + '</option>');
			});
		}
	});
	
	
});
</script>
</head>
<body>
 	<form action="../board/post/add" method="post" enctype="multipart/form-data"
      name="boardform" onsubmit="event.preventDefault(); submitForm();">
 		<div class="form-group">
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
 		<div class="form-group">
 			<label for="board_subject" class="labelName">제목</label>
 			<input name="board_subject" id="board_subject" type="text" maxlength="100"
 					class="form-control" placeholder="Enter board_subject">
 		</div>
 		<div class="form-group">
 			<label for="board_attachFile" class="labelName">파일첨부</label>
 			<div class="attachFlie">
 				<img src="${pageContext.request.contextPath}/image/attach.png" alt="파일첨부">
 				이 곳에 파일을 드래그 하세요. 또는 
	 			<label class="fileLabel">
	 				파일선택
 					<input type="file" id="upfile" onchange="addFile(this);" multiple />
	 			</label>
	 			<span id="filevalue"></span>
 			</div>
 		</div>
 		<div class="file-list"></div>
		<textarea class="summernote form-control" id="board_content" name="content" required></textarea>
		<div class="form-group-btn">
	 		<button type="submit" class="btn registerBtn">등록</button>
 		</div>
 	</form>
</body>
</html>