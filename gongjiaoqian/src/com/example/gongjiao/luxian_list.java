package com.example.gongjiao;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.gongjiao.adapter.CommonAdapter;
import com.example.gongjiao.adapter.ViewHolder;
import com.example.gongjiao.pojo.luxian;
import com.example.gongjiao.util.UserClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class luxian_list extends Activity {

	List<luxian> list;
	EditText key;
	Button cx;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.luxian_list);
		key = (EditText) findViewById(R.id.key);
		cx = (Button) findViewById(R.id.cx);
		cx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RequestParams ps = new RequestParams();
				ps.add("key", key.getText().toString());
				UserClient.get("main/getlx", ps,
						new AsyncHttpResponseHandler() {
							@Override
							@Deprecated
							public void onSuccess(int statusCode, String content) {
								// TODO Auto-generated method stub
								super.onSuccess(statusCode, content);
								list = JSON.parseArray(content, luxian.class);
								listView.setAdapter(new CommonAdapter<luxian>(
										luxian_list.this, list,
										R.layout.lx_item) {

									@Override
									public void convert(ViewHolder helper,
											luxian item) {
										// TODO Auto-generated method stub
										helper.setText(R.id.name,
												item.getName());
									}
								});
							}
						});
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				startActivity(new Intent(luxian_list.this, zhandian_cx.class)
						.putExtra("lid", list.get(arg2).getId()));
			}
		});
	}

}
