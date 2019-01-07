package com.geetest.g4project;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by 谷闹年 on 2019/1/7.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
    }
}
