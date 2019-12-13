package com.jlu.mapgis.bean;

/**
 * Project Name:MapGis
 * File Name:MapBean
 * Package Name:com.jlu.mapgis.bean
 * Date:2019/12/1220:10
 * Copyright (c) 2019, www.bonc.com.cn All Rights Reserved.
 */
public class MapBean {

    //地图名称
    private String name;
    //地图文件夹
    private String folder;
    //地图路径
    private String path;
    //地图id
    private int isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
