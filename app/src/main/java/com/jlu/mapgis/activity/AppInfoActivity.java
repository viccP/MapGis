package com.jlu.mapgis.activity;

import android.content.DialogInterface;
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
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * :(项目简介页). <br/>
 *
 * @author liboqiang
 * @params
 * @return
 * @since JDK 1.6
 */
public class AppInfoActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_app_info);
        ExpandableTextView textViewS1=findViewById(R.id.app_info_section1);
        ExpandableTextView textViewS2=findViewById(R.id.app_info_section2);
        ExpandableTextView textViewS3=findViewById(R.id.app_info_section3);
        ExpandableTextView textViewS4=findViewById(R.id.app_info_section4);
        textViewS1.setText("地理信息系统（简称GIS）是近20年来新兴的一门集地理学、计算机、遥感技术和地图学于一体的边缘学科，经过上世纪60年代的开拓发展阶段、70年代的巩固阶段、80年代的突破阶段和90年代的社会化阶段,目前处于大规模应用和全面发展阶段。我国的地理信息系统则经历了20世纪70年代的准备阶段、80年代的试验起步阶段和其后的发展阶段,现在正处于持续发展、形成行业和走向产业化的阶段。\n" +
                "MapGIS是中国地质大学开发的通用工具型地理信息系统软件，它是在享有盛誉的地图编辑出版系统的MAPCAD基础上发展起来的，可对空间数据进行采集、存储、检索、分析和图形表示。MapGIS包括了MAPCAD的全部基本制图功能，可以制作具有出版精度的十分复杂的地形图和地质图。同时，它能对地形数据与各种专业数据进行一体化管理和空间分析查询，从而为多源地学信息的综合分析提供了一个理想的平台。\n");
        textViewS2.setText("在淄博市典型地区1:5万土地质量地球化学调查及评价成果基础上，建立土地质量地球化学调查与评价数据库，研发数据管理系统手机APP。目的是搭建调查成果与基本用户之间的桥梁，服务于土地质量地球化学调查成果的转化应用。土地质量数据管理应用手机APP系统是面向地球化学工作研究人员提供所需要的数据分析能力和IT服务能力，为工作研究人员提供方便实用的功能支撑");
        textViewS3.setText("通过土地质量数据管理应用手机APP的建设，实现地方土地资源的精细化管理，研究人员通过上传MapGis原始地图的方式，在移动端设备中快速便捷的访问地图数据，并通过APP提供的搜索和定位功能，快速获取指定区域的土壤环境质量指标、土壤肥力指标以及基于土壤信息与土壤质量的综合土壤质量评价信息。");
        textViewS4.setText("本系统建设在总体设计上满足以下要求：\n" +
                "技术先进成熟性：采用成熟、合理、先进的技术，在选用开发组件时要在保证其成熟性和可靠性的同时保证APP的适度先进性。\n" +
                "安全性和可靠性：APP针对移动终端设备要制定相应的安全策略和可靠性策略，保障APP的安全性和可靠性，APP应具有处理各种非正常状态和事件的能力。\n" +
                "开放性：APP采用多层开放式体系结构，具有清晰的体系结构。提供灵活的二次开发手段，在面向对象的业务组件应用框架上，能够在不影响APP的情况下快速开发新业务，同时可方便地对业务进行修改和动态加载的支持。\n");
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
