package com.tarena.cookbook.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    public ViewPager getViewPager() {
        return vpContainer;
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

        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomMenu.check(R.id.rb_food);
                        break;
                    case 1:
                        bottomMenu.check(R.id.rb_classify);
                        break;
                    case 2:
                        bottomMenu.check(R.id.rb_share);
                        break;
                    case 3:
                        bottomMenu.check(R.id.rb_more);
                        break;
                }
            }
        });
    }


    /**
     * 上次按下BACK键的时间
     */
     private long lastTime;

    @Override
    /**
     * @param keyCode
     *            按下了并弹起的是哪个键
     * @param event
     *            操作事件对象
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 判断当前按下并弹起的是不是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
             // 记录当前弹起BACK键的时间t
             long t = System.currentTimeMillis();
             // 2次按下的时间差为1.5s以内，判定为连续按下
             if (t - lastTime < 1500) {
             finish();
             } else {
             Toast.makeText(this, "连续按2次退出程序！",
             Toast.LENGTH_SHORT).show();
             lastTime = t;
             }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
