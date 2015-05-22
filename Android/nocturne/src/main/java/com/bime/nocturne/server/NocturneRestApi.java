package com.bime.nocturne.server;

import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by andy on 02/05/15.
 */
public interface NocturneRestApi {

    @POST("/users/register")
    boolean registerUser(@Body User user);

    @GET("/users")
    List<User> getUsers();

    @GET("/users/user_email={useremail}")
    User getUsers(@Path("useremail") String useremail);

    @POST("/users")
    boolean updateUser(@Body User user);

    @POST("/users/connect")
    boolean connectUsers(@Body UserConnect userCnct);

    @GET("/users/connect/user_email={useremail}")
    List<User> getUserConnections(@Path("useremail") String useremail);

    /*
    @POST("/alerts/send")

    @POST("/alerts/respond")

    @GET("/alerts/to/user_email={useremail}")

    @GET("/alerts/from/user_email={useremail}")

    @POST("/sensors/reading")

    @GET("/sensors/reading/user_email={useremail}")
*/

}