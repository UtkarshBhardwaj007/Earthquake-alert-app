package com.example.earthquakealert;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakelist) {
        super(context, 0, earthquakelist);
    }

    private static final String LOCATION_SEPARATOR = " of ";

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        String primaryLocation;
        String locationOffset;
        String originalLocation = currentEarthquake.getmLocation();

        //magnitudeView.setText(currentEarthquake.getmMagnitude());

        TextView magnitudeView = listItemView.findViewById(R.id.magnitude_textView);
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(currentEarthquake.getmMagnitude());
        magnitudeView.setText(output);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        // Create a new Date object from the time in milliseconds of the earthquake
        TextView dateView = listItemView.findViewById(R.id.date_textView);
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = dateFormatter.format(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = listItemView.findViewById(R.id.time);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormatter.format(dateObject);
        timeView.setText(formattedTime);

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = "Near the";
            primaryLocation = originalLocation;
        }

        TextView primaryLocationView = listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        TextView locationOffsetView = listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        return listItemView;
    }
}
