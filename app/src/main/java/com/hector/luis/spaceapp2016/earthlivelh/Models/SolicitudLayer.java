package com.hector.luis.spaceapp2016.earthlivelh.Models;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class SolicitudLayer {
    private Layer layer;
    private int zoom;
    private String date;

    public SolicitudLayer() { }

    public SolicitudLayer(Layer layer, int zoom, String date) {
        this.layer = layer;
        this.zoom = zoom;
        this.date = date;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
