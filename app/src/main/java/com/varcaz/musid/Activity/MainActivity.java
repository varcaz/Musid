package com.varcaz.musid.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.varcaz.musid.Fragments.SearchFragment;
import com.varcaz.musid.Fragments.onClickItemFragment;
import com.varcaz.musid.Fragments.mainFragment;
import com.varcaz.musid.Fragments.sliderFragment;
import com.varcaz.musid.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import MediaLoaders.MediaQueries;
import MediaPlayer.MediaService;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SlidingUpPanelLayout slidinglayout;
    private static MediaService mediaService;
    public Button bt_backgroundChanger;
    private Fragment mainFragment;
    private Intent serviceIntent;
    private final String mainActivityPreferences = "mainActivityPreferences";
    private final String backgroundIndexTAG = "backgroundIndexTAG";
    public int STORAGE_READ_CODE = 1;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceIntent = new Intent(this, MediaService.class);
        bindService(serviceIntent, mediaConnection, Context.BIND_AUTO_CREATE);
        startService(serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(mainActivityPreferences, MODE_PRIVATE);                  //shared preference background index retrieval
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_main);
        backgroundIndex = sharedPreferences.getInt("backgroundIndex", 1);
        mainFragment = new mainFragment();
        slidinglayout = findViewById(R.id.activity_main);


        int permissionReadCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadCheck != PackageManager.PERMISSION_GRANTED && permissionWriteCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_READ_CODE);
        } else {
            loadEverything();
        }

        new Thread(() ->
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,                                                 //background extension to status bar
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        ).start();


        bt_backgroundChanger = findViewById(R.id.background_change_button);
        bt_backgroundChanger.setEnabled(true);
        bt_backgroundChanger.setVisibility(View.VISIBLE);


        //background loading
        new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getWindow().getDecorView().setBackground(null);
            }
            int id = getApplication().getResources().getIdentifier("background" + sharedPreferences.getInt(backgroundIndexTAG, 4), "drawable", getApplicationContext().getPackageName());
            getWindow().getDecorView().setBackgroundResource(id);
        }).start();
        new Thread(() -> changeBackground()).start();//thread to change background

        new Thread(() -> {
            FragmentTransaction sliderTransaction = getSupportFragmentManager().beginTransaction();                               //slider thread
            sliderTransaction.add(R.id.slider_container, sliderFragment.getInstance()).commit();
        }).start();
        new Thread(() -> {

            slidinglayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {


                }

                @Override
                public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                    if (newState == PanelState.DRAGGING || newState == PanelState.EXPANDED) {
                        panel.findViewById(R.id.nowplaying_linearlayout).setVisibility(View.GONE);
                        panel.findViewById(R.id.tv_now_playing).setVisibility(View.VISIBLE);
                    } else {
                        panel.findViewById(R.id.tv_now_playing).setVisibility(View.INVISIBLE);
                        panel.findViewById(R.id.nowplaying_linearlayout).setVisibility(View.VISIBLE);
                    }
                }
            });


        }).start();

//        Handler handler=new Handler();
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mediaService != null && mediaService.mState == mediaService.STATE_PLAYING) {
//                    Log.i("Run", "RUN HANDLER ");
//                    int currentPos = mediaService.mediaPlayer.getCurrentPosition();
//                    sliderFragment.getInstance().seekBar.setProgress(currentPos);
//                    handler.postDelayed(this, 500);
//                }
//            }
//        });

        Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mediaService != null && mediaService.mediaPlayer != null) {
                    int mCurrentPosition = mediaService.mediaPlayer.getCurrentPosition();
                    sliderFragment.getInstance().seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }


    private ServiceConnection mediaConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.PlayerBinder binder = (MediaService.PlayerBinder) service;
            mediaService = binder.getService();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onClickFragmentReplacer(int id, String table, String title, Uri art) {
        bt_backgroundChanger.setEnabled(false);
        bt_backgroundChanger.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment frag = new onClickItemFragment();
        fragmentTransaction.replace(R.id.main_fragment_container, frag);
        Bundle bundle = new Bundle();
        bundle.putString("table", table);
        bundle.putInt("id", id);
        bundle.putString("title", title);
        bundle.putString("art", art.toString());
        frag.setArguments(bundle);


        fragmentTransaction.commit();

    }

    public void addSearchFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(R.id.main_fragment_container, new SearchFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_READ_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadEverything();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt(backgroundIndexTAG, backgroundIndex).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mediaConnection);
        editor.putInt("backgroundIndex", backgroundIndex).commit();
    }

    @Override
    public void onBackPressed() {

        if (mainFragment == null || !mainFragment.isVisible()) {
            bt_backgroundChanger.setEnabled(true);
            bt_backgroundChanger.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, mainFragment).commit();
            return;
        }

        if (slidinglayout != null &&
                (slidinglayout.getPanelState() == PanelState.EXPANDED || slidinglayout.getPanelState() == PanelState.ANCHORED)) {
            slidinglayout.setPanelState(PanelState.COLLAPSED);

        } else {
            super.onBackPressed();
        }


    }

    private static int backgroundIndex;

    private int i = 0;

    void changeBackground() {
        bt_backgroundChanger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };
                if (i == 1)
                    handler.postDelayed(runnable, 350);
                else if (i == 2) {
                    i = 0;
                    if (backgroundIndex == 15)
                        backgroundIndex = 1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getWindow().getDecorView().setBackground(null);
                    }
                    int id = getApplication().getResources().getIdentifier("background" + backgroundIndex, "drawable", getApplicationContext().getPackageName());
                    getWindow().getDecorView().setBackgroundResource(id);
                    backgroundIndex++;
                }
            }
        });
    }

    void loadEverything() {
//        MediaQueries.LoadAllSongs(getApplicationContext());
        getSupportLoaderManager().initLoader(1, null, this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();                                 //mainFragment loading
        fragmentTransaction.replace(R.id.main_fragment_container, mainFragment).commit();

    }


    public static MediaService getServiceInstance() {
        return mediaService;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Uri storageUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String[] projection = {"*"};
        return new CursorLoader(this, storageUri, projection, selection, null, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        MediaQueries.LoadAllSongs(getApplicationContext(), data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
