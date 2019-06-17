package com.varcaz.musid.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.varcaz.musid.EntityClass.ArtistsInfo;
import com.varcaz.musid.R;

import java.util.List;

import Interfaces.itemClickListener;

public class ArtistRecycleAdapter extends RecyclerView.Adapter<ArtistRecycleAdapter.ArtistRecycleHolder> {

    List<ArtistsInfo> artistsInfoList;
    Context context;
    itemClickListener itemClick;

    public ArtistRecycleAdapter(List<ArtistsInfo> artistsInfoList, Context context, itemClickListener listener) {
        this.artistsInfoList = artistsInfoList;
        this.context = context;
        itemClick = listener;
    }

    @NonNull
    @Override
    public ArtistRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artist_item, parent, false);
        final ArtistRecycleHolder artistRecycleHolder = new ArtistRecycleHolder(view);

        artistRecycleHolder.singleArtist.setOnClickListener((v) -> {
            itemClick.onItemClickListener(artistsInfoList.get(artistRecycleHolder.getAdapterPosition()).getArtistID(), artistsInfoList.get(artistRecycleHolder.getAdapterPosition()).getArtistName()
                    , artistsInfoList.get(artistRecycleHolder.getAdapterPosition()).getArtistArt());
        });

        return artistRecycleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRecycleHolder holder, int position) {
//        new Thread(() -> {
            holder.iv_artist_art.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            holder.singleArtist.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
//
//        }).start();
        holder.tv_artist_name.setText(artistsInfoList.get(position).getArtistName());
        holder.tv_album_count.setText(String.valueOf(artistsInfoList.get(position).getArtistAlbumCount()));
        holder.tv_song_count.setText(String.valueOf(artistsInfoList.get(position).getArtistSongCount()));
        holder.iv_artist_art.setImageURI(artistsInfoList.get(position).getArtistArt());

        if (holder.iv_artist_art.getDrawable() == null)
            holder.iv_artist_art.setImageResource(R.drawable.default_music_art);
    }

    @Override
    public int getItemCount() {
        return artistsInfoList.size();
    }

    public static class ArtistRecycleHolder extends RecyclerView.ViewHolder {
        private TextView tv_artist_name;
        private TextView tv_song_count;
        private TextView tv_album_count;
        private ImageView iv_artist_art;
        private RelativeLayout singleArtist;

        public ArtistRecycleHolder(@NonNull View itemView) {
            super(itemView);
            singleArtist = itemView.findViewById(R.id.single_artist);
            tv_artist_name = itemView.findViewById(R.id.tv_artist_item_name);
            tv_song_count = itemView.findViewById(R.id.tv_artists_songs_count);
            tv_album_count = itemView.findViewById(R.id.tv_artists_albums_count);
            iv_artist_art = itemView.findViewById(R.id.iv_artist_art);
        }
    }
}