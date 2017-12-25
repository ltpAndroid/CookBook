package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookItemAdapter;
import com.tarena.cookbook.dataBase.CooksDBManager;
import com.tarena.cookbook.entity.ShowCookersInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.history_back)
    ImageView ivBack;
    @BindView(R.id.lv_history)
    ListView lvHistory;

    private CookItemAdapter adapter;
    private List<ShowCookersInfo.Result.Data> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        initData();
        setListener();
    }

    private void initData() {
        info = CooksDBManager.getCooksDBManager(HistoryActivity.this).getData(true, false).getResult().getData();
        lvHistory.setAdapter(new CookItemAdapter(this,info));
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
            }
        });

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CooksDBManager.getCooksDBManager(HistoryActivity.this).setData(info.get((int) id));
                CooksDBManager.getCooksDBManager(HistoryActivity.this).insertData(info.get((int) id));
                Intent intent = new Intent(HistoryActivity.this, CookDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

}
