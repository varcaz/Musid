package com.varcaz.musid.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.varcaz.musid.EntityClass.AlbumInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;

import Interfaces.itemClickListener;

public class AlbumRecycleAdapter extends RecyclerView.Adapter<AlbumRecycleAdapter.AlbumRecycleHolder> {

    ArrayList<AlbumInfo> albumInfoList;
    Context context;

    private itemClickListener itemClick;

    public AlbumRecycleAdapter(ArrayList<AlbumInfo> albumInfoList, Context context, itemClickListener listener) {
        this.albumInfoList = albumInfoList;

        Log.i("album", "album adapter size " + albumInfoList.size());
        this.context = context;
        itemClick = listener;
    }

    @NonNull
    @Override
    public AlbumRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        final AlbumRecycleHolder albumRecycleHolder = new AlbumRecycleHolder(view);

        albumRecycleHolder.singleAlbum.setOnClickListener((v) -> {
            itemClick.onItemClickListener(albumInfoList.get(albumRecycleHolder.getAdapterPosition()).getAlbum_id(), albumInfoList.get(albumRecycleHolder.getAdapterPosition()).getAlbumName()
                    , albumInfoList.get(albumRecycleHolder.getAdapterPosition()).getAlbumArt());

        });


        return albumRecycleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRecycleHolder holder, int position) {
//        new Thread(() -> {

            holder.iv_album_art.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            holder.singleAlbum.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));

//        }).start();

        holder.tv_album_name.setText(albumInfoList.get(position).getAlbumName());
        holder.tv_artist_name.setText(String.valueOf(albumInfoList.get(position).getArtistName()));
        holder.tv_song_count.setText(String.valueOf(albumInfoList.get(position).getSongsCount()));
        holder.iv_album_art.setImageURI(albumInfoList.get(position).getAlbumArt());

        if (holder.iv_album_art.getDrawable() == null)
            holder.iv_album_art.setImageResource(R.drawable.default_music_art);
    }


    @Override
    public int getItemCount() {
        return albumInfoList.size();
    }

    public static class AlbumRecycleHolder extends RecyclerView.ViewHolder {
        private TextView tv_album_name;
        private TextView tv_song_count;
        private TextView tv_artist_name;
        private ImageView iv_album_art;
        private RelativeLayout singleAlbum;

        public AlbumRecycleHolder(@NonNull View itemView) {
            super(itemView);
            singleAlbum = itemView.findViewById(R.id.single_album);
            tv_album_name = itemView.findViewById(R.id.tv_album_item_name);
            tv_song_count = itemView.findViewById(R.id.tv__albums_song_count);
            tv_artist_name = itemView.findViewById(R.id.tv_album_artist_name);
            iv_album_art = itemView.findViewById(R.id.iv_album_art);
        }
    }
}