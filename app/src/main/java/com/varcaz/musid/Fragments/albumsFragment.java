package com.varcaz.musid.Fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.Adapters.AlbumRecycleAdapter;
import com.varcaz.musid.EntityClass.AlbumInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;

import Interfaces.itemClickListener;
import MediaLoaders.DataSource;
import MediaLoaders.MediaQueries;

public class albumsFragment extends Fragment {
    View view;
    ArrayList<AlbumInfo> albumInfoList;
    RecyclerView recyclerView;
    Thread AlbumLoadingThread;

    public albumsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumInfoList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String table = DataSource.SONGS.SONGS_TABLE + "." + DataSource.SONGS.SONG_ALBUM_ID_COLUMN;
        AlbumLoadingThread = new Thread(() -> albumInfoList = MediaQueries.QueryAlbums(getContext()));
        AlbumLoadingThread.start();
        view = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerView = view.findViewById(R.id.album_recycle_view);
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
            AlbumLoadingThread.join();
        } catch (Exception e) {

        }
        recyclerView.setAdapter(new AlbumRecycleAdapter(albumInfoList, getContext(), itemClickListener));
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        return view;
    }

}