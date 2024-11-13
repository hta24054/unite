function go(page){
	const limit = $('#viewcount').val();
	/*const data =`limit=${limit}&state=ajax&page=${page}`; */
	const data = {limit:limit, state:"ajax", page:page}
	ajax(data);
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
	
	//이전 버튼
	let preHref = data.page > 1 ? `href=javascript:go(${data.page - 1})` : "";
	output += setPaging(preHref, '이전&nbsp;');
	
	//페이지 번호
	for(let i = data.startpage; i <= data.endpage; i++){
		const isActive = (i == data.page);
		let pageHref = !isActive ? `href=javascript:go(${i})` : "";
		output += setPaging(pageHref, i, isActive);
	}
	
	//다음 버튼
	let nextHref = (data.page < data.maxpage) ? `href=javascript:go(${data.page + 1})` : "";
	output += setPaging(nextHref, '&nbsp;다음&nbsp;');
	
	$('.pagination').empty().append(output);
}//end


function updateBoardList(data){
	let num = data.listcount - (data.page - 1) * data.limit;
	let output = "<tbody>";
	
	$(data.boardlist).each(function(index, item){
		const blank = '&nbsp;&nbsp;'.repeat(item.board_re_lev * 2);
		const img = item.board_re_lev > 0 ? "<img src='../image/line.gif'>" : "";
		const subject = item.board_subject.length >= 20 ? item.board_subject.substr(0,20) + "..." : item.board_subject;
		const changeSubject = subject.replace(/</g,'&lt;').replace(/>/g,'&gt;');
		
		output +=`
			<tr>
				<td>${num--}</td>
				<td><div>${blank}${img}<a href='detail?num=${item.board_num}'>${changeSubject}</a>[${item.cnt}]</div></td>
				<td><div>${item.board_name}</div></td>
				<td><div>${item.board_date}</div></td>
				<td><div>${item.board_readcount}</div></td>
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
		url: "list",
		dataType: "json",
		cache: false,
		success: function(data){
			$("#viewcount").val(data.limit);
			$("thead").find("span").text("글 개수 : "+data.listcount);
			
			if(data.listcount>0){
				$("tbody").remove();
				updateBoardList(data);// 게시판 내용 업데이트
				generatePagination(data);// 페이지네이션 생성
			}
		},
		error: function(){
			console.log('에러');
		}
	});
}

$(function(){
	
	$("#viewcount").change(function(){
		go(1);//보여줄 페이지를 1페이지로 설정합니다.
	});//change end
})