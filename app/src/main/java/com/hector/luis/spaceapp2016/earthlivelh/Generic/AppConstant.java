package com.hector.luis.spaceapp2016.earthlivelh.Generic;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class AppConstant {
    public final static String KEY_INTENT_MAP = "mapTitle";

    public final static String JSON_RESOURCE_FILE = "WMTSCapabilities.json";

//    public final static String URL_SERVER = "http://192.168.2.108:8080";
    private final static String URL_SERVER_PREF = "/public_html";
    public final static String URL_SERVER = "http://earthlivenetwork.esy.es";
    public final static String DATA = URL_SERVER + "/earthlive/index.php";
    public final static String GET_IMAGE = URL_SERVER + "/earthlive/getImg.php";
    public final static String DECODE_META_DATA = URL_SERVER + "/earthlive/decodeMetaData.php";
}
