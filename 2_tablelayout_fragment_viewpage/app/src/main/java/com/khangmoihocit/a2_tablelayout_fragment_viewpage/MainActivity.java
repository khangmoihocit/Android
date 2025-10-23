package com.khangmoihocit.a2_tablelayout_fragment_viewpage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPage = findViewById(R.id.view_page);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mViewPage.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPage);

    }
}