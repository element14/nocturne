var exec = require("child_process").exec;
var querystring = require("querystring");
var fs = require("fs");
var formidable = require('formidable');
var sys = require('sys');

var database = require("./database");


function users(response, request) {
	console.log("Request handler 'users' was called.");
	response.writeHead(200, {
		"Content-Type" : "text/plain"
	});
	response.write("Hello users");
	response.end();
}

function users_register(response, request) {
	console.log("Request handler 'users_register' was called.");
	response.writeHead(200, {
		"Content-Type" : "text/plain"
	});
	response.write("<success>");
	response.end();
}

exports.users = users;
exports.users_register = users_register;
