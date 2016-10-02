package de.example.andy.bandwatch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

    private List<String> artists;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView textView;
    private static LocationManager manager;
    private static LocationListener listener;
    private Location location;
    private List<Address> addresses;
    private String city;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        log("onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressTextView = (TextView) rootView.findViewById(R.id.progressTextView);
        textView = (TextView) rootView.findViewById(R.id.nearbyTextView);

        if(!isNetworkAvailable()){
            textView.append("\nNo internet connection available!\n\nPlease restart app with an internet connection..");
            return rootView;
        }


        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

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
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        };

        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        new Thread(new Runnable() {
            public void run() {
                try {
                    final List<Event> events = new ArrayList<Event>();

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


        return rootView;

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

}

//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link NearbyFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link NearbyFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class NearbyFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public NearbyFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment NearbyFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static NearbyFragment newInstance(String param1, String param2) {
//        NearbyFragment fragment = new NearbyFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_nearby, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
