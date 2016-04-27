package com.hector.luis.spaceapp2016.earthlivelh.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hector.luis.spaceapp2016.earthlivelh.Generic.AppConstant;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.AppController;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.BitmapLruCache;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.CustomRequest;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.DialogDatePiker;
import com.hector.luis.spaceapp2016.earthlivelh.Helpers.Utils;
import com.hector.luis.spaceapp2016.earthlivelh.Models.ColorMapEntry;
import com.hector.luis.spaceapp2016.earthlivelh.Models.Layer;
import com.hector.luis.spaceapp2016.earthlivelh.Models.MetaDataColor;
import com.hector.luis.spaceapp2016.earthlivelh.Models.TileMatrix;
import com.hector.luis.spaceapp2016.earthlivelh.Models.TileMatrixSet;
import com.hector.luis.spaceapp2016.earthlivelh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapActivity extends AppCompatActivity {
    private Layer layer;
    private Bitmap bmpLayout = null;
    private MetaDataColor metaDataColors = new MetaDataColor();
    private ArrayList<ColorMapEntry> colorMapEntries = new ArrayList<>();

    private PhotoViewAttacher mAttacher;
    private ImageView imgCoastLines;
    private TextView txvResolution;
    private EditText edtDate;
    private TextView txvZoomLevel;
    private LinearLayout lyBottom;
    private LinearLayout lyRight;
    private TextView txvUnits;
    private ListView listViewMetaData;
    private ColorMapEntitiesAdapter colorMapEntitiesAdapter;

    private final int ICON_ARROW_UP = R.drawable.ic_keyboard_arrow_up_white_36dp;
    private final int ICON_ARROW_DOWN = R.drawable.ic_keyboard_arrow_down_white_36dp;
    private final int ICON_ARROW_LEFT = R.drawable.ic_keyboard_arrow_left_white_36dp;
    private final int ICON_ARROW_RIGHT = R.drawable.ic_keyboard_arrow_right_white_36dp;

    private Realm realm;

    private final int TIME_OUT_REQUEST = 45000; //45 seg
    private int mZoom = 0;
    private int mTileRow = 2;
    private int mTileCol = 1;
    private String mTime = "";
    private final int[] mTitleRowScale = {2, 3, 5, 10};
    private final int[] mTitleColScale = {1, 2, 3, 5};

    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String layerTitle = getIntent().getStringExtra(AppConstant.KEY_INTENT_MAP);
        String title = layerTitle.replace("_"," ");

        title = title.replace("Day", "(Day)");
        title = title.replace(" Night", " (Night)");
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolbar);


        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);
        dialog = new ProgressDialog(this);

        layer = realm.where(Layer.class)
                .equalTo("title", layerTitle).findFirst();

        imgCoastLines = (ImageView) findViewById(R.id.imgCoastLines);
        txvResolution = (TextView) findViewById(R.id.txvResolution);
        txvZoomLevel = (TextView) findViewById(R.id.txvZoomLevel);
        edtDate = (EditText) findViewById(R.id.edtDate);
        lyBottom = (LinearLayout) findViewById(R.id.lyBottom);
        lyRight = (LinearLayout) findViewById(R.id.lyRight);
        txvUnits = (TextView) findViewById(R.id.txvUnits);
        listViewMetaData = (ListView) findViewById(R.id.listMetaData);
        colorMapEntitiesAdapter = new ColorMapEntitiesAdapter(this, R.layout.item_meta_data_colors, colorMapEntries);
        mAttacher = new PhotoViewAttacher(imgCoastLines, true);

        mAttacher.setScaleType(ImageView.ScaleType.FIT_START);
        mAttacher.setScaleLevels(1, 5, 10);
        listViewMetaData.setAdapter(colorMapEntitiesAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (layer != null) {
            String url = createUrlTemplete();

            getDataRequest();   //obtiene la metadata del layout
            getUrlImagenFromGIBS(url);
        } else{
            Toast.makeText(MapActivity.this, R.string.err_result_null, Toast.LENGTH_SHORT).show();
            this.finish();
        }

        String resolution = getResources().getString(R.string.resolution_per_pixel);
        String[] layoutResoluion = layer.getTileMatrixSet().split("_");
        if (layoutResoluion.length > 0)
            txvResolution.setText(resolution + layoutResoluion[1]);

        mTime = layer.getDimencionDefault();
        if (!TextUtils.isEmpty(mTime)) {
            edtDate.setText(mTime);
        }
    }

    public void loadImagenRequest(String url) {
        startDialog(R.string.load_img);

        ImageRequest request0 = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bmpLayout = response;
                imgCoastLines.setImageBitmap(response);
                mAttacher.update();
                closeDialog();
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgCoastLines.setImageResource(R.drawable.coast_lines_zoom_0);
                closeDialog();
                Toast.makeText(MapActivity.this, R.string.err_create_file, Toast.LENGTH_SHORT).show();
            }
        });

        request0.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT_REQUEST, 1, 1f));
        AppController.getInstance().addToRequestQueue(request0);
    }

    private void handlePanels(LinearLayout prLayout) {
        int lVisibility = View.GONE;

        if (prLayout.getVisibility() == View.GONE) {
            lVisibility = View.VISIBLE;
        }

        prLayout.setVisibility(lVisibility);
        //setPaddingLyRight();
    }

    /**
     * @deprecated
     */
    private void setPaddingLyRight() {
        if (lyBottom.getVisibility() == View.VISIBLE) {
            int paddingPixel = 50;
            float density = getResources().getDisplayMetrics().density;
            int paddingDp = (int) (paddingPixel * density);
            lyRight.setPadding(0, 0, 0, paddingDp);
        } else {
            lyRight.setPadding(0, 0, 0, 0);
        }
    }

    public void onClickZoomOut(View v) {
        if (mZoom > 0) {
            mZoom--;
            boolean inRange = setMatrixSet();

            mTileRow = mTitleRowScale[mZoom];
            mTileCol = mTitleColScale[mZoom];
            // if (inRange) {
            String url = createUrlTemplete();
            getUrlImagenFromGIBS(url);
//            } else {
//                mZoom++;
//            }

            layer.getTileMatrixSet();
        }
    }

    public void onClickZoomIn(View v) {
        if (mZoom < 3) {
            mZoom++;
            boolean inRange = setMatrixSet();

            mTileRow = mTitleRowScale[mZoom];
            mTileCol = mTitleColScale[mZoom];
//            if (inRange) {
            String url = createUrlTemplete();
            getUrlImagenFromGIBS(url);
//            } else {
//                mZoom--;
//            }

            updateZoomLevel();
        }
    }

    private boolean setMatrixSet() {
        boolean has = true;
        String aaa = layer.getTileMatrixSet();
        List<TileMatrixSet> matrixSet = realm.where(TileMatrixSet.class).findAll();
        //.equalTo("identifier", layer.getTileMatrixSet()).findFirst();

        if (matrixSet != null) {
            TileMatrix tileMatrix = null;

//            for (TileMatrix item : matrixSet.getTileMatrixes()) {
//                if (item.getIdentifier() == mZoom) {
//                    tileMatrix = item;
//                    break;
//                }
//            }

            if (tileMatrix != null) {
                mTileRow = tileMatrix.getTileWidth();
                mTileCol = tileMatrix.getTileHeight();
            } else {
                has = false;
            }
        }

        return has;
    }

    public void getUrlImagenFromGIBS(String url) {
        bmpLayout = null;    //Limpiando la imagen de memoria
        imgCoastLines.setImageDrawable(Utils.getDrawableById(MapActivity.this, R.drawable.coast_lines_zoom_0));
        mAttacher.update();
        updateZoomLevel();
        startDialog();

        CustomRequest request = new CustomRequest(this, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ImageLoader.ImageCache imageCache = new BitmapLruCache();
                ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(MapActivity.this), imageCache);

                try {
                    dialog.setMessage("Creando imagen...");
                    String lUrl = response.getString("data");
                    lUrl = AppConstant.URL_SERVER + lUrl.replace('\\','/');

                    closeDialog();
                    loadImagenRequest(lUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                    closeDialog();
                    Toast.makeText(MapActivity.this, "Ocurrio un error al cargar la imagen :(", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeDialog();
                Toast.makeText(MapActivity.this, "Ocurrio un error al cargar la imagen :(", Toast.LENGTH_SHORT).show();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT_REQUEST, 1, 1f));
        AppController.getInstance().addToRequestQueue(request);
    }

    private String createUrlTemplete(){
        String lTemplete = layer.getResourceURLTemplete();
        String lTileMatrixSet = layer.getTileMatrixSet();
        String lTime = layer.getDimencionDefault();
        String lTitle = layer.getTitle();

        String lUrl = AppConstant.GET_IMAGE
                + "?Templete=" + lTemplete
                + "&TileMatrixSet=" + lTileMatrixSet
                + "&TileMatrix=" + + mZoom
                + "&TileMaxRow=" + mTileRow
                + "&TileMaxCol=" + mTileCol
                + "&TitleTemplete=" + lTitle;

        if (!TextUtils.isEmpty(lTime)) {
            lUrl += ("&Time=" + lTime);
        }

        return lUrl;
    }

    private void updateZoomLevel() {
        txvZoomLevel.setText(String.valueOf(mZoom));
    }

    private void getDataRequest() {
        String href = layer.getMetaDataHref();
        if (TextUtils.isEmpty(href))
            return;

        startDialog(R.string.load_meta_data);

        Map<String,String> params = new HashMap<>();
        params.put("xmlFile", layer.getMetaDataHref());

        CustomRequest request = new CustomRequest(this, Request.Method.POST, AppConstant.DECODE_META_DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!response.isNull("data")) {
                    try {
                        JSONObject jsoData = response.getJSONObject("data");
                        JSONObject jsoAttributes = jsoData.getJSONObject("@attributes");
                        JSONArray jsaColorMapEntry = jsoData.getJSONArray("ColorMapEntry");
                        ArrayList<ColorMapEntry> lColorMapEntries = new ArrayList<>();

                        metaDataColors.setUnits(jsoAttributes.getString("units"));
                        String lUnit = MapActivity.this.getResources().getString(R.string.units);
                        txvUnits.setText(lUnit + " " + metaDataColors.getUnits());

                        for (int i = 0; i < jsaColorMapEntry.length(); i++) {
                            JSONObject item = jsaColorMapEntry.getJSONObject(i).getJSONObject("@attributes");
                            ColorMapEntry lColorMapEntry = new ColorMapEntry();
                            boolean isTransparent = item.getBoolean("transparent");

                            lColorMapEntry.setRgb(item.getString("rgb"), isTransparent);
                            lColorMapEntry.setTransparent(isTransparent);
                            lColorMapEntry.setValue(String.valueOf(item.opt("value")));
                            lColorMapEntry.setLabel(item.getString("label"));

                            MapActivity.this.colorMapEntries.add(lColorMapEntry);
                        }

                        metaDataColors.setColorMapEntries(lColorMapEntries);
                        colorMapEntitiesAdapter.notifyDataSetChanged();
                        closeDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        closeDialog();
                        Toast.makeText(MapActivity.this, "Server error" , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    closeDialog();
                    Toast.makeText(MapActivity.this, "Meta data is null" , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeDialog();
                Toast.makeText(MapActivity.this, R.string.err_load_data, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request, AppConstant.DATA);
    }

    private void startDialog() {
        String msj = getResources().getString(R.string.loading);
        showDialog(msj);
    }

    private void startDialog(int prString) {
        String msj = MapActivity.this.getResources().getString(prString);


    }

    private void closeDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void showDialog(String prMsj) {
        dialog.setMessage(prMsj);

        if (!dialog.isShowing()) {
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public void onShowDatePiker(View v) {
        new DialogDatePiker(this){
            @Override
            public void showDate(int year, int month, int day) {
                super.showDate(year, month, day);

                edtDate.setText(year + "-" + month + "-" + day);
                edtDate.clearFocus();
            }
        }.createDialog().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_shared:
                if (bmpLayout != null) {
                    Uri lUri = Utils.storeImage(this, bmpLayout);
                    Utils.sharedImg(this, lUri);
                }
                return  true;
            case R.id.action_handle_menu_bottom:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                handlePanels(lyBottom);

                break;
            case R.id.action_handle_menu_right:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                handlePanels(lyRight);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}

class ColorMapEntitiesAdapter extends ArrayAdapter<ColorMapEntry> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<ColorMapEntry> colorMapEntries;

    public ColorMapEntitiesAdapter(Context context, int resource, ArrayList<ColorMapEntry> colorMapEntries) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.colorMapEntries = colorMapEntries;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return colorMapEntries.size();
    }

    @Override
    public ColorMapEntry getItem(int position) {
        return colorMapEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
            view = inflater.inflate(resource, null);


        ImageView color = (ImageView) view.findViewById(R.id.color);
        TextView label = (TextView) view.findViewById(R.id.label);

        color.setBackgroundColor(colorMapEntries.get(position).getRgb());
        label.setText(colorMapEntries.get(position).getLabel());
        return view;
    }
}
