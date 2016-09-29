package de.example.andy.bandwatch.musicbrainz;

import java.util.Date;

/**
 * Created by ACid on 29.09.2016.
 */

public class Album {

    private String title;
    private String type;
    private Date date;
    private String label;
    private String arid; // MB Artist ID
    private String rgid; // MB Releasegroup ID
    private String reid; // MB Release ID

    public Album(String arid, String rgid, String reid, String title, String type, Date date, String label) {
        this.rgid = rgid;
        this.title = title;
        this.type = type;
        this.date = date;
        this.label = label;
        this.arid = arid;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", label='" + label + '\'' +
                '}';
    }

}
