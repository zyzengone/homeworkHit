package com.example.zyzeng.homeworkhit.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.zyzeng.homeworkhit.R;
import com.example.zyzeng.homeworkhit.fragment.HomeFragment;
import com.example.zyzeng.homeworkhit.fragment.NotFragment;
import com.example.zyzeng.homeworkhit.fragment.OkFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends android.support.v4.app.FragmentActivity {
    private BottomNavigationView mBottomNavigationView;
    private int lastIndex;
    List<Fragment> mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomNavigation();
        initData();
    }
    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bv_bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_notok:
                        setFragmentPosition(0);
                        break;
                    case R.id.menu_ok:
                        setFragmentPosition(1);
                        break;
                    case R.id.menu_dead:
                        setFragmentPosition(2);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }
    public void initData(){
        mFragments = new ArrayList<>();
        mFragments.add(new NotFragment());
        mFragments.add(new OkFragment());
        mFragments.add(new HomeFragment());
        setFragmentPosition(0);
    }

    public void setFragmentPosition(int position){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if(!currentFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frameLayout,currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }
}
