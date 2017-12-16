package com.tarena.cookbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.adapter.CookStepListAdapter;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.util.NetworkUtil;
import com.tarena.cookbook.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_back_cookery)
    ImageView ivBack;
    @BindView(R.id.tv_cook_title)
    TextView tvCookTitle;
    @BindView(R.id.header_img)
    ImageView headerImg;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_collect)
    CheckedTextView tvCollect;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.text_intro)
    TextView textIntro;
    @BindView(R.id.materials_layout)
    LinearLayout materialsLayout;
    @BindView(R.id.lv_steps)
    MyListView lvSteps;

    private CookStepListAdapter adapter;
    private int cookId;
    private List<ShowCookersInfo.Result.Data.Steps> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_details);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        cookId = getIntent().getIntExtra("id",0);
    }


    @OnClick({R.id.iv_back_cookery, R.id.tv_share, R.id.tv_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_cookery:
                finish();
                break;
            case R.id.tv_share:
                break;
            case R.id.tv_collect:
                break;
        }
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
                    Request request = new Request.Builder().url(NetworkUtil.getURL(cookId)).build();
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
//        ShowCookersInfo loadInfo = gson.fromJson(responseData, ShowCookersInfo.class);
//        info.addAll(loadInfo.getResult().getData().get(0).getSteps());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new CookStepListAdapter(info,CookDetailsActivity.this);
                lvSteps.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
