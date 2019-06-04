package com.varcaz.musid.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.Fragments.sliderFragment;
import com.varcaz.musid.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import Interfaces.songClickListener;

public class TrackRecycleAdapter extends RecyclerView.Adapter<TrackRecycleAdapter.TrackRecyclerrHolder> {

    Context context;
    ArrayList<MediaInfo> tracksInfoList;
    songClickListener songClickListener;

    public TrackRecycleAdapter(Context context, ArrayList<MediaInfo> mediaInfoList) {
        this.context = context;
        this.tracksInfoList = mediaInfoList;
//        this.songClickListener=songClickListener;
    }

    @NonNull
    @Override
    public TrackRecyclerrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.track_item, parent, false);
        final TrackRecyclerrHolder trackRecyclerrHolder = new TrackRecyclerrHolder(view);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_track);
        dialog.getWindow().setWindowAnimations(R.style.zoomTransition);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));                                                                                      //custom dialog declaration
        TextView d_songName = dialog.findViewById(R.id.dialog_song_name);
        TextView d_songArtist = dialog.findViewById(R.id.dialog_song_artist);
        TextView d_songAlbum = dialog.findViewById(R.id.dialog_song_album);
        TextView d_songGenre = dialog.findViewById(R.id.dialog_song_genre);
        TextView d_songComposer = dialog.findViewById(R.id.dialog_song_composer);
        ImageView d_songArt = dialog.findViewById(R.id.dialog_song_art);

//        RelativeLayout relativeLayoutdialog=dialog.findViewById(R.id.dialog_relative_layout);
//        relativeLayoutdialog.setAlpha(0.8f);


        trackRecyclerrHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, (trackRecyclerrHolder.getAdapterPosition()) + " track named " +
                        tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSongName() + " clicked", Toast.LENGTH_SHORT).show();   //track clicked toast


                MainActivity.getServiceInstance().setSelectedSong(tracksInfoList, trackRecyclerrHolder.getAdapterPosition());

            }
        });


        trackRecyclerrHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {                                                     //show dialog on long press
            @Override
            public boolean onLongClick(View v) {

                d_songName.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSongName());
                d_songAlbum.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getAlbum());
                d_songArtist.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getArtist());
                d_songComposer.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getComposer());
                d_songGenre.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getGenre());
                d_songArt.setImageURI(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSong_art());
                if (d_songArt.getDrawable() == null)
                    d_songArt.setImageResource(R.drawable.default_music_art);

                dialog.show();
                return true;

            }
        });


        return trackRecyclerrHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull TrackRecyclerrHolder holder, int position) {
        new Thread(() -> {
            holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
            holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
        }).start();
        holder.tv_songName.setText(tracksInfoList.get(position).getSongName());
        holder.tv_artistName.setText(tracksInfoList.get(position).getArtist());
        holder.iv_art.setImageURI(tracksInfoList.get(position).getSong_art());
        if (holder.iv_art.getDrawable() == null)
            holder.iv_art.setImageResource(R.drawable.default_music_art);


    }

    @Override
    public int getItemCount() {
        return tracksInfoList.size();
    }

    public static class TrackRecyclerrHolder extends RecyclerView.ViewHolder {
        private TextView tv_songName;
        private TextView tv_artistName;
        private ImageView iv_art;
        private LinearLayout linearLayout;

        public TrackRecyclerrHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.single_track);
            tv_artistName = itemView.findViewById(R.id.track_artist_name);
            tv_songName = itemView.findViewById(R.id.track_song_name);
            iv_art = itemView.findViewById(R.id.track_song_art);
        }
    }
}



