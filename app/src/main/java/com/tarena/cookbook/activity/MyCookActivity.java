package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.ShareFoodAdapter;
import com.tarena.cookbook.entity.Message;
import com.tarena.cookbook.entity.MyUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyCookActivity extends AppCompatActivity {

    @BindView(R.id.tv_mc_back)
    TextView tvBack;
    @BindView(R.id.lv_my_cook)
    ListView lvCook;

    private ShareFoodAdapter adapter;
    private List<Message> messages;
    private MyUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cook);
        ButterKnife.bind(this);

        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        currentUser = BmobUser.getCurrentUser(MyUser.class);
        loadmessages();
    }

    private void loadmessages() {
        BmobQuery<Message> bq = new BmobQuery<>();
        //让查询结果中包含user字段详情
        bq.include("user");
        bq.addWhereEqualTo("user",currentUser);
        bq.order("-createdAt");
        bq.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    messages = list;
                    adapter = new ShareFoodAdapter(MyCookActivity.this, list);
                    lvCook.setAdapter(adapter);
                }
            }
        });
    }

    private void setListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
            }
        });

        lvCook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCookActivity.this, MessageDetailActivity.class);
                intent.putExtra("msg",messages.get(position));
                startActivity(intent);
            }
        });
    }
}
