package com.myapp.app04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity_vp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);

        ViewPager vP =  findViewById(R.id.viewpager);
        MyPageAdapter pA = new MyPageAdapter(getSupportFragmentManager());
        vP.setAdapter(pA);

        TabLayout tL=(TabLayout) findViewById(R.id.sliding_tabs);
        tL.setupWithViewPager(vP);
    }
}