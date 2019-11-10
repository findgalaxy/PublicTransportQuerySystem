package com.example.gongjiao;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.gongjiao.pojo.User;
import com.example.gongjiao.pojo.zd;
import com.example.gongjiao.util.LocationService;
import com.example.gongjiao.util.MyApplication;
import com.example.gongjiao.util.UserClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class zhandian_cx extends Activity{
	protected static MapView bmapViews = null;
	protected static BaiduMap mBaiduMaps = null;
	private LocationService locationService;
	private double lat, lng;// 经纬度
	private String address = "";// 地址
	List<zd> list;
	List<User> ulist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhandian_cx);
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
		getall();
	}

	public void getalls() {
		RequestParams ps = new RequestParams();
		ps.add("lid", getIntent().getStringExtra("lid"));
		UserClient.get("main/getgjsj", ps, new AsyncHttpResponseHandler() {

			@Override
			@Deprecated
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				ulist = JSON.parseArray(content, User.class);
				BitmapDescriptor bbs = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_openmap_focuse_mark);
				for (int i = 0; i < ulist.size(); i++) {
					LatLng l = new LatLng(Double.parseDouble(ulist.get(i)
							.getLng()), Double
							.parseDouble(ulist.get(i).getLat()));
					OverlayOptions markers = new MarkerOptions()
							.title(ulist.get(i).getNickname()).position(l).icon(bbs)
							.zIndex(9).draggable(false);
					mBaiduMaps.addOverlay(markers);
					mBaiduMaps
							.setOnMarkerClickListener(new OnMarkerClickListener() {

								@Override
								public boolean onMarkerClick(Marker m) {
									// TODO Auto-generated method
									// stub
									showPop(m.getPosition().latitude,
											m.getPosition().longitude,
											m.getTitle());
									return true;
								}
							});
				}
			}
		});
	}
	
	public void getall() {
		RequestParams ps = new RequestParams();
		ps.add("lid", getIntent().getStringExtra("lid"));
		UserClient.get("main/getzd", ps, new AsyncHttpResponseHandler() {

			@Override
			@Deprecated
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				list = JSON.parseArray(content, zd.class);
				System.out.println(list.size() + "----");
				BitmapDescriptor bbs = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_openmap_focuse_mark);
				for (int i = 0; i < list.size(); i++) {
					LatLng l = new LatLng(Double.parseDouble(list.get(i)
							.getLng()), Double
							.parseDouble(list.get(i).getLat()));
					OverlayOptions markers = new MarkerOptions()
							.title(list.get(i).getName()).position(l).icon(bbs)
							.zIndex(9).draggable(false);
					mBaiduMaps.addOverlay(markers);
					mBaiduMaps
							.setOnMarkerClickListener(new OnMarkerClickListener() {

								@Override
								public boolean onMarkerClick(Marker m) {
									// TODO Auto-generated method
									// stub
									showPop(m.getPosition().latitude,
											m.getPosition().longitude,
											m.getTitle());
									return true;
								}
							});
				}
			}
		});
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
			}
		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	};

	public void showPop(final double lng, final double lat, final String title) {
		// 创建InfoWindow展示的view
		Button button = new Button(getApplicationContext());
		button.setBackgroundResource(R.drawable.button_lan);
		button.setText(title);
		
		// 定义用于显示该InfoWindow的坐标点
		LatLng pt = new LatLng(lng, lat);
		// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
		InfoWindow mInfoWindow = new InfoWindow(button, pt, -87);
		// 显示InfoWindow
		// OnInfoWindowClickListener listener = new OnInfoWindowClickListener()
		// {
		// public void onInfoWindowClick() {
		// mBaiduMaps.hideInfoWindow();//影藏气泡
		//
		// }
		// };
		mBaiduMaps.showInfoWindow(mInfoWindow);
		mBaiduMaps.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mBaiduMaps.hideInfoWindow();// 影藏气泡
			}
		});
	}

}
