package com.tarena.cookbook.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tarena.cookbook.activity.LoginActivity;
import com.tarena.cookbook.entity.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class MyApplication extends Application {
//    private Context context;
//
//    public MyApplication(Context context) {
//        this.context = context;
//    }


    @Override
    public void onCreate() {
        super.onCreate();

        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(this,"dc26cab31ce8e8723600676711e68875");
    }
}
