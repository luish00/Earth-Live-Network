package com.hector.luis.spaceapp2016.earthlivelh.Models;

import io.realm.RealmObject;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class TileMatrix extends RealmObject {
    private int identifier;
    private int tileWidth;
    private int tileHeight;
    private int matrixWidth;    //row
    private int matrixHeight;   //col

    public TileMatrix() {}

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getMatrixHeight() {
        return matrixHeight;
    }

    public void setMatrixHeight(int matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getMatrixWidth() {
        return matrixWidth;
    }

    public void setMatrixWidth(int matrixWidth) {
        this.matrixWidth = matrixWidth;
    }
}
