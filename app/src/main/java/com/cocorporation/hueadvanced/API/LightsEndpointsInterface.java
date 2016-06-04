package com.cocorporation.hueadvanced.API;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Corentin on 3/24/2016.
 */
public interface LightsEndPointsInterface {
    String SERVICE_ENDPOINT = "192.168.0.10";

    @GET("lights")
    Call<JsonObject> getLights();

    @GET("lights")
    Observable<JsonObject> getLightsReactive();

    @PUT("lights/{lightId}/state")
    Call<ResponseBody> turnOnOffLight(@Path("lightId") Integer lightId, @Body JsonElement body);
}
