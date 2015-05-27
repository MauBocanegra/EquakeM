package com.maubocanegra.earthquakemonitor.earthquakemonitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mauro on 24/05/2015.
 */
public class EarthquakeObject {
    //Log.d("","Title="+props.getString("place")+" type="+props.getString("type")+" mag0"+props.getString("mag")+" coor="+coor);
    private String title;
    private String type;
    private double mag;
    private double[] coor;
    private String date;

    public EarthquakeObject(String t, String type_, String m, String c, long date_){
        title = t;
        type = type_;
        mag = Double.parseDouble(m);

        coor = new double[3];
        String[] coo = c.split(",");
        for(int i=0; i <coo.length; i++)
            coor[i] = Double.parseDouble(coo[i]);

        Date date_d = new Date(date_);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        this.date = formatter.format(date_d);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public double getMag() {
        return mag;
    }

    public double[] getCoor() {
        return coor;
    }

    public String getDate(){return date;}
}