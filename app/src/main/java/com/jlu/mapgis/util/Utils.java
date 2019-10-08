package com.jlu.mapgis.util;

import com.jlu.mapgis.bean.MapFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 */
public class Utils {

    /**
     * 文件扩展名
     */
    private static final String FILE_EXT_TYPE = "mapx";

    /**
     * 私有化构造函数
     */
    private Utils(){}

    /**
     * 获取地图文件
     */
    public static List<MapFile> getMap(String rootPath) {
        List<MapFile> res=new ArrayList<>();
        File dir = new File(rootPath);
        List<File> files = getFileList(dir);
        for(File file:files){
            MapFile mapFile=new MapFile();
            mapFile.setFolder(file.getParent());
            mapFile.setFileName(file.getName());
            res.add(mapFile);
        }
        return res;
    }

    /**
     * 获取地图文件
     */
    public static List<MapFile> getMap(File rootDir) {
        List<MapFile> res=new ArrayList<>();
        List<File> files = getFileList(rootDir);
        for(File file:files){
            MapFile mapFile=new MapFile();
            mapFile.setFolder(file.getParent());
            mapFile.setFileName(file.getName());
            res.add(mapFile);
        }
        return res;
    }

    /**
     *
     * getFileList:(获取文件列表：递归调用). <br/>
     *
     * @author liboqiang
     * @since JDK 1.6
     */
    private static List<File> getFileList(File dir) {
        List<File> filelist = new ArrayList<>();
        //如果传入的是文件
        if (!dir.isDirectory()) {
            if (dir.getName().endsWith(FILE_EXT_TYPE)) {
                filelist.add(dir);
            }
            return filelist;
        }

        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // 判断是文件还是文件夹
                if (file.isDirectory()) {
                    // 递归调用
                    filelist.addAll(getFileList(file));
                }
                // 判断文件名是否以给定扩展名结尾
                else if (fileName.endsWith(FILE_EXT_TYPE)) {
                    filelist.add(file);
                }
            }
        }
        return filelist;
    }
}
