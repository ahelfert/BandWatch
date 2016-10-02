package de.example.andy.bandwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

// http://stackoverflow.com/questions/15321666/replace-listfragment-with-fragment-inside-viewpager-with-tabs

public class BandsWrapperFragment extends Fragment {

    private static final String LOG_TAG = BandsWrapperFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");

        final BandsFragment bandsFragment = new BandsFragment();

        bandsFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                BandDetailsFragment details = BandDetailsFragment.newInstance(((TextView)v).getText().toString());
                getFragmentManager()
                        .beginTransaction()
                        //.replace(R.id.container, details) // not good, because replaced fragment will call its onDestroyView, so its better just to hide it
                        .add(R.id.container, details)
                        .addToBackStack(null) // adds transaction to backstack, so it could be reversed when back bttn pressed
                        .hide(bandsFragment)
                        .commit();
            }
        });

        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, bandsFragment)
                .commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        log("onCreateView");
        return inflater.inflate(R.layout.fragment_bands_wrapper, container, false);
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


    private static void log(String s) {
        Log.d(LOG_TAG, s);
    }


//    public static class MyListFragment extends ListFragment {
//
//        private OnItemClickListener listener;
//
//        public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
//            this.listener = l;
//        }
//
//        public MyListFragment() {
//        }
//
//        @Override
//        public void onListItemClick(ListView l, View v, int position, long id) {
//            if(listener != null) {
//                listener.onItemClick(l, v, position, id);
//            }
//        }
//    }
}

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link BandsWrapperFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link BandsWrapperFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class BandsWrapperFragment extends Fragment {
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
//    public BandsWrapperFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BandsWrapperFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BandsWrapperFragment newInstance(String param1, String param2) {
//        BandsWrapperFragment fragment = new BandsWrapperFragment();
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
//        return inflater.inflate(R.layout.fragment_bands_wrapper, container, false);
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
