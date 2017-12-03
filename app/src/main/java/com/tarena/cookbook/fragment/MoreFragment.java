package com.tarena.cookbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.activity.LoginActivity;
import com.tarena.cookbook.activity.MainActivity;
import com.tarena.cookbook.activity.PersonalActivity;
import com.tarena.cookbook.entity.MyUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.iv_actionBar_left)
    ImageView ivSetting;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_username)
    TextView tvName;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.rl_my_setting)
    RelativeLayout rlMySetting;
    @BindView(R.id.btn_quit)
    Button btnQuit;

    MyUser currentUser;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        initialUI();


        return contentView;
    }

    @Override
    protected void initialUI() {
        actionBar = contentView.findViewById(R.id.acitonBar_more);
        initialActionBar(R.drawable.setting, "更多");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_actionBar_left, R.id.rl_my_setting, R.id.btn_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_actionBar_left:

                break;
            case R.id.rl_my_setting:
                if (currentUser != null) {
                    Intent intent = new Intent(getActivity(), PersonalActivity.class);
                    startActivity(intent);
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }

                break;
            case R.id.btn_quit:

                if (currentUser != null) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    MyUser.logOut();   //清除缓存用户对象 //现在的currentUser是null了
                } else {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        Log.i("TAG", "onResume: "+currentUser);
        loadMySetting();
    }

    private void loadMySetting() {
        if (currentUser == null) {
            tvName.setText("点击登录");
            tvSignature.setText("说点什么...");
            ivHeader.setImageResource(R.drawable.ic_my_default_head);
        } else {
            tvName.setText(currentUser.getNickname());
            tvSignature.setText(currentUser.getSignature());
            Picasso.with(getActivity()).load(currentUser.getHeadPath()).placeholder(R.drawable.default_headicon).error(R.drawable.ic_placeholder).into(ivHeader);
        }

    }
}
