package de.example.andy.bandwatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import de.example.andy.bandwatch.bandintown.BandsInTownUtils;
import de.example.andy.bandwatch.bandintown.Event;

public class BandDetailsFragment_Events extends Fragment {

    private int position;
    private String artist;
    private TextView textView;
    private ImageView imageView;
    private ProgressBar progressBar;


    public static BandDetailsFragment_Events newInstance(String artist) {
        BandDetailsFragment_Events fragment = new BandDetailsFragment_Events();
        fragment.artist = artist;
        return fragment;
    }

    public BandDetailsFragment_Events() {
        //Required empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_band_details, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        textView = (TextView) rootView.findViewById(R.id.bandDetailsTextView);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        new Thread(new Runnable() {
            public void run() {

                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                        }
                    });

                    final List<Event> events = BandsInTownUtils.getEvents(artist, null, null);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);

                            if(events.size()==0) textView.append("(no upcoming events found)");
                            else textView.append("All upcoming events for " + artist + ":\n\n");

                            try {
                                imageView.setImageBitmap(BandsInTownUtils.getArtistImage(artist));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            for (Event event : events) {
                                textView.append(event.getDateString() +  " in " + event.getVenue().getCity() + ", " + event.getVenue().getCountry() + " @ " + event.getVenue().getName() + "\n\n");
                                //System.out.println(event);
                            }
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();



        return rootView;
    }

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
//    public BandDetailsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BandDetailsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BandDetailsFragment newInstance(String param1, String param2) {
//        BandDetailsFragment fragment = new BandDetailsFragment();
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
//        return inflater.inflate(R.layout.fragment_band_details, container, false);
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
}
