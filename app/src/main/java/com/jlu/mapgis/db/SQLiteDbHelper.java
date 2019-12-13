package com.jlu.mapgis.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jlu.mapgis.bean.MapBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:MapGis
 * File Name:SQLiteDbHelper
 * Package Name:com.jlu.mapgis.db
 * Date:2019/12/1215:53
 * Copyright (c) 2019, www.jlu.end All Rights Reserved.
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "MapActivity";

    public static final String DB_NAME = "mapgis.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_MAP_CFG = "map_cfg";

   //建表语句
    private static final String STUDENTS_CREATE_TABLE_SQL = "create table " + TABLE_MAP_CFG + "("
            + "name varchar(20) not null,"
            + "folder varchar(20) not null,"
            + "path varchar(500) PRIMARY KEY not null,"
            + "is_default integer(1) not null"
            + ");";

    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        db.execSQL(STUDENTS_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // 启动外键
            String query = String.format("PRAGMA foreign_keys = %s", "ON");
            db.execSQL(query);
        }
    }

    /**
     * :(添加地图). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    public void insertMap(String path){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //实例化常量值
            File file=new File(path);
            ContentValues cValue = new ContentValues();
            cValue.put("name",file.getName());
            cValue.put("folder",file.getParent());
            cValue.put("path",path);
            cValue.put("is_default",0);
            db.insert(TABLE_MAP_CFG,null,cValue);
        } catch (Exception e) {
            Log.e(TAG,"地图添加异常",e);
        }
    }

    /**
     * :(查询所有地图). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    public List<MapBean> selectAllMap(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("map_cfg",null,null,null,null,null,null,null);
        List<MapBean> mapList=new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path= cursor.getString(cursor.getColumnIndex("path"));
            String folder=cursor.getString(cursor.getColumnIndex("folder"));
            int isDefault=cursor.getInt(cursor.getColumnIndex("is_default"));
            MapBean bean=new MapBean();
            bean.setName(name);
            bean.setPath(path);
            bean.setFolder(folder);
            bean.setIsDefault(isDefault);
            mapList.add(bean);
        }
        return mapList;
    }

    /**
     * :(删除地图). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    public void deleteMap(String title, String subtitle) {
        String path=subtitle+"/"+title;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MAP_CFG,"path=?",new String[]{path});
    }
}
