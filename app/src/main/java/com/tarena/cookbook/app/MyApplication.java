package com.tarena.cookbook.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(this,"dc26cab31ce8e8723600676711e68875");
    }
}
