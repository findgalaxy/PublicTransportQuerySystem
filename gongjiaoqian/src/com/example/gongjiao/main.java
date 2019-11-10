package com.example.gongjiao;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.gongjiao.util.LocationService;
import com.example.gongjiao.util.MyApplication;
import com.example.gongjiao.util.UserClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity{
	protected static MapView bmapViews = null;
	protected static BaiduMap mBaiduMaps = null;
	private LocationService locationService;
	private double lat, lng;// 经纬度
	private String address = "";// 地址
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		bmapViews = (MapView) findViewById(R.id.bmapView);
		mBaiduMaps = bmapViews.getMap();
		bmapViews.showZoomControls(true);
		MyApplication myapp = (MyApplication) getApplication();
		locationService = myapp.locationService;
		locationService.registerListener(mListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPRS
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		locationService.setLocationOption(option);
		locationService.start();
	}

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			System.out.println(location.getCity() + location.getStreet());

			// TODO Auto-generated method stub
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				address = location.getAddrStr();
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				lat = ll.latitude;
				lng = ll.longitude;
				// 添加起点图标
				BitmapDescriptor bmStartss = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_openmap_focuse_mark);
				MarkerOptions sss = new MarkerOptions().position(ll)
						.icon(bmStartss).zIndex(9).draggable(true);
				mBaiduMaps.addOverlay(sss);
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMaps.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				locationService.stop();
				RequestParams ps=new RequestParams();
				ps.add("gjsj.id", MyApplication.getApp().getUser().getId());
				ps.add("gjsj.lat", lat+"");
				ps.add("gjsj.lng", lng+"");
				UserClient.get("main/gxwz", ps, new AsyncHttpResponseHandler(){
					public void onSuccess(int statusCode, String content) {
						
					};
				});
			}
		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	};

}
