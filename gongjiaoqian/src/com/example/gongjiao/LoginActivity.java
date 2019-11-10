package com.example.gongjiao;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.gongjiao.pojo.User;
import com.example.gongjiao.util.MyApplication;
import com.example.gongjiao.util.MyToastUtil;
import com.example.gongjiao.util.SharedPreferencesUtils;
import com.example.gongjiao.util.UserClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * @author Administrator
 *
 */
public class LoginActivity extends Activity {
	private ImageView loginImage;
	private TextView regist;
	private TextView topText;
	private TextPaint tp;
	private Button loginbtn, wwl;
	private Button jinru;
	private EditText username, tel, qq;
	private EditText password;
	private Drawable mIconPerson;
	private Drawable mIconLock;
	ImageView yzm;
	EditText key;
	Button yz;
	TextView zhaohui;
	String url="user/ulogin/";//Ĭ�ϵ�¼��·����Ϣ
	RadioGroup type;
	String types="用户";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);// ������Ļ��ת
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);
		type=(RadioGroup)findViewById(R.id.type);
		type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.yh)
					types="用户";
				else
					types="司机";
			}
		});
		regist = (TextView) findViewById(R.id.regist);
		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						RegistActivity.class));
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
		username = (EditText) findViewById(R.id.username);
		username.setText(SharedPreferencesUtils.getParam(LoginActivity.this,"username", "").toString());
		username.setCompoundDrawables(mIconPerson, null, null, null);
		password = (EditText) findViewById(R.id.pass);
		password.setText(SharedPreferencesUtils.getParam(LoginActivity.this, "pass", "").toString());
		password.setCompoundDrawables(mIconLock, null, null, null);
		loginbtn = (Button) findViewById(R.id.loginbtn);
		init();

	}

	@SuppressWarnings("deprecation")
	public void init() {
		loginbtn = (Button) findViewById(R.id.loginbtn);
		loginbtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.getBackground().setAlpha(20);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.getBackground().setAlpha(255);
					if(types.equals("用户")){
						UserClient.get(url
								+ username.getText().toString() + "-"
								+ password.getText().toString(), null,
								new AsyncHttpResponseHandler() {
									@Override
									@Deprecated
									public void onSuccess(int statusCode,
											String content) {
										// TODO Auto-generated method stub
										super.onSuccess(statusCode, content);
										if (!content.equals("1")) {
												User u=JSON.parseObject(content, User.class);
												MyApplication.getApp().setUser(u);
													Intent i = new Intent(
															LoginActivity.this,
															MainActivity.class);
													startActivity(i);
												finish();
										}else{
											MyToastUtil.ShowToast(LoginActivity.this, "登录失败");
										}
									}
								});
					}else{
						RequestParams ps=new RequestParams();
						ps.add("username",username.getText().toString());
						ps.add("pass", password.getText().toString());
						UserClient.get("main/sjlogin", ps, new AsyncHttpResponseHandler(){
							public void onSuccess(int statusCode, String content) {
								if(content.equals("1")){
									MyToastUtil.ShowToast(LoginActivity.this, "失败");
								}else{
									User u=JSON.parseObject(content,User.class);
									MyApplication.getApp().setUser(u);
									Intent i = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(i);
								finish();
								}
							};
						});
					}
				}
					

				return true;
			}

		});

	}

}
