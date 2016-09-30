package de.example.andy.bandwatch.musicbrainz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ACid on 29.09.2016.
 */

public class Album implements Comparable<Album> {

    private String title;
    private String type;
    private Date date;
    private String dateStr;
    private String arid; // MB Artist ID
    private String rgid; // MB Releasegroup ID
    private String reid; // MB Release ID

    private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy MMM", Locale.ENGLISH);
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy MMM d", Locale.ENGLISH);

    public Album(String title, String type, Date date, String dateStr, String arid, String rgid, String reid) {
        this.title = title;
        this.type = type;
        this.date = date;
        this.dateStr = dateStr;
        this.arid = arid;
        this.rgid = rgid;
        this.reid = reid;
    }

    public String getArid() {
        return arid;
    }

    public void setArid(String arid) {
        this.arid = arid;
    }

    public String getRgid() {
        return rgid;
    }

    public void setRgid(String rgid) {
        this.rgid = rgid;
    }

    public String getReid() {
        return reid;
    }

    public void setReid(String reid) {
        this.reid = reid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDateStr() {
        if (dateStr.length() == 4) return sdfYear.format(date);
        if (dateStr.length() == 7) return sdfMonth.format(date);
        else
            return sdfDate.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public String toString() {
        return "Album [title=" + title + ", type=" + type + ", date=" + date + ", dateStr=" + dateStr + ", arid=" + arid
                + ", rgid=" + rgid + ", reid=" + reid + "]";
    }

    @Override
    public int compareTo(Album album) {
        // TODO Auto-generated method stub
        return this.date.compareTo(album.date);
    }

}
