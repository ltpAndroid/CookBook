package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarena.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowCookeryActivity extends AppCompatActivity {

    @BindView(R.id.cookery_back)
    ImageView ivBack;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cookery);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String search = bundle.getString("title");
        tvSearch.setText(search);
    }

    @OnClick({R.id.cookery_back, R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cookery_back:
                finish();
                break;
            case R.id.ll_search:
                Intent intent = new Intent(ShowCookeryActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
