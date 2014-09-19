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

/**
 * @author aspela
 */
public class MockServer implements Container {

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
        System.out.println("MockServer() handle() starting");PrintStream body=null;
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

            for (String seg : segments){
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
}
