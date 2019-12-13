package com.jlu.mapgis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapBean;
import com.jlu.mapgis.db.SQLiteDbHelper;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";

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
                Intent intent= new Intent(AboutActivity.this,AppInfoActivity.class);
                startActivity(intent);
            }
        });

        //设置地图按钮
        findViewById(R.id.map_cfg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AboutActivity.this,MapConfigActivity.class);
                startActivity(intent);
            }
        });

        //地图管理按钮
        findViewById(R.id.map_mgr_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AboutActivity.this,MapManagerActivity.class);
                startActivity(intent);
            }
        });

        //进入地图按钮
        findViewById(R.id.enter_map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //找到默认地图
                SQLiteDbHelper db=new SQLiteDbHelper(AboutActivity.this);
                MapBean selMap=db.selectDefaultMap();
                //进入地图
                Intent intent= new Intent(AboutActivity.this,MapActivity.class);
                intent.putExtra("mapPath",selMap.getPath());
                intent.putExtra("mapFolder",selMap.getFolder());
                intent.putExtra("mapName",selMap.getName());
                startActivity(intent);
            }
        });
    }
}
