package com.geartocare.ManageMechanicP.Helpers;

public class KeyHelper {
    private String Key;
    private Integer totalKeys;
    private Integer used;
    private Integer onHold;
    private Integer earning;


    public KeyHelper(String key, Integer totalKeys, Integer used, Integer onHold,Integer earning) {
        this.Key = key;
        this.totalKeys = totalKeys;
        this.used = used;
        this.onHold = onHold;
        this.earning = earning;


    }


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Integer getTotalKeys() {
        return totalKeys;
    }

    public void setTotalKeys(Integer totalKeys) {
        this.totalKeys = totalKeys;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getOnHold() {
        return onHold;
    }

    public void setOnHold(Integer onHold) {
        this.onHold = onHold;
    }

    public Integer getEarning() {
        return earning;
    }

    public void setEarning(Integer earning) {
        this.earning = used*50;
    }



}
