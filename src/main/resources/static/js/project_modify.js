/**
 * 
 */
$(document).ready(function(){
	let check = 0;
	//submit 버튼 클릭할 때 이벤트 부분
	$("form[name=modifyform").submit(function(){
		const $board_subject = $("#board_subject");
		if($board_subject.val().trim() == ""){
			alert("제목을 입력하세요");
			$board_subject.focus();
			return false;
		}
		const $board_content = $("#board_content");
		if($board_content.val().trim() == ""){
			alert("내용을 입력하세요");
			$board_content.focus();
			return false;
		}
		const $board_pass= $("#board_pass");
		if($board_pass.val().trim() == ""){
			alert("비밀번호을 입력하세요");
			$board_pass.focus();
			return false;
		}
		
		//파일첨부를 변경하지 않으면 $('#filevalue').text()의 파일명을
		//파라미터 'check'라는 이름으로 form에 추가하여 전송
		if(check == 0){
			const value = $('#filevalue').text();
			const task = $('#tasknum').val();
			console.log(task);
			console.log(html);
			const html = `<input type='hidden' value='${value}' name='check'>`;
			$(this).append(html);
		}
	});
	
	$("#upfile").change(function(){
		check++;
		const maxSizeInBytes = 5 * 1024 * 1024;
		const file = this.files[0]; //선택된 파일
		if(file.size > maxSizeInBytes){
			alert("5MB 이하 크기로 업로드 하세요");
			$(this).val('');
		}else{
			$('#filevalue').text(file.name); //파일이름
		}
		show();
	});
	
	function show(){
		//파일 이름이 있는 경우 remove 이미지를 보이게 하고
		//파일 이름이 없는 경우 remove 이미지를 보이지 않게 함
		$('.remove').css('display', $('#filevalue').text() ? 'inline-block' : 'none')
		.css({'position':'relative', 'top' : '-5px'});
	}
	show();
	
	
	//remove 이미지를 클릭하면 파일명을 ''로 변경하고 remove 이미지를 보이지 않게
	$(".remove").click(function(){
		$("#filevalue").text('');
		$(this).css('display', 'none');
	})
})