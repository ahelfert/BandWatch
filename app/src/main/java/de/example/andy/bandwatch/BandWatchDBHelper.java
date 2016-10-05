package de.example.andy.bandwatch;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Master on 04.10.2016.
 */

public class BandWatchDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = BandWatchDBHelper.class.getSimpleName();

    // database and tables
    private static final String DB_NAME = "bandwatch.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_ARTIST = "artist";
    public static final String TABLE_ALBUM = "album";
    public static final String TABLE_VENUE = "venue";
    public static final String TABLE_EVENT = "event";
    public static final String TABLE_EVENT_ARTIST = "event_artist";

    // artist
    private static final String SQL_CREATE_TABLE_ARTIST = "CREATE TABLE " + TABLE_ARTIST + " (" +
            "_id    TEXT PRIMARY KEY," +    // arid from MusicBrainz
            "name   TEXT NOT NULL);";

    // album
    private static final String SQL_CREATE_TABLE_ALBUM = "CREATE TABLE " + TABLE_ALBUM + " (" +
            "_id	TEXT PRIMARY KEY, " +    // reid from MusicBrainz
            "title	TEXT NOT NULL, " +
            "type	TEXT, " +
            "date	TEXT NOT NULL, " +
            "arid	TEXT, " +
            "rgid	TEXT, " +
            "FOREIGN KEY(`arid`) REFERENCES artist(_id));";

    // venue
    private static final String SQL_CREATE_TABLE_VENUE = "CREATE TABLE " + TABLE_VENUE + " (" +
            "_id	INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name	TEXT NOT NULL," +
            "lat	REAL," +
            "lng	REAL," +
            "city	TEXT NOT NULL," +
            "country	TEXT," +
            "region	TEXT);";

    // event
    private static final String SQL_CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + " (" +
            "_id	TEXT PRIMARY KEY, " +    // id from BandsInTown
            "title	TEXT, " +
            "date	TEXT NOT NULL, " +
            "description TEXT, " +
            "venue_id	INTEGER, " +
            "FOREIGN KEY(venue_id) REFERENCES venue(_id));";

    // event_artist
    private static final String SQL_CREATE_TABLE_EVENT_ARTIST = "CREATE TABLE " + TABLE_EVENT_ARTIST + " (" +
            "event_id	Text, " +    // id from BandsInTown
            "arid   TEXT, " +
            "PRIMARY KEY(event_id, arid), " +
            "FOREIGN KEY(event_id) REFERENCES event(_id), " +
            "FOREIGN KEY(arid) REFERENCES artist(_id));";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS ";


    public BandWatchDBHelper(Context con) {
        super(con, DB_NAME, null, DB_VERSION);  // DB wird angelegt
        Log.d(LOG_TAG, "Helper has created database " + getDatabaseName());
    }

    // onCreate will be called only if database not yet exists (e.g. app new install)
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Log.d(LOG_TAG, "Table will be created.");
            db.execSQL(SQL_CREATE_TABLE_ARTIST);
            db.execSQL(SQL_CREATE_TABLE_ALBUM);
            db.execSQL(SQL_CREATE_TABLE_VENUE);
            db.execSQL(SQL_CREATE_TABLE_EVENT);
            db.execSQL(SQL_CREATE_TABLE_EVENT_ARTIST);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "Failure on create table " + ex.getMessage());
        }


    }

    // Die onUpgrade-Methode wird aufgerufen, sobald die neue Versionsnummer höher
    // als die alte Versionsnummer ist und somit ein Upgrade notwendig wird
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Die Tabelle mit Versionsnummer " + oldVersion + " wird entfernt.");
        db.execSQL(SQL_DROP + TABLE_ARTIST);
        db.execSQL(SQL_DROP + TABLE_ALBUM);
        db.execSQL(SQL_DROP + TABLE_VENUE);
        db.execSQL(SQL_DROP + TABLE_EVENT);
        db.execSQL(SQL_DROP + TABLE_EVENT_ARTIST);
        onCreate(db);
    }

    private static void log(String s) {
        Log.d(LOG_TAG, s);
    }
}