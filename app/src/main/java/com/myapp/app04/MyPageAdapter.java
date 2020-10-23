package com.myapp.app04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPageAdapter extends FragmentPagerAdapter {
    public MyPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Title" + position;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new HomeFg();
        }else if(position==1){
            return new FuncFg();
        }else{
            return new SettingFg();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
