package com.example.gongjiao;


import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gongjiao.pojo.User;
import com.example.gongjiao.util.HttpUtils;
import com.example.gongjiao.util.MyApplication;
import com.example.gongjiao.util.Url;

public class update extends Activity {
	private LinearLayout back;
	private Button login;
	private EditText nickname, pass;
	User u;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update);
		u = MyApplication.getApp().getUser();
		nickname = (EditText) findViewById(R.id.nickname);
		pass = (EditText) findViewById(R.id.pass);
		nickname.setText(u.getNickname());
		pass.setText(u.getPass());
		login = (Button) findViewById(R.id.tijiao);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (nickname.getText().equals("")
						|| pass.getText().toString().length() == 0) {
					Toast.makeText(update.this, "信息不能空", Toast.LENGTH_SHORT)
							.show();
				} else {
					useradd us = new useradd();
					us.execute(u.getId(), nickname.getText().toString(), pass
							.getText().toString());
				}
			}
		});
		back = (LinearLayout) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
	}

	/**
	 * 注锟斤拷
	 * 
	 * @author Administrator
	 * 
	 */
	public class useradd extends AsyncTask<String, Void, String> {

		ProgressDialog p = new ProgressDialog(update.this,
				ProgressDialog.STYLE_SPINNER);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p.setMessage("Loading....");
			p.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String result = null;
			result = HttpUtils.doGet(Url.url() + "user/updateU/" + arg0[0]
					+ "-" + URLEncoder.encode(arg0[1]) + "-" + arg0[2]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			p.dismiss();
			if (result.equals("success")) {
				Toast.makeText(update.this, "鎴愬姛", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(update.this, "澶辫触", Toast.LENGTH_SHORT).show();
			}
		}

	}
}