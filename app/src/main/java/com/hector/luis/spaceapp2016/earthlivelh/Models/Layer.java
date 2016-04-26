package com.hector.luis.spaceapp2016.earthlivelh.Models;

import io.realm.RealmObject;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class Layer extends RealmObject {
    private String title;

    private String metaDataTitle;
    private String metaDataHref;

    private String dimencionIndentifier;
    private String dimencionDefault;
    private boolean dimencionCurrent;
    private String dimencionValue;

    private String TileMatrixSet;

    private String resourceURLTemplete;

    public Layer() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetaDataTitle() {
        return metaDataTitle;
    }

    public void setMetaDataTitle(String metaDataTitle) {
        this.metaDataTitle = metaDataTitle;
    }

    public String getMetaDataHref() {
        return metaDataHref;
    }

    public void setMetaDataHref(String metaDataHref) {
        this.metaDataHref = metaDataHref;
    }

    public String getDimencionIndentifier() {
        return dimencionIndentifier;
    }

    public void setDimencionIndentifier(String dimencionIndentifier) {
        this.dimencionIndentifier = dimencionIndentifier;
    }

    public String getDimencionDefault() {
        return dimencionDefault;
    }

    public void setDimencionDefault(String dimencionDefault) {
        this.dimencionDefault = dimencionDefault;
    }

    public boolean isDimencionCurrent() {
        return dimencionCurrent;
    }

    public void setDimencionCurrent(boolean dimencionCurrent) {
        this.dimencionCurrent = dimencionCurrent;
    }

    public String getDimencionValue() {
        return dimencionValue;
    }

    public void setDimencionValue(String dimencionValue) {
        this.dimencionValue = dimencionValue;
    }

    public String getTileMatrixSet() {
        return TileMatrixSet;
    }

    public void setTileMatrixSet(String tileMatrixSet) {
        TileMatrixSet = tileMatrixSet;
    }

    public String getResourceURLTemplete() {
        return resourceURLTemplete;
    }

    public void setResourceURLTemplete(String resourceURLTemplete) {
        this.resourceURLTemplete = resourceURLTemplete;
    }
}
//
//class LayerMetaData {
//    private String metaDataTitle;
//    private String metaDataHref;
//}
//
//class Dimencion {
//    private String dimencionIndentifier;
//    private String dimencionDefault;
//    private boolean dimencionCurrent;
//    private String dimencionValue;
//}
//
//class TileMatrixSetLink{
//    private String TileMatrixSet;
//}
//
//class ResourceURL {
//    private String resourceURLTemplete;
//}