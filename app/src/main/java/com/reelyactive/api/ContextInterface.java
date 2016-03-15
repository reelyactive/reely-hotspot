package com.reelyactive.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by saiimons on 16-03-03.
 */
public interface ContextInterface {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/events")
    Call<Void> events(@Body String body);
}
