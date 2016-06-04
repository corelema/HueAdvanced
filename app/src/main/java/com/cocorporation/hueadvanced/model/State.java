package com.cocorporation.hueadvanced.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class State {

    private Boolean on;
    private Integer bri;
    private Integer hue;
    private Integer sat;
    private String effect;
    private List<Float> xy = new ArrayList<Float>();
    private Integer ct;
    private String alert;
    private String colormode;
    private Boolean reachable;

    /**
     * No args constructor for use in serialization
     *
     */
    public State() {
    }

    /**
     *
     * @param bri
     * @param effect
     * @param sat
     * @param reachable
     * @param alert
     * @param hue
     * @param colormode
     * @param on
     * @param ct
     * @param xy
     */
    public State(Boolean on, Integer bri, Integer hue, Integer sat, String effect, List<Float> xy, Integer ct, String alert, String colormode, Boolean reachable) {
        this.on = on;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.effect = effect;
        this.xy = xy;
        this.ct = ct;
        this.alert = alert;
        this.colormode = colormode;
        this.reachable = reachable;
    }

    /**
     *
     * @return
     * The on
     */
    public Boolean getOn() {
        return on;
    }

    /**
     *
     * @param on
     * The on
     */
    public void setOn(Boolean on) {
        this.on = on;
    }

    public State withOn(Boolean on) {
        this.on = on;
        return this;
    }

    /**
     *
     * @return
     * The bri
     */
    public Integer getBri() {
        return bri;
    }

    /**
     *
     * @param bri
     * The bri
     */
    public void setBri(Integer bri) {
        this.bri = bri;
    }

    public State withBri(Integer bri) {
        this.bri = bri;
        return this;
    }

    /**
     *
     * @return
     * The hue
     */
    public Integer getHue() {
        return hue;
    }

    /**
     *
     * @param hue
     * The hue
     */
    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public State withHue(Integer hue) {
        this.hue = hue;
        return this;
    }

    /**
     *
     * @return
     * The sat
     */
    public Integer getSat() {
        return sat;
    }

    /**
     *
     * @param sat
     * The sat
     */
    public void setSat(Integer sat) {
        this.sat = sat;
    }

    public State withSat(Integer sat) {
        this.sat = sat;
        return this;
    }

    /**
     *
     * @return
     * The effect
     */
    public String getEffect() {
        return effect;
    }

    /**
     *
     * @param effect
     * The effect
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    public State withEffect(String effect) {
        this.effect = effect;
        return this;
    }

    /**
     *
     * @return
     * The xy
     */
    public List<Float> getXy() {
        return xy;
    }

    /**
     *
     * @param xy
     * The xy
     */
    public void setXy(List<Float> xy) {
        this.xy = xy;
    }

    public State withXy(List<Float> xy) {
        this.xy = xy;
        return this;
    }

    /**
     *
     * @return
     * The ct
     */
    public Integer getCt() {
        return ct;
    }

    /**
     *
     * @param ct
     * The ct
     */
    public void setCt(Integer ct) {
        this.ct = ct;
    }

    public State withCt(Integer ct) {
        this.ct = ct;
        return this;
    }

    /**
     *
     * @return
     * The alert
     */
    public String getAlert() {
        return alert;
    }

    /**
     *
     * @param alert
     * The alert
     */
    public void setAlert(String alert) {
        this.alert = alert;
    }

    public State withAlert(String alert) {
        this.alert = alert;
        return this;
    }

    /**
     *
     * @return
     * The colormode
     */
    public String getColormode() {
        return colormode;
    }

    /**
     *
     * @param colormode
     * The colormode
     */
    public void setColormode(String colormode) {
        this.colormode = colormode;
    }

    public State withColormode(String colormode) {
        this.colormode = colormode;
        return this;
    }

    /**
     *
     * @return
     * The reachable
     */
    public Boolean getReachable() {
        return reachable;
    }

    /**
     *
     * @param reachable
     * The reachable
     */
    public void setReachable(Boolean reachable) {
        this.reachable = reachable;
    }

    public State withReachable(Boolean reachable) {
        this.reachable = reachable;
        return this;
    }

}