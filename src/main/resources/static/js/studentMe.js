$(document).ready(function () {
    $.ajax({
        headers: {'Authorization': localStorage.getItem('token')},
        url: "http://localhost:8080/students/me",
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            $('#idStudent').text(data['id']);
            $('#name').text(data['name']);
            $('#secondName').text(data['secondName']);
            $('#email').text(data['email']);
        },
        error: function() { alert('Failed!'); },

    });
});

