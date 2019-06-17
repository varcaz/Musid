package com.varcaz.musid.Fragments;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.EntityClass.AlbumInfo;
import com.varcaz.musid.EntityClass.ArtistsInfo;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;
import com.google.android.material.tabs.TabLayout;
import com.varcaz.musid.Adapters.ViewPagerCustomAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.nio.channels.SeekableByteChannel;

import MediaLoaders.DataSource;
import MediaLoaders.MediaQueries;
import MediaPlayer.MediaPlayerService;

public class sliderFragment extends Fragment {
    LinearLayout nowplaying_linearbar;
    public ImageButton play, pause, play_main, pause_main, prev_btn, prev_main_btn, next_btn, next_main_btn;
    TextView tv_nowPlaying, tv_artist;
    TextView tv_main_nowPlaying, tv_artist_main;
    private static sliderFragment fragment;
    MediaInfo currentSong;
    RoundedImageView barSongArt, mainSongArt;
    public SeekBar seekBar;
    public TextView tv_elapsedTime, tv_totalDuration;
    public CheckBox fav_check, shuffle_check, loop_check;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static sliderFragment getInstance() {
        if (fragment == null)
            fragment = new sliderFragment();
        return fragment;
    }

    private sliderFragment() {

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.slider_now_playing, container, false);
        play = view.findViewById(R.id.not_play_button);
        pause = view.findViewById(R.id.not_pause_button);
        play_main = view.findViewById(R.id.play_button_main);
        pause_main = view.findViewById(R.id.pause_button_main);
        fav_check = view.findViewById(R.id.fav_check);
        shuffle_check = view.findViewById(R.id.shuffle_check);
        loop_check = view.findViewById(R.id.loop_check);
        nowplaying_linearbar = view.findViewById(R.id.nowplaying_linearlayout);
        tv_nowPlaying = view.findViewById(R.id.bar_song_now_playing);
        tv_main_nowPlaying = view.findViewById(R.id.main_song_now_playing);
        seekBar = view.findViewById(R.id.seekBar_main);
        tv_elapsedTime = view.findViewById(R.id.time_elapsed);
        tv_totalDuration = view.findViewById(R.id.total_duration);
        Log.i("In sliderFragment", " in slider Fragment " + tv_main_nowPlaying.getText());

        barSongArt = view.findViewById(R.id.album_art);
        mainSongArt = view.findViewById(R.id.album_art_main);
        prev_btn = view.findViewById(R.id.backward_button);
        prev_main_btn = view.findViewById(R.id.backward_button_main);
        next_btn = view.findViewById(R.id.forward_button);
        next_main_btn = view.findViewById(R.id.forward_button_main);
        tv_artist = view.findViewById(R.id.bar_artist);
        tv_artist_main = view.findViewById(R.id.bar_artist_main);

        new Thread(() -> {
            play.setOnClickListener(view -> {

                MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().play();

            });
        }).start();
        new Thread(() -> {
            next_main_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().skipToNext();
                }
            });

        }).start();

        new Thread(() -> {
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().skipToNext();
                }
            });

        }).start();
        new Thread(() -> {
            prev_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().skipToPrevious();
                }
            });

        }).start();

        new Thread(() -> {
            prev_main_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().skipToPrevious();
                }
            });

        }).start();


        new Thread(() -> {
            pause.setOnClickListener(view -> {
                MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().pause();
            });

        }).start();

        new Thread(() -> {
            play_main.setOnClickListener(view -> {

                MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().play();
            });
        }).start();

        new Thread(() -> {
            pause_main.setOnClickListener(view -> {

                MediaPlayerService.getServiceInstance().mSession.getController().getTransportControls().pause();
            });

        }).start();

        new Thread(() -> {
            shuffle_check.setOnClickListener(view -> {
                if (shuffle_check.isChecked()) {
                    Toast.makeText(getContext(), "Shuffling enabled", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Shuffling disabled", Toast.LENGTH_SHORT).show();
            });

        }).start();
        new Thread(() -> {

            loop_check.setOnClickListener(view -> {
                if (loop_check.isChecked()) {

                    Toast.makeText(getContext(), "looping enabled", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "looping disabled", Toast.LENGTH_SHORT).show();
            });

        });

        new Thread(() ->
                tv_nowPlaying.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate))).start();
        new Thread(() ->
                tv_main_nowPlaying.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate))).start();


        return view;

    }


    public void updateUi(MediaInfo currentSong) {

        Log.i("In sliderFragment", " in slider Fragment update " + currentSong.getSongName());
        tv_nowPlaying.setText(currentSong.getSongName());
        tv_main_nowPlaying.setText(currentSong.getSongName());
        tv_artist_main.setText(currentSong.getArtist());
        tv_artist.setText(currentSong.getArtist());
        barSongArt.setImageURI(currentSong.getSong_art());
        mainSongArt.setImageURI(currentSong.getSong_art());

    }


}


