package com.tarena.cookbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.MyFragmentPagerAdapter;
import com.tarena.cookbook.fragment.MenuFragment;
import com.tarena.cookbook.fragment.MoreFragment;
import com.tarena.cookbook.fragment.RecommendFragment;
import com.tarena.cookbook.fragment.ShareFragment;
import com.tarena.cookbook.view.MyViewPager;

public class MainActivity extends AppCompatActivity {

    private MyViewPager vpContainer;
    private RadioGroup bottomMenu;
    private MyFragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialFragments();
        setListener();
    }

    private void initialFragments() {
        vpContainer = findViewById(R.id.vp_container);
        bottomMenu = findViewById(R.id.rg_cookBook_menu);

        RecommendFragment recommendFragment = new RecommendFragment();
        MenuFragment menuFragment = new MenuFragment();
        ShareFragment shareFragment = new ShareFragment();
        MoreFragment moreFragment = new MoreFragment();

        fragmentAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(recommendFragment);
        fragmentAdapter.addFragment(menuFragment);
        fragmentAdapter.addFragment(shareFragment);
        fragmentAdapter.addFragment(moreFragment);
        vpContainer.setAdapter(fragmentAdapter);

        //将首页的fragment设置为默认的fragment
        vpContainer.setCurrentItem(0, false);

    }

    private void setListener() {
        bottomMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_food:
                        vpContainer.setCurrentItem(0, false);
                        break;
                    case R.id.rb_classify:
                        vpContainer.setCurrentItem(1, false);
                        break;
                    case R.id.rb_share:
                        vpContainer.setCurrentItem(2, false);
                        break;
                    case R.id.rb_more:
                        vpContainer.setCurrentItem(3, false);
                        break;
                }
            }
        });
    }

}
