$(document).ready(function () {
    $("#submit").click(() => {

        var settings = {
            "url": "http://localhost:8080/login",
            "method": "POST",
            "timeout": 0,
            "data": JSON.stringify( {
                "username": $("#inputName").val(),
                "password": $("#inputPassword").val()
            }),
            "success": function (data, textStatus, request) {
                console.log('success: ',  request.getAllResponseHeaders());
            },
            "error": function (request, textStatus, errorThrown) {
                alert('User or password incorrect: '+ $("#inputName").val())
                console.log('error: ' + textStatus + ' headers: ' + request.getAllResponseHeaders() + ' ErrorThrown: ' + errorThrown);
            }
        };

        $.ajax(settings).done(function (data, textStatus, request) {
            localStorage.setItem('token', request.getResponseHeader('authorization'))
            console.log('Done Response. Data: ', request.getResponseHeader('authorization'));
            window.location.href = "./studentMe.html";
        });
        return false;  //this is very important because of click. If not here, the page is reloaded and the ajax callback is never called.
    });
});

