package MediaLoaders;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataSource extends SQLiteOpenHelper {
    public static final String DB_Name = "musid";
    public static final int DB_VERSION = 1;


    public static abstract class SONGS implements BaseColumns {
        public static final String SONGS_TABLE = "SONGS_TABLE";
        public static final String SONG_ID_COLUMN = "SONG_ID_COLUMN";
        public static final String SONG_NAME_COLUMN = "SONG_NAME_COLUMN";
        public static final String SONG_PATH_COLUMN = "SONG_PATH_COLUMN";
        public static final String SONG_ART_COLUMN = "SONG_ART_COLUMN";
        public static final String SONG_URI_COLUMN = "SONG_URI_COLUMN";
        public static final String SONG_GENRE = "SONG_GENRE";
        public static final String SONG_COMPOSER = "SONG_COMPOSER";
        public static final String SONG_DATE_ADDED_COLUMN = "SONG_DATE_ADDED_COLUMN";
        public static final String SONG_ARTIST_ID_COLUMN = "SONG_ARTIST_ID_COLUMN";
        public static final String SONG_ARTIST_NAME_COLUMN = "SONG_ARTIST_NAME_COLUMN";
        public static final String SONG_ALBUM_ID_COLUMN = "SONG_ALBUM_ID_COLUMN";
        public static final String SONG_ALBUM_NAME_COLUMN = "SONG_ALBUM_NAME_COLUMN";
        public static final String SONG_DURATION_COLUMN = "SONG_DURATION_COLUMN";


    }

    public static abstract class ALBUMS implements BaseColumns {
        public static final String ALBUMS_TABLE = "ALBUMS_TABLE";
        public static final String ALBUM_ID_COLUMN = "ALBUM_ID_COLUMN";
        public static final String ALBUM_NAME_COLUMN = "ALBUM_NAME_COLUMN";
        public static final String ALBUM_ART_COLUMN = "ALBUM_ART_COLUMN";
        public static final String ALBUM_ARTIST_ID_COLUMN = "ALBUM_ARTIST_ID_COLUMN";


    }

    public static abstract class FAVORITE implements BaseColumns {
        public static final String FAVORITE_TABLE = "FAVORITE_TABLE";
        public static final String FAV_SONG_ID_COLUMN = "FAV_SONG_ID_COLUMN";
    }


    public static abstract class ARTISTS implements BaseColumns {
        public static final String ARTISTS_TABLE = "ARTISTS_TABLE";
        public static final String ARTIST_ID_COLUMN = "ARTIST_ID_COLUMN";
        public static final String ARTIST_NAME_COLUMN = "ARTIST_NAME_COLUMN";
        public static final String ARTIST_ART_COLUMN = "ARTIST_ART_COLUMN";
    }

    public static final String PLAYLIST_SONGS_TABLE = "PLAYLIST_SONGS_TABLE";
    public static final String PLAYLIST_SONGS_ID_COLUMN = "PLAYLIST_SONGS_ID_COLUMN";
    public static final String PLAYLIST_SONGS_PLAYLIST_ID_COLUMN = "PLAYLIST_SONGS_PLAYLIST_ID_COLUMN";
    public static final String PLAYLIST_SONGS__SONGS_ID_COLUMN = "PLAYLIST_SONGS__SONGS_ID_COLUMN";


    public static abstract class PLAYLIST implements BaseColumns {
        public static final String PLAYLIST_TABLE = "PLAYLIST_TABLE";
        public static final String PLAYLIST_ID_COLUMN = "PLAYLIST_ID_COLUMN";
        public static final String PLAYLIST_NAME_COLUMN = "PLAYLIST_NAME_COLUMN";
        public static final String PLAYLIST_ART_COLUMN = "PLAYLIST_ART_COLUMN";
    }

    public static abstract class ALBUMSONGS implements BaseColumns {
        public static final String ALBUMSONGS_TABLE = "ALBUMSONGS_TABLE";
        public static final String FK_ALBUM_ID = "ALBUM_ID";
        public static final String FK_SONG_ID = "SONG_ID";
    }

    public static abstract class ARTISTSONGS implements BaseColumns {
        public static final String ARTISTSONGS_TABLE = "ARTISTSONGS_TABLE";
        public static final String FK_ARTIST_ID = "FK_ARTIST_ID";
        public static final String FK_SONG_ID = "SONG_ID";
    }

    public static final String ALBUMS_INFO_V = "ALBUMS_INFO_V";
    public static final String ARTISTS_INFO_V = "ARTISTS_INFO_V";
    public static final String SONGS_INFO_V = "SONGS_INFO_V";

    public static final String ALBUMS_V = "ALBUMS_V";
    public static final String ARTISTS_V = "ARTISTS_V";

    public static final String PLAYLIST_ART_SELECTION_QUERY = "SELECT " + SONGS.SONG_ART_COLUMN + " FROM " + SONGS.SONGS_TABLE + " WHERE " + SONGS.SONG_ID_COLUMN + "=";

    public static final String SONG_COUNT = "SONG_COUNT";
    public static final String PLAYLIST_V = "PLAYLIST_V";

    public static final String PLAYLIST_SONGS_V = "PLAYLIST_SONGS_V";

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.setForeignKeyConstraintsEnabled(true);
        }

    }

    public DataSource(@Nullable Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }


    @Override
    public synchronized void close() {
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS " + ARTISTS.ARTISTS_TABLE );
//        db.execSQL("DROP TABLE IF EXISTS " + ALBUMS.ALBUMS_TABLE );
//        db.execSQL("DROP TABLE IF EXISTS " + SONGS.SONGS_TABLE );


        db.execSQL("CREATE TABLE IF NOT EXISTS " + ARTISTS.ARTISTS_TABLE +
                "(" + ARTISTS.ARTIST_ID_COLUMN + " INTEGER PRIMARY KEY , " +
                ARTISTS.ARTIST_NAME_COLUMN + "  VARCHAR , "
                + ARTISTS.ARTIST_ART_COLUMN + " VARCHAR " +
                ");"
        );


        db.execSQL("CREATE TABLE IF NOT EXISTS " + ARTISTSONGS.ARTISTSONGS_TABLE +
                "(" + ARTISTSONGS.FK_ARTIST_ID + " INTEGER  , " +
                ARTISTSONGS.FK_SONG_ID + " INTEGER  , " +
                "FOREIGN KEY (" + ARTISTSONGS.FK_ARTIST_ID + ") REFERENCES " + ARTISTS.ARTISTS_TABLE + " (" + ARTISTS.ARTIST_ID_COLUMN + ") " + "," +
                "FOREIGN KEY (" + ARTISTSONGS.FK_SONG_ID + ") REFERENCES " + SONGS.SONGS_TABLE + " (" + SONGS.SONG_ID_COLUMN + ") " +
                ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALBUMSONGS.ALBUMSONGS_TABLE +
                "(" + ALBUMSONGS.FK_ALBUM_ID + " INTEGER  , " +
                ALBUMSONGS.FK_SONG_ID + " INTEGER  , " +
                "FOREIGN KEY (" + ALBUMSONGS.FK_ALBUM_ID + ") REFERENCES " + ALBUMS.ALBUMS_TABLE + " (" + ALBUMS.ALBUM_ID_COLUMN + ") " + "," +
                "FOREIGN KEY (" + ALBUMSONGS.FK_SONG_ID + ") REFERENCES " + SONGS.SONGS_TABLE + " (" + SONGS.SONG_ID_COLUMN + ") " +
                ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALBUMS.ALBUMS_TABLE +
                "(" + ALBUMS.ALBUM_ID_COLUMN + " INTEGER PRIMARY KEY , " +
                ALBUMS.ALBUM_NAME_COLUMN + "  VARCHAR , "
                + ALBUMS.ALBUM_ART_COLUMN + " VARCHAR ," +
                ALBUMS.ALBUM_ARTIST_ID_COLUMN + " INTEGER ," +
                "FOREIGN KEY (" + ALBUMS.ALBUM_ARTIST_ID_COLUMN + ") REFERENCES " + ARTISTS.ARTISTS_TABLE + " (" + ARTISTS.ARTIST_ID_COLUMN + ") " +
                ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SONGS.SONGS_TABLE +
                        "(" + SONGS.SONG_ID_COLUMN + " INTEGER PRIMARY KEY, " +
                        SONGS.SONG_NAME_COLUMN + "  VARCHAR , "
                        + SONGS.SONG_PATH_COLUMN + " VARCHAR ," +
                        SONGS.SONG_URI_COLUMN + "  VARCHAR , " +
                        SONGS.SONG_ART_COLUMN + "  VARCHAR , " +
                        SONGS.SONG_GENRE + "  VARCHAR , " +
                        SONGS.SONG_COMPOSER + "  VARCHAR , " +
                        SONGS.SONG_DATE_ADDED_COLUMN + " VARCHAR , " +
                        SONGS.SONG_DURATION_COLUMN + " VARCHAR , " +
                        SONGS.SONG_ARTIST_ID_COLUMN + " INTEGER ," +
                        SONGS.SONG_ARTIST_NAME_COLUMN + "  VARCHAR , " +
                        SONGS.SONG_ALBUM_ID_COLUMN + " INTEGER," +
                        SONGS.SONG_ALBUM_NAME_COLUMN + "  VARCHAR  " +
//                "FOREIGN KEY (" + SONGS.SONG_ARTIST_ID_COLUMN + ") REFERENCES " + ARTISTS.ARTISTS_TABLE + " (" + ARTISTS.ARTIST_ID_COLUMN + ") ," +
//                "FOREIGN KEY (" + SONGS.SONG_ALBUM_ID_COLUMN + ") REFERENCES " + ALBUMS.ALBUMS_TABLE + " (" + ALBUMS.ALBUM_ID_COLUMN + ") " +
                        ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITE.FAVORITE_TABLE +
                "(" + FAVORITE.FAV_SONG_ID_COLUMN + " INTEGER  " +

                ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLAYLIST.PLAYLIST_TABLE +
                "(" + PLAYLIST.PLAYLIST_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PLAYLIST.PLAYLIST_NAME_COLUMN + " VARCHAR " + ", " + PLAYLIST.PLAYLIST_ART_COLUMN +
                ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PLAYLIST_SONGS_TABLE +
                "(" + PLAYLIST_SONGS_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PLAYLIST_SONGS__SONGS_ID_COLUMN + " INTEGER, " +
                PLAYLIST_SONGS_PLAYLIST_ID_COLUMN + " INTEGER  ," +
                "FOREIGN KEY (" + PLAYLIST_SONGS__SONGS_ID_COLUMN + ") REFERENCES " + SONGS.SONGS_TABLE + " (" + SONGS.SONG_ID_COLUMN + ") ," +
                "FOREIGN KEY (" + PLAYLIST_SONGS_PLAYLIST_ID_COLUMN + ") REFERENCES " + PLAYLIST.PLAYLIST_TABLE + " (" + PLAYLIST.PLAYLIST_ID_COLUMN + ") " +
                ");"
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + ALBUMS_INFO_V + " AS SELECT " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN + ", " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_NAME_COLUMN + ", " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ART_COLUMN + ", " +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + "," +
                "COUNT(" + SONGS.SONGS_TABLE + "." + SONGS.SONG_ID_COLUMN + ")" +
                " FROM " + ALBUMS.ALBUMS_TABLE + "," + SONGS.SONGS_TABLE + "," +
                ARTISTS.ARTISTS_TABLE +
                " ON " + SONGS.SONGS_TABLE + "." + SONGS.SONG_ALBUM_ID_COLUMN + "=" + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN +
                " AND " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "=" + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ARTIST_ID_COLUMN +
                " ORDER BY " + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_NAME_COLUMN + ";"
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + ARTISTS_INFO_V + " AS SELECT " +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + " , " +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + ", " +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ART_COLUMN +
                ",COUNT(" + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN + ")" +
                ",COUNT(" + SONGS.SONGS_TABLE + "." + SONGS.SONG_ID_COLUMN + ")" + " FROM " + SONGS.SONGS_TABLE + " , "
                + ALBUMS.ALBUMS_TABLE + " , " + ARTISTS.ARTISTS_TABLE + " ON " +
                SONGS.SONGS_TABLE + "." + SONGS.SONG_ARTIST_ID_COLUMN + "=" + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN +
                " AND "
                + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "=" + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ARTIST_ID_COLUMN +
                " ORDER BY " + SONGS.SONG_ARTIST_NAME_COLUMN + " ;"
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + SONGS_INFO_V + " AS SELECT " +
                SONGS.SONG_ID_COLUMN + "," +
                SONGS.SONG_NAME_COLUMN + "," +
                SONGS.SONG_ARTIST_NAME_COLUMN + "," +
                SONGS.SONG_ALBUM_NAME_COLUMN + "," +
                SONGS.SONG_ART_COLUMN + "," +
                SONGS.SONG_GENRE + "," +
                SONGS.SONG_COMPOSER + " FROM " + SONGS.SONGS_TABLE +
                " ORDER BY " + SONGS.SONG_NAME_COLUMN + ";"
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + ALBUMS_V + " AS SELECT " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN + ", " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_NAME_COLUMN + ", " +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ART_COLUMN + "," +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + "," +
                "(" + "SELECT" + " COUNT(" + ALBUMSONGS.ALBUMSONGS_TABLE + "." + ALBUMSONGS.FK_SONG_ID + ")" +
                " FROM " + ALBUMSONGS.ALBUMSONGS_TABLE + " WHERE " +
                ALBUMSONGS.ALBUMSONGS_TABLE + "." + ALBUMSONGS.FK_ALBUM_ID + "=" +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN + ")" + SONG_COUNT +      //song count
                " FROM " + ALBUMS.ALBUMS_TABLE + "," + ARTISTS.ARTISTS_TABLE +
                " WHERE " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "=" +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ARTIST_ID_COLUMN +
                " ORDER BY " + ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_NAME_COLUMN + ";"
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + PLAYLIST_V + " AS SELECT " +
                PLAYLIST.PLAYLIST_TABLE + "." + PLAYLIST.PLAYLIST_ID_COLUMN + ", " +
                PLAYLIST.PLAYLIST_TABLE + "." + PLAYLIST.PLAYLIST_NAME_COLUMN + ", " +
                PLAYLIST.PLAYLIST_TABLE + "." + PLAYLIST.PLAYLIST_ART_COLUMN + ", " +
                "(" + "SELECT" + " COUNT(" + PLAYLIST_SONGS_TABLE + "." + PLAYLIST_SONGS__SONGS_ID_COLUMN + ")" +
                " FROM " + PLAYLIST_SONGS_TABLE + " WHERE " +
                PLAYLIST_SONGS_TABLE + "." + PLAYLIST_SONGS_PLAYLIST_ID_COLUMN + "=" +
                PLAYLIST.PLAYLIST_TABLE + "." + PLAYLIST.PLAYLIST_ID_COLUMN + ")" + SONG_COUNT +      //song count
                " FROM " + PLAYLIST.PLAYLIST_TABLE
        );


        db.execSQL("CREATE VIEW IF NOT EXISTS " + ARTISTS_V + " AS SELECT " +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "," +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + "," +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ART_COLUMN + "," +
                "(" + "SELECT" + " COUNT(" + ARTISTSONGS.ARTISTSONGS_TABLE + "." + ARTISTSONGS.FK_SONG_ID + ")" +
                " FROM " + ARTISTSONGS.ARTISTSONGS_TABLE + " WHERE " +
                ARTISTSONGS.ARTISTSONGS_TABLE + "." + ARTISTSONGS.FK_ARTIST_ID + "=" +
                ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + ")" + SONG_COUNT + "," +    //song count
                "(" + "SELECT" + " COUNT(" + ALBUMSONGS.ALBUMSONGS_TABLE + "." + ALBUMSONGS.FK_ALBUM_ID + ")" +
                " FROM " + ALBUMSONGS.ALBUMSONGS_TABLE + " WHERE " +
                ALBUMSONGS.ALBUMSONGS_TABLE + "." + ALBUMSONGS.FK_ALBUM_ID + "=" +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ID_COLUMN + ")" +      //song count

                " FROM " + ALBUMS.ALBUMS_TABLE + " inner join " + ARTISTS.ARTISTS_TABLE +
                " ON " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "=" +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ARTIST_ID_COLUMN +
                " ORDER BY " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + ";"
        );

        db.execSQL("CREATE VIEW IF NOT EXISTS " + PLAYLIST_SONGS_V + " AS SELECT " +
                " DISTINCT " + SONGS.SONG_ID_COLUMN + "," +
                SONGS.SONG_NAME_COLUMN + "," +
                SONGS.SONG_ARTIST_NAME_COLUMN + "," +
                SONGS.SONG_ART_COLUMN + "," +
                SONGS.SONG_DURATION_COLUMN +
                " FROM " + SONGS.SONGS_TABLE + " , " + PLAYLIST.PLAYLIST_TABLE + "," + PLAYLIST_SONGS_TABLE +
                " WHERE " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_ID_COLUMN + "=" +
                ALBUMS.ALBUMS_TABLE + "." + ALBUMS.ALBUM_ARTIST_ID_COLUMN +
                " ORDER BY " + ARTISTS.ARTISTS_TABLE + "." + ARTISTS.ARTIST_NAME_COLUMN + ";"
        );
        AddPlaylistByID(db, 1, "Favorite");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    ///////////////////////////////////////////////////////////////////////get Albums /////////////////////////////////////////////////////////////////
    public static Cursor getAlbums(SQLiteDatabase db) {
        try {
//            Cursor cursor = db.rawQuery("SELECT * FROM "+ALBUMS_INFO_V+ " ;", null);
            Cursor cursor = db.query(ALBUMS_V, new String[]{"*"}, null, null, null, null, null);

            return cursor;
        } catch (Exception e) {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////get All Songs /////////////////////////////////////////////////////////////////
    public static Cursor getSongs(SQLiteDatabase db) {
        try {

            Cursor cursor = db.query(SONGS.SONGS_TABLE, new String[]{"*"}, null, null, null, null, SONGS.SONG_NAME_COLUMN);

            return cursor;
        } catch (Exception e) {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////get Artists /////////////////////////////////////////////////////////////////
    public static Cursor getArtists(SQLiteDatabase db) {
        try {
            Cursor cursor = db.query(ARTISTS_V, new String[]{"*"}, null, null, null, null, ARTISTS.ARTIST_NAME_COLUMN);

            return cursor;
        } catch (Exception e) {
            return null;
        }
    }

    public static Cursor getPlaylist(SQLiteDatabase db) {
        try {
            Cursor cursor = db.query(PLAYLIST_V, new String[]{"*"}, null, null, null, null, PLAYLIST.PLAYLIST_ID_COLUMN);

            return cursor;
        } catch (Exception e) {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////get Song for particular Album/////////////////////////////////////////////////////////////////
    public static Cursor getSongsForAlbumORArtists(SQLiteDatabase db, String TABLE, int ID) {
        Cursor cursor;
        try {

            cursor = db.query(SONGS.SONGS_TABLE, new String[]{"*"}, TABLE + "=" + ID, null, null, null, SONGS.SONG_NAME_COLUMN);


        } catch (Exception e) {
            cursor = null;

        }

        Log.i("meddai", "mediiaaa c" + cursor.getCount());
        return cursor;
    }

    public static Cursor getSongsForPlaylists(SQLiteDatabase db, int ID) {
        Cursor cursor;
        try {

            cursor = db.query(SONGS.SONGS_TABLE, new String[]{"*"}, SONGS.SONG_ID_COLUMN + " IN " +
                            "( SELECT " + SONGS.SONG_ID_COLUMN + " FROM " + PLAYLIST_SONGS_TABLE + " PS ," +
                            PLAYLIST.PLAYLIST_TABLE + " P WHERE P." + PLAYLIST.PLAYLIST_ID_COLUMN + "=" + ID + " AND PS." +
                            PLAYLIST_SONGS_PLAYLIST_ID_COLUMN + "= P." + PLAYLIST.PLAYLIST_ID_COLUMN + " AND PS." + PLAYLIST_SONGS__SONGS_ID_COLUMN +
                            "=" + SONGS.SONG_ID_COLUMN + ")"

                    , null, null, null, SONGS.SONG_NAME_COLUMN);
            Log.i("Songs query on Click", "playlist on click " + cursor.getCount());

//            cursor = db.rawQuery("SELECT * FROM "+SONGS.SONGS_TABLE+" WHERE "+SONGS.SONG_ID_COLUMN+" IN "+
//                            "( SELECT "+SONGS.SONG_ID_COLUMN+" FROM "+PLAYLIST_SONGS_TABLE +" PS ,"+
//                            PLAYLIST.PLAYLIST_TABLE+" P WHERE P."+PLAYLIST.PLAYLIST_ID_COLUMN+"="+ID+" AND PS."+
//                            PLAYLIST_SONGS__SONGS_ID_COLUMN +"= P."+PLAYLIST.PLAYLIST_ID_COLUMN +" AND PS."+PLAYLIST_SONGS__SONGS_ID_COLUMN+
//                            "="+SONGS.SONG_ID_COLUMN+")"+" ORDER BY "+
//
//                     SONGS.SONG_NAME_COLUMN,null);
            Log.i("Songs query on Click", "playlist on click " + cursor.getCount());


        } catch (Exception e) {
            Log.i("DB insert", e.toString());
            cursor = null;

        }

        Log.i("meddai", "playlis c" + cursor.getCount());
        return cursor;
    }

    ///////////////////////////////////////////////////////////////////////Insert into Artist table/////////////////////////////////////////////////////////////////
    public static void InsertArtist(SQLiteDatabase db, int ID, String name, String art) {
        try {
            ContentValues values = new ContentValues();
            values.put(ARTISTS.ARTIST_ID_COLUMN, ID);
            values.put(ARTISTS.ARTIST_NAME_COLUMN, name);
            values.put(ARTISTS.ARTIST_ART_COLUMN, art);

            db.insertWithOnConflict(ARTISTS.ARTISTS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//            int rowID = db.update(ARTISTS.ARTISTS_TABLE, values, ARTISTS.ARTIST_ID_COLUMN + " = "+ID, null);
//            if (rowID == 0)
//                try {
//                    int row = (int) db.insert(ARTISTS.ARTISTS_TABLE, null, values);
//
//                    Log.i("ARTIST", "artist inserted into row " + row + " " + name);
//
//                } catch (Exception e) {
//                }
            Log.i("DB insert", "artist inserted count " + ID + " " + name);
//            Log.i("DB insert", "artist inserted count " + DatabaseUtils.queryNumEntries(db, ARTISTS.ARTISTS_TABLE));
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////Insert into Album table/////////////////////////////////////////////////////////////////
    public static void InsertAlbum(SQLiteDatabase db, int ID, String name, int artist_id, String art) {
        try {

            ContentValues values = new ContentValues();
            values.put(ALBUMS.ALBUM_ID_COLUMN, ID);
            values.put(ALBUMS.ALBUM_NAME_COLUMN, name);
            values.put(ALBUMS.ALBUM_ARTIST_ID_COLUMN, artist_id);
            values.put(ALBUMS.ALBUM_ART_COLUMN, art);


            db.insertWithOnConflict(ALBUMS.ALBUMS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//            int rowID = db.update(ALBUMS.ALBUMS_TABLE, values, ALBUMS.ALBUM_ID_COLUMN + " = "+ID, null);
//            if (rowID == 0)
//                try {
//                    int row = (int) db.insert(ALBUMS.ALBUMS_TABLE, null, values);
//
//                    Log.i("ALBUM", "album inserted into row " + row + " " + name);
//
//                } catch (Exception e) {
//                }

            Log.i("DB insert", "album inserted count " + DatabaseUtils.queryNumEntries(db, ALBUMS.ALBUMS_TABLE));
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////Insert into AlbumSongs table/////////////////////////////////////////////////////////////////
    public static void InsertALBUMSONGS(SQLiteDatabase db, int album_id, int song_id) {
        try {

            ContentValues values = new ContentValues();
            values.put(ALBUMSONGS.FK_ALBUM_ID, album_id);
            values.put(ALBUMSONGS.FK_SONG_ID, song_id);

            int row = (int) db.insert(ALBUMSONGS.ALBUMSONGS_TABLE, null, values);

            Log.i("ALBUM", "albumSONGS inserted into row " + row + " ");
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////Insert into ArtistSongs table/////////////////////////////////////////////////////////////////
    public static void InsertARTISTSONGS(SQLiteDatabase db, int artist_id, int song_id) {
        try {

            ContentValues values = new ContentValues();
            values.put(ARTISTSONGS.FK_ARTIST_ID, artist_id);
            values.put(ARTISTSONGS.FK_SONG_ID, song_id);

            int row = (int) db.insert(ARTISTSONGS.ARTISTSONGS_TABLE, null, values);

            Log.i("ALBUM", "artistSongs inserted into row " + row + " ");
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////Insert into PLAYLISTSONGS table/////////////////////////////////////////////////////////////////
    public static void InsertPLAYLISTSONGS(SQLiteDatabase db, int playlist_id, int song_id) {
        try {

            ContentValues values = new ContentValues();
            values.put(PLAYLIST_SONGS__SONGS_ID_COLUMN, song_id);
            values.put(PLAYLIST_SONGS_PLAYLIST_ID_COLUMN, playlist_id);

            db.insert(PLAYLIST_SONGS_TABLE, null, values);

            ContentValues playlistvalues = new ContentValues();
//            Cursor cursor=db.query(SONGS.SONGS_TABLE,new String[]{SONGS.SONG_ART_COLUMN},SONGS.SONG_ID_COLUMN+"="+song_id,null,null,null,null);
//            Log.i("Playlist","playlist get sub "+cursor.getString(cursor.getColumnIndex(SONGS.SONG_ART_COLUMN)));
//            playlistvalues.put(PLAYLIST.PLAYLIST_ART_COLUMN,cursor.getString(cursor.getColumnIndex(SONGS.SONG_ART_COLUMN)));
//            db.update(PLAYLIST.PLAYLIST_TABLE,playlistvalues,PLAYLIST.PLAYLIST_ID_COLUMN+"="+playlist_id,null);
//
            db.execSQL("update " + PLAYLIST.PLAYLIST_TABLE + " set " + PLAYLIST.PLAYLIST_ART_COLUMN + "=(SELECT " + SONGS.SONG_ART_COLUMN + " from " + SONGS.SONGS_TABLE + " where " + SONGS.SONG_ID_COLUMN + "=" + song_id + ")" +
                    " where " + PLAYLIST.PLAYLIST_ID_COLUMN + "=" + playlist_id);
            Log.i("playlistsongs", "playlist song inserted");
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////add new Playlist tables/////////////////////////////////////////////////////////////////
    public static void AddPlaylist(SQLiteDatabase db, String name) {
        try {
            ContentValues values = new ContentValues();
            values.put(PLAYLIST.PLAYLIST_NAME_COLUMN, name);
            db.insert(PLAYLIST.PLAYLIST_TABLE, null, values
            );

            Log.i("playlist", "playlist inserted");
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    public static void AddPlaylistByID(SQLiteDatabase db, int ID, String name) {
        try {
            ContentValues values = new ContentValues();

            values.put(PLAYLIST.PLAYLIST_ID_COLUMN, ID);
            values.put(PLAYLIST.PLAYLIST_NAME_COLUMN, name);
            db.insert(PLAYLIST.PLAYLIST_TABLE, null, values
            );

            Log.i("playlist", "playlist inserted");
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////insert into SONGS table/////////////////////////////////////////////////////////////////
    public static void InsertSong(SQLiteDatabase db, int ID, String name, String path, String art, String URI, String genre, String composer, String date_added, String duration, int artist_id, String artist, int album_id, String album) {
        try {
            ContentValues values = new ContentValues();
            values.put(SONGS.SONG_ID_COLUMN, ID);
            values.put(SONGS.SONG_ART_COLUMN, art);
            values.put(SONGS.SONG_NAME_COLUMN, name);
            values.put(SONGS.SONG_PATH_COLUMN, path);
            values.put(SONGS.SONG_URI_COLUMN, URI);
            values.put(SONGS.SONG_GENRE, genre);
            values.put(SONGS.SONG_COMPOSER, composer);
            values.put(SONGS.SONG_DATE_ADDED_COLUMN, date_added);
            values.put(SONGS.SONG_DURATION_COLUMN, duration);
            values.put(SONGS.SONG_ARTIST_ID_COLUMN, artist_id);
            values.put(SONGS.SONG_ALBUM_ID_COLUMN, album_id);
            values.put(SONGS.SONG_ARTIST_NAME_COLUMN, artist);
            values.put(SONGS.SONG_ALBUM_NAME_COLUMN, album);

            db.insertWithOnConflict(SONGS.SONGS_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            Log.i("DB insert", "genre and " + genre + " " + composer);

            Log.i("DB insert", "inserted count " + DatabaseUtils.queryNumEntries(db, SONGS.SONGS_TABLE));

        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    ///////////////////////////////////////////////////////////////////////add Favorite Song /////////////////////////////////////////////////////////////////
    public static void addFav(SQLiteDatabase db, int song_id) {
        try {
            ContentValues values = new ContentValues();
            values.put(FAVORITE.FAV_SONG_ID_COLUMN, song_id);
            db.insertOrThrow(FAVORITE.FAVORITE_TABLE, FAVORITE.FAV_SONG_ID_COLUMN, values);
        } catch (Exception e) {
            Log.i("DB insert", e.toString());
        }
    }

    public static void deletePlaylist(SQLiteDatabase db, int playlist_id) {
        try {
            db.delete(PLAYLIST.PLAYLIST_TABLE, PLAYLIST.PLAYLIST_ID_COLUMN + "=" + playlist_id, null);
        } catch (Exception e) {

        }
    }

    public static void deleteSongsFromPlaylist(SQLiteDatabase db, int playlist_id, int song_id) {
        try {
            db.delete(PLAYLIST_SONGS_TABLE, PLAYLIST_SONGS_PLAYLIST_ID_COLUMN + "=" + playlist_id + " AND " + PLAYLIST_SONGS__SONGS_ID_COLUMN + "=" + song_id, null);
        } catch (Exception e) {

        }

    }

    ///////////////////////////////////////////////////////////////////////drop tables/////////////////////////////////////////////////////////////////
    public static void dropTables(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM " + ARTISTS.ARTISTS_TABLE);
        } catch (Exception e) {

        }
        try {
            db.execSQL("DELETE FROM " + ALBUMS.ALBUMS_TABLE);
        } catch (Exception e) {

        }
        try {
            db.execSQL("DELETE FROM " + SONGS.SONGS_TABLE);
        } catch (Exception e) {

        }
        try {
            db.execSQL("DELETE FROM " + ARTISTSONGS.ARTISTSONGS_TABLE);
        } catch (Exception e) {

        }
        try {
            db.execSQL("DELETE FROM " + ALBUMSONGS.ALBUMSONGS_TABLE);
        } catch (Exception e) {

        }
//        try{
//        db.execSQL("DELETE FROM " + PLAYLIST.PLAYLIST_TABLE);
//    } catch (Exception e) {
//
//    }
    }
}
