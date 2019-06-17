package com.varcaz.musid.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.varcaz.musid.Activity.MainActivity;
import com.varcaz.musid.Adapters.ViewPagerCustomAdapter;
import com.varcaz.musid.EntityClass.MediaInfo;
import com.varcaz.musid.R;

import java.util.ArrayList;
import java.util.List;


public class mainFragment extends Fragment {

    private ViewPager viewPager;
    List<String> fragmentTitles;
    private TabLayout tabLayout;
    ImageButton menuButton;

    private SharedPreferences viewPagerPreferences;
    private SharedPreferences.Editor editor;
    private final String mainFragmentPreferences = "mainFragmentPreferences";
    private final String restoreViewPagerIndexTAG = "RestoreViewPagerIndex";
    ImageButton search_reveal_btn;

    @Override
    public void onPause() {
        super.onPause();
        editor.putInt(restoreViewPagerIndexTAG, tabLayout.getSelectedTabPosition()).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.putInt(restoreViewPagerIndexTAG, tabLayout.getSelectedTabPosition()).commit();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        fragmentTitles = new ArrayList<>();

        fragmentTitles.add("Playlists");
        fragmentTitles.add("Tracks");
        fragmentTitles.add("Albums");
        fragmentTitles.add("Artists");
        super.onCreate(savedInstanceState);
        viewPagerPreferences = getContext().getSharedPreferences(mainFragmentPreferences, Context.MODE_PRIVATE);
        editor = viewPagerPreferences.edit();

    }

    private ArrayList<MediaInfo> loadMedia() {                                                      //    retrieve from database
        return null;
    }

    public mainFragment() {

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        menuButton = view.findViewById(R.id.menu_main_fragment);
        search_reveal_btn = view.findViewById(R.id.search_revealer);
        LinearLayout logoLayout = view.findViewById(R.id.logo_layout);
        LinearLayout searchLayout = view.findViewById(R.id.linearlayout_search);
        Button tabButton = view.findViewById(R.id.tab_button);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);


        search_reveal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addSearchFragment();


            }
        });


        ViewPagerCustomAdapter adapter = new ViewPagerCustomAdapter(getChildFragmentManager());

        adapter.AddFragment(new playlistsFragment(), "");
        adapter.AddFragment(new tracksFragment(), "");
        adapter.AddFragment(new albumsFragment(), "");
        adapter.AddFragment(new artistsFragment(), "");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        int continuatoinIndex = viewPagerPreferences.getInt(restoreViewPagerIndexTAG, 1);
        viewPager.setCurrentItem(continuatoinIndex);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


            tabButton.setText(fragmentTitles.get(continuatoinIndex));
            tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tabButton.setText(fragmentTitles.get(tab.getPosition()));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        return view;
    }


}