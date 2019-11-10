package com.example.gongjiao;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.gongjiao.adapter.CommonAdapter;
import com.example.gongjiao.adapter.ViewHolder;

public class luxian_des extends Activity{
	
	List<Object> route;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.luxian_des);
		listView=(ListView)findViewById(R.id.listView);
		route= (List<Object>) getIntent().getExtras().getSerializable("r");
		listView.setAdapter(new CommonAdapter<Object>(luxian_des.this,route,R.layout.luxian_item) {

			@Override
			public void convert(ViewHolder helper, Object item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.des,  ((TransitRouteLine.TransitStep) item)
						.getInstructions());
			}
		});
	}

}
