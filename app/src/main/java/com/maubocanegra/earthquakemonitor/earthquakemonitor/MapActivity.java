package com.maubocanegra.earthquakemonitor.earthquakemonitor;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_compass);
        getSupportActionBar().setTitle(getResources().getString(R.string.details_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        int[] colors = {
                getResources().getColor(R.color.material_green_600),getResources().getColor(R.color.material_green_800),getResources().getColor(R.color.material_green_900),
                getResources().getColor(R.color.material_amber_400),getResources().getColor(R.color.material_amber_600),getResources().getColor(R.color.material_amber_800),
                getResources().getColor(R.color.material_deep_orange_400),getResources().getColor(R.color.material_red_500),getResources().getColor(R.color.material_red_900)};
        findViewById(R.id.earthquake_list_layout).findViewById(R.id.warningColor).setBackgroundColor(colors[(int)extras.getDouble("mag")]);
        ((TextView) findViewById(R.id.earthquake_list_layout).findViewById(R.id.title)).setText(extras.getString("title"));
        ((TextView) findViewById(R.id.earthquake_list_layout).findViewById(R.id.place)).setText(extras.getString("date"));
        ((TextView) findViewById(R.id.earthquake_list_layout).findViewById(R.id.mag)).setText(getResources().getString(R.string.magni_text)+extras.getDouble("mag"));
        /*
        int[] colors = {
                context.getResources().getColor(R.color.material_green_600),context.getResources().getColor(R.color.material_green_800),context.getResources().getColor(R.color.material_green_900),
                context.getResources().getColor(R.color.material_amber_400),context.getResources().getColor(R.color.material_amber_600),context.getResources().getColor(R.color.material_amber_800),
                context.getResources().getColor(R.color.material_deep_orange_400),context.getResources().getColor(R.color.material_red_500),context.getResources().getColor(R.color.material_red_900)};
        holder.mView.findViewById(R.id.warningColor).setBackgroundColor(colors[(int)eqObj.getMag()]);
         */

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Bundle b = getIntent().getExtras();
        double[] coor = b.getDoubleArray("coor");
        Log.d("latlon", "" + coor[1] + "," + coor[0]);
        double mag = b.getDouble("mag");

        mMap.addMarker(new MarkerOptions().position(new LatLng(coor[1], coor[0])).title(b.getString("title"))
                .icon(BitmapDescriptorFactory.defaultMarker(
                        mag > 0 && mag <= 3 ? BitmapDescriptorFactory.HUE_GREEN : (mag > 3 && mag <= 6 ? BitmapDescriptorFactory.HUE_YELLOW : BitmapDescriptorFactory.HUE_RED)
        )));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coor[1],coor[0]),5));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();

            Log.d("","home");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
