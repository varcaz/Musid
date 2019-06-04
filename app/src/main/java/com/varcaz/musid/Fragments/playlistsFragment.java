package com.varcaz.musid.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.Adapters.PlaylistRecycleAdapter;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.EntityClass.PlaylistInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;
import java.util.List;

import Interfaces.itemClickListener;
import MediaLoaders.DataSource;
import MediaLoaders.MediaQueries;


public class playlistsFragment extends Fragment {
    View view;
    List<PlaylistInfo> playlistInfoList;

    ImageButton addPlaylistButton;

    RecyclerView playlistRecylcerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistInfoList = new ArrayList<>();

        //getting data from the intent getExtras....  passed from database

    }


    public playlistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread PlaylistLoadingThread = new Thread(() -> playlistInfoList = MediaQueries.QueryPlaylists(getContext()));
        PlaylistLoadingThread.start();

        view = inflater.inflate(R.layout.fragment_playlists, container, false);
        playlistRecylcerView = view.findViewById(R.id.playlist_recycle_view);
        addPlaylistButton = view.findViewById(R.id.btn_add_playlist);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_playlist);
        EditText newPlaylistName = dialog.findViewById(R.id.new_playlist_name);
        Button okBtn = dialog.findViewById(R.id.new_playlist_ok_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.floatButtonAnimation);
        TextView tv_nameError = dialog.findViewById(R.id.error);
        tv_nameError.setVisibility(View.INVISIBLE);
        itemClickListener itemClickListener = new itemClickListener() {
            @Override
            public void onItemClickListener(int id, String title, Uri art) {
                ((MainActivity) getActivity()).onClickFragmentReplacer(id, "playlist", title, art);
            }
        };

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#16ffffff")));
        playlistRecylcerView.addItemDecoration(dividerItemDecoration);
        try {
            PlaylistLoadingThread.join();
        } catch (Exception e) {

        }

        Log.i("Playlist", "playlist ok frgmet " + playlistInfoList.get(0).getPlatlistName());
        playlistRecylcerView.setAdapter(new PlaylistRecycleAdapter(playlistInfoList, getContext(), itemClickListener));
        playlistRecylcerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = newPlaylistName.getText().toString().trim();
                        if (!newName.equalsIgnoreCase("")) {
                            boolean alreadyexists = false;
                            for (PlaylistInfo playlistInfo : playlistInfoList) {
                                if (newName.equalsIgnoreCase(playlistInfo.getPlatlistName())) {
                                    newPlaylistName.setText("");
                                    Handler handler = new Handler();

                                    Log.i("Playlist", "new Playlist Exists");
                                    Runnable r = () -> tv_nameError.setVisibility(View.INVISIBLE);

                                    tv_nameError.setVisibility(View.VISIBLE);
                                    handler.postDelayed(r, 2000);


                                    alreadyexists = true;
                                    break;
                                }
                            }

                            if (alreadyexists == false) {
                                MediaQueries.AddPlaylist(getContext(), newName);
                                playlistInfoList = MediaQueries.QueryPlaylists(getContext());
                                Log.i("Playlist", "new Playlist");
                                playlistRecylcerView.setAdapter(new PlaylistRecycleAdapter(playlistInfoList, getContext(), itemClickListener));
                                playlistRecylcerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                dialog.cancel();
                            }
                        }
                    }
                });


            }
        });

        return view;
    }

}