package com.tarena.cookbook.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarena.cookbook.R;

public class MessageActivity extends AppCompatActivity {

    private LinearLayout actionBar;
    private ImageView ivBack;
    private ImageView ivRight;
    private TextView tvActionbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initialUI();
        setListener();
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialUI() {
        ivBack = findViewById(R.id.iv_actionBar_left);
        ivBack.setImageResource(R.drawable.back);
        ivRight = findViewById(R.id.iv_actionBar_right);
        ivRight.setVisibility(View.INVISIBLE);
        tvActionbarTitle = findViewById(R.id.tv_actionBar_title);
        tvActionbarTitle.setText("消息通知");
    }
}
