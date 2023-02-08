package com.cbxboy.baseapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.cbxboy.baseapplication.utils.ActivityManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseVBActivity<T extends ViewBinding> extends AppCompatActivity implements BaseVBInterFace {
    protected T binding;
    public String mToken = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //压入当前activity
        ActivityManager.getManager().addActivity(this);
        // 禁用横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //通过反射 获取当前activity ViewBinding
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (T) method.invoke(null, getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        initView();
        initData();
        initListener();
//        keyboard();
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }

/*    //沉浸式
    public void baseImmersionBar(View view) {
        ImmersionBar.with(this).titleBar(view).init();
    }

    //沉浸式
    public void baseImmersionBar(View view, boolean isDarkFont) {
        ImmersionBar.with(this).titleBar(view).statusBarDarkFont(isDarkFont).init();
    }*/


    //启动指定activity
    public void startActivity(Class<?> clz) {
        Intent intent = baseIntent(clz);
        startActivity(intent);
    }

    //创建Intent
    public Intent baseIntent(Class<?> clz) {
        return new Intent(this, clz);
    }

    //布局返回
    public void baseFinish(View v) {
        finish();
    }

    //toast
    protected void showToast(String msg) {
        //ToastUtil.showToast(this, msg);
    }

    //log
    protected void showLog(String msg) {
        //LogUtil.e(msg);
    }

    //关闭 软键盘
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            hideKeyboard(ev, view, this);//调用方法判断是否需要隐藏键盘
        }
        return super.dispatchTouchEvent(ev);
    }

    //点击键盘外部 隐藏键盘
    private void hideKeyboard(MotionEvent event, View view, Activity activity) {
        try {
            if (view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听软键盘 弹出与隐藏
    private void keyboard() {
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                binding.getRoot().getWindowVisibleDisplayFrame(r);

                int heightDiff = binding.getRoot().getRootView().getHeight() - r.height();
                if (heightDiff > 0.25 * binding.getRoot().getRootView().getHeight()) {
                    onKeyBoardVis();
                } else {
                    onKeyBoardHide();
                }
            }
        });
    }

    protected void onKeyBoardHide() {
    }

    protected void onKeyBoardVis() {
    }
}
