package com.jlu.mapgis.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.FeatureBean;

import java.util.List;

 /**
  * @description: 属性适配器. <br/>
  *
  * @author liboqiang
  * @date: 2019/10/8
  * @since JDK 1.6
  */
public class FeatureAdapter extends ArrayAdapter<FeatureBean> {

    private final int resourceId;

     /**
      * @description: 构造函数. <br/>
      *
      * @author liboqiang
      * @date: 2019/10/8
      * @since JDK 1.6
      */
    public FeatureAdapter(Context context, int resource, List<FeatureBean> objects) {
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
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        }
        FeatureBean featureBean = getItem(position);
        TextView featureName = convertView.findViewById(R.id.featureName);
        TextView featureFolder = convertView.findViewById(R.id.featureValue);
        if (featureBean != null) {
            featureName.setText(featureBean.getName());
            featureFolder.setText(featureBean.getValue());
        }
        return convertView;
    }
}
