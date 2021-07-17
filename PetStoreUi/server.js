// modules =================================================
var express        = require('express');
var app            = express();
var bodyParser     = require('body-parser');
var methodOverride = require('method-override');

var port = process.env.PORT || 10080; // set our port

// get all data/stuff of the body (POST) parameters
app.use(bodyParser.json()); // parse application/json 
app.use(bodyParser.json({ type: 'application/vnd.api+json' })); // parse application/vnd.api+json as json
app.use(bodyParser.urlencoded({ extended: true })); // parse application/x-www-form-urlencoded

app.use(methodOverride('X-HTTP-Method-Override')); // override with the X-HTTP-Method-Override header in the request. simulate DELETE/PUT
app.use(express.static(__dirname + '/public')); // set the static files location /public/img will be /img for users

var base="/api/v2/";

app.post(base + 'pet', function(req, res){
  res.send(200, "4");
});

app.delete(base + 'pet/1', function(req, res){
  res.send(200);
});

app.delete(base + 'pet/2', function(req, res){
  res.send(200);
});

app.delete(base + 'pet/3', function(req, res){
  res.send(200);
});

app.delete(base + 'pet/4', function(req, res){
  res.send(200);
});

// start app ===============================================
app.listen(port);	
console.log('Magic happens on port ' + port); 			// shoutout to the user
exports = module.exports = app; 						// expose app
