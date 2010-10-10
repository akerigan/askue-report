package org.swing.sample.interactive;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 01.10.2010 23:47:48 (Europe/Moscow)
 */
public class AudioRecord {

    protected String title;
    protected String artist;
    protected String album;

    public AudioRecord() {
        title = "";
        artist = "";
        album = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

}
