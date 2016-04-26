package com.hector.luis.spaceapp2016.earthlivelh.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hector.luis.spaceapp2016.earthlivelh.Generic.AppConstant;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.AppController;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.BitmapLruCache;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.CoreImagenResource;
import com.hector.luis.spaceapp2016.earthlivelh.Models.Layer;
import com.hector.luis.spaceapp2016.earthlivelh.Models.SolicitudLayer;
import com.hector.luis.spaceapp2016.earthlivelh.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MapTestZoom0Activity extends AppCompatActivity {
    private Realm realm;

    private ImageView img0;
    private NetworkImageView img1;
    private List<String> urlList = new ArrayList<>();

    private LinearLayout lyCoastColor;
    private LinearLayout lyCoastLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test_zoom_0);

        String layerTitle = getIntent().getStringExtra(AppConstant.KEY_INTENT_MAP);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);

        Layer results = realm.where(Layer.class)
                .equalTo("title", layerTitle).findFirst();

        img0 = (ImageView) findViewById(R.id.img0);
        img1 = (NetworkImageView) findViewById(R.id.img1);
        lyCoastColor = (LinearLayout) findViewById(R.id.lyCoastTrueColor);
        lyCoastLinear = (LinearLayout) findViewById(R.id.lyCoastLines);

        if (results != null) {
            urlList.addAll(new CoreImagenResource().getImgenUrlTest(new SolicitudLayer(results, 0, "")));

            //img0.setImageUrl(urlList.get(0), imageLoader);
            img1.setImageUrl(urlList.get(1), imageLoader);


           ImageRequest request0 = new ImageRequest(urlList.get(0), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    img0.setImageBitmap(response);
                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img0.setImageResource(R.mipmap.ic_launcher);
                }
            });
//
            AppController.getInstance().addToRequestQueue(request0);
//            ImageRequest request1 = new ImageRequest(urlList.get(0), new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    img1.setImageBitmap(response);
//                }
//            }, 512, 512, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//
//            AppController.getInstance().addToRequestQueue(request0);
//            AppController.getInstance().addToRequestQueue(request1);
        }
    }

    public void onHideCoastColor(View v) {
        boolean isVisible = lyCoastColor.getVisibility() == View.VISIBLE ? true : false;

        if (isVisible)
            lyCoastColor.setVisibility(View.GONE);
        else
            lyCoastColor.setVisibility(View.VISIBLE);
    }
}
