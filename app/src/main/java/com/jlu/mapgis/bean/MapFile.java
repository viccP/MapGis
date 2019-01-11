package com.jlu.mapgis.bean;

/**
 * 地图类
 */
public class MapFile {

    //地图文件夹
    private String folder;
    //地图名称
    private String fileName;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}