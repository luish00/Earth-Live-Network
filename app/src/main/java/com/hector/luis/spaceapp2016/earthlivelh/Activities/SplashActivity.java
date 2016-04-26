package com.hector.luis.spaceapp2016.earthlivelh.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hector.luis.spaceapp2016.earthlivelh.Generic.AppConstant;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.AppController;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.CustomRequest;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.PreferencesManager;
import com.hector.luis.spaceapp2016.earthlivelh.Models.Layer;
import com.hector.luis.spaceapp2016.earthlivelh.Models.TileMatrix;
import com.hector.luis.spaceapp2016.earthlivelh.Models.TileMatrixSet;
import com.hector.luis.spaceapp2016.earthlivelh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class SplashActivity extends AppCompatActivity {

    private final static String TAG = SplashActivity.class.getSimpleName();
    private final long SPLASH_TIME_OUT = 3000;
    private PreferencesManager pref;

    private RelativeLayout lyRoot;
    private ProgressBar progressBar;
    private TextView txv;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new PreferencesManager(this);

        lyRoot = (RelativeLayout) findViewById(R.id.lyRoot);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txv = (TextView) findViewById(R.id.textView);
        btnRetry = (Button) findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayaut();
            }
        });

        if (pref.idDBload()) {
            splashDummy();
        } else {
            loadLayaut();
        }
    }

    public void loadLayaut() {
        showRetry(false);
        Map<String,String> params = new HashMap<>();
        params.put("jsonFile",AppConstant.JSON_RESOURCE_FILE);

        CustomRequest request = new CustomRequest(this, Request.Method.POST, AppConstant.DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               if (!response.isNull("data")) {
                    boolean isProcessData = processData(response.toString());

                   if (isProcessData) {
                       pref.setDBload(true);
                       splashDummy();
                   } else {
                       showRetry(true);
                   }
               } else {
                   Toast.makeText(SplashActivity.this, "Data is null" , Toast.LENGTH_SHORT).show();
                   showRetry(true);
               }
               // splashDummy();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showRetry(true);

                Snackbar.make(lyRoot, R.string.err_load_data, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadLayaut();
                    }
                }).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request, AppConstant.DATA);
    }

    private void splashDummy() {
        progressBar.setVisibility(View.INVISIBLE);
        txv.setText(R.string.app_name);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, SPLASH_TIME_OUT);
    }

    private void showRetry(boolean isError) {
        int showProgrees = isError ? View.INVISIBLE : View.VISIBLE;
        int showBtnRetry = isError ? View.VISIBLE : View.GONE;
        int title = isError ? R.string.app_name : R.string.loading;

        progressBar.setVisibility(showProgrees);
        txv.setText(title);
        btnRetry.setVisibility(showBtnRetry);
    }

    private boolean processData (String sJson) {
        boolean isConfigured = false;// Obtain a Realm instance
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        Realm realm = Realm.getInstance(realmConfig);
        realm.beginTransaction();
        realm.clear(Layer.class);  //clean db
        realm.clear(TileMatrixSet.class);  //clean db
        realm.clear(TileMatrix.class);  //clean db
        realm.commitTransaction();

        try {
            JSONObject data = new JSONObject(sJson).getJSONObject("data");
            JSONObject jsoCapabilities = data.getJSONObject("Capabilities");
            JSONObject jsoContens = jsoCapabilities.getJSONObject("Contents");
            JSONArray jsaLayer = jsoContens.getJSONArray("Layer");
            JSONArray jsaTileMatrixSet = jsoContens.getJSONArray("TileMatrixSet");

            for (int i = 0; i < jsaLayer.length(); i++) {
                realm.beginTransaction();
                Layer layer = realm.createObject(Layer.class);
                JSONObject item = jsaLayer.getJSONObject(i);
                JSONObject itemTitleMatrixSetLink = item.getJSONObject("TileMatrixSetLink");
                JSONObject itemResourceURL = item.getJSONObject("ResourceURL");

                layer.setTitle(item.getString("ows:Title"));

                if (!item.isNull("ows:Metadata")) {
                    JSONObject itemMetaData = item.getJSONObject("ows:Metadata");
                    layer.setMetaDataHref(itemMetaData.getString("-xlink:href"));
                    layer.setMetaDataTitle(itemMetaData.getString("-xlink:title"));
                }

                if (!item.isNull("Dimension")) {
                    JSONObject itemDimension = item.getJSONObject("Dimension");
                    layer.setDimencionIndentifier(itemDimension.getString("ows:Identifier"));
                    layer.setDimencionDefault(itemDimension.getString("Default"));
                    layer.setDimencionCurrent(itemDimension.getBoolean("Current"));
                    layer.setDimencionValue(itemDimension.getString("Value"));

                }
                layer.setTileMatrixSet(itemTitleMatrixSetLink.getString("TileMatrixSet"));

                layer.setResourceURLTemplete(itemResourceURL.getString("-template"));

                realm.commitTransaction();
            }

            for (int i = 0; i<jsaTileMatrixSet.length(); i++) {
                realm.beginTransaction();
                TileMatrixSet tileMatrixSet = realm.createObject(TileMatrixSet.class);
                RealmList<TileMatrix> titleMatrixList = new RealmList();
                JSONObject item = jsaTileMatrixSet.getJSONObject(i);
                JSONArray jsaTileMatrix = item.getJSONArray("TileMatrix");

                String identifier = item.getString("ows:Identifier");
                tileMatrixSet.setIdentifier(identifier);

                for(int j=0; j<jsaTileMatrix.length(); j++) {
                    JSONObject itemTileMatrix = jsaTileMatrix.getJSONObject(j);
                    TileMatrix tileMatrix = realm.createObject(TileMatrix.class);

                    tileMatrix.setIdentifier(itemTileMatrix.getInt("ows:Identifier"));
                    tileMatrix.setTileWidth(itemTileMatrix.getInt("TileWidth"));
                    tileMatrix.setTileHeight(itemTileMatrix.getInt("TileHeight"));
                    tileMatrix.setMatrixWidth(itemTileMatrix.getInt("MatrixWidth"));
                    tileMatrix.setMatrixHeight(itemTileMatrix.getInt("MatrixHeight"));
                }

                tileMatrixSet.setTileMatrixes(titleMatrixList);

                realm.commitTransaction();
            }

            isConfigured = true;
        } catch (JSONException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
       // realm.commitTransaction();

        return isConfigured;
    }
}
