package de.example.andy.bandwatch;

import java.util.List;

/**
 * Created by Master on 29.09.2016.
 */

public class MyArtistsSingleton {

    private static MyArtistsSingleton instance = null;

    public List<String> globalVarArtists;

    private MyArtistsSingleton() {
    }

    public static synchronized MyArtistsSingleton getInstance() {
        if (instance == null) {
            instance = new MyArtistsSingleton();
        }
        return instance;
    }
}
