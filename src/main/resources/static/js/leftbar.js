$(document).ready(function () {
    adjustSidebarHeight();

    // 리사이즈 이벤트 핸들러
    $(window).on('resize', function () {
        adjustSidebarHeight();
    });

    const currentPath = window.location.pathname; // 현재 URL 경로 (파라미터 제외)
    const contextPath = '${pageContext.request.contextPath}'; // contextPath 가져오기

    $('.sidebar a').each(function () {
        //게시판 리스트일 경우
        if (currentPath.includes("board/boardList")) {
            const params = new URLSearchParams(window.location.search);
            const boardName2 = params.get('boardName2');
            const thisBoardName2 = decodeURIComponent($(this).attr('href').split('=')[2]);

            if (thisBoardName2 === boardName2) {
                $(this).addClass('active'); // 현재 링크 활성화
            }
            return;
        }

        const link = $(this);
        const linkPath = link.attr('href').split('?')[0]; // 링크 경로에서 파라미터 제거
        // 경로만 비교 (contextPath 포함)
        if (linkPath === currentPath) {
            link.addClass('active'); // 현재 링크 활성화
        }
    });
});

function adjustSidebarHeight() {
    const sidebar = $('.sidebar'); // jQuery 객체로 선택
    sidebar.css('height', $(document).height() + 'px'); // 문서 높이로 동적 설정
}

// 하위 메뉴 열고 닫기
function toggleSubMenu(submenuId) {
    $(submenuId).toggle();
}

// 현재 URL에 따라 메뉴 항목 강조 표시
$(document).ready(function () {
    const currentPath = window.location.pathname;

    $('.sidebar a').each(function () {
        const link = $(this);
        if (link.attr('href') === currentPath) {
            link.addClass('active'); // 현재 링크 활성화
            link.closest('.submenu').show(); // 하위 메뉴 열기
            link.closest('.submenu').prev('a').addClass('active'); // 상위 메뉴 활성화
        }
    });
});

$(document).ready(function () {
    const currentPath = window.location.pathname; // 현재 URL 경로
    const contextPath = '${pageContext.request.contextPath}'; // JSP에서 contextPath 가져오기

    $('.sidebar a').each(function () {
        const link = $(this);
        const linkPath = link.attr('href'); // 각 링크의 href 속성 가져오기

        // URL 비교 (contextPath를 포함한 비교)
        if (linkPath === contextPath + currentPath) {
            link.addClass('active'); // 현재 링크 활성화
        }
    });
});
