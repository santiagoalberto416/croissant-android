package com.example.santiago.data.services;

import com.example.santiago.data.Event.EventInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by santiago on 06/08/2016.
 */
public interface EventService {

        @GET("getEvent.php")
        Call<EventInfo> getEvent(@Query("id") int user);

}
