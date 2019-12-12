package com.jlu.mapgis.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Project Name:MapGis
 * File Name:SQLiteDbHelper
 * Package Name:com.jlu.mapgis.db
 * Date:2019/12/1215:53
 * Copyright (c) 2019, www.jlu.end All Rights Reserved.
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "mapgis.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_MAP_CFG = "map_cfg";

   //建表语句
    private static final String STUDENTS_CREATE_TABLE_SQL = "create table" + TABLE_MAP_CFG + "("
            + "id integer primary key autoincrement,"
            + "name varchar(20) not null,"
            + "folder varchar(20) not null,"
            + "path varchar(200) not null"
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
}
