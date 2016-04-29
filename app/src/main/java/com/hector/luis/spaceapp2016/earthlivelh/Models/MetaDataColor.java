package com.hector.luis.spaceapp2016.earthlivelh.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector Arredondo on 24/04/2016.
 */
public class MetaDataColor {
    private String units = "";
    private String type = "";
    private String minLabel = "";
    private String maxLabel = "";
    private List<ColorMapEntry> colorMapEntries = new ArrayList<>();

    public MetaDataColor() { }

    public MetaDataColor(String units, List<ColorMapEntry> colorMapEntries) {
        this.units = units;
        this.colorMapEntries = colorMapEntries;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<ColorMapEntry> getColorMapEntries() {
        return colorMapEntries;
    }

    public void setColorMapEntries(List<ColorMapEntry> colorMapEntries) {
        this.colorMapEntries = colorMapEntries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinLabel() {
        return minLabel;
    }

    public void setMinLabel(String minLabel) {
        this.minLabel = minLabel;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public void setMaxLabel(String maxLabel) {
        this.maxLabel = maxLabel;
    }
}
