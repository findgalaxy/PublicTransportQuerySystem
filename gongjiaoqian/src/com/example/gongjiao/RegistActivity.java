package com.example.gongjiao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gongjiao.util.UserClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegistActivity extends Activity {
	private LinearLayout back;
	private EditText username, nickname;
	private EditText pass;
	private EditText queding;
	private Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_regist);
		pass = (EditText) findViewById(R.id.pass);
		username = (EditText) findViewById(R.id.username);
		nickname = (EditText) findViewById(R.id.nickname);
		queding = (EditText) findViewById(R.id.queding);
		login = (Button) findViewById(R.id.tijiao);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (username.getText().equals("")
						|| nickname.getText().equals("")
						|| pass.getText().equals("")
						|| queding.getText().equals("")) {
					Toast.makeText(RegistActivity.this, "信息不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					if (pass.getText().toString()
							.equals(queding.getText().toString())) {
						RequestParams ps = new RequestParams();
						ps.add("user.username", username.getText().toString());
						ps.add("user.nickname", nickname.getText().toString());
						ps.add("user.pass", pass.getText().toString());
						UserClient.get("user/addUser", ps,
								new AsyncHttpResponseHandler() {
									@Override
									@Deprecated
									public void onSuccess(int statusCode,
											String content) {
										// TODO Auto-generated method stub
										super.onSuccess(statusCode, content);
										Toast.makeText(RegistActivity.this,
												"成功", Toast.LENGTH_SHORT)
												.show();
										finish();
									}

									@Override
									@Deprecated
									public void onFailure(Throwable error,
											String content) {
										// TODO Auto-generated method stub
										super.onFailure(error, content);
										Toast.makeText(RegistActivity.this,
												"失败", Toast.LENGTH_SHORT)
												.show();
									}
								});
					} else {
						Toast.makeText(RegistActivity.this, "两次密码输入不同",
								Toast.LENGTH_SHORT).show();
					}
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

}
