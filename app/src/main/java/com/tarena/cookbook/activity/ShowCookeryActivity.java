package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.OkHttpDownloader;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.dataBase.CooksDBManager;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.util.NetworkUtil;
import com.tarena.cookbook.util.OkHttpUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
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
    @BindView(R.id.rv_cookery)
    RecyclerView rvCookery;

    private CookItemAdapter adapter;
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();
    private int cookId;
    private int pn = 0;
    private String title;
    private String search_key;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cookery);
        ButterKnife.bind(this);

        adapter = new CookItemAdapter(R.layout.item_cooks_list, info);
        rvCookery.setLayoutManager(new LinearLayoutManager(ShowCookeryActivity.this));
        rvCookery.setAdapter(adapter);

        initData();
        setListener();

    }

    private void setListener() {
        //        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //                Log.i("mycook", info.get(position).getAlbums().get(0));
        //                CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).setData(info.get((int) (position+1)));
        //                CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).insertData(info.get((int) (position+1)));
        //                Intent intent = new Intent(ShowCookeryActivity.this, CookDetailsActivity.class);
        //                startActivity(intent);
        //            }
        //        });


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
        cookId = bundle.getInt("id", 0);//cid

        //adapter = new CookItemAdapter(R.layout.item_cooks_list,)
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onFailure: ");

        requestHttpData();
        loadMoreData();
    }

    private void requestHttpData() {
        OkHttpUtil.sendOkHttpRequest(NetworkUtil.getURL(cookId, search_key, pn, 10), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseDataWithGson(responseData);
                //pn += 10;
            }
        });
    }

    private void loadMoreData() {
        Log.i(TAG, "loadMoreData: " + adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvCookery.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pn += 10;
                        requestHttpData();
                    }
                }, 2000);
            }
        }, rvCookery);
    }

    private void parseDataWithGson(final String responseData) {
        Gson gson = new Gson();
        ShowCookersInfo loadInfo = gson.fromJson(responseData, ShowCookersInfo.class);
        Log.i(TAG, "loadInfo: " + loadInfo.toString());
        Log.i(TAG, "loadInfo.getResult: "+loadInfo.getResult().toString());
        Log.i(TAG, "info: "+info.toString());
        info.addAll(loadInfo.getResult().getData());
        Log.i(TAG, "info: " + info.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.setNewData(info);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Log.i("mycook", info.get(position).getAlbums().get(0));
                        CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).setData(info.get(position));
                        CooksDBManager.getCooksDBManager(ShowCookeryActivity.this).insertData(info.get(position));
                        Intent intent = new Intent(ShowCookeryActivity.this, CookDetailsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}

