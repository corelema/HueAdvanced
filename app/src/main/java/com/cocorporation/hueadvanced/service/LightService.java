package com.cocorporation.hueadvanced.service;

import android.util.Log;

import com.cocorporation.hueadvanced.API.LightsEndPointsInterface;
import com.cocorporation.hueadvanced.model.Light;
import com.cocorporation.hueadvanced.service.events.LightsLoadedEvent;
import com.cocorporation.hueadvanced.service.events.LoadLightsEvent;
import com.cocorporation.hueadvanced.service.events.TurnOnOffEvent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Corentin on 3/24/2016.
 */
public class LightService {
    private EventBus bus;
    private Retrofit retrofit;
    private LightsEndPointsInterface apiService;

    public static final String TAG = "LightService";

    public static final String BASE_URL = "http://192.168.0.103/api/26cd52782ddd2ddf2828f4283814d5ef/";

    public LightService(EventBus bus) {
        this.bus = bus;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(LightsEndPointsInterface.class);
    }

    @Subscribe
    public void getLights(LoadLightsEvent event) {
        Call<JsonObject> call = apiService.getLights();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "Call succeed:\n" + call);

                Integer lightNumber = 1;
                Gson gson = new Gson();
                List<Light> lights = new ArrayList<Light>();

                JsonElement lightJson = response.body().get((lightNumber).toString());
                while (lightJson != null) {
                        Light light = gson.fromJson(lightJson, Light.class);
                    light.setLightId(lightNumber);
                        lights.add(light);

                    Log.d(TAG, "Item number " + lightNumber + ":\n" + response.body().get("1").toString());
                    lightNumber++;
                    lightJson = response.body().get((lightNumber).toString());
                }

                LightsLoadedEvent event = new LightsLoadedEvent(lights);
                bus.post(event);

                Log.d(TAG, "Total number of lights: " + lights.size());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Call failed:\n" + call);
            }
        });

    }

    @Subscribe
    public void turnOnOffLight(TurnOnOffEvent event) {
        String body = "{\"on\": " + (event.isOn()?"true":"false") + "}";
        JsonParser parser = new JsonParser();
        Call<ResponseBody> call = apiService.turnOnOffLight(event.getLightId(), parser.parse(body));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Call Successful");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Call Failed");
            }
        });
    }
}
