package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.tarena.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.img_main)
    ImageView imgMain;

    private static final int MSG_START_ANIM = 1;
    private static final int MSG_GOTO_MAIN = 2;
    private AlphaAnimation anim;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_ANIM:
                    imgMain.startAnimation(anim);
                    imgMain.setVisibility(View.VISIBLE);
                    break;
                case MSG_GOTO_MAIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.new_to_left, R.anim.old_to_left);
                    finish();
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        init();
        initAfter();

    }

    private void init() {
        anim = new AlphaAnimation(0, 1);
        anim.setDuration(2000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(MSG_GOTO_MAIN, 1000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initAfter() {
        handler.sendEmptyMessageDelayed(MSG_START_ANIM, 1000);
    }

}
