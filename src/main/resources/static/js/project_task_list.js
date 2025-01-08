$(function () {
    go(1);
    $("#viewcount").change(function () {
        go(1); //보여줄 페이지를 1페이지로 설정
    });
})

function go(page) {
    const limit = $('#viewcount').val();
    const memberId = $('.memberId').val();
    //const data = `limit=${limit}&state=ajax&page=${page}`;
    const data = {limit: limit, state: "ajax", page: page, memberId: memberId}
    ajax(data);
}


function ajax(sdata) {
    console.log(sdata);
    $.ajax({
        data: sdata,
        url: contextPath + "/projectb/list",
        dataType: "json",
        cache: false,
        success: function (data) {
            $("#viewcount").val(data.limit);
            $("thead").find("span").text("글 개수 : " + data.listcount);
            if (data.listcount > 0) {
                $("tbody").remove();
                updateBoardList(data); //게시판 내용 업데이트
                generatePagination(data);
            }
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
        const subject = item.projectTitle.length >= 20 ? item.projectTitle.substr(0, 20) + "..." : item.projectTitle;
        const changeSubject = subject.replace(/</g, '&lt;').replace(/>/g, '&gt;');
        const taskFileOriginal = item.task_file_original ? item.task_file_original : "";
        let file_name = ''; // file_name 기본값 설정

        // task_file_original이 null 또는 undefined일 경우 체크
        if (item.task_file_original && item.task_file_original !== '')
            file_name = item.task_file_original.length > 10 ? item.task_file_type + "파일" : item.task_file_original;

        output += `
            <tr>
                <td>${num--}</td>
                <td><a href="javascript:void(0);" onclick="submitForm(${item.taskNum}, '${item.memberId}')">${changeSubject}</a>${item.board_cnt > 0 ? `[${item.board_cnt}]` : ''}</td>
                <td><div>${item.projectContent}</div></td>
                <td><div>${item.projectDate}</div></td>
                <td><div>${item.projectUpdateDate}</div></td>
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


function setPaging(href, digit, isActive = false) {
    const gray = (href === "" && isNaN(digit)) ? "gray" : "";
    const active = isActive ? "active" : "";
    const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;
    return `<li class="page-item ${active}">${anchor}</li>`;
}

function generatePagination(data) {
    let output = "";

    // 맨 처음 버튼
    let firstHref = data.page > 1 ? `href=javascript:go(1)` : "";
    output += setPaging(firstHref, '<<');

    // 이전 버튼
    let prevHref = data.page > 1 ? `href=javascript:go(${data.page - 1})` : "";
    output += setPaging(prevHref, '<');

    // 페이지 번호
    for (let i = data.startpage; i <= data.endpage; i++) {
        const isActive = (i === data.page);
        let pageHref = !isActive ? `href=javascript:go(${i})` : "";
        output += setPaging(pageHref, i, isActive);
    }

    // 다음 버튼
    let nextHref = (data.page < data.maxpage) ? `href=javascript:go(${data.page + 1})` : "";
    output += setPaging(nextHref, '>');

    // 맨 마지막 버튼
    let lastHref = data.page < data.maxpage ? `href=javascript:go(${data.maxpage})` : "";
    output += setPaging(lastHref, '>>');

    $('.pagination').empty().append(output);
}
