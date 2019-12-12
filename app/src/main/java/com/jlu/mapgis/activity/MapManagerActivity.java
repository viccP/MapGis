package com.jlu.mapgis.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapBean;
import com.jlu.mapgis.db.SQLiteDbHelper;
import com.jlu.mapgis.util.UriDecoder;
import com.lucasurbas.listitemview.ListItemView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample of checkable attributes.
 *
 * @author Lucas Urbas
 */
public class MapManagerActivity extends AppCompatActivity  {

    private static final int FILE_SELECT_CODEB = 1;

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
                    startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODEB);
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.e(TAG,"没有找到想要的文件",ex);
                }
            }
        });


        //生成列表
        SQLiteDbHelper conn=new SQLiteDbHelper(this);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.query("map_cfg",null,null,null,null,null,null,null);
        List<MapBean> mapList=new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path= cursor.getString(cursor.getColumnIndex("path"));
            MapBean bean=new MapBean();
            bean.setName(name);
            bean.setPath(path);
            mapList.add(bean);
        }
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
            SQLiteDbHelper conn=new SQLiteDbHelper(this);
            SQLiteDatabase db = conn.getWritableDatabase();
            //实例化常量值
            File file=new File(path);
            ContentValues cValue = new ContentValues();
            cValue.put("name",file.getName());
            cValue.put("folder",file.getParent());
            cValue.put("path",path);
            db.insert("map_cfg",null,cValue);
        }
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
            ListItemView item=new ListItemView(this);
            item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            item.setDisplayMode(ListItemView.MODE_ICON);
            item.setIconDrawable(ContextCompat.getDrawable(this,R.drawable.trash_can));
            item.setIconColor(ContextCompat.getColor(this, R.color.color_state_checked));
            item.setSubtitle(bean.getPath());
            item.setTitle(bean.getName());
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
