package com.tarena.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.MyUser;
import com.tarena.cookbook.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.et_register_mobilePhone)
    EditText registerMobilePhone;
    @BindView(R.id.et_register_securityCode)
    EditText registerSecurityCode;
    @BindView(R.id.tv_register_getCode)
    TextView registerGetCode;
    @BindView(R.id.et_register_password)
    EditText registerPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initialUI();

    }

    private void initialUI() {
        Intent intent = getIntent();
        String btnText = intent.getStringExtra("tvBtn");
        btnRegister.setText(btnText);
        String title = intent.getStringExtra("tvTitle");
        tvTitle.setText(title);
    }

    @OnClick({R.id.tv_register_getCode, R.id.btn_register, R.id.iv_back})
    public void onViewClicked(View view) {
        final String securityCode = registerSecurityCode.getText().toString();
        String password = registerPassword.getText().toString();
        String phoneNum = registerMobilePhone.getText().toString();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_register_getCode:
                BmobSMS.requestSMSCode(phoneNum, "code", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "成功:" + integer, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "失败" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.btn_register:
                if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password) || TextUtils.isEmpty(securityCode)) {
                    Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                String titleText = tvTitle.getText().toString();
                if (titleText.equals("注    册")) {
                    MyUser user = new MyUser();
                    user.setUsername(phoneNum);
                    user.setPassword(password);
                    user.setNickname("用户" + phoneNum);
                    user.setSignature("正在通往美食达人的路上...");

                    user.setMobilePhoneNumber(phoneNum);

                    user.signOrLogin(securityCode, new SaveListener<MyUser>() {
                        @Override
                        public void done(final MyUser user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (titleText.equals("重置密码")) {
                    BmobUser.resetPasswordBySMSCode(securityCode, password, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "重置失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;
        }
    }


}
