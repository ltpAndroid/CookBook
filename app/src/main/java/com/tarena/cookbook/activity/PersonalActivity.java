package com.tarena.cookbook.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.MyUser;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonalActivity extends AppCompatActivity {

    @BindView(R.id.iv_left)
    ImageView ivBack;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.iv_headicon)
    ImageView ivHeadicon;
    @BindView(R.id.et_signature)
    EditText etSignature;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String imagePath;
    private static final int IMAGE_REQUEST_CODE = 101;
    private static final int REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);

        //获取当前用户 显示当前用户昵称和头像
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        etNickname.setText(currentUser.getNickname());
        etSignature.setText(currentUser.getSignature());
        Picasso.with(this).load(currentUser.getHeadPath()).into(ivHeadicon);

        ivHeadicon.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @OnClick({R.id.iv_left, R.id.iv_headicon, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_headicon:
                checkPermission();
                break;
            case R.id.btn_submit:
                submitSetting();
                break;
        }
    }

    private void submitSetting() {
        //得到当前登录的用户
        final MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        currentUser.setNickname(etNickname.getText().toString());
        currentUser.setSignature(etSignature.getText().toString());
        if (imagePath != null) {//需要修改头像
            //上传图片
            final BmobFile uploadHead = new BmobFile(new File(imagePath));
            uploadHead.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //把上传完的图片网址得到给到用户
                        currentUser.setHeadPath(uploadHead.getFileUrl());
                        update(currentUser);
                    } else {
                        Toast.makeText(PersonalActivity.this, "上传头像失败：" + e.getLocalizedMessage(), Toast
                                .LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                    Log.i("tarena", "onProgress: " + value);
                }
            });
        } else {
            update(currentUser);
        }
    }

    private void update(MyUser user) {
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    Toast.makeText(PersonalActivity.this, "修改成功", Toast
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalActivity.this, "修改失败：" + e.getLocalizedMessage(), Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
    }

    //请求权限
    private void checkPermission() {
        //判断是否打开文件存储全校
        if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest
                .permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //请求打开文件存储权限
            ActivityCompat.requestPermissions(PersonalActivity.this, new
                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            choosePhoto();
        }
    }

    //请求权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            //判断用户是否允许
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                choosePhoto();

            } else {
                Toast.makeText(PersonalActivity.this, "此App没有访问用户相册的权限，请前往设置授予权限！", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    private void showImage(String imagePath) {
        this.imagePath = imagePath;
        Picasso.with(this).load(new File(imagePath)).into(ivHeadicon);
    }
}
