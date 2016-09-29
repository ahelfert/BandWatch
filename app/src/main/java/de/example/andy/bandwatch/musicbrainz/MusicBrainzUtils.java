package de.example.andy.bandwatch.musicbrainz;

/**
 * Created by ACid on 29.09.2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicBrainzUtils {

    private static final String URL_ARTIST = "http://musicbrainz.org/ws/2/artist/?query=artist:{0}&fmt=json";
    private static final String URL_RELEASEGROUP = "http://musicbrainz.org/ws/2/release-group/?query=arid:{0}&fmt=json";
    private static final String URL_RELEASE = "http://musicbrainz.org/ws/2/release/?query=rgid:{0}&fmt=json";


    private static final String ARID = "artist id";
    private static final String RGID = "release-group id";
    private static final String REID = "release id";

    private static final String TITLE = "title";
    private static final String DATE = "date";

    private static final String TYPE = "primary-type";
    private static final String LABEL = "name";


    public static List<Album> getAlbums(String artist) throws JSONException, IOException /* MalformedURLException */ {

        List<Album> albums = new ArrayList<>();

        String title = null;
        String type = null;
        Date date = null;
        String label = "";
        String arid = null; // MB Artist ID
        String rgid = null; // MB Releasegroup ID
        String reid = null; // MB Release ID

        artist = URLEncoder.encode(artist, "UTF-8").replace("+", "%20"); // encode
        // the
        // string
        // (white
        // spaces
        // etc)

        // fetch artist id (arid)
        System.out.println("before fetch artist id");
        String jsonString = getFromServer(MessageFormat.format(URL_ARTIST, artist));
        if (jsonString == "")
            return albums;

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = new JSONArray();

        if (jsonObject.has("artists")) {
            jsonArray = jsonObject.getJSONArray("artists");
            jsonObject = jsonArray.getJSONObject(0);
        }

        if (jsonObject.has("id")) {
            arid = jsonObject.getString("id");
        }

        // fetch release groups
        System.out.println("before fetch release groups");
        jsonString = getFromServer(MessageFormat.format(URL_RELEASEGROUP, arid));
        if (jsonString == "")
            return albums;

        jsonObject = new JSONObject(jsonString);

        if (jsonObject.has("release-groups")) {
            jsonArray = jsonObject.getJSONArray("release-groups");

            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("id")) {
                    rgid = jsonObject.getString("id");
                }
                if (jsonObject.has("title")) {
                    title = jsonObject.getString("title");
                }
                if (jsonObject.has("primary-type")) {
                    type = jsonObject.getString("primary-type");
                }

                // fetch release
                jsonString = getFromServer(MessageFormat.format(URL_RELEASE, rgid));
                if (jsonString == "")
                    return albums;

                jsonObject = new JSONObject(jsonString);

                if (jsonObject.has("releases")) {
                    jsonArray = jsonObject.getJSONArray("releases");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("date")) {
//							try {
//								date = parseJsonDate(jsonObject.getString("date"));
//							} catch (ParseException e) {
//								e.printStackTrace();
//							}
                        }
                        albums.add(new Album(arid, rgid, reid, title, type, date, label));
                    }

                }
            }

        }

        return albums;
    }

    public static String getFromServer(String url) throws IOException /* MalformedURLException */ {
        StringBuilder sb = new StringBuilder();
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

        //System.out.println("###LOG###: " + url);
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
}

