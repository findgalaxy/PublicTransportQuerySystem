/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.example.gongjiao;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.MassTransitRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.gongjiao.adapter.RouteLineAdapter;
import com.example.gongjiao.util.LocationService;
import com.example.gongjiao.util.MyApplication;
import com.example.gongjiao.util.MyToastUtil;

/**
 * 此demo用来展示如何进行驾车、步行、公交、骑行、跨城综合路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class RoutePlanDemo extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

	// 浏览路线节点相关
	Button mBtnPre = null; // 上一个节点
	Button mBtnNext = null; // 下一个节点
	int nodeIndex = -1; // 节点索引,供浏览节点时使用
	RouteLine route = null;

	MassTransitRouteLine massroute = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null; // 泡泡view

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	WalkingRouteResult nowResultwalk = null;
	BikingRouteResult nowResultbike = null;
	TransitRouteResult nowResultransit = null;
	DrivingRouteResult nowResultdrive = null;
	MassTransitRouteResult nowResultmass = null;
	EditText ks, js;
	int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。

	String startNodeStr = "济南奥体中心";
	String endNodeStr = "济南动物园";
	// 定位相关
	LocationClient mLocClient;
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xAAFFFF88;
	private static final int accuracyCircleStrokeColor = 0xAA00FF00;
	BaiduMap mBaiduMap;
	private LocationService locationService;
	private Handler handler = null;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true; // 是否首次定位
	String kss = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_routeplan);
		handler = new Handler();
		mCurrentMode = LocationMode.NORMAL;
		// 地图初始化
		ks = (EditText) findViewById(R.id.ks);
		js = (EditText) findViewById(R.id.js);
		mMapView = (MapView) findViewById(R.id.map);
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(true);
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

		CharSequence titleLable = "路线规划功能";
		setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			ks.setText(kss);
		}

	};

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			System.out.println(location.getCity() + location.getStreet());

			// TODO Auto-generated method stub
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				new Thread() {
					public void run() {
						kss = location.getAddrStr();
						handler.post(runnableUi);
					}
				}.start();
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				// 添加起点图标
				BitmapDescriptor bmStartss = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_openmap_focuse_mark);
				MarkerOptions sss = new MarkerOptions().position(ll)
						.icon(bmStartss).zIndex(9).draggable(true);
				mBaidumap.addOverlay(sss);
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				locationService.stop();
			}
		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 发起路线规划搜索示例
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		if (ks.getText().toString().length() == 0
				|| js.getText().toString().length() == 0) {
			MyToastUtil.ShowToast(RoutePlanDemo.this, "请输入起点和终点地址");
		} else {
			// 重置浏览节点的路线数据
			route = null;
			mBtnPre.setVisibility(View.INVISIBLE);
			mBtnNext.setVisibility(View.INVISIBLE);
			mBaidumap.clear();
			// 处理搜索按钮响应
			// 设置起终点信息，对于tranist search 来说，城市名无意义
			PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", ks
					.getText().toString());
			PlanNode enNode = PlanNode.withCityNameAndPlaceName("", js
					.getText().toString());

			// 实际使用中请对起点终点城市进行正确的设定

			if (v.getId() == R.id.transit) {
				mSearch.transitSearch((new TransitRoutePlanOption())
						.from(stNode).city("北京").to(enNode));
				nowSearchType = 2;
			}
		}
	}

	/**
	 * 节点浏览示例
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = null;

		if (nowSearchType != 0 && nowSearchType != -1) {
			// 非跨城综合交通
			if (route == null || route.getAllStep() == null) {
				return;
			}
			if (nodeIndex == -1 && v.getId() == R.id.pre) {
				return;
			}
			// 设置节点索引
			if (v.getId() == R.id.next) {
				if (nodeIndex < route.getAllStep().size() - 1) {
					nodeIndex++;
				} else {
					return;
				}
			} else if (v.getId() == R.id.pre) {
				if (nodeIndex > 0) {
					nodeIndex--;
				} else {
					return;
				}
			}
			// 获取节结果信息
			step = route.getAllStep().get(nodeIndex);
			if (step instanceof DrivingRouteLine.DrivingStep) {
				nodeLocation = ((DrivingRouteLine.DrivingStep) step)
						.getEntrance().getLocation();
				nodeTitle = ((DrivingRouteLine.DrivingStep) step)
						.getInstructions();
			} else if (step instanceof WalkingRouteLine.WalkingStep) {
				nodeLocation = ((WalkingRouteLine.WalkingStep) step)
						.getEntrance().getLocation();
				nodeTitle = ((WalkingRouteLine.WalkingStep) step)
						.getInstructions();
			} else if (step instanceof TransitRouteLine.TransitStep) {
				nodeLocation = ((TransitRouteLine.TransitStep) step)
						.getEntrance().getLocation();
				nodeTitle = ((TransitRouteLine.TransitStep) step)
						.getInstructions();
			} else if (step instanceof BikingRouteLine.BikingStep) {
				nodeLocation = ((BikingRouteLine.BikingStep) step)
						.getEntrance().getLocation();
				nodeTitle = ((BikingRouteLine.BikingStep) step)
						.getInstructions();
			}
		} else if (nowSearchType == 0) {
			// 跨城综合交通 综合跨城公交的结果判断方式不一样

			if (massroute == null || massroute.getNewSteps() == null) {
				return;
			}
			if (nodeIndex == -1 && v.getId() == R.id.pre) {
				return;
			}
			boolean isSamecity = nowResultmass.getOrigin().getCityId() == nowResultmass
					.getDestination().getCityId();
			int size = 0;
			if (isSamecity) {
				size = massroute.getNewSteps().size();
			} else {
				for (int i = 0; i < massroute.getNewSteps().size(); i++) {
					size += massroute.getNewSteps().get(i).size();
				}
			}

			// 设置节点索引
			if (v.getId() == R.id.next) {
				if (nodeIndex < size - 1) {
					nodeIndex++;
				} else {
					return;
				}
			} else if (v.getId() == R.id.pre) {
				if (nodeIndex > 0) {
					nodeIndex--;
				} else {
					return;
				}
			}
			if (isSamecity) {
				// 同城
				step = massroute.getNewSteps().get(nodeIndex).get(0);
			} else {
				// 跨城
				int num = 0;
				for (int j = 0; j < massroute.getNewSteps().size(); j++) {
					num += massroute.getNewSteps().get(j).size();
					if (nodeIndex - num < 0) {
						int k = massroute.getNewSteps().get(j).size()
								+ nodeIndex - num;
						step = massroute.getNewSteps().get(j).get(k);
						break;
					}
				}
			}

			nodeLocation = ((MassTransitRouteLine.TransitStep) step)
					.getStartLocation();
			nodeTitle = ((MassTransitRouteLine.TransitStep) step)
					.getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}

		// 移动节点至中心
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(RoutePlanDemo.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon(View v) {
		Intent i = new Intent(RoutePlanDemo.this, luxian_des.class);
		Bundle b = new Bundle();
		b.putSerializable("r", (Serializable) route.getAllStep());
		i.putExtras(b);
		startActivity(i);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

			if (result.getRouteLines().size() > 1) {
				nowResultwalk = result;

				MyTransitDlg myTransitDlg = new MyTransitDlg(
						RoutePlanDemo.this, result.getRouteLines(),
						RouteLineAdapter.Type.WALKING_ROUTE);
				myTransitDlg
						.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
							public void onItemClick(int position) {
								route = nowResultwalk.getRouteLines().get(
										position);
								WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
										mBaidumap);
								mBaidumap.setOnMarkerClickListener(overlay);
								routeOverlay = overlay;
								overlay.setData(nowResultwalk.getRouteLines()
										.get(position));
								overlay.addToMap();
								overlay.zoomToSpan();
							}

						});
				myTransitDlg.show();

			} else if (result.getRouteLines().size() == 1) {
				// 直接显示
				route = result.getRouteLines().get(0);
				WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
						mBaidumap);
				mBaidumap.setOnMarkerClickListener(overlay);
				routeOverlay = overlay;
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();

			} else {
				Log.d("route result", "结果数<0");
				return;
			}

		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

			if (result.getRouteLines().size() > 1) {
				nowResultransit = result;

				MyTransitDlg myTransitDlg = new MyTransitDlg(
						RoutePlanDemo.this, result.getRouteLines(),
						RouteLineAdapter.Type.TRANSIT_ROUTE);

				myTransitDlg
						.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
							public void onItemClick(int position) {
								route = nowResultransit.getRouteLines().get(
										position);
								TransitRouteOverlay overlay = new MyTransitRouteOverlay(
										mBaidumap);
								mBaidumap.setOnMarkerClickListener(overlay);
								routeOverlay = overlay;
								overlay.setData(nowResultransit.getRouteLines()
										.get(position));
								overlay.addToMap();
								overlay.zoomToSpan();
							}

						});
				myTransitDlg.show();

			} else if (result.getRouteLines().size() == 1) {
				// 直接显示
				route = result.getRouteLines().get(0);
				TransitRouteOverlay overlay = new MyTransitRouteOverlay(
						mBaidumap);
				mBaidumap.setOnMarkerClickListener(overlay);
				routeOverlay = overlay;
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();

			} else {
				Log.d("route result", "结果数<0");
				return;
			}

		}
	}

	@Override
	public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点模糊，获取建议列表
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nowResultmass = result;

			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

			// 列表选择
			MyTransitDlg myTransitDlg = new MyTransitDlg(RoutePlanDemo.this,
					result.getRouteLines(),
					RouteLineAdapter.Type.MASS_TRANSIT_ROUTE);
			nowResultmass = result;
			myTransitDlg
					.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
						public void onItemClick(int position) {
							MyMassTransitRouteOverlay overlay = new MyMassTransitRouteOverlay(
									mBaidumap);
							mBaidumap.setOnMarkerClickListener(overlay);
							routeOverlay = overlay;
							massroute = nowResultmass.getRouteLines().get(
									position);
							overlay.setData(nowResultmass.getRouteLines().get(
									position));

							MassTransitRouteLine line = nowResultmass
									.getRouteLines().get(position);
							overlay.setData(line);
							if (nowResultmass.getOrigin().getCityId() == nowResultmass
									.getDestination().getCityId()) {
								// 同城
								overlay.setSameCity(true);
							} else {
								// 跨城
								overlay.setSameCity(false);

							}
							overlay.addToMap();
							overlay.zoomToSpan();
						}

					});
			myTransitDlg.show();
		}

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;

			if (result.getRouteLines().size() > 1) {
				nowResultdrive = result;

				MyTransitDlg myTransitDlg = new MyTransitDlg(
						RoutePlanDemo.this, result.getRouteLines(),
						RouteLineAdapter.Type.DRIVING_ROUTE);
				myTransitDlg
						.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
							public void onItemClick(int position) {
								route = nowResultdrive.getRouteLines().get(
										position);
								DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(
										mBaidumap);
								mBaidumap.setOnMarkerClickListener(overlay);
								routeOverlay = overlay;
								overlay.setData(nowResultdrive.getRouteLines()
										.get(position));
								overlay.addToMap();
								overlay.zoomToSpan();
							}

						});
				myTransitDlg.show();

			} else if (result.getRouteLines().size() == 1) {
				route = result.getRouteLines().get(0);
				DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(
						mBaidumap);
				routeOverlay = overlay;
				mBaidumap.setOnMarkerClickListener(overlay);
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();
				mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			} else {
				Log.d("route result", "结果数<0");
				return;
			}

		}
	}

	@Override
	public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

			if (result.getRouteLines().size() > 1) {
				nowResultbike = result;

				MyTransitDlg myTransitDlg = new MyTransitDlg(
						RoutePlanDemo.this, result.getRouteLines(),
						RouteLineAdapter.Type.DRIVING_ROUTE);
				myTransitDlg
						.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
							public void onItemClick(int position) {
								route = nowResultbike.getRouteLines().get(
										position);
								BikingRouteOverlay overlay = new MyBikingRouteOverlay(
										mBaidumap);
								mBaidumap.setOnMarkerClickListener(overlay);
								routeOverlay = overlay;
								overlay.setData(nowResultbike.getRouteLines()
										.get(position));
								overlay.addToMap();
								overlay.zoomToSpan();
							}

						});
				myTransitDlg.show();

			} else if (result.getRouteLines().size() == 1) {
				route = result.getRouteLines().get(0);
				BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
				routeOverlay = overlay;
				mBaidumap.setOnMarkerClickListener(overlay);
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();
				mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			} else {
				Log.d("route result", "结果数<0");
				return;
			}

		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyBikingRouteOverlay extends BikingRouteOverlay {
		public MyBikingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}

	}

	private class MyMassTransitRouteOverlay extends MassTransitRouteOverlay {
		public MyMassTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}

	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mSearch != null) {
			mSearch.destroy();
		}
		mMapView.onDestroy();
		super.onDestroy();
	}

	// 响应DLg中的List item 点击
	interface OnItemInDlgClickListener {
		public void onItemClick(int position);
	}

	// 供路线选择的Dialog
	class MyTransitDlg extends Dialog {

		private List<? extends RouteLine> mtransitRouteLines;
		private ListView transitRouteList;
		private RouteLineAdapter mTransitAdapter;

		OnItemInDlgClickListener onItemInDlgClickListener;

		public MyTransitDlg(Context context, int theme) {
			super(context, theme);
		}

		public MyTransitDlg(Context context,
				List<? extends RouteLine> transitRouteLines,
				RouteLineAdapter.Type type) {
			this(context, 0);
			mtransitRouteLines = transitRouteLines;
			mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines,
					type);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_transit_dialog);

			transitRouteList = (ListView) findViewById(R.id.transitList);
			transitRouteList.setAdapter(mTransitAdapter);

			transitRouteList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							onItemInDlgClickListener.onItemClick(position);
							mBtnPre.setVisibility(View.VISIBLE);
							mBtnNext.setVisibility(View.VISIBLE);
							dismiss();

						}
					});
		}

		public void setOnItemInDlgClickLinster(
				OnItemInDlgClickListener itemListener) {
			onItemInDlgClickListener = itemListener;
		}

	}
}