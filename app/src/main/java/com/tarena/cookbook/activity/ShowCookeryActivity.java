package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.OkHttpDownloader;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.util.NetworkUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();
    private int cookId;
    private int pn = 0;
    private String title;
    private String search_key;
    private String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cookery);
        ButterKnife.bind(this);

        initData();
        setListener();

    }

    private void setListener() {
        lvCookery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowCookeryActivity.this,CookDetailsActivity.class);
                intent.putExtra("id",info.get(position).getId());
                startActivity(intent);
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCookeryActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String search = bundle.getString("title");
        tvSearch.setText(search);

        search_key = bundle.getString("search_key");
        cookId = bundle.getInt("id", 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendRequestsWithOkhttp();

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
                    Log.i("yeshen", responseData);
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
        Log.i(TAG, "loadInfo: "+loadInfo.toString());
        info.addAll(loadInfo.getResult().getData());
        Log.i(TAG, "info: "+info.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new CookItemAdapter(ShowCookeryActivity.this,info);
                lvCookery.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}

