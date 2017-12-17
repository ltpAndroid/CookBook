package com.tarena.cookbook.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.adapter.CookStepListAdapter;
import com.tarena.cookbook.dataBase.CooksDBManager;
import com.tarena.cookbook.entity.Collecttion;
import com.tarena.cookbook.entity.MyUser;
import com.tarena.cookbook.entity.ShowCookersInfo;
import com.tarena.cookbook.view.MyListView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private ShowCookersInfo.Result.Data data;

    private View dialogView;
    private TextView dialog_number;
    private ImageView dialog_img;
    private TextView dialog_text_info;

    private MyUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_details);
        ButterKnife.bind(this);

        initData();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = BmobUser.getCurrentUser(MyUser.class);

        BmobQuery<Collecttion> bq = new BmobQuery<>();
        bq.addWhereEqualTo("myUser",currentUser);
        bq.addWhereEqualTo("cookName",data.getTitle());
        bq.findObjects(new FindListener<Collecttion>() {
            @Override
            public void done(List<Collecttion> list, BmobException e) {

            }
        });
    }

    private void initData() {
        data = CooksDBManager.getCooksDBManager(this).getData();
        tvCookTitle.setText(data.getTitle());
    }


    private void initUI() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.details_dialog_layout, null);
        Picasso.with(this).load(data.getAlbums().get(0)).into(headerImg);
        name.setText(data.getTitle());
        textIntro.setText(data.getImtro());
        tvCollect.setChecked(CooksDBManager.getCooksDBManager(this).isLikeNowCook(data.getId()));
        addMaterial();//添加食材
        adapter = new CookStepListAdapter(data.getSteps(), this);
        lvSteps.setAdapter(adapter);
        lvSteps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowCookersInfo.Result.Data.Steps step = adapter.getItem(position);
                getDialog("第" + (position + 1) + "步", step.getImg(), step.getStep());
            }
        });
    }


    //添加食材
    private void addMaterial() {
        String ingredients = data.getIngredients();//主食材
        String burden = data.getBurden();//辅助食材
        String materialsStr = ingredients + ";" + burden;
        String[] split = materialsStr.split(";");
        //每行放两项  算用多少行
        int lines = (split.length % 2) == 0 ? split.length / 2 : split.length / 2 + 1;
        for (int i = 0; i < lines; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_material, null);
            materialsLayout.addView(view);
            String[] texts = split[i * 2].split(",");
            TextView tv1 = view.findViewById(R.id.text1);
            tv1.setText(texts[0]);
            TextView tv2 = view.findViewById(R.id.text2);
            tv2.setText(texts[1]);

            if (i == lines - 1 && split.length % 2 != 0) {

                continue;
            }
            texts = split[i * 2 + 1].split(",");
            TextView tv3 = view.findViewById(R.id.text3);
            tv3.setText(texts[0]);
            TextView tv4 = view.findViewById(R.id.text4);
            tv4.setText(texts[1]);
            texts = null;
        }
    }

    //回退键、收藏和分享，3个键的监听
    @OnClick({R.id.iv_back_cookery, R.id.tv_share, R.id.tv_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back_cookery:
                finish();
                overridePendingTransition(R.anim.new_to_right, R.anim.old_to_right);
                break;

            case R.id.tv_share:
                if (currentUser == null) {
                    Intent intent = new Intent(CookDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                StringBuilder builder = new StringBuilder();
                builder.append("美　　食：" + data.getTitle() + "\n");
                builder.append("美食定位：\n　　" + data.getTags() + "\n");
                builder.append("介　　绍：\n　　" + data.getImtro() + "\n");
                builder.append("食　　材：\n　　" + data.getIngredients() + ";" + data.getBurden() + "\n");
                builder.append("制作步骤：" + "\n");
                for (int i = 0; i < data.getSteps().size(); i++) {
                    builder.append("第" + (i + 1) + "步：\n　　" + data.getSteps().get(i).getStep() + "\n");
                }

                shareMsg("选择分享", "分享", builder.toString(), null);
                break;

            case R.id.tv_collect:
                if (currentUser == null) {
                    Intent intent = new Intent(CookDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                tvCollect.toggle();
                CooksDBManager.getCooksDBManager(this).updateData(data, tvCollect.isChecked());

                Collecttion collecttion = new Collecttion();
                if (tvCollect.isChecked()) {
                    collecttion.setMyUser(currentUser);
                    collecttion.setCookName(data.getTitle());
                    collecttion.setLike(true);
                    collecttion.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(CookDetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CookDetailsActivity.this, "收藏失败:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    collecttion.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(CookDetailsActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CookDetailsActivity.this, "取消失败:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;
        }
    }

    /**
     * @param activityTitle 分享列表标题
     * @param msgTitle      消息标题
     * @param msgText       内容
     * @param imgPath       图片路径
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);// 系统分享功能
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本,// 分享发送的数据类型
        } else {
            File file = new File(imgPath);
            if (file != null && file.exists() && file.isFile()) {
                intent.setType("image/jpg");// 分享发送的数据类型
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);// 分享的内容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));// 目标应用选择对话框的标题
    }


    private AlertDialog dialog;

    private void getDialog(String itemId, String img, String infoText) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).create();
            dialog.show();
            dialog.setContentView(dialogView);
            dialog_number = dialogView.findViewById(R.id.dialog_number);
            dialog_img = dialogView.findViewById(R.id.dialog_img);
            dialog_text_info = dialogView.findViewById(R.id.dialog_text_info);
        }

        if (dialog != null) {
            dialog_number.setText(itemId);
            Picasso.with(this).load(img).placeholder(R.drawable.ic_placeholder).into(dialog_img);
            dialog_text_info.setText(infoText);
            dialog.show();
        }
    }
}
