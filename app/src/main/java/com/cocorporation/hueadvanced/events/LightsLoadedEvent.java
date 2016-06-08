package com.cocorporation.hueadvanced.events;

import java.util.List;
import com.cocorporation.hueadvanced.model.Light;

/**
 * Created by Corentin on 3/27/2016.
 */
public class LightsLoadedEvent {
    private List<Light> lights;

    public LightsLoadedEvent(List<Light> lights) {
        this.lights = lights;
    }

    public List<Light> getLights() {
        return lights;
    }
}
