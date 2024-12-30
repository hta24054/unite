$(document).ready(function () {
    $('input[name="trip_start"], input[name="trip_end"]').on('change', function () {
        const startDate = $('input[name="trip_start"]').val();
        const endDate = $('input[name="trip_end"]').val();

        if (startDate && endDate) {
            if (new Date(endDate) < new Date(startDate)) {
                alert("종료일은 시작일 이후여야 합니다. 다시 선택해 주세요.");
                $('input[name="trip_start"]').val('');
                $('input[name="trip_end"]').val('');
            }
        }
    });


    const $tripStartInput = $("input[name='trip_start']");
    const $tripEndInput = $("input[name='trip_end']");
    const $cardStartInput = $("input[name='card_start']");
    const $cardEndInput = $("input[name='card_end']");
    const $cardReturnInput = $("input[name='card_return']");
    // 출장 시작일과 종료일에 따라 카드 사용 일자를 자동 설정
    $tripStartInput.on("change", function () {
        $cardStartInput.val($tripStartInput.val());
    });

    $tripEndInput.on("change", function () {
        $cardEndInput.val($tripEndInput.val());
        $cardReturnInput.val($tripEndInput.val());
    });
});