package com.varcaz.musid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;

import Interfaces.selectedSongsPasserInterface;

public class AddSongsAdapter extends RecyclerView.Adapter<AddSongsAdapter.TrackRecyclerrHolder> {
    static selectedSongsPasserInterface selectedSongsPasserInterface;
    Context context;
    ArrayList<MediaInfo> tracksInfoList;

    public AddSongsAdapter(Context context, ArrayList<MediaInfo> mediaInfoList, selectedSongsPasserInterface selectedsongs) {
        this.context = context;
        this.tracksInfoList = mediaInfoList;

        selectedSongsPasserInterface = selectedsongs;
//        this.songClickListener=songClickListener;
    }

    public static void initInterface(selectedSongsPasserInterface selectedsongs) {
    }

    ArrayList<Integer> idsToAdd;

    @NonNull
    @Override
    public TrackRecyclerrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_track_item, parent, false);
        final TrackRecyclerrHolder trackRecyclerrHolder = new TrackRecyclerrHolder(view);

        idsToAdd = new ArrayList<>();
        trackRecyclerrHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackRecyclerrHolder.checkButton.isChecked()) {
                    trackRecyclerrHolder.checkButton.setChecked(false);
                    try {
                        idsToAdd.remove(idsToAdd.indexOf(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSong_id()));
                    } catch (Exception e) {
                    }
                } else {
                    trackRecyclerrHolder.checkButton.setChecked(true);
                    idsToAdd.add(tracksInfoList.get(trackRecyclerrHolder.getAdapterPosition()).getSong_id());
                }
                selectedSongsPasserInterface.selectedSongsPasser(idsToAdd);
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
        holder.checkButton.setChecked(false);
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
        RadioButton checkButton;

        public TrackRecyclerrHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.single_track);
            tv_artistName = itemView.findViewById(R.id.track_artist_name);
            tv_songName = itemView.findViewById(R.id.track_song_name);
            iv_art = itemView.findViewById(R.id.track_song_art);
            checkButton = itemView.findViewById(R.id.radio_add_song);

        }
    }
}



