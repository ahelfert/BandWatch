package de.example.andy.bandwatch.bandintown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.example.andy.bandwatch.NearbyFragment;

public class BandsInTownUtils {

    private static final String LOG_TAG = NearbyFragment.class.getSimpleName();

    private static final String URL = "http://api.bandsintown.com/artists/{0}/events{1}.json?api_version=2.0&app_id=BandWatch{2}{3}";


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DATETIME = "datetime";
    private static final String DESCRIPTION = "description";

    private static final String ARTISTS = "artists";
    private static final String ARTISTS_NAME = "name";

    private static final String VENUE = "venue";
    private static final String VENUE_NAME = "name";
    private static final String VENUE_CITY = "city";
    private static final String VENUE_REGION = "region";
    private static final String VENUE_COUNTRY = "country";
    private static final String VENUE_LAT = "latitude";
    private static final String VENUE_LON = "longitude";

    private static final String URL_PARAM_SEARCH = "/search";
    private static final String URL_PARAM_LOCATION = "&location=";
    private static final String URL_PARAM_RADIUS = "&radius=";


    public static List<Event> getEvents(String artist, String location, Integer radius) throws JSONException, IOException /* MalformedURLException */ {

        List<Event> events = new ArrayList<>();

        int id = 0;
        String title = null;
        Date date = null;
        String description = null;
        Venue venue = null;
        String[] artists = null;

        String urlParamSearch;
        String urlParamLocation;
        String urlParamRadius;

        artist = URLEncoder.encode(artist, "UTF-8").replace("+", "%20"); // encode the string (white spaces etc)

        if (location != null && location != "") {
            urlParamLocation = URL_PARAM_LOCATION + location;
            if (radius == null || radius <= 0) radius = 0;
            if (radius > 150) radius = 150;
            urlParamSearch = URL_PARAM_SEARCH;
            urlParamRadius = URL_PARAM_RADIUS + radius;
        } else {
            urlParamSearch = "";
            urlParamLocation = "";
            urlParamRadius = "";
        }


        String jsonString = BandsInTownUtils.getFromServer(MessageFormat.format(URL, artist, urlParamSearch, urlParamLocation, urlParamRadius));

        if (jsonString == "") return events;

        JSONArray jsonArray = new JSONArray(jsonString);


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has(ID)) {
                id = jsonObject.getInt(ID);
            }
            if (jsonObject.has(TITLE)) {
                title = jsonObject.getString(TITLE);
            }
            if (jsonObject.has(DATETIME)) {
                try {
                    date = parseJsonDate(jsonObject.getString((DATETIME)));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (jsonObject.has(DESCRIPTION)) {
                description = jsonObject.getString(DESCRIPTION);
            }

            if (jsonObject.has(ARTISTS)) {
                JSONArray jsonArrayArtists = jsonObject.getJSONArray(ARTISTS);

                int artistCount = jsonArrayArtists.length();
                artists = new String[artistCount];

                for (int j = 0; j < artistCount; j++) {
                    artists[j] = jsonArrayArtists.getJSONObject(j).getString(ARTISTS_NAME);
                }

            }
            if (jsonObject.has(VENUE)) {
                venue = new Venue();
                JSONObject venueJSONObject = jsonObject.getJSONObject(VENUE);
                venue.setName(venueJSONObject.getString(VENUE_NAME));
                venue.setCity(venueJSONObject.getString(VENUE_CITY));
                venue.setRegion(venueJSONObject.getString(VENUE_REGION));
                venue.setCountry(venueJSONObject.getString(VENUE_COUNTRY));
                venue.setLatitude(venueJSONObject.getDouble(VENUE_LAT));
                venue.setLongitude(venueJSONObject.getDouble(VENUE_LON));
            }
            events.add(new Event(id, title, date, description, artists, venue));
        }

        return events;
    }

    public static String getFromServer(String url) throws IOException /* MalformedURLException */ {
        StringBuilder sb = new StringBuilder();
        logv("BandsInTownUtils.getFromServer() for " + url);
        URL _url = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) _url.openConnection();
        final int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            try {
                bufferedReader.close();
            } catch (IOException e) {
                // ein Fehler beim Schließen wird bewusst ignoriert
            }
        }
        httpURLConnection.disconnect();


        return sb.toString();
    }

    public static Bitmap getArtistImage(String artist) throws IOException /* MalformedURLException */ {

        artist = URLEncoder.encode(artist, "UTF-8").replace("+", "%20"); // encode the string (white spaces etc)
        URL req = new URL("http://www.bandsintown.com/" + artist + "/photo/small.jpg");
        HttpURLConnection c = (HttpURLConnection) req.openConnection();
        Bitmap bmp = BitmapFactory.decodeStream(c
                .getInputStream());
        c.disconnect();
        return bmp;
    }

    public static Date parseJsonDate(String input) throws java.text.ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return df.parse(input);

    }

    private static void log(String s) {
        Log.d(LOG_TAG, s);
    }
    private static void logv(String s) {
        Log.v(LOG_TAG, s);
    }
}
