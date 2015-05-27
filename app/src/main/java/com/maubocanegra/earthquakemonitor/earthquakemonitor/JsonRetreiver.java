package com.maubocanegra.earthquakemonitor.earthquakemonitor;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JsonRetreiver extends AsyncTask<Void,Void,String>{

    DownloaderListener downloaderListener;
    Context context;

    public JsonRetreiver(DownloaderListener dl){
        downloaderListener=dl;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            InputStream is=null;
            OutputStream os=null;
            HttpURLConnection conn=null;
            try {
                //constants
                URL url = new URL("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson");
                String message = new JSONObject().toString();
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                //do somehting with response
                is = conn.getInputStream();

                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();

                String line;
                try {

                    br = new BufferedReader(new InputStreamReader(is));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return sb.toString();
            } catch(Exception e){e.printStackTrace();} finally {
                //clean up
                assert os != null; os.close();
                assert is != null; is.close();
                conn.disconnect();
            }
        }catch(Exception e){e.printStackTrace();}

        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<EarthquakeObject> qLists=new ArrayList<EarthquakeObject>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonFeatures = new JSONArray(jsonObject.getString("features"));
            for(int i=0; i<jsonFeatures.length();i++){
                JSONObject pre = jsonFeatures.getJSONObject(i);
                JSONObject props = new JSONObject(pre.getString("properties"));
                JSONObject geom = new JSONObject(pre.getString("geometry"));
                String coor = geom.getString("coordinates");
                Log.d("","Title="+props.getString("place")+" type="+props.getString("type")+" mag0"+props.getString("mag")+" coor="+coor.substring(1,coor.length()-1));
                qLists.add(new EarthquakeObject(props.getString("place"),props.getString("type"),props.getString("mag"),coor.substring(1, coor.length() - 1), props.getLong("time")));
            }
        }catch(Exception e){e.printStackTrace();}
        downloaderListener.onDownloadCompleted(new Object[]{result,qLists});
    }
}
