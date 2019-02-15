let express = require('express');

let port = 9000;

let app = express();
let server = app.listen(port, function(){
	console.log('listening to requests on port ' + port);
});
console.log('im alive');

app.use(express.static('public'));




