package com.varcaz.musid.EntityClass;

import android.net.Uri;

import java.util.List;

public class ArtistsInfo {
    int artistID;

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public ArtistsInfo() {

    }

    String artistName;
    int artistSongCount;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getArtistSongCount() {
        return artistSongCount;
    }

    public void setArtistSongCount(int artistSongCount) {
        this.artistSongCount = artistSongCount;
    }

    public int getArtistAlbumCount() {
        return artistAlbumCount;
    }

    public void setArtistAlbumCount(int artistAlbumCount) {
        this.artistAlbumCount = artistAlbumCount;
    }

    public Uri getArtistArt() {
        return artistArt;
    }

    public void setArtistArt(Uri artistArt) {
        this.artistArt = artistArt;
    }


    public ArtistsInfo(String artistName, int artistSongCount, int artistAlbumCount, Uri artistArt) {
        this.artistName = artistName;
        this.artistSongCount = artistSongCount;
        this.artistAlbumCount = artistAlbumCount;
        this.artistArt = artistArt;
    }

    int artistAlbumCount;
    Uri artistArt;


}
