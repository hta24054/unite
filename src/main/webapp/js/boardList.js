var searchValue=false;

function go(page){
	const limit = $('#viewCount').val();
	/*const data =`limit=${limit}&state=ajax&page=${page}`; */
	const boardName2 = $('#boardName2').val();
	
	if(searchValue){
		const data = {limit:limit, page:page}
		searchAjax(data);
	}else{
		const data = {boardName2:boardName2,limit:limit, state:"ajax", page:page}
		ajax(data);
	}
	
}
//총 2페이지 페이징 처리된 경우
//이전 1 2 다음
//현재 페이지가 1페이지인 경우 아래와 같은 페이징 코드가 필요
//<li class="page-item"><a class="page-link gray">이전&nbsp;</a></li>
//<li class="page-item active"><a class="page-link">1</a></li>
//<li class="page-item"><a class="page-link" href="javascript:go(2)">2</a></li> 
//<li class="page-item"><a class="page-link" href="javascript:go(2)">다음&nbsp;</a></li>

//현재 페이지가 2페이지인 경우 아래와 같은 페이징 코드가 필요
//<li class="page-item"><a class="page-link" href="javascript:go(1)">이전&nbsp;</a></li>
//<li class="page-item"><a class="page-link" href="javascript:go(1)">1</a></li>
//<li class="page-item active"><a class="page-link">2</a></li>
//<li class="page-item"><a class="page-link gray">다음&nbsp;</a></li>

function setPaging(href, digit, isActive = false){
	const gray = (href ==="" && isNaN(digit))? "gray":"";
	const active = isActive ? "active" : "";
	const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;
	return `<li class="page-item ${active}">${anchor}</li>`;
}

function generatePagination(data){
	let output="";
	
	//맨 처음으로 이동 버튼
	let firstPreHref = data.page > 1 ? `href=javascript:go(1)` : "";
	output += setPaging(firstPreHref, '<<');
	
	//이전 버튼
	let preHref = data.page > 1 ? `href=javascript:go(${data.page - 1})` : "";
	output += setPaging(preHref, '<');
	
	//페이지 번호
	for(let i = data.startPage; i <= data.endPage; i++){
		const isActive = (i == data.page);
		let pageHref = !isActive ? `href=javascript:go(${i})` : "";
		output += setPaging(pageHref, i, isActive);
	}
	
	//다음 버튼
	let nextHref = (data.page < data.maxPage) ? `href=javascript:go(${data.page + 1})` : "";
	output += setPaging(nextHref, '>');
	
	//맨 마지막으로 이동 버튼
	let lastNextHref = (data.page < data.maxPage) ? `href=javascript:go(${data.maxPage})` : "";
	output += setPaging(lastNextHref, '>>');
	
	$('.pagination').empty().append(output);
	
	
}//end


function updateBoardList(data){
	let num = data.listCount - (data.page - 1) * data.limit;
	let output = "<tbody>";
	
	$(data.postList).each(function(index, item){
		const blank = '&nbsp;&nbsp;'.repeat(item.postReLev * 2);
		const img = item.postReLev > 0 ? "<img class='lineImg' src='/unite/image/postLine.png'>" : "";
		const subject = item.postSubject.length >= 20 ? item.postSubject.substr(0,20) + "..." : item.postSubject;
		const changeSubject = subject.replace(/</g,'&lt;').replace(/>/g,'&gt;');
		
		console.log(index)
		
		output +=`
			<tr class="tr-post" data-page="${item.postId}" data-name="${data.boardName2}">
				<td>${num--}</td>
				<td><div>${blank}${img}${changeSubject}[${item.postCommentCnt}]</div></td>
				<td><div>${data.empMap[item.postWriter]}</div></td>
				<td><div>${item.postDate}</div></td>
				<td><div>${item.postView}</div></td>
			</tr>
		`;
	});
	output +="</tbody>";
	$('table').append(output);
}//end

function ajax(sdata){
	console.log(sdata)
	$.ajax({
		data: sdata,
		url: "boardList",
		dataType: "json",
		cache: false,
		success: function(data){
			$("#viewCount").val(data.limit);
			$("thead").find("span").text("총 "+data.listCount+"건");
			
			if(data.listCount>0){
				$("tbody").remove();
				updateBoardList(data);// 게시판 내용 업데이트
				generatePagination(data);// 페이지네이션 생성
				
				updateDividerHeight();//구분선 길이 업데이트
			}
		},
		error: function(){
			console.log('에러');
		}
	});
}


function searchAjax(sdata){
	const category = $('#search-category').val(); // 선택된 검색 범위
    const query = $('#search-input').val(); // 검색어
	const boardName2 = $('.boardTitle').text();
	
	console.log("boardName2123=",boardName2)
	if (query.trim()) {
		// AJAX 요청을 통해 서버로 검색을 전송
  		$.ajax({
	        url: 'search', // 서버의 검색 엔드포인트 (적절히 변경)
	        method: 'GET',
	        data: { boardName2: boardName2, category: category, query: query , state:"ajax", page:sdata.page,limit:sdata.limit},
	        success: function(data) {
		    	
	          	$("#viewCount").val(data.limit);
				$("thead").find("span").text("글 개수 : "+data.listCount);
				
				if(data.listCount>0){
					$("tbody").remove();
					updateBoardList(data);// 게시판 내용 업데이트
					generatePagination(data);// 페이지네이션 생성
					searchValue = true;
					
					updateDividerHeight();//구분선 길이 업데이트
				}else{
					$('tbody').html('<tr style="border-bottom:1px solid #dee2e6;"><td colspan="5" style="text-align: center;">검색 결과가 없습니다.</td></tr>');
					$('.pagination').html('');
				}
	        },
	        error: function(error) {
	          console.log(error);
	        }
  		});
    } else {
		alert('검색어를 입력해주세요.');
	}
}
$(function(){
	
	$("#viewCount").change(function(){
		go(1);//보여줄 페이지를 1페이지로 설정합니다.
	});//change end
	
	$('#search-input').on('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); // 폼 제출 방지
            $('#search-button').click(); // 버튼 클릭
        }
    });
	
	$('#search-button').click(function() {
		searchAjax(1);
	});
})