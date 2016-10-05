package de.example.andy.bandwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.util.Log;
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

import static android.content.ContentValues.TAG;


public class BandsFragment extends ListFragment {

    private static final String LOG_TAG = BandsFragment.class.getSimpleName();
    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 51;

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
        log("onCreate");

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

        log("onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_bands, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        textView = (TextView) rootView.findViewById(R.id.textView);

        // listArtists()
        // check permissions for API>22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                log("onCreateView: request permissions for external storage");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                //return;
            } else {
                log("onCreateView: permissions for external storage already granted");
                listArtists();
            }
        } else {
            listArtists();
        }


        return rootView;
    }

    private void listArtists() {

        new Thread(new Runnable() {
            public void run() {


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                long l1 = System.nanoTime();
                artists = getArtists();

                log("getArtists()");
                log(artists.size() + " artists found in " + (System.nanoTime() - l1) / 1_000_000 + "ms");

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
    }

    private List<String> getArtists() {

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
                    }
                }
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songsList;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 51:
                log("onRequestPermissionsResult = 51");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "grantResults contains PERMISSION_GRANTED");
                    listArtists();
                } else {
                    textView.append("no external storage permissions granted, music library scan functionality disabled");
                    textView.setVisibility(View.VISIBLE);
                }
                return;
        }

    }

}
