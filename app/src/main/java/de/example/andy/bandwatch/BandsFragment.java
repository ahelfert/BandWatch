package de.example.andy.bandwatch;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BandsFragment extends ListFragment {

    private List<String> artists;
    private AdapterView.OnItemClickListener listener;
    private ProgressBar progressBar;
    private TextView textView;

    public BandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        this.listener = l;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.onItemClick(l, v, position, id);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bands, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        textView = (TextView) rootView.findViewById(R.id.textView);

        new Thread(new Runnable() {
            public void run() {


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                artists = getArtists();
                System.out.println("getArtists() called");

                // make Umlaute sorted correctly
                Collator collator = Collator.getInstance();
                collator.setStrength(Collator.SECONDARY);

                Collections.sort(artists, collator);

                MyArtistsSingleton.getInstance().globalVarArtists = artists;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (artists.size() == 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.append("(no music media found on your device!!)");
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, artists);
                        setListAdapter(arrayAdapter);

                    }

                });


            }
        }).start();


        return rootView;
    }

    private List<String> getArtists() {

        //long l1 = System.nanoTime();
        List<String> songsList = new ArrayList<>();

        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.ARTIST}, null, null,
                "LOWER(" + MediaStore.Audio.Media.ARTIST + ") ASC");

        int index;
        String artist;

        if (mCursor.moveToFirst()) {
            do {
                if ((index = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) != -1 && !(artist = mCursor.getString(index)).contains("<unknown>")) {
                    if (!songsList.contains(artist)) {
                        songsList.add(artist);
                        //System.out.println(artist);
                    }
                }
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        //System.out.println("runtime: " + (System.nanoTime() - l1) + "ns");
        return songsList;
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
// * {@link BandsFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link BandsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class BandsFragment extends Fragment {
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
//    public BandsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BandsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BandsFragment newInstance(String param1, String param2) {
//        BandsFragment fragment = new BandsFragment();
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
//        return inflater.inflate(R.layout.fragment_bands, container, false);
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
