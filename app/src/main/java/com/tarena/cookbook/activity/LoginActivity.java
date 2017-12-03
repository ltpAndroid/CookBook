package com.tarena.cookbook.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.cookbook.R;
import com.tarena.cookbook.entity.MyUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.et_login_mobilePhone)
    EditText etPhone;
    @BindView(R.id.et_login_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        if (BmobUser.getCurrentUser() != null) {
//            Intent intent = new Intent(LoginActivity.this, PersonalActivity.class);
//            startActivity(intent);
//            finish();
//        }

        String userName = getSharedPreferences("login", MODE_PRIVATE).getString("username", "");
        String password = getSharedPreferences("login", MODE_PRIVATE).getString("password", "");

        if (userName != null) {
            etPhone.setText(userName);
        }
        if (password != null) {
            etPassword.setText(password);
        }
    }

    @OnClick({R.id.tv_cancel, R.id.btn_login, R.id.tv_forget, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.btn_login:
                String username = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyUser user = new MyUser();
                user.setUsername(username);
                user.setPassword(password);
                user.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                            editor.putString("username", etPhone.getText().toString());
                            editor.putString("password", etPassword.getText().toString());
                            editor.commit();
                            //跳转页面添加渐变
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_forget:
                Intent forgetIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                forgetIntent.putExtra("tvTitle", "重置密码");
                forgetIntent.putExtra("tvBtn", "确定");
                startActivity(forgetIntent);
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.putExtra("tvTitle", "注    册");
                registerIntent.putExtra("tvBtn", "注册");
                startActivity(registerIntent);
                break;
        }
    }
}
