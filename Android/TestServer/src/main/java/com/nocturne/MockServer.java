/**
 --------------------------------------------------------------------------

 SOFTWARE FILE HEADER
 -----------------------------

 Classification : UNCLASSIFIED

 Project Name   : ASTRAEA 2 - Mobile I.P. Node

 --------------------------------------------------------------------------

 Copyright Notice
 ----------------

 The copyright in this document is the property of Cassidian
 Systems Limited.

 Without the written consent of Cassidian Systems Limited
 given by Contract or otherwise the document must not be copied, reprinted or
 reproduced in any material form, either wholly or in part, and the contents
 of the document or any method or technique available there from, must not be
 disclosed to any other person whomsoever.

 Copyright 2014 Cassidian Systems Limited.
 --------------------------------------------------------------------------

 */
package com.nocturne;

import org.simpleframework.http.ContentType;
import org.simpleframework.http.Path;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author aspela
 */
public class MockServer implements Container {

    org.hsqldb.Server hsqlServer = null;
    java.sql.Connection connection = null;

    public static void main(final String[] list) throws Exception {
        System.out.println("MockServer() main() starting");
        final Container container = new MockServer();
        final Server server = new ContainerServer(container);
        final Connection connection = new SocketConnection(server);
        final SocketAddress address = new InetSocketAddress(9090);
        System.out.println("here we go");
        connection.connect(address);
    }

    private String getJsonString(final String key, final String value) {
        return "\"" + key + "\":\"" + value + "\"";
    }

    @Override
    public void handle(final Request request, final Response response) {
        System.out.println("MockServer() handle() starting");
        PrintStream body = null;
        try {
            body = response.getPrintStream();
            final long time = System.currentTimeMillis();

            //response.setValue("Content-Type", "text/plain");
            response.setValue("Content-Type", "application/json");
            //response.setValue("Content-Encoding", "application/json");
            response.setValue("Server", "MockServer/1.0 (Simple 4.0)");
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);

            final ContentType type = request.getContentType();
            if (type != null) {
                System.out.println("request context-type was [" + type.toString() + "]");
                final String primary = type.getPrimary();
                final String secondary = type.getSecondary();
                final String charset = type.getCharset();
            }

            final long length = request.getContentLength();
            final String contentBody = request.getContent();
            final boolean persistent = request.isKeepAlive();
            System.out.println("MockServer() handle() contentBody [" + contentBody + "]");

            final Path path = request.getPath();
            final String directory = path.getDirectory();
            final String name = path.getName();
            final String[] segments = path.getSegments();

            System.out.println("MockServer() handle() path [" + path + "]");
            System.out.println("MockServer() handle() directory [" + directory + "]");
            System.out.println("MockServer() handle() name [" + name + "]");

            for (String seg : segments) {
                System.out.println("MockServer() handle() Segment [" + seg + "]");
            }

            final Query query = request.getQuery();
            final String value = query.get("key");

            if (directory.equalsIgnoreCase("/users/")) {
                if (name.equalsIgnoreCase("register")) {
                    handleRequestUserRegister(request, body);
                }
            }

            //body.println("Hello World");

            System.out.println("MockServer() handle() sending response [" + response.toString() + "]");
            response.commit();
            body.close();
        } catch (final Exception e) {
            e.printStackTrace();
            if (body != null) {
                // body.println("{\"RESTResponseMsg\": {\"request\":\"/users/register\",\"status\":\"failed\",\"message\": \"exception occured\"}}");
                body.println("{\"request\":\"/users/register\",\"status\":\"failed\",\"message\": \"exception occured\"}");
                body.close();
            }
        }
    }

    /**
     * @param request
     * @param body
     */
    private void handleRequestUserRegister(final Request request, final PrintStream body) {
        System.out.println("handleRequestUserRegister()");
        // body.println("{" + getJsonString("key", "value") + "}");
        //body.println("{\"RESTResponseMsg\": {\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}}");
        body.println("{\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}");
    }

    private void dbInitialise() {
        dbStartServer();
        dbCreateTables();
    }

    private void dbCreateTables() {
        try {
            dbOpenConnection();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`users` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`users` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `username` VARCHAR(45) NOT NULL,\n" +
                    "  `name_first` VARCHAR(45) NOT NULL,\n" +
                    "  `name_last` VARCHAR(45) NOT NULL,\n" +
                    "  `email1` VARCHAR(45) NOT NULL,\n" +
                    "  `phone_mbl` VARCHAR(45) NOT NULL,\n" +
                    "  `phone_home` VARCHAR(45) NULL,\n" +
                    "  `addr_line1` VARCHAR(45) NOT NULL,\n" +
                    "  `addr_line2` VARCHAR(45) NULL,\n" +
                    "  `addr_line3` VARCHAR(45) NULL,\n" +
                    "  `postcode` VARCHAR(45) NOT NULL,\n" +
                    "  `registration_status` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`_id`))").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`conditions` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`conditions` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `condition_name` VARCHAR(45) NOT NULL,\n" +
                    "  `condition_desc` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`_id`));").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`user_condition` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`user_condition` (\n" +
                    "  `user_id` INT NOT NULL,\n" +
                    "  `condition_id` INT NOT NULL,\n" +
                    "  INDEX `condition_id_idx` (),\n" +
                    "  PRIMARY KEY (`user_id`, `condition_id`),\n" +
                    "  CONSTRAINT `user_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`users` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `condition_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`conditions` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`user_connect` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`user_connect` (\n" +
                    "  `patient_user_id` VARCHAR(45) NOT NULL,\n" +
                    "  `caregiver_user_id` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`patient_user_id`, `caregiver_user_id`),\n" +
                    "  INDEX `user2_id_idx` (),\n" +
                    "  CONSTRAINT `user1_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`users` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `user2_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`users` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`alerts` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`alerts` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `user_Id` INT NOT NULL,\n" +
                    "  `alert_name` VARCHAR(45) NOT NULL,\n" +
                    "  `alert_desc` VARCHAR(45) NULL,\n" +
                    "  `response` VARCHAR(255) NULL,\n" +
                    "  `response_sent` TINYINT(1) NULL,\n" +
                    "  PRIMARY KEY (`_id`),\n" +
                    "  INDEX `user_id_idx` (),\n" +
                    "  CONSTRAINT `user_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`users` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`sensor` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`sensor` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `sensor_name` VARCHAR(45) NULL,\n" +
                    "  `sensor_desc` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`_id`));").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`sensor_timeperiods` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`sensor_timeperiods` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `start_time` VARCHAR(45) NULL,\n" +
                    "  `stop_time` VARCHAR(45) NULL,\n" +
                    "  `sensor_value_exprected` VARCHAR(45) NULL,\n" +
                    "  `sensor_warm_time` VARCHAR(45) NULL,\n" +
                    "  `sensor_alert_time` VARCHAR(45) NULL,\n" +
                    "  `sensor_id` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`_id`, `sensor_id`),\n" +
                    "  INDEX `fk_sensor_timeperiods_sensor1_idx` (`sensor_id` ASC),\n" +
                    "  CONSTRAINT `fk_sensor_timeperiods_sensor1`\n" +
                    "    FOREIGN KEY (`sensor_id`)\n" +
                    "    REFERENCES `nocturne`.`sensor` (`_id`)\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`sensor_reading` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`sensor_reading` (\n" +
                    "  `_id` INT NOT NULL,\n" +
                    "  `sensor_id` INT NOT NULL,\n" +
                    "  `sensor_value` VARCHAR(45) NOT NULL,\n" +
                    "  `sensor_reading_time` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`_id`, `sensor_id`),\n" +
                    "  INDEX `fk_sensor_reading_sensors1_idx` (`sensor_id` ASC),\n" +
                    "  CONSTRAINT `fk_sensor_reading_sensors1`\n" +
                    "    FOREIGN KEY (`sensor_id`)\n" +
                    "    REFERENCES `nocturne`.`sensor` (`_id`)\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();

            connection.prepareStatement("DROP TABLE IF EXISTS `nocturne`.`user_sensors` ;").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `nocturne`.`user_sensors` (\n" +
                    "  `user_id` INT NOT NULL,\n" +
                    "  `sensor_timeperiods_id` INT NOT NULL,\n" +
                    "  `sensor_reading__id` INT NOT NULL,\n" +
                    "  `sensor_reading_sensor_id` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`user_id`, `sensor_timeperiods_id`),\n" +
                    "  INDEX `fk_user_sensors_sensor_timeperiods1_idx` (`sensor_timeperiods_id` ASC),\n" +
                    "  INDEX `fk_user_sensors_sensor_reading1_idx` (`sensor_reading__id` ASC, `sensor_reading_sensor_id` ASC),\n" +
                    "  CONSTRAINT `user_id`\n" +
                    "    FOREIGN KEY ()\n" +
                    "    REFERENCES `nocturne`.`users` ()\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `fk_user_sensors_sensor_timeperiods1`\n" +
                    "    FOREIGN KEY (`sensor_timeperiods_id`)\n" +
                    "    REFERENCES `nocturne`.`sensor_timeperiods` (`_id`)\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `fk_user_sensors_sensor_reading1`\n" +
                    "    FOREIGN KEY (`sensor_reading__id` , `sensor_reading_sensor_id`)\n" +
                    "    REFERENCES `nocturne`.`sensor_reading` (`_id` , `sensor_id`)\n" +
                    "    ON DELETE NO ACTION\n" +
                    "    ON UPDATE NO ACTION);").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dbOpenConnection() {
        try {
            // Getting a connection to the newly started database
            Class.forName("org.hsqldb.jdbcDriver");
            // Default user of the HSQLDB is 'sa' with an empty password
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dbCloseConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dbStartServer() {

        // 'Server' is a class of HSQLDB representing the database server
        try {
            hsqlServer = new org.hsqldb.Server();

            // HSQLDB prints out a lot of information when starting and closing,
            // which we don't need now. Normally you should point the setLogWriter
            // to some Writer object that could store the logs.
            hsqlServer.setLogWriter(null);
            hsqlServer.setSilent(true);

            // The actual database will be named 'xdb' and its settings
            // and data will be stored in files testdb.properties and testdb.script
            hsqlServer.setDatabaseName(0, "xdb");
            hsqlServer.setDatabasePath(0, "file:testdb");

            // Start the database!
            hsqlServer.start();

            // We have here two 'try' blocks and two 'finally' blocks because
            // we have two things to close after all - HSQLDB server and connection
            try {
                // Getting a connection to the newly started database
                Class.forName("org.hsqldb.jdbcDriver");
                // Default user of the HSQLDB is 'sa' with an empty password
                connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");

                // Here we run a few SQL statements to see if everything is working.
                // We first drop an existing 'testtable' (supposing it was there from the previous run),
                // create it once again, insert some data and then read it with SELECT query.
                connection.prepareStatement("drop table testtable;").execute();
                connection.prepareStatement("create table testtable ( id INTEGER, " + "name VARCHAR);").execute();
                connection.prepareStatement("insert into testtable(id, name) " + "values (1, 'testvalue');").execute();
                java.sql.ResultSet rs = connection.prepareStatement("select * from testtable;").executeQuery();

                // Checking if the data is correct
                rs.next();
                System.out.println("Id: " + rs.getInt(1) + " Name: " + rs.getString(2));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Closing the connection
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing the server
            if (hsqlServer != null) {
                hsqlServer.stop();
            }
        }
    }
}
