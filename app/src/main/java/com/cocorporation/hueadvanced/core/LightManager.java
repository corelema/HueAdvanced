package com.cocorporation.hueadvanced.core;

import android.content.Context;
import android.util.Log;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Corentin on 1/12/2016.
 */
public class LightManager {
    public static final String TAG = "LightManager";

    private static LightManager mLightManager;
    private PHHueSDK phHueSDK;
    private PHBridge bridge;

    private List<PHLight> allLights;

    public static LightManager get(Context context) {//TODO: check in the book why they passe the context
        if (mLightManager == null) {
            mLightManager = new LightManager();
        }
        return mLightManager;
    }

    public static LightManager get() {
        if (mLightManager == null) {
            mLightManager = new LightManager();
        }
        return mLightManager;
    }

    private LightManager() {
        phHueSDK = PHHueSDK.create(); //TODO: Check if I need to create a new one across the code
        bridge = phHueSDK.getSelectedBridge();

        if (bridge != null) {
            allLights = bridge.getResourceCache().getAllLights();
        } else {
            allLights = new ArrayList<PHLight>();
        }

    }

    private void debugFunction(PHLight light) {
        PHLightState lightState = light.getLastKnownLightState();
        Log.d(TAG, "Debug = " + lightState.getTransitionTime());
    }

    public boolean switchOnMax(PHLight light) {
        if (allLights.contains(light)) {
            PHLightState lightState = light.getLastKnownLightState();
            lightState.setTransitionTime(65535);
            debugFunction(light);
            if (!lightState.isOn() || lightState.getBrightness() != 254) {
                lightState.setOn(true);
                lightState.setBrightness(254);
                lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_NONE);
                bridge.updateLightState(light, lightState, listener);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean switchOnPrevious(PHLight light) {
        if (allLights.contains(light)) {
            PHLightState lightState = light.getLastKnownLightState();
            if (!lightState.isOn()) {
                lightState.setOn(true);
                bridge.updateLightState(light, lightState, listener);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean switchOff(PHLight light) {
        if (allLights.contains(light)) {
            debugFunction(light);
            PHLightState lightState = light.getLastKnownLightState();
            if (lightState.isOn()) {
                lightState.setOn(false);
                bridge.updateLightState(light, lightState, listener);
            }
            return true;
        } else {
            return false;
        }
    }

    /*GETTERS AND SETTERS*/

    public List<PHLight> getAllLights() {
        if (bridge != null) {
            allLights = bridge.getResourceCache().getAllLights();
        }
        return allLights;
    }

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
}
