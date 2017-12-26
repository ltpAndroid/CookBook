package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.dataBase.CooksDBManager;
import com.tarena.cookbook.entity.ShowCookersInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectActivity extends AppCompatActivity {

    @BindView(R.id.collect_back)
    ImageView ivBack;
    @BindView(R.id.rv_collect)
    RecyclerView rvCollect;

    private CookItemAdapter adapter;
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);

        initData();
        setListener();
    }

    private void initData() {
        info = CooksDBManager.getCooksDBManager(CollectActivity.this).getData(false, true).getResult().getData();
        adapter = new CookItemAdapter(R.layout.item_cooks_list, info);
        rvCollect.setLayoutManager(new LinearLayoutManager(CollectActivity.this));
        rvCollect.setAdapter(adapter);
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CooksDBManager.getCooksDBManager(CollectActivity.this).setData(info.get(position));
                CooksDBManager.getCooksDBManager(CollectActivity.this).insertData(info.get(position));
                Intent intent = new Intent(CollectActivity.this, CookDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
