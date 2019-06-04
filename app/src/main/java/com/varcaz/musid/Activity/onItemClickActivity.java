//package com.varcaz.musid.Activity;
//
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import com.varcaz.musid.Adapters.TrackRecycleAdapter;
//import com.varcaz.musid.EntityClass.MediaInfo;
//import com.varcaz.musid.R;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class onItemClickActivity extends AppCompatActivity {
//    private TextView tv_title;
//    private ImageView iv_image;
//    private RecyclerView recyclerView;
//    List<MediaInfo> mediaInfo;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_on_item_click);
//        Toolbar toolbar = findViewById(R.id.onClick_toolbar);
//        setSupportActionBar(toolbar);
//        mediaInfo=new ArrayList<>();
////        getting data from the intent getExtras....  passed from database
//        mediaInfo.add(new MediaInfo("Treat You Better","Shawn Mendus","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Love me like you do","Bruce Lee","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Better than words","One Direction","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Faded","Alan Walker","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("F.R.I.E.N.D.S","unknown","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("AUG436436G","unknown","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Stitches","Shawn Mendus","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Zammil","Arabic","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Treat You Better","Shawn Mendus","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Love me like you do","Bruce Lee","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Better than words","One Direction","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Faded","Alan Walker","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("F.R.I.E.N.D.S","unknown","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("AUG436436G","unknown","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Stitches","Shawn Mendus","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        mediaInfo.add(new MediaInfo("Zammil","Arabic","Unknown","Classic",R.drawable.default_music_art,"unknown"));
//        tv_title=findViewById(R.id.onClickPage_Title);
//        iv_image=findViewById(R.id.onClickPage_Image);
//        recyclerView=findViewById(R.id.recycle_onClickActivity);
//
//        recyclerView.setAdapter(new TrackRecycleAdapter(this,mediaInfo));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#16ffffff")));
//        recyclerView.addItemDecoration(dividerItemDecoration);
//
//    }
//
//}
