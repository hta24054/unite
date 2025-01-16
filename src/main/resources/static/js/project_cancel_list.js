$(function () {
    go(1);
    $("#viewcount").change(function () {
        go(1); //보여줄 페이지를 1페이지로 설정
    });
})

function go(page) {
    const limit = $('#viewcount').val();
    const data = {limit: limit, state: "ajax", page: page}
    ajax(data);
}


function ajax(sdata) {
    console.log(sdata);
    $.ajax({
        data: sdata,
        url: "/project/cancelList",
        type: 'GET',
        dataType: "json",
        cache: false,
        success: function (data) {
            console.log("aaaaa");
            $("#viewcount").val(data.limit);
            $("thead").find("span").text("글 개수 : " + data.listcount);
            if (data.listcount > 0) {
                $("tbody").remove();
                updateBoardList(data); //게시판 내용 업데이트
                generatePagination(data);
            } else {
                // 데이터가 없을 경우, "등록된 글이 없습니다" 메시지를 표시
                $("tbody").html("<tr><td colspan='7' style='text-align:center;'>등록된 글이 없습니다</td></tr>");
            }
        },
        error: function () {
            console.log('에러');
        }
    });
}

function updateBoardList(data) {
    let output = "<tbody>";

    $(data.boardlist).each(function (index, item) {
        var participantNames = item.participantNames || [];
        var viewerNames = item.viewers || [];
        var participants = participantNames.length > 1
            ? "<span title='" + participantNames.slice(1).join(', ') + "'>" + participantNames[0] + " 외 " + (participantNames.length - 1) + "명</span>"
            : (participantNames[0] || '없음');
        var viewers = viewerNames.length > 1
            ? "<span title='" + viewerNames.slice(1).join(', ') + "'>" + viewerNames[0] + " 외 " + (viewerNames.length - 1) + "명</span>"
            : (viewerNames[0] || '없음');
        const ProjectFileOriginal = item.project_file_original ? item.project_file_original : "";
        let file_name = ''; // file_name 기본값 설정
        // task_file_original이 null 또는 undefined일 경우 체크
        if (item.project_file_original && item.project_file_original !== '')
            file_name = item.project_file_original.length > 10 ? item.project_file_type + "파일" : item.project_file_original;


        output += `
			<tr>
				<td>#&nbsp;${item.projectId}</td>
				<td><div>${item.projectName}</div></td>
				<td><div>${item.managerName}</div></td>
				<td><div>${participants}</div></td>
				<td><div>${viewers}</div></td>
				<td><div>${item.projectStartDate}</div></td>
				<td><div>${item.projectEndDate}</div></td>
				<td>
					<div>
						${ProjectFileOriginal ?
            `<a href="${contextPath}/projectb/down?filename=${item.project_file_uuid}${item.project_file_type}&originalFilename=${item.project_file_original}"title="${item.project_file_original}">
									${file_name}
							</a>` : ''}
					</div>
				</td>
			</tr>
			`;
    });
    output += "</tbody>";
    $('table').append(output);
}


