package com.lanling.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lanling.activity.LoginActivity;
import com.lanling.activity.R;
import com.lanling.activity.UploadDataActivity;
import com.lanling.util.Util;
import com.lanling.view.TitlebarView;

public class ZhuyaoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TitlebarView titlebarView;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private String province;//省级
    private String city;//市级
    private String district;//县级/区级
    private double latitude;//纬度
    private double longtitude;//经度
    private String addrStr;//详细地理信息
    private boolean isFirstLocate = true;//第一次定位
    private SharedPreferences sharedPreferences;
    private Context context;

    public ZhuyaoFragment() {
    }
    public static ZhuyaoFragment newInstance(String param1, String param2) {
        ZhuyaoFragment fragment = new ZhuyaoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        SDKInitializer.initialize(getContext());
        View view = inflater.inflate(R.layout.fragment_zhuyao, container, false);
        //标题栏
        titlebarView = view.findViewById(R.id.fuwuchanping_titlebarview_title);
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void leftClick() {
            }
            @Override
            public void rightClick() {
                Intent intent = null;
                //判断一下用户是否登录
                if(Util.isLogin(getContext()) != 0){//如果用户已经登录的话
                    intent = new Intent(context, UploadDataActivity.class);
                    intent.putExtra("province",province);
                    intent.putExtra("city",city);
                    intent.putExtra("district",district);
                    intent.putExtra("addrStr",addrStr);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longtitude",longtitude);
                    intent.putExtra("email",sharedPreferences.getString("email","没有绑定"));
                    intent.putExtra("openid",sharedPreferences.getString("openid","没有绑定"));
                }else{//如果用户没有登录
                    Util.toastShort(context,"你暂未登录，请先登录");
                    intent = new Intent(context, LoginActivity.class);
                }
                startActivity(intent);
            }
        });//标题栏点击事件
        mMapView = (MapView)view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星地图
        mBaiduMap.setMyLocationEnabled(true);//开启地图的定位图层
        //定位初始化
        mLocationClient = new LocationClient(getContext());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        sharedPreferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
        mMapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            if (isFirstLocate){
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(18f);
                mBaiduMap.animateMapStatus(update);
                /*判断baiduMap是已经移动到指定位置*/
                if (mBaiduMap.getLocationData()!=null)
                    if (mBaiduMap.getLocationData().latitude==location.getLatitude()
                            &&mBaiduMap.getLocationData().longitude==location.getLongitude()){
                        isFirstLocate = false;
                    }
                mBaiduMap.setMyLocationData(new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build());
            }else{
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                addrStr = location.getAddrStr();//获取详细地址信息
                province = location.getProvince();//获取省份
                city = location.getCity();//城市
                district = location.getDistrict();//县级/区级
                latitude = location.getLatitude();//纬度
                longtitude = location.getLongitude();//经度
            }
        }
    }
}