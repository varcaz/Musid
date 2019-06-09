package com.varcaz.musid.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.Adapters.AddSongsAdapter;
import com.varcaz.musid.Adapters.TrackRecycleAdapter;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;

import Interfaces.selectedSongsPasserInterface;
import MediaLoaders.MediaQueries;

public class onClickItemFragment extends Fragment {

    private TextView tv_title;
    private ImageView iv_image;
    private RecyclerView recyclerView;
    ArrayList<MediaInfo> mediaInfo;
    private ImageButton addSongBtn;
    String title;
    Uri art;
    int id;
    String table;
    AppBarLayout appbarLayout;
    ImageView backButton;
    ArrayList<MediaInfo> allSongslist;
    selectedSongsPasserInterface selectedSongsPasserInterface;

    ArrayList<Integer> songIDs = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSongslist = new ArrayList<>();
        new Thread(() -> {
            allSongslist = MediaQueries.QuerySongs(getContext());
        }).start();
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        art = Uri.parse(bundle.getString("art"));
        title = bundle.getString("title");
        table = bundle.getString("table");
        mediaInfo = MediaQueries.QueryOnClickSongs(getContext(), table, id);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_click_item, container, false);
        appbarLayout = view.findViewById(R.id.appbarLayout);
        tv_title = view.findViewById(R.id.title_onClick);
        iv_image = view.findViewById(R.id.art_onClick);
        recyclerView = view.findViewById(R.id.recycle_onClickFragment);
        tv_title.setText(title);
        addSongBtn = view.findViewById(R.id.addSong_onClickBtn);


        Log.i("meddai", "mediiaaa  " + table + " " + id);
        iv_image.setImageURI(art);
        if (iv_image.getDrawable() == null)
            iv_image.setImageResource(R.drawable.default_music_art);
        backButton = view.findViewById(R.id.back_onClick);
        TrackRecycleAdapter adapter = new TrackRecycleAdapter(getContext(), mediaInfo);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(20);
        new Thread(() -> {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#16ffffff")));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }).start();


        new Thread(() -> {
            appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                    float percentage = 1.3f - ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                    iv_image.setAlpha(percentage);


                }
            });
        }).start();
        new Thread(() -> {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });


        }).start();

        if (table.equalsIgnoreCase("playlist")) {
            addSongBtn.setVisibility(View.VISIBLE);
            new Thread(() -> {
                addSongBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Dialog selectSongDialog = new Dialog(getContext());
                        selectSongDialog.setContentView(R.layout.dialog_select_playlist_songs);
                        selectSongDialog.getWindow().setWindowAnimations(R.style.zoomTransition);
                        selectSongDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        RecyclerView recyclerView = selectSongDialog.findViewById(R.id.dialog_select_songs_recycle);
                        selectedSongsPasserInterface = new selectedSongsPasserInterface() {
                            @Override
                            public void selectedSongsPasser(ArrayList<Integer> songIds) {
                                songIDs = songIds;
                            }
                        };
                        recyclerView.setAdapter(new AddSongsAdapter(getContext(), allSongslist, selectedSongsPasserInterface));
                        TextView tv_nameError = selectSongDialog.findViewById(R.id.error);

                        Button addButton = selectSongDialog.findViewById(R.id.songs_add_btn);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        selectSongDialog.show();
                        if (selectSongDialog.isShowing()) {
                            addButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    AddSongsAdapter.initInterface(selectedSongsPasserInterface);

                                    boolean alreadyexists = false;
                                    for (int id : songIDs) {
                                        for (MediaInfo song : mediaInfo) {
                                            if (id == song.getSong_id()) {
                                                Handler handler = new Handler();
                                                Log.i("Playlist", "new Playlist Exists");
                                                Runnable r = () -> tv_nameError.setVisibility(View.INVISIBLE);
                                                tv_nameError.setVisibility(View.VISIBLE);
                                                handler.postDelayed(r, 2000);
                                                alreadyexists = true;
                                                break;
                                            }
                                        }
                                        if (alreadyexists)
                                            break;

                                    }


                                    if (alreadyexists == false) {
                                        for (int Songid : songIDs)
                                            MediaQueries.AddSongsToPlaylist(getContext(), id, Songid);
                                        TrackRecycleAdapter adapter = new TrackRecycleAdapter(getContext(), mediaInfo);
                                        Log.i("playlistsongs", "playlist song already false");
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setItemViewCacheSize(20);
                                        selectSongDialog.cancel();
                                    }

                                }

                            });


                        }
                    }
                });
            }).start();


        } else addSongBtn.setVisibility(View.INVISIBLE);

        return view;
    }
}