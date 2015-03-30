package com.bime.nocturne;

import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;

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
    @POST("/users/register")
    void createUser(@Body User user, Callback<User> cb);

    @GET("/users")
    void getUsers(Callback<List<User>> callback);

    @GET("/users/user_email={useremail}")
    void getUser(@Path("useremail") String useremail, Callback<User> callback);


    @POST("/micro_posts")
    void createPost(@Body UserConnect mPost, Callback<UserConnect> callback);
}
