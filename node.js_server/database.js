// see : http://blog.modulus.io/nodejs-and-sqlite
// 

var fs = require("fs");
var file = "nocturne.db";
var exists = fs.existsSync(file);

var sqlite3 = require("sqlite3").verbose();
var db = new sqlite3.Database(file);

function createTables() {
db.serialize(function() {
  if(!exists) {
    db.run("CREATE TABLE IF NOT EXISTS `users` (`_id` INT NOT NULL, `username` VARCHAR(45) NOT NULL, `name_first` VARCHAR(45) NOT NULL, `name_last` VARCHAR(45) NOT NULL, `email1` VARCHAR(45) NOT NULL, `phone_mbl` VARCHAR(45) NOT NULL, `phone_home` VARCHAR(45) NULL, `addr_line1` VARCHAR(45) NOT NULL,`addr_line2` VARCHAR(45) NULL,`addr_line3` VARCHAR(45) NULL,`postcode` VARCHAR(45) NOT NULL,PRIMARY KEY (`_id`))");
    db.run("CREATE TABLE IF NOT EXISTS `nocturne`.`conditions` (`_id` INT NOT NULL, `condition_name` VARCHAR(45) NOT NULL, `condition_desc` VARCHAR(45) NULL, PRIMARY KEY (`_id`))");
    db.run("");
    db.run("");
    db.run("");
  }
}
}


function execSql(SqlStr) {
db.serialize(function() {
  var stmt = db.prepare();
  stmt.run(SqlStr);
  stmt.finalize();
}
}


function querySql(SqlStr) {
db.serialize(function() {
  db.each(SqlStr, function(err, row) {
    console.log(row.id + ": " + row.thing);
  });
}
}


function test() {
db.serialize(function() {
  if(!exists) {
    db.run("CREATE TABLE Stuff (thing TEXT)");
  }

  var stmt = db.prepare("INSERT INTO Stuff VALUES (?)");

  //Insert random data
  var rnd;
  for (var i = 0; i &lt; 10; i++) {
    rnd = Math.floor(Math.random() * 10000000);
    stmt.run("Thing #" + rnd);
  }

  stmt.finalize();
});
}

exports.dbase = dbase;
