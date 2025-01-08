/**
 *
 */
$(document).ready(function () {
    $("#upfile").change(function () {
        console.log($(this).val())
        const inputfile = $(this).val().split('\\');
        $('#filevalue').text(inputfile[inputfile.length - 1]);
    })

    $("form[name=boardform]").submit(function () {
        const $boardSubject = $("#board_subject");
        if ($boardSubject.val().trim() == "") {
            alert("제목을 입력하세요");
            $boardSubject.focus();
            return false;
        }
        const $boardContent = $("#board_content");
        if ($boardContent.val().trim() == "") {
            alert("내용을 입력하세요");
            $boardContent.focus();
            return false;
        }
    })
})
