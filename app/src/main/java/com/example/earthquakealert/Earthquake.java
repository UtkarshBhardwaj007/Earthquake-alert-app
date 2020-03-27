package com.example.earthquakealert;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    public Earthquake(double Magnitude, String Location, long timeInMilliseconds, String url){
        mMagnitude = Magnitude;
        mLocation = Location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public double getmMagnitude(){
        return mMagnitude;
    }

    public String getmLocation(){
        return mLocation;
    }

    public long getmTimeInMilliseconds(){
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return mUrl;
    }
}
