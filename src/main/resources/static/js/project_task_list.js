$(function () {
    go();
    $("#viewcount").change(function () {
        go(); //보여줄 페이지를 1페이지로 설정
    });
})

function go() {
    const data = {
        state: "ajax",
        projectId: 53
    };
    ajax(data);
}


function ajax(sdata) {
    console.log(sdata);
    $.ajax({
        data: sdata,
        url: "/projectBoard/lists",
        dataType: "json",
        cache: false,
        success: function (data) {
            console.log('hello');
            // console.log(data);
            // $("#viewcount").val(data.limit);
            // $("thead").find("span").text("글 개수 : " + data.listcount);
            // if(data.listcount > 0){
            // 	$("tbody").remove();
            updateBoardList(data); //게시판 내용 업데이트
            // }
        },
        error: function () {
            console.log('에러');
        }
    });
}

function updateBoardList(data) {
    let num = data.listcount - (data.page - 1) * data.limit;
    let output = "<tbody>";

    $(data.boardlist).each(function (index, item) {
        const blank = '&nbsp;&nbsp;'.repeat(item.board_re_lev * 2);
        const img = item.board_re_lev > 0 ? `<img src='${contextPath}/image/line.gif'>` : ""; // contextPath 사용
        const subject = item.taskSubject.length >= 20 ? item.taskSubject.substr(0, 20) + "..." : item.taskSubject;
        const changeSubject = subject.replace(/</g, '&lt;').replace(/>/g, '&gt;');
        const taskFileOriginal = item.taskFileOriginal ? item.taskFileOriginal : "";
        let file_name = ''; // file_name 기본값 설정

        // task_file_original이 null 또는 undefined일 경우 체크
        if (item.task_file_original && item.task_file_original !== '')
            file_name = item.task_file_original.length > 10 ? item.task_file_type + "파일" : item.task_file_original;

        output += `
            <tr>
                <td>${num--}</td>
                <td><a href="javascript:void(0);" onclick="submitForm(${item.taskNum}, '${item.memberId}')">${changeSubject}</a>${item.board_cnt > 0 ? `[${item.board_cnt}]` : ''}</td>
                <td><div>${item.taskContent}</div></td>
                <td><div>${item.taskDate}</div></td>
                <td><div>${item.taskUpdateDate}</div></td>
                <td>
                    ${taskFileOriginal ?
            `<a href="down?filename=${item.task_file_uuid}${item.task_file_type}&originalFilename=${item.task_file_original}" 
                           title="${item.task_file_original}">${file_name}</a>` : ''}
                </td>
            </tr>
        `;
    });

    output += "</tbody>";
    $('table').append(output);
}

// JavaScript function to submit POST request with form
function submitForm(taskNum, memberId) {
    // 숨겨진 form을 통해 POST 방식으로 전달
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'comm'; // 서블릿으로 전달할 URL

    // 'num'과 'userid' 값을 hidden input으로 추가
    const numInput = document.createElement('input');
    numInput.type = 'hidden';
    numInput.name = 'num';
    numInput.value = taskNum;
    form.appendChild(numInput);

    const userInput = document.createElement('input');
    userInput.type = 'hidden';
    userInput.name = 'userid';
    userInput.value = memberId;
    form.appendChild(userInput);

    // form을 body에 추가하여 전송
    document.body.appendChild(form);
    form.submit();
}
