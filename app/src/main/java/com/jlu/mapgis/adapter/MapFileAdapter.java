package com.jlu.mapgis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapFile;

import org.w3c.dom.Text;

import java.util.List;

public class MapFileAdapter extends ArrayAdapter<MapFile> {

    private final int resourceId;

    public MapFileAdapter(Context context, int resource, List<MapFile> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        MapFile mapFile=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView mapFileName = (TextView) view.findViewById(R.id.map_file_name);
        TextView mapFileFolder = (TextView) view.findViewById(R.id.map_file_folder);
        mapFileName.setText(mapFile.getFileName());
        mapFileFolder.setText(mapFile.getFolder());
        return view;
    }
}
