package com.cocorporation.hueadvanced.service.events;

/**
 * Created by Corentin on 3/29/2016.
 */
public class TurnOnOffEvent {
    private Integer lightId;
    private boolean on;

    public TurnOnOffEvent(Integer lightId, boolean on) {
        this.lightId = lightId;
        this.on = on;
    }

    public Integer getLightId() {
        return lightId;
    }

    public boolean isOn() {
        return on;
    }
}
