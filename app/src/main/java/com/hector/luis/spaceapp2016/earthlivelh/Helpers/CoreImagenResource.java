package com.hector.luis.spaceapp2016.earthlivelh.Helpers;

import com.hector.luis.spaceapp2016.earthlivelh.Models.Layer;
import com.hector.luis.spaceapp2016.earthlivelh.Models.SolicitudLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class CoreImagenResource {

    public List<String> getImgenUrlTest(SolicitudLayer prObject) {
        List<String> list = new ArrayList<>();

        String templete = prObject.getLayer().getResourceURLTemplete();
        templete = templete.replace("{TileMatrixSet}",prObject.getLayer().getTileMatrixSet());
        templete = templete.replace("{TileMatrix}",String.valueOf(prObject.getZoom()));
        if (prObject.getLayer().getDimencionDefault() != null)
            templete = templete.replace("{Time}", prObject.getLayer().getDimencionDefault());

        String templete0 = templete.replace("{TileRow}/{TileCol}","0/0");
        String templete1 = templete.replace("{TileRow}/{TileCol}","0/1");

        list.add(templete0);
        list.add(templete1);

        return list;
    }
}
