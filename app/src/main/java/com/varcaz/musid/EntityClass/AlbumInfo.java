package com.varcaz.musid.EntityClass;

import android.net.Uri;

import java.util.List;

public class AlbumInfo {
    private int album_id;

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }


    private String albumName;
    private int songsCount;
    private Uri albumArt;
    private String artistName;

    public AlbumInfo() {

    }

    public AlbumInfo(String albumName, int songsCount, String artistName, Uri albumArt) {

        this.albumName = albumName;
        this.songsCount = songsCount;
        this.albumArt = albumArt;
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    public Uri getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Uri albumArt) {
        this.albumArt = albumArt;
    }
}
