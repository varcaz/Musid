package com.varcaz.musid.Adapters;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;

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

        Button playbutton=dialog.findViewById(R.id.playbutton_dialog_track);
        Button playnext=dialog.findViewById(R.id.playNextbutton_dialog_track);

        RelativeLayout relativeLayoutdialog = dialog.findViewById(R.id.dialog_relative_layout);
        relativeLayoutdialog.setAlpha(0.9f);


        trackRecyclerrHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getServiceInstance().setSelectedSong(tracksInfoList, trackRecyclerrHolder.getAdapterPosition());

            }
        });


        trackRecyclerrHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {                                                     //show dialog on long press
            @Override
            public boolean onLongClick(View v) {
                Log.i("the fuck", "the fuck");
                d_songName.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSongName());
                d_songAlbum.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getAlbum());
                d_songArtist.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getArtist());
                d_songComposer.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getComposer());
                d_songGenre.setText(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getGenre());
                d_songArt.setImageURI(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSong_art());
                if (d_songArt.getDrawable() == null)
                    d_songArt.setImageResource(R.drawable.default_music_art);

                dialog.show();

                if(dialog.isShowing()){
                    playbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MainActivity.getServiceInstance().setSelectedSong(tracksInfoList, trackRecyclerrHolder.getAdapterPosition());
                            dialog.dismiss();

                        }
                    });
                    playnext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                               int pos=trackRecyclerrHolder.getAdapterPosition();
                                        dialog.cancel();
                            while (MainActivity.getServiceInstance().mMediaPlayer.getCurrentPosition() == MainActivity.getServiceInstance().mMediaPlayer.getDuration()) {
                                        }
                            MainActivity.getServiceInstance().setSelectedSong(tracksInfoList, pos);





                        }
                    });


                }


                return true;

            }
        });


        return trackRecyclerrHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull TrackRecyclerrHolder holder, int position) {
//        new Thread(() -> {
            holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
            holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out));
//        }).start();
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



