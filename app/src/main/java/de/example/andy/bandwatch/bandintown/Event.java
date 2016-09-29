package de.example.andy.bandwatch.bandintown;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Event implements Comparable<Event> {

    private int id;

    private String title;

    private Date date;

    private String description;

    private String[] artists;

    private Venue venue;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE d.MMM HH:mm");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return simpleDateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Event(int id, String title, Date date, String description, String[] artists, Venue venue) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.artists = artists;
        this.venue = venue;
    }

    public String[] getArtists() {
        return artists;
    }

    public void setArtists(String[] artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {

        return "Event [id=" + id + ", title=" + title + ", datetime=" + date + ", description=" + description
                + ", artist=" + Arrays.toString(artists) + ", venue=" + venue + "]";
    }

    @Override
    public int compareTo(Event event) {
        return this.date.compareTo(event.date);
    }
}
