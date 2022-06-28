var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/socketPort?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJxaWFvaGUiLCJwaG9uZSI6IjEzMTMzMzMzMzMzIiwib3BlbmlkIjoiMSIsImV4cCI6MTYzOTY1MjMyMn0.wLwHziUyaokYUC3Us3rnRIOcQ5Ta22J2UlTeYjnvM0M');
    stompClient = Stomp.over(socket);
    var headers = {};
    headers["token"] = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJxaWFvaGUiLCJwaG9uZSI6IjEzMTMzMzMzMzMzIiwib3BlbmlkIjoiMSIsImV4cCI6MTYzOTcwOTYzMH0.w25HMwZszIHl21yUa7Te_RcT1IgGU2w5I5LaxbP-THc";
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/message/5', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log(JSON.stringify({'content': $("#name").val(),'to':5,'sendTime':'1234'}));
    stompClient.send("/app/chat/private", {}, JSON.stringify({'content': $("#name").val(),'toUser':5,'sendTime':'1234'}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});