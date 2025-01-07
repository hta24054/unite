var option = 1; //선택한 등록순과 최신순을 수정, 삭제, 추가 후에도 유지되도록 하기 위한 변수로 사용됩니다.

function getList(state){
	console.log(state);
	option = state;
	$.ajax({
		type:"get",
		url:"comments/list",
		data: {
			"postId":$("#postId").val(),
			state:state
		},
		dataType:"json",
		success:function(rdata){
			$('#count').text(rdata.listCount).css('font-family','arial,sans-serif');
			let red1 = (state == 1) ? 'sodomy' : 'gray';
			let red2 = (state == 2) ? 'sodomy' : 'gray';
			
			let output = `
				<li class='comment-order-item ${red1}'>
					<a href='javascript:getList(1)' class='comment-order-button'>등록순</a>
				</li>
				<li class='comment-order-item ${red2}'>
					<a href='javascript:getList(2)' class='comment-order-button'>최신순</a>
				</li>`;
			$('.comment-order-list').html(output);
			
			output = ''//초기화 합니다.
			if(rdata.postCommentList.length){
				$('.comment-head').css('display','block');
				
				rdata.postCommentList.forEach(comment =>{
					let lev = comment.postCommentReLev;
					let replayClass = (lev == 1) ? 'comment-list-item--reply lev1': (lev == 2) ? 'comment-list-item--reply lev2': '';
					
					let src = `${contextPath}/emp/profile-image?UUID=`+comment.imgUUID;
					
					let replyButton = (lev < 2) ? `<a href='javascript:replyform(${comment.commentId}, ${lev}, ${comment.postCommentReSeq},${comment.postCommentReRef})' class='comment-info-button'>
													<img class='replyImg' src='/unite/image/postLine.png'>댓글</a>`:'';
					
					//로그인한 사람이 댓글 작성자인 경우
					var empValue = $("#empId").val();
					let toolButtons = empValue == comment.postCommentWriter || empValue == 'admin'? `
						<div class='comment-tool'>
							<div title='더보기' class='comment-tool-button'>
								<div>&#46;&#46;&#46;</div>
							</div>
							<div id='comment-list-item-layer${comment.commentId}' class='LayerMore'>
								<ul class='layer-item'>
									<li class='layer-list'>
										<a href='javascript:updateForm(${comment.commentId},"${comment.postCommentFileOriginal}")' class='layer-button'>수정</a>
										<a href='javascript:del(${comment.commentId})' class='layer-button'>삭제</a>
									</li>
								</ul>
							</div>
						</div>` : '';
					
					output += `
						<li id='${comment.commentId}' class='comment-list-item ${replayClass}'>
							<div class='comment-nick-area'>
								<div class='comment-box'>
									<div class='comment-nick-box'>
										
									<img src='${src}' class="profileImg" alt='프로필 사진' width='36' height='36'>
									<span class='comment-nickname'>${rdata.empMap[comment.postCommentWriter]}</span>
									${replyButton}
									<span class='comment-info-date'>${comment.postCommentDate}</span>
									
									<div class='comment-nick-info'>
									</div>
									</div>
									<div class='comment-text-box'>
											<span class='text-comment'>${comment.postCommentContent}</span>
									</div>
									<div class='comment-info-box'>`;
					//파일이 존재할 경우
					if(comment.postCommentFileUUID != null){
						output += `<img src="/unite/image/attach.png" style="width:13px; margin: 0px 5px 0px 20px;">
		    					   <a href="comments/down?commentId=${comment.commentId}" style="font-size: 16px; color: gray;">${comment.postCommentFileOriginal}</a>`;
					}		
					
					output += `</div>
								${toolButtons}
							</div>
						</div>
					</li>`;
				});
				
				$('.comment-list').html(output);
			}
			
			if(!rdata.postCommentList.length){
				$('.comment-list, .comment-order-list').empty();
				$('.comment-head').css('display','none');
			}
			
			
			updateDividerHeight();// 구분선 업데이트
		},//success: function(rdata){
		error:function(){
			console.log("error")
		}
	});//$.ajax({
}//function getList()

//더보기-수정 클릭한 경우에 수정 폼을 보여줍니다.
function updateForm(commentId, postCommentFileOriginal) {
   $(".comment-tool, .LayerMore").hide(); // 더보기 및 수정 삭제 영역 숨김
   
   let $commentId = $('#'+commentId);
   const content = $commentId.find('.text-comment').text(); //선택한 댓글 내용
   
   $commentId.find('.comment-nick-area').hide();//댓글 닉네임 영역 숨김
   $commentId.append($('.comment-list+.comment-write-container').clone()); //기본 글쓰기 폼 복사하여 추가
   
   //수정 폼의 <textarea>에 내용을 나타냅니다.
   $commentId.find('.comment-write textarea').val(content).focus();
   
   //'.btn-register' 영역에 수정할 글 번호를 속성 'data-id'에 나타내고 클래스 'commentUpdate'를 추가합니다.
   $commentId.find('.btn-register').attr('data-id',commentId).addClass('commentUpdate').text('수정완료');
   
   //폼에서 취소를 사용할 수 있도록 보이게 합니다.
   $commentId.find('.btn-cancel').show();
   
   //글자 수 표시
   $commentId.find('.comment-write-area-count').text(`${content.length}/200`);
   
   console.log('postCommentFileOriginal=',postCommentFileOriginal)
   
   if(postCommentFileOriginal !== "undefined" && postCommentFileOriginal !== null){
	   var file = {
			name: postCommentFileOriginal,
			commentId: commentId
	   };
	   
	   commentAddFile(file);
	   
	   // "x" 버튼 클릭 시 파일 이름과 파일 삭제
	   $('.file-info .remove-file').on('click', function() {
	   		$('#'+commentId+" #upfile").value = ''; // 파일 선택 초기화
	    	$('.comment-attachFile').innerHTML = ''; // 파일 이름 삭제
	   });
   }
   
   updateDividerHeight();
}
	
//더보기 -> 삭제 클릭한 경우 실행하는 함수
function del(commentId){//num : 댓글 번호
  	if(!confirm('정말 삭제하시겠습니까')){
		  $('#comment-list-item-layer'+commentId).hide();//'수정 삭제' 영역 숨겨요
		  return;
	  }
	  
	  $.ajax({
		  url:'comments/delete',
		  data:{commentId:commentId},
		  success:function(rdata){
			  
			  if(rdata==1){
				  getList(option);
			  }else{
				  alert('삭제 중 오류가 발생했습니다.')
			  }
			  
		  }
	  })
}//function(del) end


//답글 달기 폼
function replyform(commentId, lev, seq, ref) {
	//수정 삭제 영역 선택 후 답글쓰기를 클릭한 경우
	$(".LayerMore").hide(); //수정 삭제 영역 숨겨요
	
	let $commentId = $('#' + commentId);
	//선택한 글 뒤에 답글 폼을 추가합니다.
	$commentId.after(`<li class="comment-list-item comment-list-item--reply lev${lev}"></li>`);
	
	//글쓰기 영역 복사합니다.
	let replyForm = $('.comment-list+.comment-write-container').clone();
	
	//복사한 폼을 답글 영역에 추가
	let $commentId_next = $commentId.next().html(replyForm);
	
	//답글 폼의 <textarea>의 속성 'placeholder'를 '답글을 남겨보세요'로 바꾸어 줍니다.
	$commentId_next.find('textarea').attr('placeholder','답글을 남겨보세요').focus();
	
	//답글 폼의 '.btn-cancel'을 보여주고 클래스 'reply-cancel'를 추가합니다.
	$commentId_next.find('.btn-cancel').show().addClass('reply-cancel');
	
	//답글 폼의 '.btn-register'에 클래스 'reply' 추가합니다.
	//속성 'data-ref'에 ref, 'data-lev'에 lev, 'data-seq'에 seq값을 설정합니다.
	//등록을 답글 완료로 변경합니다.
	$commentId_next.find('.btn-register')
					.addClass('reply')
					.attr({'data-ref':ref,'data-lev':lev, 'data-seq':seq})
					.text('답글완료');
	
	//댓글 폼 보이지 않습니다.
	$('div > div.comment-area > div.comment-write-container').hide();
	
	updateDividerHeight();
}//replyform end

function commentAddFile(file) {
    if (file) {
        const fileName = file.name;

        // 조건에 따라 fileDiv를 선택
        const fileDiv = file.commentId
            ? $(`#${file.commentId} .comment-attachFile`)
            : $('.comment-attachFile');

        if (fileDiv.length === 0) {
            console.error('fileDiv not found');
            return; // fileDiv가 존재하지 않으면 처리 중단
        }

        // 기존 파일이 있으면 삭제
        fileDiv.empty();

        // 파일 이름을 표시할 새로운 요소 생성
        const fileNameHtml = `
            <div class="file-info">
                ${fileName}
                <span class="remove-file" style="cursor: pointer; color: red; font-weight: bold;">x</span>
            </div>
        `;

        // 파일 이름을 .comment-attachFile에 추가
        fileDiv.html(fileNameHtml);
        updateDividerHeight();
    }
}


$(function(){
	getList(option);	//처음 로드 될 때는 등록순 정렬
	
	$('.comment-area').on('keyup','.comment-write-area-text', function(){
		const value = $(this).val();
		const length = value.length;
		const maxlength = $(this).attr('maxlength');
		$(this).prev().text(length+'/200');
		
		if(length > maxlength){
			$(this).prev().text(maxlength+'/200');
			$(this).val(value.slice(0,maxlength));
		}
	})//'keyup','.comment-write-area-text', function(){
	
	//댓글 등록을 클릭하면 데이터베이스에 저장 -> 저장 성공 후에 리스트를 불러옵니다
	$('.comment-list + .comment-write-container .comment-write .btn-register').click(function(){
		const content=$('.comment-write-area-text').val();
		if(!content.trim()){//내용없이 등록 클릭한 경우
			alert("1자 이상, 200자 이하로 입력해야 합니다.")
			return
		}
		const file = $('#upfile')[0].files[0];
		
		var formData = new FormData();
		if(file){
			formData.append("file", file);
		}
		formData.append("empId", $("#empId").val());
		formData.append("postCommentContent", $(".comment-write-area-text").val());
		formData.append("postCommentReLev", 0);
		formData.append("postId", $("#postId").val());
		formData.append("postCommentReSeq", 0);
		
		
		$.ajax({
			url : "comments/add",//원문 등록
			data: formData,
			type : 'post',
			processData: false, // 데이터를 자동으로 처리하지 않음
    		contentType: false, // contentType을 자동으로 설정하지 않음
			success : function(rdata){
				if(rdata == 1){
					getList(option);
				}
				
			}
		})//ajax
		
		$('.comment-write-area-text').val('')//textarea 초기화
		$('.comment-write-area-count').text('0/200')//입력한 글 카운트 초기화
		$('.comment-attachFile').html('');//첨부파일 초기화
		$('#upfile').val("");
		
	})//$('ul + .comment-write .btn-register').click(function(){
	
	//더보기를 클릭한 경우
	$(".comment-list").on('click', '.comment-tool-button', function() {        		
	    //더보기를 클릭하면 수정과 삭제 영역이 나타나고 다시 클릭하면 사라져요
		$(this).next().toggle();
		
		//클릭 한 곳만 수정 삭제 영역이 나타나도록 합니다.
		$(".comment-tool-button").not(this).next().hide(); 
	})
	
	//수정 후 수정완료를 클릭한 경우
	$('.comment-area').on('click','.commentUpdate',function(){
		const content = $(this).parent().parent().find('textarea').val();
		if(!content){//내용없이 등록 클릭한 경우
			alert("수정할 글을 입력하세요");
			return;
		}
		const commentId = $(this).attr('data-id');
		
		const file = $('#upfile')[0].files[0];
		
		var formData = new FormData();
		if(file){
			formData.append("file", file);
		}
		formData.append("commentId", commentId);
		formData.append("postCommentContent", content);
		
		$.ajax({
			url:'comments/update',
			data:formData,
			type : 'post',
			processData: false, // 데이터를 자동으로 처리하지 않음
    		contentType: false, // contentType을 자동으로 설정하지 않음
			success:function(rdata){
				if(rdata==1){
					getList(option);
					
				}else{
					alert('수정 중 오류가 발생했습니다.');
				}
			}//success
		})//ajax
		$('.comment-attachFile').empty(); // 첨부파일 정보 삭제
		
	})//수정 후 수정완료를 클릭한 경우
	
	//수정 후 취소 버튼을 클릭한 경우
	$('.comment-area').on('click','.btn-cancel',function(){
		//댓글 번호를 구해 selector를 만든다
		const selector='#' + $(this).next().attr('data-id');
		
		//.comment-write 영역 삭제 합니다.
		$(selector + ' .comment-write-container').remove();
		
		//숨겨두었던 .comment-nick-area 영역 보여줍니다.
		$(selector + '>.comment-nick-area').css('display','block');
		
		//수정 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더보기 영역을 숨겼는데 취소를 선택하면 보여주도록 합니다.
		$(".comment-tool").show();
		
		$('.comment-attachFile').empty(); // 첨부파일 정보 삭제
	})//수정 후 취소 버튼을 클릭한 경우
	
	//답글완료 클릭한 경우
	$('.comment-area').on('click','.reply',function(){
		const content = $(this).parent().parent().find('.comment-write-area-text').val();
		if(!content){//내용없이 답글완료 클릭한 경우
			alert("답글을 입력하세요");
			return;
		}
		const file = $('#upfile')[0].files[0];
		
		var formData = new FormData();
		if(file){
			formData.append("file", file);
		}
		formData.append("empId", $("#empId").val());
		formData.append("postCommentContent", content);
		formData.append("postCommentReLev", $(this).attr('data-lev'));
		formData.append("postId", $("#postId").val());
		formData.append("postCommentReSeq", $(this).attr('data-seq'));
		formData.append("postCommentReRef", $(this).attr('data-ref'));
		
		$.ajax({
			url:'comments/reply',
			data:formData,
			type : 'post',
			processData: false, // 데이터를 자동으로 처리하지 않음
    		contentType: false, // contentType을 자동으로 설정하지 않음
			success : function(rdata){
				
				if(rdata==1){
					getList(option);
				}else{
					alert('답글 등록 중 오류가 발생했습니다.')
				}
			}
		})//ajax
		$('.comment-attachFile').empty(); // 첨부파일 정보 삭제
		//답글 폼 보여줍니다.
		$('div > div.comment-area > div.comment-write-container').show();
	})//답글완료 클릭한 경우
	
	//답글쓰기 후 취소버튼을 클릭한 경우
	$('.comment-area').on('click','.reply-cancel',function(){
		$(this).parent().parent().parent().remove();
		$(".comment-tool").show(); //더보기 영역 보이도록 합니다.
		
		//댓글 폼 보여줍니다.
		$("div > div.comment-area > div.comment-write-container").show();
	})//답글쓰기 후 취소버튼을 클릭한 경우
	
	
	//답글쓰기 클릭 후 계속 누르는 것을 방지하기 위한 작업
	$('.comment-area').on('click','.comment-info-button',function(event){
		//답변쓰기 폼에 있는 상태에서 더보기를 클릭할 수 없도록 더 보기 영역을 숨겨요
		$('.comment-tool').hide();
		
		//답글쓰기 폼의 갯수를 구합니다.
		const length = $(".comment-area .btn-register.reply").length;
		if(length==1){  //답글쓰기 폼이 한 개가 존재하면 anchor 태그(<a>)의 기본 이벤트를 막아
						//또 다른 답글쓰기 폼이 나타나지 않도록 합니다.
			alert('답글 취소를 누른 후 다시 시도해주세요.')
			event.preventDefault();
		}
	})//답글쓰기 클릭 후 계속 누르는 것을 방지하기 위한 작업
	
	//업로드할 파일을 바꿀시
	$(document).on('change', '#upfile', function(event) {
	    const fileInput = event.target;
	    let file = fileInput.files[0]; // 선택된 파일
		
		console.log('change')
		const $commentListItem = $(fileInput).closest('.comment-list-item');
    	const commentId = $commentListItem.length ? $commentListItem.attr('id') : 0;
		
	    if (file) {
			
			file.commentId = commentId;
	        commentAddFile(file); // 댓글 첨부파일 추가 또는 수정
	
	        // "x" 버튼 클릭 시 파일 초기화
	        $('.file-info .remove-file').on('click', function() {
	            fileInput.value = ''; // 파일 선택 초기화
	            $('.comment-attachFile').empty(); // 첨부파일 정보 삭제
	        });
	    }
	});
	
	$(document).on('click', '.file-info .remove-file', function () {
	    const commentAttachFile = $(this).closest('.comment-attachFile'); // 파일 이름 컨테이너 선택
	    const fileInput = commentAttachFile.siblings('.register-box').find('#upfile'); // 관련 파일 입력 요소
	
	    if (fileInput.length) {
	        fileInput.val(''); // 파일 입력 초기화
	        commentAttachFile.empty(); // 첨부 파일 표시 영역 초기화
	    }
	    console.log('파일 삭제 완료');
	    
	    $('#upfile').val(''); // 파일 입력 초기화
	    $('.comment-attachFile').empty(); // 첨부파일 정보 삭제
	});
	
	$.getScript("https://cdn.jsdelivr.net/npm/@joeattardi/emoji-button@3.0.3/dist/index.min.js", function() {
		const picker = new EmojiButton({
        	position: 'bottom-start'
    	});

	    // 이모지 버튼 클릭 이벤트를 문서에 위임하여 동적으로 생성되는 버튼에도 적용
	    $(document).on('click', '#emoji_btn', function() {
        	const button = this;
	
	        // 이모지 선택기가 이미 열려 있는지 확인
	        if (picker.isPickerOpen) {
	            // 이미 열려 있으면, 선택기 닫기
	            picker.hidePicker();
	        } else {
	            // 열려 있지 않으면, 선택기 열기
	            picker.togglePicker(button);
	        }
	    });
	
	    picker.on('emoji', emoji => {
	        const text_box = document.querySelector('.comment-write-area-text');
	        text_box.value += emoji;
	    });
	});
	
})//ready