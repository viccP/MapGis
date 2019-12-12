package com.jlu.mapgis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.jlu.mapgis.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";

    /**
     * :(进入地图监听). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
//    private View.OnClickListener enterMapClickListner=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent intent= new Intent(AboutActivity.this,MainActivity.class);
//            startActivity(intent);
//        }
//    };
    //设置地图页
    private Intent mapConfigIntent = null;

    //设置管理页
    private Intent mapManagerIntent = null;

    /**
     * :(关于页面的创建监听). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mapConfigIntent=new Intent(this, MapConfigActivity.class);
        mapManagerIntent=new Intent(this, MapManagerActivity.class);
        //退出系统按钮
        findViewById(R.id.exit_app_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //项目简介按钮
        findViewById(R.id.app_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"debug");
            }
        });

        //设置地图按钮
        findViewById(R.id.map_cfg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mapConfigIntent);
            }
        });

        //地图管理按钮
        findViewById(R.id.map_mgr_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mapManagerIntent);
            }
        });
    }
}
