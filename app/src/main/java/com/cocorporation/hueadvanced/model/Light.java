package com.cocorporation.hueadvanced.model;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Light {

    private State state;
    private String type;
    private String name;
    private String modelid;
    private String manufacturername;
    private String uniqueid;
    private String swversion;
    private Integer lightId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Light() {
    }

    /**
     *
     * @param uniqueid
     * @param name
     * @param state
     * @param modelid
     * @param manufacturername
     * @param swversion
     * @param type
     */
    public Light(State state, String type, String name, String modelid, String manufacturername, String uniqueid, String swversion, Integer lightId) {
        this.state = state;
        this.type = type;
        this.name = name;
        this.modelid = modelid;
        this.manufacturername = manufacturername;
        this.uniqueid = uniqueid;
        this.swversion = swversion;
        this.lightId = lightId;
    }

    /**
     *
     * @return
     * The state
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(State state) {
        this.state = state;
    }

    public Light withState(State state) {
        this.state = state;
        return this;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public Light withType(String type) {
        this.type = type;
        return this;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Light withName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     * The modelid
     */
    public String getModelid() {
        return modelid;
    }

    /**
     *
     * @param modelid
     * The modelid
     */
    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public Light withModelid(String modelid) {
        this.modelid = modelid;
        return this;
    }

    /**
     *
     * @return
     * The manufacturername
     */
    public String getManufacturername() {
        return manufacturername;
    }

    /**
     *
     * @param manufacturername
     * The manufacturername
     */
    public void setManufacturername(String manufacturername) {
        this.manufacturername = manufacturername;
    }

    public Light withManufacturername(String manufacturername) {
        this.manufacturername = manufacturername;
        return this;
    }

    /**
     *
     * @return
     * The uniqueid
     */
    public String getUniqueid() {
        return uniqueid;
    }

    /**
     *
     * @param uniqueid
     * The uniqueid
     */
    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Light withUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
        return this;
    }

    /**
     *
     * @return
     * The swversion
     */
    public String getSwversion() {
        return swversion;
    }

    /**
     *
     * @param swversion
     * The swversion
     */
    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public Light withSwversion(String swversion) {
        this.swversion = swversion;
        return this;
    }

    /**
     *
     * @return
     * The lightId
     */
    public Integer getLightId() {
        return lightId;
    }

    /**
     *
     * @param lightId
     * The lightId
     */
    public void setLightId(Integer lightId) {
        this.lightId = lightId;
    }

    public Light withLightId(Integer lightId) {
        this.lightId = lightId;
        return this;
    }

}