package com.cocorporation.hueadvanced.API;

import android.util.Log;

import com.cocorporation.hueadvanced.model.Light;
import com.cocorporation.hueadvanced.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Corentin on 6/4/2016.
 */
public class LightEndPoints {
    public void getLights() {
        LightsEndPointsInterface service = ServiceFactory.createRetrofitService(LightsEndPointsInterface.class, LightsEndPointsInterface.SERVICE_ENDPOINT);
        service.getLightsReactive()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public final void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public final void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        List<Light> lights = lightsTransformer(jsonObject);


                    }
                });
    }

    private List<Light> lightsTransformer(JsonObject jsonObject) {
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
}




