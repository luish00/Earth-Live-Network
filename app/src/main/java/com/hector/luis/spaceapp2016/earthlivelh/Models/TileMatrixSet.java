package com.hector.luis.spaceapp2016.earthlivelh.Models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class TileMatrixSet extends RealmObject {
    private String identifier;
    RealmList<TileMatrix> tileMatrixes;

    public TileMatrixSet() { }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<TileMatrix> getTileMatrixes() {
        return tileMatrixes;
    }

    public void setTileMatrixes(RealmList<TileMatrix> tileMatrixes) {
        this.tileMatrixes = tileMatrixes;
    }
}

