const contextPath = /*[[@{/}]]*/ '';
$(document).ready(function () {
    $('.summernote').summernote({
        height: 500, // set editor height
        minHeight: 500, // set minimum height of editor
        maxHeight: null, // set maximum height of editor
        focus: true // set focus to editable area after initializing summernote
    });

    // 모달이 닫힐 때 에디터와 날짜 필드를 초기화하고 등록 버튼 표시
    $('#noticeWriteModal').on('hidden.bs.modal', function () {
        $('#endDate').val(''); // 날짜 필드 초기화
        $('#subject').val(''); // 날짜 필드 초기화
        $('#content').summernote('code', ''); // 에디터 초기화
    });
});

// '등록' 버튼 클릭 시 '등록' 버튼만 보이도록 설정하고 필드 초기화
function openInsertModal() {
    $('#insertNotice').show(); // 등록 버튼 보이기
    $('#subject').val(''); // 날짜 필드 초기화
    $('#content').summernote('code', ''); // 에디터 초기화
    $('#endDate').val(''); // 날짜 필드 초기화

    $('#updateNotice, #deleteNotice').hide(); // 수정/삭제 버튼 숨기기
    $('#noticeWriteModal').modal('show'); // 모달 열기
}

//등록 함수
function insertNotice() {
    if (!validCheck()) {
        return false;
    }
    if (confirm("정말로 등록하시겠습니까?")) {
        $.ajax({
            url: contextPath + `/api/admin/notice`,
            method: 'POST',
            data: {
                noticeSubject: $('#subject').val(),
                noticeContent: $('#content').val(),
                noticeEndDate: $('#endDate').val()
            },
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("공지사항 등록 중 오류가 발생했습니다.");
            }
        });
    }
}

// 게시글 내용 클릭 시 수정/삭제 버튼을 보이도록 설정하는 함수
function openDetailModal(element) {
    // data-* 속성에서 데이터를 가져옵니다.
    const noticeId = element.dataset.id;
    const noticeSubject = element.dataset.subject;
    const noticeContent = element.dataset.content;
    const noticeEndDate = element.dataset.enddate;

    $('#noticeId').val(noticeId);
    $('#subject').val(noticeSubject);
    $('#content').summernote('code', noticeContent); // Summernote에 HTML 콘텐츠 설정
    $('#endDate').val(noticeEndDate);

    $('#insertNotice').hide(); // 등록 버튼 숨기기
    $('#updateNotice, #deleteNotice').show(); // 수정/삭제 버튼 보이기

    $('#noticeWriteModal').modal('show'); // 모달 열기
}

//수정
function updateNotice() {
    if (!validCheck()) {
        return false;
    }
    if (confirm("정말로 수정하시겠습니까?")) {
        $.ajax({
            url: contextPath + `/api/admin/notice`,
            method: 'PATCH',
            data: {
                noticeId: $('#noticeId').val(),
                noticeSubject: $('#subject').val(),
                noticeContent: $('#content').val(),
                noticeEndDate: $('#endDate').val()
            },
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    }
}

// 삭제
function deleteNotice() {
    const noticeId = $('#noticeId').val();
    if (confirm("정말로 삭제하시겠습니까?")) {
        $.ajax({
            url: contextPath + `/api/admin/notice`,
            method: 'DELETE',
            data: {noticeId: noticeId},
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("삭제 중 오류가 발생했습니다.");
            }
        });
    }
}

function validCheck() {
    const content = $('#content').val();
    const subject = $('#subject').val();
    const endDate = $('#endDate').val();
    if (!subject) {
        alert("제목을 입력해 주세요");
        return false;
    }

    if (subject.length > 150) {
        alert("제목은 50자 이하로 작성해 주세요");
        return false;
    }

    if (!content) {
        alert("내용을 입력해 주세요");
        return false;
    }

    if (!endDate) {
        alert("게시 종료일을 입력해 주세요");
        return false;
    }
    return true;
}