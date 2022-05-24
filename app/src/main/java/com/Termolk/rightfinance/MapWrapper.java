package com.Termolk.rightfinance;

import java.util.HashMap;

public class MapWrapper {
    private HashMap<String, MainActivity.Categories> myMap;

    public void setMyMap(HashMap<String, MainActivity.Categories> myMap) {
        this.myMap = myMap;
    }

    public HashMap<String, MainActivity.Categories> getMyMap() {
        return myMap;
    }
}
