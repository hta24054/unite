$(document).ready(function () {
    const contextPath = document.getElementById('contextPath').value;
    console.log(contextPath)
    $.ajax({
        url: contextPath + "home/notice",
        type: "GET",
        dataType: "json",
        success: function (notices) {
            let index = 0;

            function showPopup() {
                if (getCookie('dontShowPopup') === 'true') {
                    console.log('오늘 하루 보지 않기 활성화됨');
                    return; // 팝업 표시 안 함
                }

                if (index < notices.length) {
                    const notice = notices[index];
                    $('#popup-content').html(notice.noticeContent); // 공지사항 내용을 렌더링
                    $('#popup-container').fadeIn(200);
                    index++;
                } else {
                    $('#popup-container').fadeOut(200); // 모든 공지사항이 끝나면 팝업 숨기기
                }
            }

            $('#popup-dont-show').on('click', function () {
                setCookie('dontShowPopup', 'true', 1); // 하루 동안 유지
                $('#popup-container').fadeOut();
            });

            $('#popup-close').on('click', function () {
                $('#popup-container').fadeOut(200, function () {
                    showPopup(); // 다음 공지사항 표시
                });
            });

            // 첫 팝업 표시
            showPopup();
        },
        error: function () {
            alert("공지사항 데이터를 가져오는 데 실패했습니다.");
        }
    });

    function getCookie(name) {
        const cookies = document.cookie.split('; ');
        for (let i = 0; i < cookies.length; i++) {
            const [key, value] = cookies[i].split('=');
            if (key === name) return value;
        }
        return null;
    }

    // 쿠키 설정 함수
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000); // 하루 후 만료
        document.cookie = `${name}=${value}; expires=${date.toUTCString()}; path=/`;
    }
});
