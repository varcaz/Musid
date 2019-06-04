package MediaLoaders;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.varcaz.musid.EntityClass.AlbumInfo;
import com.varcaz.musid.EntityClass.ArtistsInfo;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.EntityClass.PlaylistInfo;

import java.util.ArrayList;

public class MediaQueries {
    DataSource dataSource;
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public MediaQueries(Context context) {
        this.context = context;
        dataSource = new DataSource(context);
        sqLiteDatabase = dataSource.getWritableDatabase();
    }

    public static void LoadAllSongs(Context context, Cursor cursor) {
        DataSource dataSource = new DataSource(context);
        SQLiteDatabase sqLiteDatabase = dataSource.getWritableDatabase();
        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
//        Uri storageUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
//        String[] projection = {"*"};
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor cursor = contentResolver.query(storageUri, projection, selection, null, null);

        Log.i("DB ", cursor.getCount() + " rows");
        DataSource.dropTables(sqLiteDatabase);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    MediaInfo song = new MediaInfo();
                    song.setSong_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));

                    song.setSongName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    song.setAlbum_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    song.setArtist_id(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));
                    song.setComposer(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    song.setSong_date_added(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    song.setSong_art(ContentUris.withAppendedId(albumArtUri, song.getAlbum_id()));
                    song.setSong_duration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    Log.i("Duration duu", "Duration duu " + song.getSong_duration());
                    song.setSong_Uri(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                    song.setSong_Uri(Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));


                    Log.i("DB insert", "uri song and " + song.getSong_Uri() + " ");

                    DataSource.InsertArtist(sqLiteDatabase, song.getArtist_id(), song.getArtist(), song.getSong_art().toString());
                    DataSource.InsertAlbum(sqLiteDatabase, song.getAlbum_id(), song.getAlbum(), song.getArtist_id(), song.getSong_art().toString());
                    DataSource.InsertSong(sqLiteDatabase, song.getSong_id(), song.getSongName(), null, song.getSong_art().toString(), song.getSong_Uri().toString(), song.getGenre(), song.getComposer(), song.getSong_date_added(),
                            song.getSong_duration(), song.getArtist_id(), song.getArtist(), song.getAlbum_id(), song.getAlbum());

                    DataSource.InsertALBUMSONGS(sqLiteDatabase, song.getAlbum_id(), song.getSong_id());
                    DataSource.InsertARTISTSONGS(sqLiteDatabase, song.getArtist_id(), song.getSong_id());
                } while (cursor.moveToNext());


            }

        }

    }

    public static ArrayList<MediaInfo> QuerySongs(Context context) {
        DataSource dataSource = new DataSource(context);
        SQLiteDatabase sqLiteDatabase = dataSource.getReadableDatabase();
        ArrayList<MediaInfo> songsList = new ArrayList<>();
        Cursor cursor = DataSource.getSongs(sqLiteDatabase);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    Log.i("Songs query", cursor.getInt(0) + " " + cursor.getString(1));
                    MediaInfo song = new MediaInfo();
                    song.setSong_id(cursor.getInt(cursor.getColumnIndex(DataSource.SONGS.SONG_ID_COLUMN)));
                    song.setSongName(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_NAME_COLUMN)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ARTIST_NAME_COLUMN)));
                    song.setAlbum(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ALBUM_NAME_COLUMN)));
                    if (cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ART_COLUMN)) != null)
                        song.setSong_art(Uri.parse(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ART_COLUMN))));
                    song.setGenre(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_GENRE)));
                    song.setSong_duration(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_DURATION_COLUMN)));
                    song.setComposer(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_COMPOSER)));
                    song.setSong_Uri(Uri.parse(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_URI_COLUMN))));
                    songsList.add(song);

                    Log.i("DB", " inserted art " + song.getComposer() + " " + song.getGenre());
                } while (cursor.moveToNext());
            }
        }

        Log.v("DB media", " songs " + songsList.size());
        return songsList;

    }

    public static ArrayList<AlbumInfo> QueryAlbums(Context context) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        ArrayList<AlbumInfo> albumInfoArrayList = new ArrayList<>();
        sqLiteDatabase = dataSource.getReadableDatabase();
        Cursor cursor = DataSource.getAlbums(sqLiteDatabase);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    AlbumInfo albumInfo = new AlbumInfo();
                    albumInfo.setAlbum_id(cursor.getInt(0));

                    albumInfo.setAlbumName(cursor.getString(1));
                    if (cursor.getString(2) != null)
                        albumInfo.setAlbumArt(Uri.parse(cursor.getString(2)));
                    albumInfo.setArtistName(cursor.getString(3));
                    albumInfo.setSongsCount(cursor.getInt(4));

                    albumInfoArrayList.add(albumInfo);

                } while (cursor.moveToNext());
            }
        }

        Log.v("DB media", "albums " + albumInfoArrayList.size());
        return albumInfoArrayList;
    }

    public static ArrayList<MediaInfo> QueryOnClickSongs(Context context, String Table, int id) {
        DataSource dataSource = new DataSource(context);
        SQLiteDatabase sqLiteDatabase = dataSource.getReadableDatabase();
        ArrayList<MediaInfo> songsList = new ArrayList<>();
        Cursor cursor;
        if (!Table.equalsIgnoreCase("playlist"))
            cursor = DataSource.getSongsForAlbumORArtists(sqLiteDatabase, Table, id);
        else
            cursor = DataSource.getSongsForPlaylists(sqLiteDatabase, id);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    Log.i("Songs query on Click", "playlist on click " + cursor.getCount() + cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
                    MediaInfo song = new MediaInfo();
                    song.setSong_id(cursor.getInt(cursor.getColumnIndex(DataSource.SONGS.SONG_ID_COLUMN)));
                    song.setSongName(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_NAME_COLUMN)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ARTIST_NAME_COLUMN)));
                    song.setAlbum(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ALBUM_NAME_COLUMN)));

                    song.setSong_duration(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_DURATION_COLUMN)));
                    if (cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ART_COLUMN)) != null)
                        song.setSong_art(Uri.parse(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_ART_COLUMN))));
                    song.setGenre(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_GENRE)));
                    song.setComposer(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_COMPOSER)));

                    song.setSong_Uri(Uri.parse(cursor.getString(cursor.getColumnIndex(DataSource.SONGS.SONG_URI_COLUMN))));
                    songsList.add(song);

                    Log.i("DB", " inserted art " + song.getComposer() + " " + song.getGenre());
                } while (cursor.moveToNext());
            }
        }

        Log.v("DB media", " songs " + songsList.size());
        return songsList;


    }

    public static ArrayList<PlaylistInfo> QueryPlaylists(Context context) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        ArrayList<PlaylistInfo> playlistInfoArrayList = new ArrayList<>();
        sqLiteDatabase = dataSource.getReadableDatabase();
        Cursor cursor = DataSource.getPlaylist(sqLiteDatabase);
        Log.i("playlist", "playlist cursor" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("playlist", "playlist get " + cursor.getCount() + "  " + cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                    PlaylistInfo playlistInfo = new PlaylistInfo();
                    playlistInfo.setPlaylist_ID(cursor.getInt(0));
                    playlistInfo.setPlatlistName(cursor.getString(1));
                    if (cursor.getString(2) != null)
                        playlistInfo.setPlaylistArt(Uri.parse(cursor.getString(2)));
                    else
                        playlistInfo.setPlaylistArt(Uri.parse("content://media/external/audio/albumart"));
                    playlistInfo.setPlaylistSongCount(cursor.getInt(3));
                    playlistInfoArrayList.add(playlistInfo);
                } while (cursor.moveToNext());
            }
        }

        Log.i("playlist", "playlist cursor" + cursor.getCount() + "  " + playlistInfoArrayList.get(0).getPlaylist_ID());
        return playlistInfoArrayList;

    }

    public static void AddPlaylist(Context context, String name) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        sqLiteDatabase = dataSource.getWritableDatabase();
        DataSource.AddPlaylist(sqLiteDatabase, name);
    }

    public static ArrayList<ArtistsInfo> QueryArtists(Context context) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        ArrayList<ArtistsInfo> artistsInfos = new ArrayList<>();
        sqLiteDatabase = dataSource.getReadableDatabase();
        Cursor cursor = DataSource.getArtists(sqLiteDatabase);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Log.i("Artists query", "Artists query " + cursor.getCount() + "  " + cursor.getInt(0) + " " + cursor.getString(1));
                    ArtistsInfo artistsInfo = new ArtistsInfo();
                    artistsInfo.setArtistID(cursor.getInt(0));
                    artistsInfo.setArtistName(cursor.getString(1));

                    if (cursor.getString(2) != null)
                        artistsInfo.setArtistArt(Uri.parse(cursor.getString(2)));

                    artistsInfo.setArtistSongCount(cursor.getInt(3));
                    artistsInfo.setArtistAlbumCount(cursor.getInt(4));
                    artistsInfos.add(artistsInfo);
                } while (cursor.moveToNext());
            }
        }

        Log.v("DB media", "artists " + artistsInfos.size());
        return artistsInfos;
    }

    public static void deletePlaylist(Context context, int playlist_id) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        sqLiteDatabase = dataSource.getWritableDatabase();
        DataSource.deletePlaylist(sqLiteDatabase, playlist_id);
    }

    public static void AddSongsToPlaylist(Context context, int playlist_id, int song_id) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        sqLiteDatabase = dataSource.getWritableDatabase();
        DataSource.InsertPLAYLISTSONGS(sqLiteDatabase, playlist_id, song_id);
    }

    public static void RemoveSongsFromPlaylist(Context context, int playlist_id, int song_id) {
        DataSource dataSource;
        SQLiteDatabase sqLiteDatabase;
        dataSource = new DataSource(context);
        sqLiteDatabase = dataSource.getWritableDatabase();
        DataSource.deleteSongsFromPlaylist(sqLiteDatabase, playlist_id, song_id);
    }
}


