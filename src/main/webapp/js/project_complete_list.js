$(function(){
	go(1);
	$("#viewcount").change(function(){
		go(1); //보여줄 페이지를 1페이지로 설정
	});
})

function go(page){
	const limit = $('#viewcount').val();
	const data = {limit:limit, state:"ajax", page: page}
	ajax(data);
}


function ajax(sdata){
	console.log(sdata);
	$.ajax({
		data: sdata,
		url: "complete",
		dataType: "json",
		cache: false,
		success: function(data){
			$("#viewcount").val(data.limit);
			$("thead").find("span").text("글 개수 : " + data.listcount);
			if(data.listcount > 0){
				$("tbody").remove();
				updateBoardList(data); //게시판 내용 업데이트
				generatePagination(data);
			}else {
                // 데이터가 없을 경우, "등록된 글이 없습니다" 메시지를 표시
                $("tbody").html("<tr><td colspan='7' style='text-align:center;'>등록된 글이 없습니다</td></tr>");
            }
		},
		error: function(){
			console.log('에러');
		}
	});
}

function updateBoardList(data){
	let output = "<tbody>";
	
	$(data.boardlist).each(function(index, item){
		var participantNames = item.participantNames || [];
	    var viewerNames = item.viewers || [];
	    var participants = participantNames.length > 1 
	        ? "<span title='" + participantNames.slice(1).join(', ') + "'>" + participantNames[0] + " 외 " + (participantNames.length - 1) + "명</span>"
	        : (participantNames[0] || '없음');
	    var viewers = viewerNames.length > 1 
	        ? "<span title='" + viewerNames.slice(1).join(', ') + "'>" + viewerNames[0] + " 외 " + (viewerNames.length - 1) + "명</span>"
	        : (viewerNames[0] || '없음');

		output += `
			<tr>
				<td>${item.projectId}</td>
				<td><div>${item.projectName}</div></td>
				<td><div>${item.empName}</div></td>
				<td><div>${participants}</div></td>
				<td><div>${viewers}</div></td>
				<td><div>${item.projectStartDate}</div></td>
				<td><div>${item.projectEndDate}</div></td>
				<td><div>${item.projectFilePath}</div></td>
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

