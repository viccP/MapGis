package com.jlu.mapgis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.adapter.MapFileAdapter;
import com.jlu.mapgis.bean.MapFile;
import com.jlu.mapgis.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

     /**
      * @description: 创建回调. <br/>
      *
      * @author liboqiang
      * @date:2019/10/8
      * @since JDK 1.6
      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
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


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.info_toolbar);
            setSupportActionBar(toolbar);

            //将Assets下的文件Copy到SD卡
            copyAssetsToSd();

            List<MapFile> mapFiles=Utils.getMap(getExternalFilesDir(null));

            //加载文件列表
            ListView mapListView = findViewById(R.id.mapFileList);
            MapFileAdapter arrayAdapter = new MapFileAdapter(this,R.layout.map_file,mapFiles);
            mapListView.setAdapter(arrayAdapter);
            //列表元素点击事件
            mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView mapFileName = view.findViewById(R.id.map_file_name);
                    TextView mapFileFolder = view.findViewById(R.id.map_file_folder);
                    String filePath=mapFileFolder.getText()+"/"+mapFileName.getText();
                    Intent intent= new Intent(MainActivity.this,MapActivity.class);
                    intent.putExtra("mapPath",filePath);
                    intent.putExtra("mapFolder",mapFileFolder.getText());
                    intent.putExtra("mapName",mapFileName.getText());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(TAG,"APP加载异常",e);
        }
    }

    /**
      * @description: 将Assets资源拷贝到SD卡. <br/>
      *
      * @author liboqiang
      * @date: 2019/10/8
      * @since JDK 1.6
     */
    private void copyAssetsToSd() {
        InputStream in = null;
        OutputStream out = null;
        try {
            AssetManager assetManager = getAssets();
            String[] files = assetManager.list("");
            //如果Assets目录下没有内容
            if(files==null){
                return;
            }
            for (String filename : files) {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                out.flush();
                //解压文件删除源文件
                unzipFile(outFile);
            }
        } catch (Exception e) {
            Log.e(TAG,"copy assets文件失败",e);
        } finally {
            try {
                if (in!=null) {
                    in.close();
                }
                if (out!=null) {
                    out.close();
                }
            } catch (IOException e) {
               Log.e(TAG,"[copyAssetsToSd]关闭流失败",e);
            }
        }
    }

    /**
     * @description: 拷贝文件. <br/>
     *
     * @author liboqiang
     * @date:2019/10/8
     * @since JDK 1.6
     */
    private void copyFile(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

     /**
      * @description: 这里用一句话描述这个方法的作用. <br/> 
      * 
      * @author liboqiang
      * @date: 2019/10/8
      * @since JDK 1.6 
      */
    private void unzipFile(File zipFile) throws IOException {
        // 解压文件
        ZipFile zip = new ZipFile(zipFile);
        String descDir=zipFile.getParent();
        for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.d(TAG,"目录"+file.getPath()+"创建成功");
                }else{
                    Log.e(TAG,"奴鲁"+file.getPath()+"创建失败");
                }
            }
            // 判断路径是否为文件夹
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // 写文件
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        zip.close();
        // 删除源文件
       if(zipFile.delete()){
           Log.d(TAG,"文件"+zipFile.getPath()+"删除成功");
       }else{
           Log.e(TAG,"文件"+zipFile.getPath()+"删除失败");
       }
    }
}
