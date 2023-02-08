package com.cbxboy.baseapplication.app;

import android.app.Application;

import com.tencent.mmkv.MMKV;

/**
 * @Author : cbx
 * @Email : 673591077@qq.com
 * @Date : on 2023-02-08 16:26.
 * @Description :描述
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
