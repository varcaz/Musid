package com.varcaz.musid.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.Adapters.ArtistRecycleAdapter;
import com.varcaz.musid.EntityClass.ArtistsInfo;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;
import java.util.List;

import Interfaces.itemClickListener;
import MediaLoaders.DataSource;
import MediaLoaders.MediaQueries;


public class artistsFragment extends Fragment {
    View view;
    List<ArtistsInfo> artistsInfoList;
    private RecyclerView recyclerView;

    public artistsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread ArtistLoadingThread = new Thread(() -> artistsInfoList = MediaQueries.QueryArtists(getContext()));
        ArtistLoadingThread.start();

        String table = DataSource.SONGS.SONGS_TABLE + "." + DataSource.SONGS.SONG_ARTIST_ID_COLUMN;
        view = inflater.inflate(R.layout.fragment_artists, container, false);
        recyclerView = view.findViewById(R.id.artist_recycle_view);
        itemClickListener itemClickListener = new itemClickListener() {
            @Override
            public void onItemClickListener(int id, String title, Uri art) {
                ((MainActivity) getActivity()).onClickFragmentReplacer(id, table, title, art);
            }
        };

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#16ffffff")));
        recyclerView.addItemDecoration(dividerItemDecoration);
        try {
            ArtistLoadingThread.join();
        } catch (Exception e) {

        }
        recyclerView.setAdapter(new ArtistRecycleAdapter(artistsInfoList, getContext(), itemClickListener));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemViewCacheSize(20);

        return view;
    }

}