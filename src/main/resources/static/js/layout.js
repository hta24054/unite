$(document).ready(function () {
    const sidebarToggle = $('#sidebarToggle');
    if (sidebarToggle.length) {
        sidebarToggle.on('click', function (event) {
            event.preventDefault();
            $('body').toggleClass('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', $('body').hasClass('sb-sidenav-toggled'));
        });
    }
});