$(document).ready(function () {
    $.ajax({
        url: contextPath + "/api/currency",
        method: "GET",
        dataType: "json",
        success: function (data) {
            const rates = {
                CNH: "#cnh-rate .rate-value",
                EUR: "#eur-rate .rate-value",
                JPY: "#jpy-rate .rate-value",
                USD: "#usd-rate .rate-value"
            };

            data.forEach(function (currency) {
                if (rates[currency.curUnit]) {
                    $(rates[currency.curUnit]).text(currency.curValue+' 원'); // 숫자만 업데이트
                }
            });
        },
        error: function () {
            console.error("Error fetching currency data.");
            $("#currency-list .rate-value").text("데이터를 불러올 수 없습니다.");
        }
    });
});
