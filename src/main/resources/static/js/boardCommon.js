//게시글 상세 페이지로 이동
$(document).on('click', '.tr-post', function (event) {
    event.preventDefault();  // 기본 동작 방지 (링크 이동 방지)

    var postId = $(this).data('no');  // data-no에서 게시글 ID 가져오기
    var boardName1 = $(this).data('name1');
    var boardName2 = $(this).data('name2');
    var page = $(this).data('page');
    location.href = `/board/post/detail?no=${postId}&boardName1=${boardName1}&boardName2=${encodeURIComponent(boardName2)}&page=${page}`;
});