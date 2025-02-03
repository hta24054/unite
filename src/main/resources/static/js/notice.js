const contextPath = /*[[@{/}]]*/ '';
$(document).ready(function () {
    const lang_kor = {
        "decimal": "",
        "emptyTable": "데이터가 없습니다.",
        "info": "_START_-_END_ (총 _TOTAL_건)",
        "infoEmpty": "0건",
        "infoFiltered": "(전체 _MAX_건 중 검색 결과)",
        "infoPostFix": "",
        "thousands": ",",
        "lengthMenu": "_MENU_개씩 보기",
        "loadingRecords": "로딩중...",
        "processing": "처리중...",
        "search": "검색:",
        "zeroRecords": "검색된 데이터가 없습니다.",
        "paginate": {"first": "첫 페이지", "last": "마지막 페이지", "next": "다음", "previous": "이전"},
        "aria": {"sortAscending": ":오름차순 정렬", "sortDescending": ":내림차순 정렬"}
    };

    // DataTable 초기화
    $('#noticeTable').DataTable({
        language: lang_kor,
        ajax: {
            url: contextPath + "/api/admin/noticeList",
            dataSrc: 'data',
        },
        columns: [
            {data: 'noticeId'},
            {
                data: 'noticeSubject',
                render: function (data, type, row) {
                    return '<span style="cursor:pointer; color:blue; text-decoration:underline;" onclick="openDetailModal(' + row.noticeId + ')">' + data + '</span>';
                }
            },
            {data: 'noticeEndDate'},
        ],
        columnDefs: [
            {width: "10%", targets: 0} // 첫 번째 열(공지 ID) 폭 10%로 설정
        ]
    });


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
                if (data === 1) {
                    alert("공지사항을 등록하였습니다.");
                } else {
                    alert("공지사항 등록 중 오류가 발생했습니다.");
                }
                location.reload();
            },
            error: function () {
                alert("공지사항 등록 중 오류가 발생했습니다.");
            }
        });
    }
}

// 게시글 내용 클릭 시 수정/삭제 버튼을 보이도록 설정하는 함수
function openDetailModal(id) {
    $.ajax({
        url: contextPath + `/api/admin/notice?id=` + id,
        method: 'GET',
        success: function (data) {
            console.log(data)
            if (data != null) {
                const noticeId = data.noticeId;
                const noticeSubject = data.noticeSubject;
                const noticeContent = data.noticeContent;
                const noticeEndDate = data.noticeEndDate;
                $('#noticeId').val(noticeId);
                $('#subject').val(noticeSubject);
                $('#content').summernote('code', noticeContent); // Summernote에 HTML 콘텐츠 설정
                $('#endDate').val(noticeEndDate);
                $('#noticeWriteModal').modal('show'); // 모달 열기

                $('#insertNotice').hide(); // 등록 버튼 숨기기
                $('#updateNotice, #deleteNotice').show(); // 수정/삭제 버튼 보이기
            } else {
                alert("공지사항 불러오기 오류");
            }
        },
        error: function () {
            alert("공지사항 불러오기 오류");
        }
    });
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
                if (data === 1) {
                    alert("공지사항을 수정하였습니다.");
                } else {
                    alert("공지사항 수정 중 오류가 발생했습니다.");
                }
                location.reload();
            },
            error: function () {
                alert("공지사항 수정 중 오류가 발생했습니다.");
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
                if (data === 1) {
                    alert("공지사항을 삭제하였습니다.");
                } else {
                    alert("공지사항 삭제 중 오류가 발생했습니다.");
                }
                location.reload();
            },
            error: function () {
                alert("공지사항 삭제 중 오류가 발생했습니다.");
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