package com.jlu.mapgis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.FeatureBean;
import com.jlu.mapgis.bean.MapFile;

import java.util.List;

public class FeatureAdapter extends ArrayAdapter<FeatureBean> {

    private final int resourceId;

    public FeatureAdapter(Context context, int resource, List<FeatureBean> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        FeatureBean featureBean=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView featureName = (TextView) view.findViewById(R.id.featureName);
        TextView featureFolder = (TextView) view.findViewById(R.id.featureValue);
        featureName.setText(featureBean.getName());
        featureFolder.setText(featureBean.getValue());
        return view;
    }
}
