
var string = "Welcome";

var app = new Vue({
    el: '#app',
    data: {
        message: string
    }
});

function getData() {
    fetch("http://localhost:8080/").then(function (response) {
        // console.log(response.text());
        response.text().then(function(text) {
            app.message = text;
        });
    })
};



console.log(string);


