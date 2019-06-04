package com.varcaz.musid.Adapters;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.varcaz.musid.EntityClass.PlaylistInfo;

import Interfaces.itemClickListener;
import MediaLoaders.MediaQueries;

import com.varcaz.musid.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistRecycleAdapter extends RecyclerView.Adapter<PlaylistRecycleAdapter.PlaylistRecylcleHolder> {  //another attribute probably of playlistInfo class shall be added;
    List<PlaylistInfo> playlistInfoList;

    Context context;
    private itemClickListener itemClick;

    public PlaylistRecycleAdapter(List<PlaylistInfo> playlistInfoList, Context context, itemClickListener listener) {

        this.playlistInfoList = playlistInfoList;
        this.context = context;
        itemClick = listener;
        Log.i("Playlist", "playlist ok " + playlistInfoList.get(0).getPlatlistName());
    }

    @NonNull
    @Override
    public PlaylistRecylcleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item, parent, false);
        final PlaylistRecylcleHolder playlistRecylcleHolder = new PlaylistRecylcleHolder(view);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_playlist);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));                                                                                      //custom dialog declaration
        TextView d_songName = dialog.findViewById(R.id.dialog_playlist_name);
        TextView d_song_count = dialog.findViewById(R.id.tv_playlist_song_count);
        Button deletePlaylist = dialog.findViewById(R.id.delete_playlist_btn);
        Button addSongsbtn = dialog.findViewById(R.id.delete_playlist_btn);
        Button playallbtn = dialog.findViewById(R.id.delete_playlist_btn);
        playlistRecylcleHolder.singlePlaylist.setOnClickListener((v) -> {
            itemClick.onItemClickListener(playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlaylist_ID(), playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlatlistName()
                    , playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlaylistArt());
        });

        playlistRecylcleHolder.singlePlaylist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                d_songName.setText(playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlatlistName());
                d_song_count.setText(String.valueOf(playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlaylistSongCount()));
                dialog.show();
                if (dialog.isShowing()) {
                    deletePlaylist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(context).setTitle("Delete Playlist").setMessage("Are You sure you want to delete playlist : " +
                                    playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlatlistName() + " ?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MediaQueries.deletePlaylist(context, playlistInfoList.get(playlistRecylcleHolder.getAdapterPosition()).getPlaylist_ID());
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setIcon(R.drawable.ic_delete_black_24dp).show();
                        }
                    });


                }
                return true;
            }
        });

        return playlistRecylcleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistRecylcleHolder holder, int position) {

        new Thread(() -> {
            holder.iv_playlist_art.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            holder.singlePlaylist.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
        }).start();
        holder.iv_playlist_art.setImageURI(playlistInfoList.get(position).getPlaylistArt());
        holder.tv_playlist_name.setText(playlistInfoList.get(position).getPlatlistName());
        holder.tv_songs_count.setText(String.valueOf(playlistInfoList.get(position).getPlaylistSongCount()));
        if (holder.iv_playlist_art.getDrawable() == null)
            holder.iv_playlist_art.setImageResource(R.drawable.default_music_art);
    }

    @Override
    public int getItemCount() {
        return playlistInfoList.size();
    }

    public static class PlaylistRecylcleHolder extends RecyclerView.ViewHolder {
        private TextView tv_playlist_name;
        private TextView tv_songs_count;
        private ImageView iv_playlist_art;
        private LinearLayout singlePlaylist;

        public PlaylistRecylcleHolder(@NonNull View itemView) {
            super(itemView);
            singlePlaylist = itemView.findViewById(R.id.single_playlist);
            tv_playlist_name = itemView.findViewById(R.id.tv_playlist_name);
            tv_songs_count = itemView.findViewById(R.id.tv_playlist_song_count);
            iv_playlist_art = itemView.findViewById(R.id.iv_playlist_art);


        }
    }
}