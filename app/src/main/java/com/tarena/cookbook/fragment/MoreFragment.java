package com.tarena.cookbook.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.activity.CollectActivity;
import com.tarena.cookbook.activity.FeedbackActivity;
import com.tarena.cookbook.activity.HistoryActivity;
import com.tarena.cookbook.activity.LoginActivity;
import com.tarena.cookbook.activity.MyCookActivity;
import com.tarena.cookbook.activity.PersonalActivity;
import com.tarena.cookbook.entity.MyUser;
import com.tarena.cookbook.util.CacheDataUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/15 0015.
 */

public class MoreFragment extends Fragment {

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_username)
    TextView tvName;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.rl_my_setting)
    RelativeLayout rlMySetting;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.ll_my_cook)
    LinearLayout llMyCook;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @BindView(R.id.ll_new)
    LinearLayout llNew;
    @BindView(R.id.ll_feedback)
    LinearLayout llFeedback;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    Unbinder unbinder;
    private MyUser currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_more, container, false);

        unbinder = ButterKnife.bind(this, contentView);
        initialUI();
        return contentView;
    }

    private void initialUI() {
        //显示缓存大小
        try {
            tvCacheSize.setText(CacheDataUtil.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({ R.id.rl_my_setting,
            R.id.btn_quit, R.id.ll_collect, R.id.ll_history,
            R.id.ll_clear_cache, R.id.ll_feedback, R.id.ll_new, R.id.ll_my_cook})
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
                showLogoutDialog();

                break;
            case R.id.ll_collect:
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_history:
                Intent it = new Intent(getActivity(), HistoryActivity.class);
                startActivity(it);
                break;
            case R.id.ll_my_cook:
                if (currentUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent mc = new Intent(getActivity(), MyCookActivity.class);
                startActivity(mc);
                break;
            case R.id.ll_clear_cache:
                showClearCacheDialog();
                break;
            case R.id.ll_feedback:
                Intent fb = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(fb);
                break;
            case R.id.ll_new:
                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showLogoutDialog() {
        if (currentUser != null) {
            AlertDialog.Builder quitDialog = new AlertDialog.Builder(getActivity());
            quitDialog.setMessage("是否退出登录?");
            quitDialog.setNegativeButton("取消", null);
            quitDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    MyUser.logOut();   //清除缓存用户对象 //现在的currentUser是null了
                }
            });
            quitDialog.create().show();
        } else {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void showClearCacheDialog() {
        AlertDialog.Builder cacheDialog = new AlertDialog.Builder(getActivity());
        cacheDialog.setMessage("是否确定清楚缓存?");
        cacheDialog.setNegativeButton("取消", null);
        cacheDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    CacheDataUtil.clearAllCache(getActivity());
                    tvCacheSize.setText(CacheDataUtil.getTotalCacheSize(getActivity()));
                    Toast.makeText(getActivity(), "缓存已清理完成", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cacheDialog.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        Log.i("TAG", "onResume: " + currentUser);
        loadMySetting();

        //        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                int scaleRatio = 0;
        //                String pattern = "5";
        //                if (TextUtils.isEmpty(pattern)) {
        //                    scaleRatio = 0;
        //                } else if (scaleRatio < 0) {
        //                    scaleRatio = 10;
        //                } else {
        //                    scaleRatio = Integer.parseInt(pattern);
        //                }
        //                //下面的这个方法必须在子线程中执行
        //                if (currentUser != null) {
        //                    final Bitmap blurBitmap = FastBlurUtil.GetUrlBitmap(currentUser.getHeadPath(), scaleRatio);
        //                    //刷新ui必须在主线程中执行
        //                    getActivity().runOnUiThread(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            Drawable d = new BitmapDrawable(blurBitmap);
        //                            rlMySetting.setBackground(d);
        //                        }
        //                    });
        //                } else {
        //                    //获取需要被模糊的原图bitmap
        //                    Resources res = getResources();
        //                    Bitmap scaledBitmap = BitmapFactory.decodeResource(res, R.drawable.filter);
        //
        //                    //scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
        //                    Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, scaleRatio);
        //                    rlMySetting.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        //                }
        //
        //            }
        //        }).start();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
