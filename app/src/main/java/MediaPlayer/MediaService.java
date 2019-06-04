package MediaPlayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.Fragments.sliderFragment;
import com.varcaz.musid.R;

import java.io.IOException;
import java.util.ArrayList;

import MediaLoaders.MediaQueries;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {


    private static final String ACTION_PAUSE = "com.varcaz.musid.PAUSE";
    private static final String ACTION_STOP = "com.varcaz.musid.STOP";
    private static final String ACTION_NEXT = "com.varcaz.musid.NEXT";
    private static final String ACTION_PREVIOUS = "com.varcaz.musid.PREVIOUS";

    public static final int STATE_PAUSED = 1;
    public static final int STATE_PLAYING = 2;
    public int mState = 0;


    private static final int REQUEST_PAUSE = 101;
    private static final int REQUEST_PREVIOUS = 102;
    private static final int REQUEST_NEXT = 103;
    private static final int REQUEST_STOP = 104;


    private int SONG_POS;
    private int NOTIFICATION_ID = 1121;
    ArrayList<MediaInfo> mediaInfoArrayList;

    public MediaPlayer mediaPlayer;
    private IBinder mediaBinder = new PlayerBinder();
    Notification mNotification;
    Notification.Builder noticatoinBuilder;
    RemoteViews notificatoinView;
    RemoteViews emptyView;
    NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.i("ACTION", "Action " + action);
            if (!TextUtils.isEmpty(action)) {

                if (action.equals(ACTION_PREVIOUS))
                    playPrevious();
                else if (action.equalsIgnoreCase(ACTION_NEXT))
                    playNext();
                else if (action.equals(ACTION_PAUSE))
                    playPauseSong();
                else if (action.equalsIgnoreCase(ACTION_STOP))
                    stopSong();
                stopSelf();

            }
        }
        return START_STICKY;
    }

    public void stopSong() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
        System.exit(0);
    }

    public void playNext() {
        if (SONG_POS != mediaInfoArrayList.size()) {
            SONG_POS++;
            startSong(mediaInfoArrayList.get(SONG_POS).getSong_Uri(), mediaInfoArrayList.get(SONG_POS).getSongName());
        }

    }

    public void playPrevious() {
        if (SONG_POS != -1) {
            SONG_POS--;
            startSong(mediaInfoArrayList.get(SONG_POS).getSong_Uri(), mediaInfoArrayList.get(SONG_POS).getSongName());
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mState = STATE_PAUSED;
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        noticatoinBuilder = new Notification.Builder(getApplicationContext());

    }

    public class PlayerBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }

    }


    public void setSelectedSong(ArrayList<MediaInfo> mediaList, int positoin) {
        mediaInfoArrayList = mediaList;
        SONG_POS = positoin;
//        NOTIFICATION_ID=notificaion_id;
        Log.i("Service", "Service POS" + SONG_POS + " " + mediaList.get(positoin).getSong_Uri());
        startSong(mediaList.get(positoin).getSong_Uri(), mediaList.get(positoin).getSongName());

        showNotification();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mediaBinder;
    }

    void initMediaPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void playPauseSong() {
        if (mState == STATE_PLAYING) {
            mediaPlayer.start();
            mState = STATE_PAUSED;
            sliderFragment.getInstance().play.setVisibility(View.GONE);
            sliderFragment.getInstance().pause.setVisibility(View.VISIBLE);
            sliderFragment.getInstance().play_main.setVisibility(View.GONE);
            sliderFragment.getInstance().pause_main.setVisibility(View.VISIBLE);
            notificatoinView.setImageViewResource(R.id.not_pause_button, R.drawable.pause_button);
        } else {
            mediaPlayer.pause();
            mState = STATE_PLAYING;
            sliderFragment.getInstance().play.setVisibility(View.VISIBLE);
            sliderFragment.getInstance().pause.setVisibility(View.GONE);
            sliderFragment.getInstance().play_main.setVisibility(View.VISIBLE);
            sliderFragment.getInstance().pause_main.setVisibility(View.GONE);
            notificatoinView.setImageViewResource(R.id.not_pause_button, R.drawable.play_button);
        }
        generateNotifcation();


    }

    public void startSong(Uri songUri, String songName) {
        mediaPlayer.reset();
        mState = STATE_PLAYING;
        sliderFragment.getInstance().play.setVisibility(View.GONE);
        sliderFragment.getInstance().pause.setVisibility(View.VISIBLE);
        sliderFragment.getInstance().play_main.setVisibility(View.GONE);
        sliderFragment.getInstance().pause_main.setVisibility(View.VISIBLE);

        sliderFragment.getInstance().updateUi(mediaInfoArrayList.get(SONG_POS));
        try {
            if (songUri.toString().startsWith("content://"))
                mediaPlayer.setDataSource(getApplicationContext(), songUri);
            else
                mediaPlayer.setDataSource(songUri.toString());
        } catch (IOException e) {
            Log.i("mediaPlayer Exception", "uri exception " + e.getLocalizedMessage());
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.i("mediaPlayer Exception", "prepare exception " + e.getLocalizedMessage());
            e.printStackTrace();
            mediaPlayer.start();
        }
        mediaPlayer.start();
        sliderFragment.getInstance().seekBar.setMax(Integer.parseInt(mediaInfoArrayList.get(SONG_POS).getSong_duration()));
        new Thread(() -> {
            int secondsT = (int) (Math.floor((sliderFragment.getInstance().seekBar.getMax() / 1000) % 60));
            int minutesT = (int) (Math.floor((sliderFragment.getInstance().seekBar.getMax() / (1000 * 60)) % 60));
            sliderFragment.getInstance().tv_totalDuration.setText(minutesT + ":" + secondsT);


        }).start();


        showNotification();
        new Thread(() -> {

            sliderFragment.getInstance().seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                    seekBar.setProgress(progress);

                    int secondsE = (int) (Math.floor((progress / 1000) % 60));
                    int minutesE = (int) (Math.floor((progress / (1000 * 60)) % 60));
                    sliderFragment.getInstance().tv_elapsedTime.setText(minutesE + ":" + secondsE);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                    mediaPlayer.seekTo(seekBar.getProgress());
                    seekBar.setProgress(seekBar.getProgress());

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());

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
//
//        new Thread(() -> {
//            int totalDuration = Integer.parseInt(mediaInfoArrayList.get(SONG_POS).getSong_duration());
//            sliderFragment.getInstance().seekBar.setMax(totalDuration);
//            int currentPosition = 0;
//            while (currentPosition < totalDuration) {                                                                                   //its on a seperate thread may be this is causing the problem
//                currentPosition = mediaPlayer.getCurrentPosition();
//                sliderFragment.getInstance().seekBar.setProgress(currentPosition);
//
//            }
//
//
//        }).start();

    }

    public void showNotification() {
        Log.i("mediaPlayer Exception", "In notificatons");
        PendingIntent pendingIntent;
        Intent intent;

        notificatoinView = new RemoteViews(getPackageName(), R.layout.notification);
        notificatoinView.setImageViewUri(R.id.notificaton_album_art, mediaInfoArrayList.get(SONG_POS).getSong_art());
        notificatoinView.setTextViewText(R.id.notification_artist_name, mediaInfoArrayList.get(SONG_POS).getArtist());
        notificatoinView.setTextViewText(R.id.notification_song_name, mediaInfoArrayList.get(SONG_POS).getSongName());
        emptyView = new RemoteViews(getPackageName(), R.layout.empty_layout);
        intent = new Intent(ACTION_PAUSE);
        pendingIntent = PendingIntent.getService(getApplicationContext(), REQUEST_PAUSE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificatoinView.setOnClickPendingIntent(R.id.not_pause_button, pendingIntent);

        intent = new Intent(ACTION_STOP);
        pendingIntent = PendingIntent.getService(getApplicationContext(), REQUEST_STOP, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificatoinView.setOnClickPendingIntent(R.id.not_stop_btn, pendingIntent);

        intent = new Intent(ACTION_PREVIOUS);
        pendingIntent = PendingIntent.getService(getApplicationContext(), REQUEST_PREVIOUS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificatoinView.setOnClickPendingIntent(R.id.not_backward_button, pendingIntent);

        intent = new Intent(ACTION_NEXT);
        pendingIntent = PendingIntent.getService(getApplicationContext(), REQUEST_NEXT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificatoinView.setOnClickPendingIntent(R.id.not_forward_button, pendingIntent);
        notificatoinView.setImageViewResource(R.id.not_pause_button, R.drawable.pause_button);


        generateNotifcation();
    }

    void generateNotifcation() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String id = "my_channel_01";

            CharSequence name = getString(R.string.channel_name);

            String description = getString(R.string.chanel_description);

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setSound(null, null);
            mChannel.setVibrationPattern(new long[0]);
            mChannel.setDescription(description);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotification = noticatoinBuilder.setSmallIcon(R.drawable.ic_music_note_black_24dp).setOngoing(true).setChannelId(id)
                    .setWhen(System.currentTimeMillis()).setCustomHeadsUpContentView(emptyView)
                    .setCustomBigContentView(notificatoinView).build();

            notificationManager.createNotificationChannel(mChannel);
        }


//    startForeground(NOTIFICATION_ID,mNotification);
        notificationManager.notify(NOTIFICATION_ID, mNotification);
//        nc.setSmallIcon(R.mipmap.ic_launcher).setOngoing(true).setCustomBigContentView(notificatoinView);

    }

    public MediaInfo getCurrentSong() {
        return mediaInfoArrayList.get(SONG_POS);
    }


}
