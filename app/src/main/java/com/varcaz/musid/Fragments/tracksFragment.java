package com.varcaz.musid.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varcaz.musid.R;
import com.varcaz.musid.Adapters.TrackRecycleAdapter;
import com.varcaz.musid.EntityClass.MediaInfo;

import java.util.ArrayList;
import java.util.List;

import MediaLoaders.MediaQueries;


public class tracksFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<MediaInfo> mediaInfoList;
    Thread SongsLoadingThread;

    public tracksFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SongsLoadingThread = new Thread(() -> mediaInfoList = MediaQueries.QuerySongs(getContext()));
        SongsLoadingThread.start();
        view = inflater.inflate(R.layout.fragment_tracks, container, false);
        recyclerView = view.findViewById(R.id.recycle_tracks);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#16ffffff")));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            SongsLoadingThread.join();
        } catch (Exception e) {

        }

        TrackRecycleAdapter trackRecycleAdapter = new TrackRecycleAdapter(getContext(), mediaInfoList);
        recyclerView.setAdapter(trackRecycleAdapter);

        return view;
    }
}