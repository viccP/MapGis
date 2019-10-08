package com.jlu.mapgis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapFile;

import java.util.List;

/**
 * @author liboqiang
 * @description: 地图文件适配器. <br/>
 * @date: 2019/10/8
 * @since JDK 1.6
 */
public class MapFileAdapter extends ArrayAdapter<MapFile> {

    private final int resourceId;

     /**
      * @description: 构造函数. <br/>
      *
      * @author liboqiang
      * @date: 2019/10/8
      * @since JDK 1.6
      */
    public MapFileAdapter(Context context, int resource, List<MapFile> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

     /**
      * @description: 获取视图. <br/>
      *
      * @author liboqiang
      * @date: 2019/10/8
      * @since JDK 1.6
      */
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        TextView mapFileName = convertView.findViewById(R.id.map_file_name);
        TextView mapFileFolder = convertView.findViewById(R.id.map_file_folder);
        MapFile mapFile = getItem(position);
        if (mapFile != null) {
            mapFileName.setText(mapFile.getFileName());
            mapFileFolder.setText(mapFile.getFolder());
        }
        return convertView;
    }
}
