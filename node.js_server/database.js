// see : http://blog.modulus.io/nodejs-and-sqlite
//

var fs = require("fs");
var file = "nocturne.db";
var exists = fs.existsSync(file);

var sqlite3 = require("sqlite3").verbose();
var db;

function createDb() {
    console.log("createDb chain");
    db = new sqlite3.Database('chain.sqlite3', createTables);
}


function createTables() {
    console.log("createTables()");
    db.run("CREATE TABLE IF NOT EXISTS `users` (`_id` INT NOT NULL, `username` VARCHAR(45) NOT NULL, `name_first` VARCHAR(45) NOT NULL, `name_last` VARCHAR(45) NOT NULL, `email1` VARCHAR(45) NOT NULL, `phone_mbl` VARCHAR(45) NOT NULL, `phone_home` VARCHAR(45) NULL, `addr_line1` VARCHAR(45) NOT NULL,`addr_line2` VARCHAR(45) NULL,`addr_line3` VARCHAR(45) NULL,`postcode` VARCHAR(45) NOT NULL,PRIMARY KEY (`_id`))");
    db.run("CREATE TABLE IF NOT EXISTS `nocturne`.`conditions` (`_id` INT NOT NULL, `condition_name` VARCHAR(45) NOT NULL, `condition_desc` VARCHAR(45) NULL, PRIMARY KEY (`_id`))");
    db.run("");
    db.run("");
    db.run("");
}


function execSql(SqlStr) {
    console.log("execSql()");
    var stmt = db.prepare();
    stmt.run(SqlStr);
    stmt.finalize();
}


function querySql(SqlStr) {
    console.log("querySql()");
    db.each(SqlStr, function(err, row) {
        console.log(row.id + ": " + row.thing);
    });
}

exports.createDb = createDb;
exports.createTables = createTables;
exports.execSql = execSql;
exports.querySql = querySql;
