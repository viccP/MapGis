package com.jlu.mapgis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.adapter.MapFileAdapter;
import com.jlu.mapgis.bean.MapFile;
import com.jlu.mapgis.util.Utils;

import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {

    // 地图默认路径
    public static final String MAPGIS_DATA = "/";
    // 手机存储根路径
    private final static String rootPath = Environment.getExternalStorageDirectory().getPath();


    /**
     * 创建回调
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);

        List<MapFile> mapFiles=Utils.getMap(rootPath+ MAPGIS_DATA);
        ListView mapListView = (ListView) findViewById(R.id.mapFileList);
        MapFileAdapter arrayAdapter = new MapFileAdapter(this,R.layout.map_file,mapFiles);
        mapListView.setAdapter(arrayAdapter);
        //列表元素点击事件
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mapFileName = (TextView) view.findViewById(R.id.map_file_name);
                TextView mapFileFolder = (TextView) view.findViewById(R.id.map_file_folder);
                String filePath=mapFileFolder.getText()+"/"+mapFileName.getText();
                Intent intent= new Intent(MainActivity.this,MapActivity.class);
                intent.putExtra("mapPath",filePath);
                intent.putExtra("mapFolder",mapFileFolder.getText());
                intent.putExtra("mapName",mapFileName.getText());
                startActivity(intent);
            }
        });
    }
}
