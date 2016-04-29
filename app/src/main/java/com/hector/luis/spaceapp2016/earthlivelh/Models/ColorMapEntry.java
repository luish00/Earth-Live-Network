package com.hector.luis.spaceapp2016.earthlivelh.Models;

import android.graphics.Color;

/**
 * Created by Hector Arredondo on 24/04/2016.
 */
public class ColorMapEntry {
    private int rgb;
    private boolean transparent = false;
    private String value = "";
    private String label = "";

    public int getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        String[] rgbArray = rgb.split(",");
        this.rgb = Color.rgb(Integer.parseInt(rgbArray[0]),Integer.parseInt(rgbArray[1]),Integer.parseInt(rgbArray[2]));
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
