package com.cocorporation.hueadvanced.service;

import android.util.JsonReader;
import android.util.Log;

import com.cocorporation.hueadvanced.API.LightsEndPointsInterface;
import com.cocorporation.hueadvanced.model.Light;
import com.cocorporation.hueadvanced.events.LightsLoadedEvent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Corentin on 3/24/2016.
 */
public class LightService {
    public static final String TAG = "LightService";

    public static void getLights(final EventBus bus) {
        LightsEndPointsInterface service = ServiceFactory.createRetrofitService(LightsEndPointsInterface.class, LightsEndPointsInterface.SERVICE_ENDPOINT);
        service.getLights()
                .subscribeOn(Schedulers.newThread())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public final void onCompleted() {

                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.d(TAG, "Call Failed");
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        Log.d(TAG, "Call Successful");

                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = null;

                        try {
                            jsonObject = parser.parse(response.body().string()).getAsJsonObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        List<Light> lights = lightsTransformer(jsonObject);

                        bus.post(new LightsLoadedEvent(lights));
                    }
                });
    }

    private static List<Light> lightsTransformer(JsonObject jsonObject) {
        Integer lightNumber = 1;
        Gson gson = new Gson();
        List<Light> lights = new ArrayList<Light>();

        JsonElement lightJson = jsonObject.get((lightNumber).toString());
        while (lightJson != null) {
            Light light = gson.fromJson(lightJson, Light.class);
            light.setLightId(lightNumber);
            lights.add(light);

            lightNumber++;
            lightJson = jsonObject.get((lightNumber).toString());
        }

        return lights;
    }

    public static void turnOnOffLight(Integer lightId, boolean state) {
        String body = "{\"on\": " + (state ? "true" : "false") + "}";
        JsonParser parser = new JsonParser();

        LightsEndPointsInterface service = ServiceFactory.createRetrofitService(LightsEndPointsInterface.class, LightsEndPointsInterface.SERVICE_ENDPOINT);
        service.turnOnOffLight(lightId, parser.parse(body))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Call Failed");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, "Call Successful");
                    }
                });
    }
}
