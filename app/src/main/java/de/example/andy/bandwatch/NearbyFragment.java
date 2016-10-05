package de.example.andy.bandwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.example.andy.bandwatch.bandintown.BandsInTownUtils;
import de.example.andy.bandwatch.bandintown.Event;

import static android.content.Context.LOCATION_SERVICE;


public class NearbyFragment extends Fragment {

    private static final String LOG_TAG = NearbyFragment.class.getSimpleName();
    private static final int GPS_REQUEST_CODE = 31;

    private List<String> artists;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView textView;
    private static LocationListener listener;
    private Location location;
    private List<Address> addresses;
    private String city;

    public NearbyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");

        listener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                log("LocationListener.onStatusChanged: " + status);
            }

            @Override
            public void onLocationChanged(Location loc) {
                log("LocationListener.onLocationChanged: " + loc);
                if (location != null) {
                    location = loc;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                log("LocationListener.onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                log("LocationListener.onProviderDisabled");
            }
        };

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        log("onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressTextView = (TextView) rootView.findViewById(R.id.progressTextView);
        textView = (TextView) rootView.findViewById(R.id.nearbyTextView);

        // listNearbyEvents();
        // check permissions for API>22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                log("onCreateView: request permissions for gps");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_REQUEST_CODE);
                //return;
            } else {
                log("onCreateView: permissions for gps already granted");
                listNearbyEvents();
            }
            //Log.d(TAG, "onCreate SDK fits.");
        } else {
            listNearbyEvents();
        }

        return rootView;

    }

    @SuppressWarnings("MissingPermission")
    private void listNearbyEvents() {

        if (!isNetworkAvailable()) {
            textView.append("\nNo internet connection available!\n\nPlease restart app with an internet connection..");
            return;
        }


        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        new Thread(new Runnable() {
            public void run() {
                try {
                    final List<Event> events = new ArrayList<>();

                    Geocoder geocoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());


                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addresses.size() > 0) {
                        city = addresses.get(0).getLocality();
                    }

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                            progressTextView.setText("loading nearby events");
                            if (city != null) progressTextView.append(" for\n" + city);
                            progressTextView.setVisibility(TextView.VISIBLE);
                        }
                    });


                    // TODO: avoid sleep here
                    while (MyArtistsSingleton.getInstance().globalVarArtists == null) {
                        Thread.currentThread().sleep(500);
                    }

                    artists = MyArtistsSingleton.getInstance().globalVarArtists;

                    long l1 = System.nanoTime();
                    log("BandsInTownUtils.getEvents() for " + artists.size() + " artists @ " + location.getLatitude() + "," + location.getLongitude());
                    for (String artist : artists) {
                        //log("BandsInTownUtils.getEvents() for " + artist + " @ " + location.getLatitude() + "," + location.getLongitude());
                        events.addAll(BandsInTownUtils.getEvents(artist, location.getLatitude() + "," + location.getLongitude(), 93));
                    }
                    log(events.size() + " events found in " + (System.nanoTime() - l1) / 1_000_000 + "ms");
                    Collections.sort(events);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            progressTextView.setVisibility(TextView.INVISIBLE);
                            textView.append("Events");
                            if (city != null) textView.append(" for " + city);
                            textView.append(" at radius 150km:\n\n");
                            textView.append(events.size() + " Events\n\n");
                            StringBuffer sb;

                            for (Event event : events) {
                                sb = new StringBuffer();
                                for (String art : event.getArtists()) {
                                    sb.append(art + ", ");
                                }
                                //sb.delete(sb.length()-2, sb.length());
                                textView.append(sb.substring(0, sb.length() - 2) + "\n" + event.getDateString() + " in " + event.getVenue().getCity() + ", " + event.getVenue().getCountry() + " @ " + event.getVenue().getName() + "\n\n");

                            }
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        log("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        log("onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        log("onAttach");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager mgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    private static void log(String s) {
        Log.d(LOG_TAG, s);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 31:
                log("onRequestPermissionsResult = 31");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    log("grantResults contains PERMISSION_GRANTED");
                    listNearbyEvents();
                } else {
                    textView.append("no gps permissions granted, nearby events functionality disabled");
                    textView.setVisibility(View.VISIBLE);
                }

                return;
        }

    }
}