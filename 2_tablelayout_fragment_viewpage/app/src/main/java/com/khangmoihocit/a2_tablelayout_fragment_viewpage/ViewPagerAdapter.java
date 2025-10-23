package com.khangmoihocit.a2_tablelayout_fragment_viewpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new MyPageFragment();
            default:
                return new HomeFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Home"; break;
            case 1:
                title = "Favorite"; break;
            case 2:
                title = "My page"; break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 3; //số lượng tab
    }


}
