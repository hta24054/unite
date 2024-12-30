let option = 1; //선택한 등록순과 최신순을 수정, 삭제, 추가 후에도 유지되도록 하기 위한 변수로 사용

function getList(state){
	const tasknum = $('#comment_board_num').val(); 
	console.log(state);
	option = state;
	$.ajax({
		type: "get",
		url: contextPath + "/projectb/commentlist",
		data: {
			"comment_board_num" : $("#comment_board_num").val(),
			state: state,
			num: tasknum,
			memberId: $('#loginid').val()
		},
		dataType: "json",
		success: function(rdata){
			$('#count').text(rdata.listcount).css('font-family', 'arial,sans-serif');
			let red1 = (state == 1) ? 'red' : 'gray';
			let red2 = (state == 2) ? 'red' : 'gray';
			
			let output = `
				<li class='comment-order-item ${red1}'>
					<a href='javascript:getList(1)' class='comment-order-button'>등록순</a>
				</li>
				<li class='comment-order-item ${red2}'>
					<a href='javascript:getList(2)' class='comment-order-button'>최신순</a>
				</li>
			`;
			$('.comment-order-list').html(output);
			
			output = ''; //초기화
			console.log(rdata.commentlist);
			
			if(rdata.commentlist.length){
				rdata.commentlist.forEach(comment =>{
					let lev = comment.task_comment_re_lev;
					let replyClass = (lev == 1) ? 'comment-list-item--reply lev1' : (lev == 2) ? 'comment-list-item--reply lev2' : '';
					let src = comment.img_original ? `../memberupload/${comment.img_original}` : '../img/profile.png'
					let replyButton = (lev < 2) ? `<a href='javascript:replyform(${comment.task_comment_id}, ${lev}, ${comment.task_comment_re_seq}, ${comment.task_comment_re_ref})' class='comment-info-button'>답글쓰기</a>` : '';
					
					let toolButtons = rdata.id == comment.task_comment_writer ? `
						<div class='comment-tool'>
							<div title='더보기' class='comment-tool-button'>
								<div>&#46;&#46;&#46;</div>
							</div>
							<div id='comment-list-item-layer${comment.task_comment_id}' class='LayerMore'>
								<ul class='layer-list'>
									<li class='layer-item'>
										<a href='javascript:updateForm(${comment.task_comment_id})' class='layer-button'>수정</a>
										<a href='javascript:del(${comment.task_comment_id})' class='layer-button'>삭제</a>
									</li>
								</ul>
							</div>
						</div>
					` : '';
					
					output += `
						<li id='${comment.task_comment_id}' class='comment-list-item ${replyClass}'>
							<div class='comment-nick-area'>
								<img src="${contextPath}/emp/profile-image?UUID=${rdata.emp[comment.task_comment_writer]}"style="width:30px; height: 30px;">
								<div class='comment-box'>
									<div class='comment-nick-box'>
										<div class='comment-nick-info'>
											<div class='comment-nickname'>${comment.ename}</div>
										</div>
									</div>
									<div class='comment-text-box'>
										<p class='comment-text-view'>
											<span class='text-comment'>${comment.task_comment_content}</span>
										</p>
									</div>
									<div class='comment-info-box'>
										<span class='comment-info-date'>${comment.task_comment_date}</span>
										${replyButton}
									</div>
									${toolButtons}
								</div>
							</div>
						</li>
					`;
				});
				$('.comment-list').html(output);
			}
			if(!rdata.commentlist.length){
				$('.comment-list, .comment-order-list').empty();
			}
		}
	});
}

$(function(){
	getList(option); //처음 로드 될때는 등록순 정렬
	
	$('form[name="deleteForm"]').submit(function(){
		if($("#board_pass").val() == ''){
			alert("비밀번호를 입력하세요");
			$("#board_pass").focus();
			return false;
		}
	})
	
	$('.comment-area').on('keyup', '.comment-write-area-text', function(){
		const length = $(this).val().length;
		$(this).prev().text(length+'/200');
	});
	
	//댓글 등록을 클릭하면 데이터 베이스에 저장 -> 저장 성공 후에 리스트 불러옴
	$('ul + .comment-write .btn-register').click(function(){
		const content=$('.comment-write-area-text').val();
		if(!content){//내용없이 등록 클릭한 경우
			alert("댓글을 입력하세요");
			return;
		}
		$.ajax({
			url: contextPath + "/projectb/commentadd",
			data: {
				userid: $("#loginid").val(),
				content: content,
				comment_board_num: $("#comment_board_num").val(),
				comment_re_lev: 0, //원문인 경우 comment_re_seq는 0, comment_re_ref는 댓글의 원문 글번호
				comment_re_seq: 0
			},
			type: 'post',
			success: function(rdata){
				if(rdata == 1){
					getList(option);
				}
			}
		})
		$('.comment-write-area-text').val('');//textarea 초기화
		$('.comment-write-area-count').text('0/200');//입력한 글 카운트 초기화
	})
	//더보기를 클릭한 경우
	$(".comment-list").on('click', '.comment-tool-button', function(){
		//더보기를 클릭하면 수정과 삭제 영역이 나타나고 다시 클릭하면 사라짐
		$(this).next().toggle();
		
		//클릭 한 곳만 수정 삭제 영역이 나타나도록 함
		$(".comment-tool-button").not(this).next().hide();
	})	
	//수정 후 수정완료를 클릭한 경우
	$('.comment-area').on('click', '.update', function(){
		const content = $(this).parent().parent().find('textarea').val();
		if(!content){//내용없이 등록 클릭한 경우
			alert("수정할 글을 입력하세요");
			return;
		}
		const num = $(this).attr('data-id');
		$.ajax({
			url: contextPath + '/projectb/update',
			data:{num:num, content:content},
			success:function(rdata){
				if(rdata==1){
					getList(option);
				}
			}
		});
	})
	
	//수정 후 취소 버튼을 클릭한 경우
	$('.comment-area').on('click', '.btn-cancel', function(){
		//댓글 번호를 구함
		const num = $(this).next().attr('data-id');
		const selector = '#' + num;
		
		//.comment-write 영역 삭제
		$(selector + ' .comment-write').remove();
		
		//숨겨두었던 .comment-nick-area 영역 보여줌
		$(selector + '>.comment-nick-area').css('display','block');
		
		//수정 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더 보기 영역을 숨겼는데 취소를 선택하면 보여주도록 함
		$(".comment-tool").show();
	})
	
	//답글완료 클릭한 경우
	$('.comment-area').on('click', '.reply', function(){
		const content = $(this).parent().parent().find('.comment-write-area-text').val();
		if(!content){//내용없이 답글완료 클릭한 경우
			alert("답글을 입력하세요");
			return;
		}
		$.ajax({
			type: 'post',
			url:  contextPath + "/projectb/reply",
			data:{
				id : $("#loginid").val(),
				content : content,
				comment_board_num : $("#comment_board_num").val(),
				comment_re_lev : $(this).attr('data-lev'),
				comment_re_ref : $(this).attr('data-ref'),
				comment_re_seq : $(this).attr('data-seq')
			},
			success : function(rdata){
				console.log('ㄴㄴㄴㄴ');
				if(rdata == 1){
					getList(option);
				}
			}
		})
		
		//답글 폼 보여줌
		$("body > div > div.comment-area > div.comment-write").show();
	})
	
	//답글쓰기 후 취소 버튼을 클릭한 경우
	$('.comment-area').on('click', '.reply-cancel', function(){
		$(this).parent().parent().parent().remove();
		$(".comment-tool").show(); //더보기 영역 보이도록 함
		
		//댓글 폼 보여줌
		$("body > div > div.comment-area > div.comment-write").show();
	})
	
	//답글쓰기 클릭 후 계속 누르는 것을 방지하기 위한 작업
	$('.comment-area').on('click', '.comment-info-button', function(event){
		//답변쓰기 폼이 있는 상태에서 더보기를 클릭할 수 없도록 더보기 영역을 숨김
		$(".comment-tool").hide();
		
		//답글쓰기 폼의 갯수를 구함
		const length = $(".comment-area .btn-register.reply").length;
		if(length == 1){//답글쓰기 폼이 한 개가 존재하면 anchor 태그(<a>)의 기본 이벤트를 막아 또 다른 답글쓰기 폼이 나타나지 않도록 함
			event.preventDefault();
		}
	})
})

//더보기 - 수정 클릭한 경우에 수정 폼을 보여줌
function updateForm(num){
	$(".comment-tool, .LayerMore").hide(); //더보기 및 수정 삭제 영역 숨김
	let $num = $('#' + num);
	const content = $num.find('.text-comment').text(); //선택한 댓글 내용
	
	$num.find('.comment-nick-area').hide(); //댓글 닉네임 영역 숨김
	$num.append($('.comment-list+.comment-write').clone()); //기본 글쓰기 폼 복사하여 추가
	
	//수정 폼의 <textarea>에 내용을 나타냄
	$num.find('.comment-write textarea').val(content);
	
	//'.btn-register' 영역에 수정할 글 번호를 속성 'data-id'에 나타내고 클리스 'update'를 추가함
	$num.find('.btn-register').attr('data-id', num).addClass('update').text('수정완료');
	
	//폼에서 취소를 사용할 수 있도록 보이게 함
	$num.find('.btn-cancel').show();
	
	//글자 수 표시
	$num.find('.comment-write-area-count').text(`${content.length}/200`);
}

//더보기 - 삭제 클릭한 경우 실행하는 함수
function del(num){
	if(!confirm('정말 삭제하시겠습니까?')){ //num : 댓글번호
		$('#comment-list-item-layer' + num).hide(); //'수정 삭제' 영역 숨김
		return;
	}
	$.ajax({
		url: contextPath + '/projectb/commentdelete',
		data:{num:num},
		success: function(rdata){
			console.log('aaaa');
			if(rdata == 1){
				getList(option);
			}
		}
	})
}

//답글 달기 폼
function replyform(num, lev, seq, ref){
	//수정 삭제 영역 선택 후 답글 쓰기를 클릭한 경우
	$(".LayerMore").hide(); //수정 삭제 영역 숨김
	
	let $num = $('#' + num);
	
	//선택한 글 뒤에 답글 폼을 추가
	$num.after(`<li class="comment-list-item comment-list-item--reply lev${lev}"></li>`);
	
	//글쓰기 영역 복사
	let replyForm = $('.comment-list+.comment-write').clone();
	
	//복사한 폼을 답글 영역에 추가
	let $num_next = $num.next().html(replyForm);
	
	//답글 폼의 <textarea>의 속성 'placeholder'를 '답글을 남겨보세요'로 바꾸어 줌
	$num_next.find('textarea').attr('placeholder', '답글을 남겨보세요');
	
	//답글 폼의 '.btn-cancel'을 보여주고 클래스 'reply-cancel'를 추가
	$num_next.find('.btn-cancel').show().addClass('reply-cancel');
	
	//답글 폼의 '.btn-register'에 클래스 'reply' 추가
	//속성 'data-ref'에 ref, 'data-lev'에 lev, 'data-seq'에 seq값을 설정
	//등록을 답글 완료로 변경
	$num_next.find('.btn-register').addClass('reply').attr({'data-ref':ref, 'data-lev':lev, 'data-seq':seq}).text('답글완료');
	
	//댓글 폼 보이지 않는다
	$("body > div > div.comment-area > div.comment-write").hide();
}














