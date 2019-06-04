package com.varcaz.musid.EntityClass;

import android.net.Uri;

import java.util.List;

public class PlaylistInfo {
    int Playlist_ID;

    public PlaylistInfo(int playlist_ID, String platlistName, int playlistSongCount, Uri playlistArt) {
        Playlist_ID = playlist_ID;
        this.platlistName = platlistName;
        this.playlistSongCount = playlistSongCount;
        this.playlistArt = playlistArt;
    }

    String platlistName;
    int playlistSongCount;
    Uri playlistArt;

    public PlaylistInfo() {

    }

    public String getPlatlistName() {
        return platlistName;
    }

    public void setPlatlistName(String platlistName) {
        this.platlistName = platlistName;
    }

    public int getPlaylistSongCount() {
        return playlistSongCount;
    }

    public int getPlaylist_ID() {
        return Playlist_ID;
    }

    public void setPlaylist_ID(int playlist_ID) {
        Playlist_ID = playlist_ID;
    }

    public void setPlaylistSongCount(int playlistSongCount) {
        this.playlistSongCount = playlistSongCount;
    }

    public Uri getPlaylistArt() {
        return playlistArt;
    }

    public void setPlaylistArt(Uri playlistArt) {
        this.playlistArt = playlistArt;
    }


}
