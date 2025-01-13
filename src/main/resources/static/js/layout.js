$(document).ready(function () {
    const sidebarToggle = $('#sidebarToggle');
    if (sidebarToggle.length) {
        sidebarToggle.on('click', function (event) {
            event.preventDefault();
            $('body').toggleClass('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', $('body').hasClass('sb-sidenav-toggled'));
        });
    }
    // 날씨정보
    function updateWeatherUI(weather) {
        const iconUrl = `http://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png`;
        $('#weatherIcon').attr('src', iconUrl);
        $('#currentTemp').text(weather.main.temp.toFixed(1));
        $('#maxTemp').text(weather.main.temp_max.toFixed(1));
        $('#minTemp').text(weather.main.temp_min.toFixed(1));
    }

    function fetchWeather() {
        $.get('/api/weather')
            .done(function (data) {
                updateWeatherUI(data);
            })
            .fail(function () {
                console.error('Failed to fetch weather data.');
            });
    }

    // 페이지 로드 시 날씨 정보 가져오기
    fetchWeather();
});