package com.silence.glidedome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.silence.glidedome.inter.IActivity;

import butterknife.InjectView;

/**
 * Created by Silence
 *
 * @time 2017/9/23 0:13
 * @des ${TODO}
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    private static int sWidth;
    private static int sHeight;

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar_subtitle)
    TextView mToolbarSubtitle;
    @InjectView(R.id.toolbar_image_back)
    ImageView mToolbarImageBack;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDisplay();
    }

    @Override
    public void initView() {
        mToolbar.setTitle("");
        mToolbar.setSubtitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public abstract void initListener();

    @Override
    public abstract int getLayoutId();

    public int getDisplay() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager  = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        sWidth = metric.widthPixels;
        // 屏幕宽度（像素）
        sHeight = metric.heightPixels;

        return sWidth;
    }

    public static int getWidth() {
        return sWidth;
    }
    public static int getHeight() {
        return sHeight;
    }

    protected void goActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
