package MediaPlayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.Fragments.sliderFragment;
import com.varcaz.musid.R;

import java.io.IOException;
import java.util.ArrayList;

import MediaLoaders.MediaQueries;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "com.varcaz.musid.PLAY";
    public static final String ACTION_PAUSE = "com.varcaz.musid.PAUSE";
    public static final String ACTION_REWIND = "com.varcaz.musid.REWIND";
    public static final String ACTION_FAST_FORWARD = "com.varcaz.musid.FF";
    public static final String ACTION_NEXT = "com.varcaz.musid.NEXT";
    public static final String ACTION_PREVIOUS = "com.varcaz.musid.PREVIOUS";
    public static final String ACTION_STOP = "com.varcaz.musid.STOP";



    private int SONG_POS;
    private int NOTIFICATION_ID = 1121;
    static ArrayList<MediaInfo> mediaInfoArrayList;
    public MediaPlayer mMediaPlayer;
    public MediaSession mSession;
    NotificationChannel mChannel = null;

    Binder binder = new PlayerBinder();

    private void buildNotification(Notification.Action action) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        Bitmap largeIcon;
        try {
            largeIcon = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mediaInfoArrayList.get(SONG_POS).getSong_art());
        } catch (IOException e) {
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_music_art);
        }

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), "my_channel_01");
            Log.i("Inside notification", "inside notification");
        } else builder = new Notification.Builder(getApplicationContext());
        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));

        builder.setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setLargeIcon(largeIcon)
                .setContentTitle(mediaInfoArrayList.get(SONG_POS).getSongName())
                .setContentText(mediaInfoArrayList.get(SONG_POS).getArtist())
                .setDeleteIntent(pendingIntent)
                .setStyle(new Notification.MediaStyle().setMediaSession(mSession.getSessionToken()).setShowActionsInCompactView(0, 2, 4));


        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_PLAY)) {

            mSession.getController().getTransportControls().play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            mSession.getController().getTransportControls().pause();
        } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mSession.getController().getTransportControls().fastForward();
        } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
            mSession.getController().getTransportControls().rewind();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mSession.getController().getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mSession.getController().getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            mSession.getController().getTransportControls().stop();
        }
    }

    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "onCreate service");
        initMediaSessions();
        if (mMediaPlayer == null) {
            Log.i("errr", "mediaPlayer failed to initialize");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String id = "my_channel_01";

            CharSequence name = getString(R.string.channel_name);

            String description = getString(R.string.chanel_description);

            int importance = NotificationManager.IMPORTANCE_LOW;

            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setSound(null, null);
            mChannel.setVibrationPattern(new long[0]);
            mChannel.setDescription(description);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }
    }

    void startNext() {
        if (SONG_POS != mediaInfoArrayList.size() - 2)
            SONG_POS++;
        else
            SONG_POS = 0;
        startSong();
    }

    void initMediaPlayer() {
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.i("onCreate", "onStart service");
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {

        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        initMediaPlayer();
        mSession.setCallback(new MediaSession.Callback() {
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     mMediaPlayer.start();
                                     sliderFragment.getInstance().play.setVisibility(View.VISIBLE);
                                     sliderFragment.getInstance().pause.setVisibility(View.GONE);
                                     sliderFragment.getInstance().play_main.setVisibility(View.VISIBLE);
                                     sliderFragment.getInstance().pause_main.setVisibility(View.GONE);
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));


                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     mMediaPlayer.pause();
                                     sliderFragment.getInstance().play.setVisibility(View.GONE);
                                     sliderFragment.getInstance().pause.setVisibility(View.VISIBLE);
                                     sliderFragment.getInstance().play_main.setVisibility(View.GONE);
                                     sliderFragment.getInstance().pause_main.setVisibility(View.VISIBLE);
                                     Log.e("MediaPlayerService", "onPause");

                                     buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));

                                 }

                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();

                                     if (SONG_POS + 1 == mediaInfoArrayList.size())
                                         SONG_POS = 0;
                                     else
                                         SONG_POS++;
                                     startSong();
                                     Log.e("MediaPlayerService", "onSkipToNext" + mediaInfoArrayList.size() + "  " + mediaInfoArrayList.get(SONG_POS).getSongName());
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     Log.e("MediaPlayerService", "onSkipToPrevious");

                                     if (SONG_POS == 0)
                                         SONG_POS = mediaInfoArrayList.size() - 1;
                                     else
                                         SONG_POS--;
                                     startSong();
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onFastForward() {
                                     super.onFastForward();
                                     Log.e("MediaPlayerService", "onFastForward");
                                     mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 5000);
                                 }

                                 @Override
                                 public void onRewind() {
                                     super.onRewind();
                                     Log.e("MediaPlayerService", "onRewind");
                                     if (mMediaPlayer.getCurrentPosition() - 5000 > 0)
                                         mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 5000);
                                     else
                                         mMediaPlayer.seekTo(0);

                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e("MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.cancel(NOTIFICATION_ID);
                                     Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                                     stopService(intent);
                                 }

                                 @Override
                                 public void onSeekTo(long pos) {
                                     super.onSeekTo(pos);
                                 }

                                 @Override
                                 public void onSetRating(Rating rating) {
                                     super.onSetRating(rating);
                                 }
                             }
        );
    }

    public void startSong() {
        if (mMediaPlayer != null)
            mMediaPlayer.reset();

        sliderFragment.getInstance().play.setVisibility(View.GONE);
        sliderFragment.getInstance().pause.setVisibility(View.VISIBLE);
        sliderFragment.getInstance().play_main.setVisibility(View.GONE);
        sliderFragment.getInstance().pause_main.setVisibility(View.VISIBLE);

        sliderFragment.getInstance().updateUi(mediaInfoArrayList.get(SONG_POS));
        try {
            if (mMediaPlayer == null)
                Log.i("werr", "media weerr");

            Uri uri = mediaInfoArrayList.get(SONG_POS).getSong_Uri();
            if (uri.toString().startsWith("content://"))
                mMediaPlayer.setDataSource(getApplicationContext(), uri);
            else
                mMediaPlayer.setDataSource(uri.toString());
        } catch (IOException e) {
            Log.i("mediaPlayer Exception", "uri exception " + e.getLocalizedMessage());
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.i("mediaPlayer Exception", "prepare exception " + e.getLocalizedMessage());
            e.printStackTrace();
            mMediaPlayer.start();
        }
        mMediaPlayer.start();
        sliderFragment.getInstance().seekBar.setMax(Integer.parseInt(mediaInfoArrayList.get(SONG_POS).getSong_duration()));
        new Thread(() -> {
            int secondsT = (int) (Math.floor((sliderFragment.getInstance().seekBar.getMax() / 1000) % 60));
            int minutesT = (int) (Math.floor((sliderFragment.getInstance().seekBar.getMax() / (1000 * 60)) % 60));
            if (secondsT < 10)
                sliderFragment.getInstance().tv_totalDuration.setText(minutesT + ":" + 0 + secondsT);
            else
                sliderFragment.getInstance().tv_totalDuration.setText(minutesT + ":" + secondsT);


        }).start();

        new Thread(() -> {

            sliderFragment.getInstance().seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                    seekBar.setProgress(progress);

                    int secondsE = (int) (Math.floor((progress / 1000) % 60));
                    int minutesE = (int) (Math.floor((progress / (1000 * 60)) % 60));
                    if (secondsE < 10)
                        sliderFragment.getInstance().tv_elapsedTime.setText(minutesE + ":" + 0 + secondsE);
                    else
                        sliderFragment.getInstance().tv_elapsedTime.setText(minutesE + ":" + secondsE);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                    mMediaPlayer.seekTo(seekBar.getProgress());
                    seekBar.setProgress(seekBar.getProgress());

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mMediaPlayer.seekTo(seekBar.getProgress());

                }
            });
        }).start();

        new Thread(() -> {
            sliderFragment.getInstance().fav_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sliderFragment.getInstance().fav_check.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Added to Favorite playlist", Toast.LENGTH_SHORT).show();
                        MediaQueries.AddSongsToPlaylist(getApplicationContext(), 1, mediaInfoArrayList.get(SONG_POS).getSong_id());
                        Log.i("FAV", "Fav add " + mediaInfoArrayList.get(SONG_POS).getSong_id() + mediaInfoArrayList.get(SONG_POS).getSongName());
                    } else {
                        Toast.makeText(getApplicationContext(), "Removed from Favorite playlist", Toast.LENGTH_SHORT).show();
                        MediaQueries.RemoveSongsFromPlaylist(getApplicationContext(), 1, mediaInfoArrayList.get(SONG_POS).getSong_id());
                        Log.i("FAV", "Fav removed " + mediaInfoArrayList.get(SONG_POS).getSong_id() + mediaInfoArrayList.get(SONG_POS).getSongName());
                    }
                }
            });

        }).start();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }

    public void setSelectedSong(ArrayList<MediaInfo> mediaList, int positoin) {

        mediaInfoArrayList = mediaList;
        SONG_POS = positoin;
        Log.i("Service", "Service POS" + SONG_POS + " " + mediaList.get(positoin).getSong_Uri());
        startSong();
        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Log.i("errr", "mediaPlayer failed to initialize-errro");
        initMediaSessions();

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (SONG_POS != mediaInfoArrayList.size() - 1)
            SONG_POS++;
        else
            SONG_POS = 0;
        startSong();
    }

    public class PlayerBinder extends Binder {
        public MediaPlayerService getService() {

            return MediaPlayerService.this;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
