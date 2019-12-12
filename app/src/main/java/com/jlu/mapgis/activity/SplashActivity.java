package com.jlu.mapgis.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.jlu.mapgis.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * 动画时长
     */
    private final long DURATION = 1600;
    private ValueAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    /**
     * 页面显示后才检查权限
     */
    @Override
    protected void onResume() {
        super.onResume();
        //获取权限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET
                    },
                    1
            );
        }
        initAnimator();
    }

    /**
     * :(播放动画). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 80);
        mAnimator.setDuration(DURATION);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                showAboutPage();
            }
        });
        mAnimator.start();
    }

    /**
     * 关闭动画,释放动画资源
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAnimator != null) {
            if(mAnimator.isRunning()){
                mAnimator.cancel();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAnimator != null){
            mAnimator = null;
        }
    }

    /**
     * 进入主页
     */
    private void showAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        finish();
    }
}
