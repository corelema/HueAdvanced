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
    String SERVICE_ENDPOINT = "http://192.168.0.100/api/26cd52782ddd2ddf2828f4283814d5ef/";

    @GET("lights")
    Observable<Response<ResponseBody>> getLights();

    @PUT("lights/{lightId}/state")
    Observable<ResponseBody> turnOnOffLight(@Path("lightId") Integer lightId, @Body JsonElement body);
}
