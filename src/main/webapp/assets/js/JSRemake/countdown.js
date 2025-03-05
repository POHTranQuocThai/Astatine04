// Set the date we're counting down to
var countDownDate = new Date("Apr 13, 2025 00:00:00").getTime();

// Update the count down every 1 second
var x = setInterval(function () {

    // Get today's date and time
    var now = new Date().getTime();

    // Find the distance between now and the count down date
    var distance = countDownDate - now;

    // Time calculations for days, hours, minutes, and seconds
    var days = Math.floor(distance / (1000 * 60 * 60 * 24));
    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((distance % (1000 * 60)) / 1000);

    // Select all <h3> inside .countdown-item
    var countdownItems = document.querySelectorAll(".countdown-item h3");

    if (countdownItems.length === 4) {
        countdownItems[0].innerHTML = days;
        countdownItems[1].innerHTML = hours;
        countdownItems[2].innerHTML = minutes;
        countdownItems[3].innerHTML = seconds;
    }

    // If the count down is over, display "EXPIRED"
    if (distance < 0) {
        clearInterval(x);
        countdownItems.forEach(item => item.innerHTML = "00");
        var demo = document.getElementById("demo");
        if (demo)
            demo.innerHTML = "EXPIRED";
    }
}, 1000);
