/**
 * 
 */
/**
 * 
 */
$(function(){
	$("button").click(function(){
		location.href="write";
	})

	$("#viewcount").change(function(){
		go(1); //보여줄 페이지를 1페이지로 설정
	});
})

function go(page){
	const limit = $('#viewcount').val();
	//const data = `limit=${limit}&state=ajax&page=${page}`;
	const data = {limit:limit, state:"ajax", page: page}
	ajax(data);
}


function ajax(sdata){
	console.log(sdata);
	$.ajax({
		data: sdata,
		url: "main",
		dataType: "json",
		cache: false,
		success: function(data){
			$("#viewcount").val(data.limit);
			$("thead").find("span").text("글 개수 : " + data.listcount);
			
			if(data.listcount > 0){
				$("tbody").remove();
				updateBoardList(data); //게시판 내용 업데이트
				generatePagination(data);
			}
		},
		error: function(){
			console.log('에러');
		}
	});
}

function updateBoardList(data){
	let num = data.listcount - (data.page - 1) * data.limit;
	let output = "<tbody>";
	
	$(data.boardlist).each(function(index, item){
		const blank = '&nbsp;&nbsp;'.repeat(item.board_re_lev * 2);
		const img = item.board_re_lev > 0 ? "<img src='../img/line.gif'>" : "";
		const subject = item.board_subject.length >= 20 ? item.board_subject.substr(0, 20) + "..." : item.board_subject;
		const changeSubject = subject.replace(/</g, '&lt;').replace(/>/g, '&gt;');
		
		output += `
			<tr>
				<td>${num--}</td>
				<td><div>${blank}${img}<a href='detail?num=${item.project_num}'>${changeSubject}</a>[${item.project_cnt}]</div></td>
				<td><div>${item.project_name}</div></td>
				<td><div>${item.project_content}</div></td>
				<td><div>${item.project_end_date}</div></td>
			</tr>
			`;
	});
	output += "</tbody>";
	$('table').append(output);
}

function setPaging(href, digit, isActive = false){
	const gray = (href === "" && isNaN(digit)) ? "gray" : "";
	const active = isActive ? "active" : "";
	const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;
	return `<li class="page-item ${active}">${anchor}</li>`;
}

function generatePagination(data){
	let output = "";	
	
	//이전버튼
	let prevHref = data.page > 1 ? `href=javascript:go(${data.page - 1})` : "";  //jsp에서 list를 javascript:go로
	output += setPaging(prevHref, '이전&nbsp;');
	
	//페이지 번호
	for(let i = data.startpage; i <= data.endpage; i++){
		const isActive = (i===data.page);
		let pageHref = !isActive ? `href=javascript:go(${i})` : "";
		output += setPaging(pageHref, i, isActive);
	}
	
	//다음 버튼
	let nextHref = (data.page < data.maxpage) ? `href=javascript:go(${data.page + 1})` : "";
	output += setPaging(nextHref, '&nbsp;다음&nbsp;');
	
	$('.pagination').empty().append(output);
}





































