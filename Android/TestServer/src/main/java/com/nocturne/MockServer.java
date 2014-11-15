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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * @author aspela
 */
public class MockServer implements Container {

    private java.sql.Connection dbConnection;

    public static void main(final String[] list) throws Exception {
        System.out.println("MockServer() main() starting");
        final MockServer mockSvr = new MockServer();
        final Server server = new ContainerServer(mockSvr);
        final Connection socketConnection = new SocketConnection(server);
        final SocketAddress address = new InetSocketAddress(9090);

        mockSvr.dbInitialise();

        System.out.println("here we go");
        socketConnection.connect(address);
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
                }else if (name.equalsIgnoreCase("connect")) {
                    handleRequestUserConnect(request, body);
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

        //FIXME : parse request message


        //FIXME : add user to database


        // body.println("{" + getJsonString("key", "value") + "}");
        //body.println("{\"RESTResponseMsg\": {\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}}");
        body.println("{\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}");
    }

    /**
     * @param request
     * @param body
     */
    private void handleRequestUserConnect(final Request request, final PrintStream body) {
        System.out.println("handleRequestUserConnect()");
        // body.println("{" + getJsonString("key", "value") + "}");
        //body.println("{\"RESTResponseMsg\": {\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}}");
        body.println("{\"request\":\"/users/register\",\"status\":\"success\",\"message\": \"User registered\"}");
    }

    public void dbInitialise() {
        try {
            dbOpenConnection();
            if (!isDbSetup()) {
                dbCreateTables();
                dbCreateDummyData();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public boolean isDbSetup() {
        boolean issetup = false;
        Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            //rs.first();
            while (rs.next()) {
                String tbleName = rs.getString("name");
                if (tbleName == "nocturne_user_sensors") {
                    issetup = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return issetup;
    }

    private void dbOpenConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:mockserver.db");
            Properties clientInfo = dbConnection.getClientInfo();
            DatabaseMetaData metadata = dbConnection.getMetaData();
            String catalog = dbConnection.getCatalog();
            System.out.println("database opened successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void dbCloseConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dbCreateTables() {
        try {
            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_users ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_users (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  username VARCHAR(45) NOT NULL,\n" +
                    "  name_first VARCHAR(45) NOT NULL,\n" +
                    "  name_last VARCHAR(45) NOT NULL,\n" +
                    "  email1 VARCHAR(45) NOT NULL,\n" +
                    "  phone_mbl VARCHAR(45) NOT NULL,\n" +
                    "  phone_home VARCHAR(45) ,\n" +
                    "  addr_line1 VARCHAR(45) ,\n" +
                    "  addr_line2 VARCHAR(45) ,\n" +
                    "  addr_line3 VARCHAR(45) ,\n" +
                    "  postcode VARCHAR(45),\n" +
                    "  registration_status VARCHAR(45) NOT NULL)").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_conditions ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_conditions (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  condition_name VARCHAR(45) NOT NULL,\n" +
                    "  condition_desc VARCHAR(45) NULL);").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_user_condition ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_user_condition (\n" +
                    "  user_id INTEGER NOT NULL,\n" +
                    "  condition_id INTEGER NOT NULL,\n" +
                    "  PRIMARY KEY (user_id, condition_id));").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_user_connect ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_user_connect (\n" +
                    "  patient_user_id VARCHAR(45) NOT NULL,\n" +
                    "  caregiver_user_id VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (patient_user_id, caregiver_user_id));").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_alerts ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_alerts (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  user_Id INTEGER NOT NULL,\n" +
                    "  alert_name VARCHAR(45) NOT NULL,\n" +
                    "  alert_desc VARCHAR(45) NULL,\n" +
                    "  response VARCHAR(255) NULL,\n" +
                    "  response_sent TINYINT(1) NULL);").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_sensor ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_sensor (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  sensor_name VARCHAR(45) NULL,\n" +
                    "  sensor_desc VARCHAR(45) NULL);").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_sensor_timeperiods ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_sensor_timeperiods (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  start_time VARCHAR(45) NULL,\n" +
                    "  stop_time VARCHAR(45) NULL,\n" +
                    "  sensor_value_exprected VARCHAR(45) NULL,\n" +
                    "  sensor_warm_time VARCHAR(45) NULL,\n" +
                    "  sensor_alert_time VARCHAR(45) NULL,\n" +
                    "  sensor_id INTEGER NOT NULL);").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_sensor_reading ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_sensor_reading (\n" +
                    "  _id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  sensor_id INTEGER NOT NULL,\n" +
                    "  sensor_value VARCHAR(45) NOT NULL,\n" +
                    "  sensor_reading_time VARCHAR(45) NOT NULL);").execute();

            dbConnection.prepareStatement("DROP TABLE IF EXISTS nocturne_user_sensors ;").execute();
            dbConnection.prepareStatement("CREATE TABLE IF NOT EXISTS nocturne_user_sensors (\n" +
                    "  user_id INTEGER NOT NULL,\n" +
                    "  sensor_timeperiods_id INTEGER NOT NULL,\n" +
                    "  sensor_reading__id INTEGER NOT NULL,\n" +
                    "  sensor_reading_sensor_id INTEGER NOT NULL,\n" +
                    "  PRIMARY KEY (user_id, sensor_timeperiods_id));").execute();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void dbCreateDummyData() {
        Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            String sql = "INSERT INTO nocturne_users (" +
                    "username," +
                    "name_first,name_last," +
                    "email1," +
                    "phone_mbl," +
                    "registration_status) " +
                    "VALUES ('user2', 'Andy2', 'Aspell2', 'droidinactu@gmail.com', '07986', 'REGISTERED' );";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

}
