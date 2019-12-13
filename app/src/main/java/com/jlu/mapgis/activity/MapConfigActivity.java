package com.jlu.mapgis.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jlu.mapgis.R;
import com.jlu.mapgis.bean.MapBean;
import com.jlu.mapgis.db.SQLiteDbHelper;
import com.jlu.mapgis.util.AnimationHelper;
import com.lucasurbas.listitemview.ListItemView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample of checkable attributes.
 *
 * @author Lucas Urbas
 */
public class MapConfigActivity extends AppCompatActivity  {

    //状态变化器
    private AnimationHelper animationHelper;
    //选中的地图路径
    private String selectMapPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_map_config);
        //初始化状态变化器
        animationHelper = new AnimationHelper(this);

        //生成列表
        SQLiteDbHelper db=new SQLiteDbHelper(this);
        List<MapBean> mapList = db.selectAllMap();
        MapBean selMap=db.selectDefaultMap();
        initMapList(mapList,selMap);

        //确定事件监听
        findViewById(R.id.map_cfg_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MapConfigActivity.this).setTitle("确认将选中的地图设置为默认么？")
                        .setIcon(R.drawable.information)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                SQLiteDbHelper db=new SQLiteDbHelper(MapConfigActivity.this);
                                db.updateDefaultMap(selectMapPath);
                                //提示信息
                                Toast.makeText(MapConfigActivity.this,"默认地图设置成功",Toast.LENGTH_SHORT);
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        }).show();
            }
        });
    }

    /**
     * :(生成地图列表). <br/>
     *
     * @author liboqiang
     * @Param
     * @Return
     * @since JDK 1.6
     */
    private void initMapList(List<MapBean> mapList, MapBean selMap) {
        LinearLayout layout = findViewById(R.id.map_cfg_list);
        //构造元素列表
        final List<ListItemView> viewList=new ArrayList<>();
        for(MapBean bean:mapList){
            ListItemView item=new ListItemView(this);
            item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            item.setDisplayMode(ListItemView.MODE_ICON);
            item.setIconDrawable(ContextCompat.getDrawable(this,R.drawable.selector_ic_radio));
            item.setIconColor(ContextCompat.getColor(this, R.color.color_state_checked));
            item.setSubtitle(bean.getFolder());
            item.setTitle(bean.getName());
            animationHelper.setupRadioButton(item);
            //设置选中
            if(!StringUtils.isEmpty(selMap.getPath())){
               if(selMap.getPath().equals(bean.getPath())){
                   animationHelper.setRadioButtonState(item, true);
                   //更改placehoder
                   ListItemView placehoder = findViewById(R.id.map_sel_holder);
                   placehoder.setSubtitle("当前选中的地图为："+item.getTitle());
               }
            }
            viewList.add(item);
            layout.addView(item);
        }
        //设置勾选监听
        for(ListItemView item:viewList){
            //初始化状态
            final ListItemView tmpItem=item;
            //添加监听
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //全部设置为反选
                    for(ListItemView subItem:viewList){
                        animationHelper.setRadioButtonState(subItem, false);
                    }
                    //当前设置为选中
                    animationHelper.setRadioButtonState(tmpItem, true);
                    //更改placehoder
                    ListItemView placehoder = findViewById(R.id.map_sel_holder);
                    placehoder.setSubtitle("当前选中的地图为："+tmpItem.getTitle());
                    //保存选中的地图变量
                    selectMapPath=tmpItem.getSubtitle()+"/"+tmpItem.getTitle();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
