    $(document).ready(function () {
    const lang_kor = {
    "decimal": "",
    "emptyTable": "데이터가 없습니다.",
    "info": "_START_-_END_ (총 _TOTAL_명)",
    "infoEmpty": "0명",
    "infoFiltered": "(전체 _MAX_명 중 검색 결과)",
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
    $('#empListTable').DataTable({
    language: lang_kor,
    columnDefs: [
{ targets: [6], visible: false } // 6번째 컬럼(jobId) 숨기기
    ],
    order: [[6, "asc"]] // 숨겨진 컬럼(jobId) 기준으로 초기 정렬
});
});
