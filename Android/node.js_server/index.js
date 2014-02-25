var server = require("./server");
var router = require("./router");
var requestHandlers = require("./requestHandlers");
var requestHandlers_users = require("./requestHandlers_users");
var requestHandlers_alerts = require("./requestHandlers_alerts");
var requestHandlers_sensors = require("./requestHandlers_sensors");
var database = require("./database");

var handle = {};
handle["/"] = requestHandlers.start;
handle["/start"] = requestHandlers.start;
handle["/upload"] = requestHandlers.upload;
handle["/show"] = requestHandlers.show;

handle["/users"] = requestHandlers_users.users;
handle["/users/register"] = requestHandlers_users.users_register;
handle["/users/connect"] = requestHandlers_users.users_connect;

handle["/alerts/send"] = requestHandlers_alerts.alerts_send;
handle["/alerts/respond"] = requestHandlers_alerts.alerts_respond;
handle["/alerts/to"] = requestHandlers_alerts.alerts_to;
handle["/alerts/from"] = requestHandlers_alerts.alerts_from;

handle["/sensors/reading"] = requestHandlers_sensors.sensors_reading;

database.createTables();

server.start(router.route, handle);
