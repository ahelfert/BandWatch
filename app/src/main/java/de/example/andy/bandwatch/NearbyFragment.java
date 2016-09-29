package de.example.andy.bandwatch;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import de.example.andy.bandwatch.bandintown.BandsInTownUtils;
import de.example.andy.bandwatch.bandintown.Event;

import static android.content.Context.LOCATION_SERVICE;


public class NearbyFragment extends Fragment {

    //private String[] bands = {"Biffy Clyro", "de#d2_.d3", "Architects", "Silverstein"};
    private List<String> artists;
    private ProgressBar progressBar;
    private TextView textView;
    private static LocationManager manager;
    private static LocationListener listener;
    private Location location;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        textView = (TextView) rootView.findViewById(R.id.nearbyTextView);


        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                System.out.println("onStatusChanged()");
            }

            @Override
            public void onLocationChanged(Location loc) {
                System.out.println("onLocationChanged()");
                if (location != null) {
                    location = loc;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println("onProviderEnabled()");
            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("onProviderDisabled()");
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        };

        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        textView.append("Events at radius 150km:\n\n");


        new Thread(new Runnable() {
            public void run() {
                try {
                    final List<Event> events = new ArrayList<Event>();

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                        }
                    });

                    // TODO: avoid sleep here
                    Thread.currentThread().sleep(500);

                    artists = MyArtistsSingleton.getInstance().globalVarArtists;

                    for (String artist : artists) {
                        events.addAll(BandsInTownUtils.getEvents(artist, location.getLatitude() + "," + location.getLongitude(), 93));
                        //System.out.println(events);
                    }
                    Collections.sort(events);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            textView.append(events.size() + " Events\n\n");
                            StringBuffer sb;

                            for (Event event : events) {
                                sb = new StringBuffer();
                                for (String art : event.getArtists()) {
                                    sb.append(art + ", ");
                                }
                                //sb.delete(sb.length()-2, sb.length());
                                textView.append(sb.substring(0, sb.length() - 2) + "\n" + event.getDateString() + " in " + event.getVenue().getCity() + ", " + event.getVenue().getCountry() + " @ " + event.getVenue().getName() + "\n\n");
                                //System.out.println(event);
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
