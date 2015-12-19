package com.cocorporation.hueadvanced.hue;

import android.util.Log;

import com.cocorporation.hueadvanced.messages.MessagesManager;
import com.cocorporation.hueadvanced.messages.MessagesType;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

/**
 * Created by Corentin on 12/10/2015.
 */
public class AccessPointConnectListener implements PHSDKListener {

    public static final String TAG = "Listener";

    private PHHueSDK phHueSDK;
    private AccessPointListAdapter adapter;

    public AccessPointConnectListener() {
        this.phHueSDK = PHHueSDK.getInstance();
    }

    @Override
    public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
        Log.w(TAG, "onCacheUpdated");
    }

    @Override
    public void onBridgeConnected(PHBridge phBridge, String s) {
        Log.w(TAG, "onBridgeConnected");
    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
        Log.w(TAG, "onAuthenticationRequired");
    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
        Log.w(TAG, "onAccessPointsFound");
        PHWizardAlertDialog.getInstance().closeProgressDialog();
        if (accessPoints != null && accessPoints.size() > 0) {
            phHueSDK.getAccessPointsFound().clear();
            phHueSDK.getAccessPointsFound().addAll(accessPoints);

            MessagesManager.getInstance().sendMessage(MessagesType.UPDATE_LIST_OF_ACCESS_POINTS, null);
        }
    }

    @Override
    public void onError(int i, String s) {
        Log.w(TAG, "onError");
    }

    @Override
    public void onConnectionResumed(PHBridge phBridge) {
        Log.w(TAG, "onConnectionResumed");
    }

    @Override
    public void onConnectionLost(PHAccessPoint phAccessPoint) {
        Log.w(TAG, "onConnectionLost");
    }

    @Override
    public void onParsingErrors(List<PHHueParsingError> list) {
        Log.w(TAG, "onParsingErrors");
    }
}
