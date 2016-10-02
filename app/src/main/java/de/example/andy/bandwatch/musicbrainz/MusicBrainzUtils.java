package de.example.andy.bandwatch.musicbrainz;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.example.andy.bandwatch.NearbyFragment;

public class MusicBrainzUtils {

    private static final String LOG_TAG = NearbyFragment.class.getSimpleName();

    private static final String CLIENT_ID = "bandwatch.app-0.1.0";

    private static final String URL_ARTIST = "http://musicbrainz.org/ws/2/artist/?query=artist:{0}&fmt=json&client=" + CLIENT_ID;
    private static final String URL_RELEASEGROUP = "http://musicbrainz.org/ws/2/release-group/?query=arid:{0}&fmt=json&client=" + CLIENT_ID;
    private static final String URL_RELEASE = "http://musicbrainz.org/ws/2/release/?query=rgid:{0}&fmt=json&client=" + CLIENT_ID;

    private static final String ID = "id";
    private static final String ARTISTS = "artists";
    private static final String TITLE = "title";
    private static final String TYPE = "primary-type";
    private static final String DATE = "date";
    private static final String RG = "release-groups";
    private static final String RELEASES = "releases";

    public static List<Album> getAlbums(String artist) throws JSONException, IOException /* MalformedURLException */ {

        List<Album> albums = new ArrayList<>();
        List<Album> releases = new ArrayList<>();

        String title = null;
        String type = null;
        Date date = null;
        String dateStr = null;
        String arid = null; // MB Artist ID
        String rgid = null; // MB Releasegroup ID
        String reid = null; // MB Release ID

        boolean alreadyAdded = false;

        artist = URLEncoder.encode(artist, "UTF-8").replace("+", "%20"); // encode (white spaces etc)

        // fetch artist id (arid)
        logv("fetch artist id for " + artist);
        String jsonString = getFromServer(MessageFormat.format(URL_ARTIST, artist));

        if (jsonString == "") return albums;

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayReleases = new JSONArray();

        if (jsonObject.has(ARTISTS)) {
            jsonArray = jsonObject.getJSONArray(ARTISTS);
            jsonObject = jsonArray.getJSONObject(0);
        }

        if (jsonObject.has(ID)) {
            arid = jsonObject.getString(ID);
        }

        // fetch all release groups

        logv("// fetch all release groups for: " + artist);
        jsonString = getFromServer(MessageFormat.format(URL_RELEASEGROUP, arid));
        if (jsonString == "") return albums;

        jsonObject = new JSONObject(jsonString);

        if (jsonObject.has(RG)) {
            jsonArray = jsonObject.getJSONArray(RG);

            // iterate over release groups
            for (int i = 0; i < jsonArray.length(); i++) {

                alreadyAdded = false;

                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(ID)) {
                    rgid = jsonObject.getString(ID);
                }
                if (jsonObject.has(TITLE)) {
                    title = jsonObject.getString(TITLE);
                }
                if (jsonObject.has(TYPE)) {
                    type = jsonObject.getString(TYPE);
                }

                // fetch all releases of one release group
                logv("// fetch all releases of one release group for: " + title);
                jsonString = getFromServer(MessageFormat.format(URL_RELEASE, rgid));

                if (jsonString == "") return albums;

                jsonObject = new JSONObject(jsonString);

                if (jsonObject.has(RELEASES)) {
                    jsonArrayReleases = jsonObject.getJSONArray(RELEASES);

                    // iterate over releases and select the earliest release
                    for (int j = 0; j < jsonArrayReleases.length(); j++) {
                        jsonObject = jsonArrayReleases.getJSONObject(j);
                        if (jsonObject.has(ID)) {
                            reid = jsonObject.getString(ID);
                        }

                        if (jsonObject.has(DATE)) {
                            try {
                                dateStr = jsonObject.getString(DATE);
                                date = parseJsonDate(jsonObject.getString(DATE));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        releases.add(new Album(title, type, date, dateStr, arid, rgid, reid));
                    }
                    Collections.sort(releases); // sort by date

                    // workaround: if date is just a year or month, select the release with more detailled date
                    for (Album release : releases) {
                        if (release.getDateStr().length() == 10) {
                            albums.add(release);
                            alreadyAdded = true;
                            break;
                        }
                    }

                    if (!alreadyAdded) albums.add(releases.get(0));

                    // clear for the next release group iteration
                    releases.clear();

                }

            }

        }
        return albums;
    }

    public static String getFromServer(String url) throws IOException /* MalformedURLException */ {
        StringBuilder sb = new StringBuilder();
        URL _url = new URL(url);

        //if(Thread.currentThread().isInterrupted()) return null;
        logv(url);

        HttpURLConnection httpURLConnection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;


        try {
            long l1 = System.nanoTime();

            httpURLConnection = (HttpURLConnection) _url.openConnection();
            final int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
            }
            httpURLConnection.disconnect();

            logv("musicbrainz response time: " + (System.nanoTime() - l1) / 1_000_000 + "ms");
        } catch (InterruptedIOException e) {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (inputStreamReader != null)
                inputStreamReader.close();
            if (bufferedReader != null)
                bufferedReader.close();
            throw e; // rethrow to interrupt thread
        }


        return sb.toString();
    }

    public static Date parseJsonDate(String input) throws java.text.ParseException {

        SimpleDateFormat df = null;
        if (input.length() == 10) df = new SimpleDateFormat("yyyy-MM-dd");
        if (input.length() == 7) df = new SimpleDateFormat("yyyy-MM");
        if (input.length() == 4) df = new SimpleDateFormat("yyyy");
        return df.parse(input);

    }

    private static void log(String s) {
        Log.d(LOG_TAG, s);
    }

    private static void logv(String s) {
        Log.v(LOG_TAG, s);
    }
}
