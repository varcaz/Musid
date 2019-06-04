package com.varcaz.musid.EntityClass;

import android.net.Uri;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private String songName;
    private String artist;

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    private String album;
    private int artist_id;
    private int album_id;
    private String genre;
    private String composer;
    private String song_duration;
    private int song_id;
    private Uri song_Uri;
    private int isFav;

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public Uri getSong_Uri() {
        return song_Uri;
    }

    public void setSong_Uri(Uri song_Uri) {
        this.song_Uri = song_Uri;
    }

    public MediaInfo() {

    }

    public MediaInfo(int song_id, String songName, String artist, String album, String genre, String composer, String song_duration, String song_date_added, Uri song_art, Uri song_Uri) {
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.composer = composer;
        this.song_duration = song_duration;
        this.song_id = song_id;
        this.song_date_added = song_date_added;
        this.song_art = song_art;
        this.song_Uri = song_Uri;
    }

    private String song_date_added;
    private Uri song_art;

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setSong_art(Uri song_art) {
        this.song_art = song_art;
    }


    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(String song_duration) {
        this.song_duration = song_duration;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_date_added() {
        return song_date_added;
    }

    public void setSong_date_added(String song_date_added) {
        this.song_date_added = song_date_added;
    }

    public String getGenre() {
        return genre;
    }

    public Uri getSong_art() {
        return song_art;
    }

    public MediaInfo(String songName, String artist, String album, String genre, Uri song_art, String composer) {
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.song_art = song_art;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }


}
