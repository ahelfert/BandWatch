package de.example.andy.bandwatch;

//
//import android.content.Context;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
///**
// * Created by Master on 04.10.2016.
// */
//
//public class BandWatchDBHelper extends SQLiteOpenHelper {
//
//    private static final String LOG_TAG = BandWatchDBHelper.class.getSimpleName();
//
//    // Datenbank, Version und Tabellen
//    private static final String DB_NAME = "bandwatch.db";
//    private static final int DB_VERSION = 1;
//
//    // artist
//    private static final String SQL_CREATE_TABLE_ARTIST = "CREATE TABLE artist (" +
//            "_id    TEXT PRIMARY KEY," +    // arid from MusicBrainz
//            "name   TEXT NOT NULL);";
//
//    // album
//    private static final String SQL_CREATE_TABLE_ALBUM = "CREATE TABLE album (" +
//            "_id	TEXT PRIMARY KEY, " +    // reid from MusicBrainz
//            "title	TEXT NOT NULL, " +
//            "type	TEXT, " +
//            "date	TEXT NOT NULL, " +
//            "arid	TEXT, " +
//            "rgid	TEXT, " +
//            "FOREIGN KEY(`arid`) REFERENCES artist(_id));";
//
//    // event
//    private static final String SQL_CREATE_TABLE_EVENT = "CREATE TABLE event (" +
//            "_id	TEXT PRIMARY KEY, " +    // id from BandsInTown
//            "title	TEXT, " +
//            "date	TEXT NOT NULL, " +
//            "description TEXT, " +
//            "venue_id	INTEGER, " +
//            "FOREIGN KEY(venue_id) REFERENCES venue(_id));";
//
//    // event_artist
//    private static final String SQL_CREATE_TABLE_EVENT_ARTIST = "CREATE TABLE event_artist (" +
//            "event_id	Text, " +    // id from BandsInTown
//            "arid   TEXT, " +
//            "PRIMARY KEY(event_id, arid), " +
//            "FOREIGN KEY(event_id) REFERENCES event(_id), " +
//            "FOREIGN KEY(arid) REFERENCES artist(_id));";
//
//    // venue
//    private static final String SQL_CREATE_TABLE_VENUE = "CREATE TABLE venue (" +
//            "_id	INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "name	TEXT NOT NULL," +
//            "lat	REAL," +
//            "lng	REAL," +
//            "city	TEXT NOT NULL," +
//            "country	TEXT," +
//            "region	TEXT);";
//
//
//
//    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST;
//
//    public BandWatchDBHelper(Context con) {
//        super(con, DB_NAME, null, DB_VERSION);  // DB wird angelegt
//        Log.d(LOG_TAG, "Helper hat die DB " + getDatabaseName() + " erzeugt");
//    }
//
//    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert (zB Neuinstallation der App)
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        try {
//            Log.d(LOG_TAG, "Die Tabelle wird erstellt.");
//            db.execSQL(SQL_CREATE);
//        } catch (SQLException ex) {
//            Log.e(LOG_TAG, "Fehler beim Anlegen " + ex.getMessage());
//        }
//
//
//    }
//
//    // Die onUpgrade-Methode wird aufgerufen, sobald die neue Versionsnummer höher
//    // als die alte Versionsnummer ist und somit ein Upgrade notwendig wird
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.d(LOG_TAG, "Die Tabelle mit Versionsnummer " + oldVersion + " wird entfernt.");
//        db.execSQL(SQL_DROP);
//        onCreate(db);
//    }
//
//    private static void log(String s) {
//        Log.d(LOG_TAG, s);
//    }
//}