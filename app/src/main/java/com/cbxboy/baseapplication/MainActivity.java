package com.cbxboy.baseapplication;

import com.cbxboy.baseapplication.base.BaseVBActivity;
import com.cbxboy.baseapplication.databinding.ActivityMainBinding;
import com.tencent.mmkv.MMKV;

public class MainActivity extends BaseVBActivity<ActivityMainBinding> {

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //Todo
        //MMKV的使用
        MMKV kv = MMKV.defaultMMKV();

        kv.encode("bool", true);
        boolean bValue = kv.decodeBool("bool");

        kv.encode("int", Integer.MIN_VALUE);
        int iValue = kv.decodeInt("int");

        kv.encode("string", "Hello from mmkv");
        String str = kv.decodeString("string");
    }

    @Override
    public void initListener() {

    }
}