package com.jlu.mapgis.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapBean;
import com.jlu.mapgis.db.SQLiteDbHelper;
import com.jlu.mapgis.util.UriDecoder;
import com.lucasurbas.listitemview.ListItemView;

import java.util.List;

/**
 * Sample of checkable attributes.
 *
 * @author Lucas Urbas
 */
public class MapManagerActivity extends AppCompatActivity  {

    private static final int FILE_SELECT_CODE = 1;

    private static final String TAG = "MapManagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_map_manager);

        //添加地图
        findViewById(R.id.add_map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.e(TAG,"没有找到想要的文件",ex);
                }
            }
        });

        //生成列表
        SQLiteDbHelper db=new SQLiteDbHelper(this);
        List<MapBean> mapList = db.selectAllMap();
        initMapList(mapList);
    }

    /**
     * :(获取读取文件返回值). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path= UriDecoder.getPath(this,uri);
            SQLiteDbHelper db=new SQLiteDbHelper(this);
            db.insertMap(path);
        }
        //刷新当前Activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * :(初始化地图列表). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    private void initMapList(List<MapBean> mapList) {
        LinearLayout layout = findViewById(R.id.map_mgr_list);
        for(MapBean bean:mapList){
            final ListItemView item=new ListItemView(this);
            item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            item.setDisplayMode(ListItemView.MODE_ICON);
            item.setIconDrawable(ContextCompat.getDrawable(this,R.drawable.trash_can));
            item.setIconColor(ContextCompat.getColor(this, R.color.color_state_checked));
            item.setSubtitle(bean.getFolder());
            item.setTitle(bean.getName());
            //添加监听
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MapManagerActivity.this).setTitle("确认删除地图吗？")
                            .setIcon(R.drawable.information)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    SQLiteDbHelper db=new SQLiteDbHelper(MapManagerActivity.this);
                                    db.deleteMap(item.getTitle(),item.getSubtitle());

                                    //刷新当前Activity
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                }
                            }).show();
                }
            });
            layout.addView(item);
        }
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
