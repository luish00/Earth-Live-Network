package com.hector.luis.spaceapp2016.earthlivelh.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector Arredondo on 24/04/2016.
 */
public class MetaDataColor {
    private String units;
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
}
