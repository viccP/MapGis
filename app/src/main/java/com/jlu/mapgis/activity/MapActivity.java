package com.jlu.mapgis.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jlu.mapgis.R;
import com.jlu.mapgis.adapter.FeatureAdapter;
import com.jlu.mapgis.bean.FeatureBean;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.environment.Environment;
import com.zondy.mapgis.android.graphic.Graphic;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.core.attr.Field;
import com.zondy.mapgis.core.attr.Fields;
import com.zondy.mapgis.core.featureservice.Feature;
import com.zondy.mapgis.core.featureservice.FeaturePagedResult;
import com.zondy.mapgis.core.featureservice.FeatureQuery;
import com.zondy.mapgis.core.geometry.Dot;
import com.zondy.mapgis.core.map.MapLayer;
import com.zondy.mapgis.core.map.VectorLayer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 地图活动页
 */
public class MapActivity extends AppCompatActivity {
    // 地图容器
    private MapView mMapView;

    // 手机存储根路径
    public final static String rootPath = android.os.Environment.getExternalStorageDirectory().getPath();

    // 地图文件路径
    private String path;

    // 属性列表
    private ListView featureListView;

    // 对话框
    private  AlertDialog dialog;

    // 悬浮按钮
    private FloatingActionButton fab;

    // 搜索框
    private MaterialSearchView searchView;

    //要素图
    private Map<String,Feature> featureMap=new HashMap<>();


    /**
     * @description:创建活动时的回调. <br/>
     *
     * @author liboqiang
     * @date:2018/11/28
     * @since JDK 1.6
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //获取前一个activity传递的参数
        Intent intent = getIntent();
        path=intent.getStringExtra("mapPath");

        //构建工具栏
        String mapName=intent.getStringExtra("mapName");
        StringBuilder toolBarTitle=new StringBuilder();
        toolBarTitle.append("当前地图").append("  ").append(mapName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(toolBarTitle.toString());
        setSupportActionBar(toolbar);

        //初始化组件
        initCoponet();

        //浮动按钮点击事件
        fab.setOnClickListener(fabClickListener);

        com.zondy.mapgis.android.environment.Environment.initialize(rootPath , this);
        com.zondy.mapgis.android.environment.Environment.setSystemLibraryPath(rootPath );
        com.zondy.mapgis.android.environment.Environment.requestAuthorization(this, new Environment.AuthorizeCallback() {
            public void onComplete() {
                initMap();
                //初始化搜索框
                initSearchView();
                //搜索按钮事件
                searchView.setOnQueryTextListener(searchViewListener);
            }
        });
    }

    /**
     * @description:初始化搜索框. <br/>
     *
     * @author liboqiang
     * @date:2018/12/3
     * @since JDK 1.6
     *
     */
    private void initSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setHint("请输入要在地图中搜索的内容");
        searchView.setSuggestions(getAllFeatureName());

    }

    /**
     * @description:获取所有要素描述. <br/>
     *
     * @author liboqiang
     * @date:2018/12/3
     * @since JDK 1.6
     *
     * @return
     */
    private String[] getAllFeatureName() {
        VectorLayer vectorLayer=getActivLayer();
        FeatureQuery featureQuery = new FeatureQuery(vectorLayer);
        featureQuery.setOutFields("ZLDWMC");
        FeaturePagedResult res = featureQuery.query();
        int pageCount = res.getPageCount();
        Set<String> resultLst=new HashSet<>();
        for(int i=0;i<pageCount;i++){
            List<Feature> datas = res.getPage(i);
            for(Feature feature:datas){
                String memo = feature.getAttributes().get("ZLDWMC");
                resultLst.add(memo);
                featureMap.put(memo,feature);
            }
        }

        return resultLst.toArray(new String[]{});
    }


    /**
     * @description:生成菜单. <br/>
     *
     * @author liboqiang
     * @date:2018/11/30
     * @since JDK 1.6
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
        //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);
        return true;
    }


    /**
     * @description:初始化组件. <br/>
     *
     * @author liboqiang
     * @date:2018/11/29
     * @since JDK 1.6
     *
     */
    private void initCoponet() {
        // 悬浮按钮
        fab=(FloatingActionButton) findViewById(R.id.fab);
        //获取地图组件
        mMapView = (MapView) findViewById(R.id.map_information);
        //获取对话框
        View dialogView = View.inflate(MapActivity.this, R.layout.feature_dialog, null);
        //获取属性列表
        featureListView = (ListView) dialogView.findViewById(R.id.featureList);
        dialog = new AlertDialog.Builder(MapActivity.this).create();// 创建对话框
        dialog.setIcon(R.mipmap.ic_launcher);// 设置对话框icon
        dialog.setTitle("查看地图信息");// 设置对话框标题
        dialog.setView(dialogView);
        // 设置一个button
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 清空绘图层
                mMapView.getGraphicsOverlay().removeAllGraphics();
                dialog.dismiss();
            }
        });
    }


    /**
     * @description:初始化地图. <br/>
     *
     * @author liboqiang
     * @date:2018/11/28
     * @since JDK 1.6
     */
    private void initMap() {
        mMapView.loadFromFile(path);
        // 显示缩放按钮
        mMapView.setZoomControlsEnabled(false);
        // 显示图标
        mMapView.setShowLogo(true);
        // 根据屏幕的高宽视图中心坐标点
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 初始获取当前视窗的中心点（窗口坐标）
        PointF vPoint = new PointF(dm.widthPixels - 60, 60);
        // 初始开启短按事件监听
        mMapView.setTapListener(mapViewTapListener);
    }

    /**
     * @description:地图点击事件监听. <br/>
     *
     * @author liboqiang
     * @date:2018/11/29
     * @since JDK 1.6
     *
     */
    MapView.MapViewTapListener mapViewTapListener = new MapView.MapViewTapListener() {
        public void mapViewTap(PointF pointf) {
            //点坐标转换
            Dot point = mMapView.viewPointToMapPoint(pointf);
            VectorLayer vectorLayer=getActivLayer();
            List<FeatureBean> featureLst= getFeatureByPoint(vectorLayer,point);
            if(!CollectionUtils.isEmpty(featureLst)){
                //弹出列表
                FeatureAdapter arrayAdapter = new FeatureAdapter(MapActivity.this,R.layout.feature_bean,featureLst);
                featureListView.setAdapter(arrayAdapter);
                dialog.show();
            }
        }
    };

    /**
     * @description:点击事件监听. <br/>
     *
     * @author liboqiang
     * @date:2018/12/3
     * @since JDK 1.6
     *
     */
    MaterialSearchView.OnQueryTextListener searchViewListener=new MaterialSearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Feature feature = featureMap.get(query);
            if(feature!=null){
                // 清空绘图层
                mMapView.getGraphicsOverlay().removeAllGraphics();
                // 获取要素对应的图形
                List<Graphic> graphics = feature.toGraphics(true);
                Dot dot=null;
                for(Graphic graphic:graphics){
                    dot = graphic.getCenterPoint();
                    break;
                }
                if(dot !=null){
                    mMapView.zoomToCenter(dot,0.005,false);
                }
                // 高亮显示查询的结果
                mMapView.getGraphicsOverlay().addGraphics(graphics);
                mMapView.refresh();
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            searchView.showSuggestions();
            return false;
        }
    };

    /**
     * @description:浮动按钮点击回调. <br/>
     *
     * @author liboqiang
     * @date:2018/11/30
     * @since JDK 1.6
     *
     */
    View.OnClickListener fabClickListener=new View.OnClickListener() {
        public void onClick(View v) {
            // 提供了访问系统位置服务。 这些 服务允许应用程序获得的定期更新 设备的地理位置
            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // 返回当前启用/禁用状态给定的提供者。
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(MapActivity.this, "请开启GPS", Toast.LENGTH_SHORT).show();
                // 打开GPS操作
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
                return;
            }
            // 返回最符合给定条件的提供者的名称
            String bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
            // 位置信息，通过Location可以获取时间、经纬度、海拔等位置信息
            @SuppressLint("MissingPermission") Location location = mLocationManager.getLastKnownLocation(bestProvider);
            if (location == null) {
                Toast.makeText(MapActivity.this, "定位失败，建议在室外定位", Toast.LENGTH_SHORT).show();
            } else {
                updateView(location.getLongitude(), location.getLatitude());
            }
        }
    };

    /**
     * @description:获取标准. <br/>
     *
     * @author liboqiang
     * @date:2018/11/30
     * @since JDK 1.6
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * @description:更新地图. <br/>
     *
     * @author liboqiang
     * @date:2018/11/30
     * @since JDK 1.6
     *
     * @param longitude
     * @param latitude
     */
    private void updateView(double longitude, double latitude) {
        // 获取地图坐标点
        Dot mdot = new Dot(longitude, latitude);
        mMapView.zoomToCenter(mdot, 0.005, true);
        // 绘制标注
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ico_main_location)).getBitmap();
        Annotation annotation = new Annotation("", "经度：" + mdot.x + "\n" + "纬度：" + mdot.y, mdot, bitmap);
        long height = annotation.getImageHeight();
        Point point = new Point(3, (int) (-height / 2));
        annotation.setCenterOffset(point);
        annotation.setTitle(null);
        mMapView.getAnnotationsOverlay().removeAllAnnotations();
        mMapView.getAnnotationsOverlay().addAnnotation(annotation);
    }


    /**
     * @description:获取所有要素. <br/>
     *
     * @author liboqiang
     * @date:2018/11/29
     * @since JDK 1.6
     *
     * @param vectorLayer
     * @param point
     * @return
     */
    private List<FeatureBean> getFeatureByPoint(VectorLayer vectorLayer, Dot point) {
        List<FeatureBean> res=new ArrayList<>();
        Fields fields = vectorLayer.getFields();
        for(short i=0;i<fields.getFieldCount();i++){
            Field field = fields.getField(i);
            String fieldName=field.getFieldName();
            FeatureQuery featureQuery = new FeatureQuery(vectorLayer);
            featureQuery.setOutFields(fieldName);
            featureQuery.setQueryBound(new FeatureQuery.QueryBound(point));
            //一页一个
            featureQuery.setPageSize(1);
            FeaturePagedResult rs = featureQuery.query();
            // 获取查询结果（第一页数据）
            List<Feature> featureList = rs.getPage(1);
            for (Feature feature : featureList) {
                HashMap<String, String> attrMap = feature.getAttributes();
                //添加到返回值
                FeatureBean tmp=new FeatureBean();
                tmp.setName(fieldName);
                tmp.setValue(attrMap.get(fieldName));
                res.add(tmp);
                //最后一条
                if (i==fields.getFieldCount()-1) {
                    // 获取要素对应的图形
                    List<Graphic> graphics = feature.toGraphics(true);
                    // 高亮显示查询的结果
                    mMapView.getGraphicsOverlay().addGraphics(graphics);
                }
            }

            //最后一条
            if (i==fields.getFieldCount()-1) {
                mMapView.refresh();
            }
        }
        return res;
    }

    /**
     * @description:获取活动层. <br/>
     *
     * @author liboqiang
     * @date:2018/11/29
     * @since JDK 1.6
     *
     * @return
     */
    private VectorLayer getActivLayer() {
        com.zondy.mapgis.core.map.Map map = mMapView.getMap();
        // 获取查询图层对象（指定区图层）
        for (int i = 0; i < map.getLayerCount(); i++) {
            MapLayer mapLayer = map.getLayer(i);
            if ("土地质量综合属性.WP".equals(mapLayer.getName())) {
               return (VectorLayer) mapLayer;
            }
        }
        return null;
    }
}