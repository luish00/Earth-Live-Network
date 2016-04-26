package com.hector.luis.spaceapp2016.earthlivelh.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hector.luis.spaceapp2016.earthlivelh.Generic.AppConstant;
import com.hector.luis.spaceapp2016.earthlivelh.Models.Layer;
import com.hector.luis.spaceapp2016.earthlivelh.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private ListView list;

    private ArrayList<Layer> layersList = new ArrayList<>();
    private LayerAdaper adaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

        RealmResults<Layer> results = realm.where(Layer.class).findAll();

        layersList.addAll(results);

        list = (ListView) findViewById(R.id.list);
        adaper = new LayerAdaper(this, android.R.layout.simple_list_item_1, layersList);

        list.setAdapter(adaper);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(MainActivity.this, MapTestZoom0Activity.class);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra(AppConstant.KEY_INTENT_MAP, layersList.get(position).getTitle());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class LayerAdaper extends ArrayAdapter<Layer> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<Layer> layers;

    public LayerAdaper(Context context, int resource, ArrayList<Layer> layers) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
        this.layers = layers;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return layers.size();
    }

    @Override
    public Layer getItem(int position) {
        return layers.get(position);
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


        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        String title = layers.get(position).getTitle().replace('_', ' ');
        title = title.replace("Day", "(Day)");
        title = title.replace(" Night", " (Night)");

        text1.setText(title);
        return view;
    }
}
