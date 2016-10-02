package de.example.andy.bandwatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Collections;
import java.util.List;

import de.example.andy.bandwatch.bandintown.BandsInTownUtils;
import de.example.andy.bandwatch.musicbrainz.Album;
import de.example.andy.bandwatch.musicbrainz.MusicBrainzUtils;

public class BandDetailsFragment extends Fragment {

    private static final String LOG_TAG = BandDetailsFragment.class.getSimpleName();

    private int position;
    private String artist;
    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView textView;
    private ImageView imageView;

    private List<Album> albums;
    private Bitmap artistBitmap;

    private Thread workingThread;


    public static BandDetailsFragment newInstance(String artist) {
        BandDetailsFragment fragment = new BandDetailsFragment();
        fragment.artist = artist;
        return fragment;
    }


    public BandDetailsFragment() {
        //Required empty constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_band_details, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressTextView = (TextView) rootView.findViewById(R.id.progressTextView);
        textView = (TextView) rootView.findViewById(R.id.bandDetailsTextView);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);


        workingThread = new Thread(new Runnable() {
            public void run() {

                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                            progressTextView.setText("loading albums for\n" + artist);
                            progressTextView.setVisibility(TextView.VISIBLE);
                        }
                    });

                    long l1 = System.nanoTime();
                    log(" MusicBrainzUtils.getAlbums(artist) for " + artist);
                    albums = MusicBrainzUtils.getAlbums(artist);
                    //if((Thread.currentThread().isInterrupted()) ) return;

                    log(albums.size() + " albums found in " + (System.nanoTime() - l1) / 1_000_000 + "ms");

                    Collections.sort(albums);

                    artistBitmap = BandsInTownUtils.getArtistImage(artist);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            progressTextView.setVisibility(TextView.INVISIBLE);

                            if (albums.size() == 0) textView.append("(no albums found)");
                            else textView.append("Yet released albums for " + artist + ":\n\n");

                            imageView.setImageBitmap(artistBitmap);


                            for (Album album : albums) {
                                textView.append(album.getDateStr() + " title: " + album.getTitle() + " type: " + album.getType() + "\n\n");
                            }
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedIOException e) {
                    log(e.getClass().getSimpleName() + ": " + e.getMessage() + " for MusicBrainzUtils.getAlbums(artist)");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        workingThread.start();


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
        workingThread.interrupt();
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

    private static void log(String s) {
        Log.d(LOG_TAG, s);
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
