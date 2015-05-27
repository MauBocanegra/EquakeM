package com.maubocanegra.earthquakemonitor.earthquakemonitor;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;


public class MainContainer extends AppCompatActivity implements
        DownloaderListener,
        Eq_RecyclerViewAdapter.OnClickedItemListener{

    RecyclerView rv;
    RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        //setting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_icon);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //setting and preparing the recyclerview
        rv = (RecyclerView)findViewById(R.id.mainRecyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                callDownload();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        //requesting Earthquakes list
        callDownload();
    }

    private void callDownload(){


        JsonRetreiver jsonR = new JsonRetreiver(this);
        jsonR.execute();


        /*
        ArrayList<EarthquakeObject> objs = new ArrayList<EarthquakeObject>();
        objs.add(new EarthquakeObject("Titulo","earthquake","2.1","-109.254678,17.375926,31.4", System.currentTimeMillis()));
        objs.add(new EarthquakeObject("Titulo","earthquake","5.4","-99.254678,19.375926,31.4", System.currentTimeMillis()));
        objs.add(new EarthquakeObject("Titulo","earthquake","8.7","-89.254678,21.375926,31.4", System.currentTimeMillis()));
        mAdapter = new Eq_RecyclerViewAdapter(objs, this, getApplicationContext());
        rv.setAdapter(mAdapter);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            callDownload();
            swipeRefreshLayout.setRefreshing(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadCompleted(Object[] params) {
        Log.d("stringJson", (String) params[0]);
        mAdapter = new Eq_RecyclerViewAdapter((ArrayList<EarthquakeObject>)params[1], this, getApplicationContext());
        rv.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClicked(Object[] obj) {
        EarthquakeObject eqObj = (EarthquakeObject)obj[1];
        Intent intent = new Intent(this,MapActivity.class);
        Bundle b = new Bundle();
        //(new EarthquakeObject("Titulo","earthquake","6.3","-99.254678,19.375926,31.4", System.currentTimeMillis()
        b.putString("title", eqObj.getTitle());
        b.putDoubleArray("coor", eqObj.getCoor());
        b.putDouble("mag",eqObj.getMag());
        b.putString("date",eqObj.getDate());
        intent.putExtras(b);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,(View)obj[2],"detailView");
            startActivity(intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }
}
