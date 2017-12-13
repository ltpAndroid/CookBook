package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;

public class ShowCookeryActivity extends AppCompatActivity {

    @BindView(R.id.cookery_back)
    ImageView ivBack;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.lv_cookery)
    ListView lvCookery;

    private CookItemAdapter adapter;
    private ShowCookersInfo info;
    private int cookId;
    private int pn = 0;
    private String title;
    private String search_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cookery);
        ButterKnife.bind(this);

        initData();

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String search = bundle.getString("title");
        tvSearch.setText(search);

        search_key = bundle.getString("search_key");
        cookId = bundle.getInt("id", 0);
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
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequestsWithOkhttp();
        lvCookery.setAdapter(adapter);
    }

    private void sendRequestsWithOkhttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(NetworkUtil.getURL(cookId, search_key, pn, 10)).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseDataWithGson(responseData);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void parseDataWithGson(String responseData) {
        Gson gson = new Gson();
        ShowCookersInfo loadInfo = gson.fromJson(responseData, ShowCookersInfo.class);
        for (int i = 0; i < loadInfo.getResult().getData().size(); i++) {
            info.getResult().getData().add(loadInfo.getResult().getData().get(i));
            adapter = new CookItemAdapter(this, info);

        }
    }
}
