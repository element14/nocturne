package com.bime.nocturne;

import com.bime.nocturne.datamodel.Alert;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;
import com.projectnocturne.datamodel.SensorReading;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by jbi on 18/03/2015.
 */
public interface RetrofitNetworkService {

    /**
     * Create a new user
     *
     * @param user
     * @param cb
     */
    @POST("/users/register")
    void createUser(@Body User user, Callback<User> cb);

    /**
     * Get all users
     *
     * @param cb
     */
    @GET("/users")
    void getUsers(Callback<List<User>> cb);

    /**
     * Get information about user {useremail}
     *
     * @param useremail
     * @param cb
     */
    @GET("/users/user_email={useremail}")
    void getUser(@Path("useremail") String useremail, Callback<User> cb);

    /**
     * Update a users details
     *
     * @param user
     * @param cb
     */
    @POST("/users")
    void updateUser(@Body User user, Callback<User> cb);

    /**
     * Request a connection between two users via their email addresses
     *
     * @param user
     * @param cb
     */
    @GET("/users/connect/")
    void userConnect(@Body UserConnect user, Callback<UserConnect> cb);

    /**
     * Get a connections for the user {useremail}
     *
     * @param cb
     */
    @GET("/users/connect/user_email={useremail}")
    void getconnections(@Path("useremail") String useremail, Callback<List<UserConnect>> cb);

    /**
     * Send an alert
     *
     * @param cb
     */
    @POST("/alerts/send")
    void sendAlert(@Body Alert alrt, Callback<UserConnect> cb);

    /**
     * Send a response to an alert
     *
     * @param cb
     */
    @POST("/alerts/respond")
    void sendAlertResponse(@Body Alert alrtRspnse, Callback<UserConnect> cb);

    /**
     * get all alerts sent to user {useremail}
     *
     * @param cb
     */
    @GET("/alerts/to/user_email={useremail}")
    void getAlertsTo(@Path("useremail") String useremail, Callback<List<Alert>> cb);

    /**
     * get all alerts sent from user {useremail}
     *
     * @param cb
     */
    @GET("/alerts/from/user_email={useremail}")
    void getAlertsFrom(@Path("useremail") String useremail, Callback<List<Alert>> cb);

    /**
     * create a new  sensor reading
     *
     * @param cb
     */
    @POST("/sensors/reading")
    void sendSensorReading(@Body SensorReading sr, Callback<SensorReading> cb);

    /**
     * gets the sensor readings for user {useremail}
     *
     * @param cb
     */
    @GET("/sensors/reading/user_email={useremail}")
    void getSensorReadings(@Path("useremail") String useremail, Callback<List<SensorReading>> cb);
}
