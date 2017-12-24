package com.tarena.cookbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tarena.cookbook.R;
import com.tarena.cookbook.activity.LoginActivity;
import com.tarena.cookbook.activity.MessageDetailActivity;
import com.tarena.cookbook.activity.SendMessageActivity;
import com.tarena.cookbook.adapter.ShareFoodAdapter;
import com.tarena.cookbook.entity.Message;
import com.tarena.cookbook.entity.MyUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class ShareFragment extends BaseFragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    Unbinder unbinder;
    @BindView(R.id.lv_messages)
    ListView lvMessages;
    @BindView(R.id.swip)
    SwipeRefreshLayout refresh;

    private ShareFoodAdapter adapter;
    private List<Message> messages;
    private MyUser currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_share, container, false);
        initialUI();
        unbinder = ButterKnife.bind(this, contentView);

        setListener();
        loadmessages();

        return contentView;
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
        bq.order("-createdAt");
        bq.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    messages = list;
                    adapter = new ShareFoodAdapter(getActivity(), list);
                    lvMessages.setAdapter(adapter);
                }
            }
        });
    }

    private void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser==null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                startActivity(intent);
            }
        });

        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("msg",messages.get(position));
                startActivity(intent);
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadmessages();
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void initialUI() {
        actionBar = contentView.findViewById(R.id.acitonBar_share);
        initialActionBar(0, "邻里厨房");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
