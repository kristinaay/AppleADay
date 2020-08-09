package com.kristinaandalex.appleaday;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");


                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    JSONObject locObject = jsonObject.getJSONObject("geometry").getJSONObject("location");

                    String lat = locObject.getString("lat");
                    String lng = locObject.getString("lng");

                    JSONObject nameObj = resultsArray.getJSONObject(i);
                    String name = nameObj.getString("name");

                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(name);
                    markerOptions.position(latLng);

                    googleMap.addMarker(markerOptions);
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
