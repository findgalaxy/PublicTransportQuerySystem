package com.gongjiao.pojo;

import java.io.UnsupportedEncodingException;

import com.jfinal.core.Controller;

/**
 */
public class UserController extends Controller {
	// 用户注册
	public void addUser() throws UnsupportedEncodingException {
		getModel(user.class).save();
		renderJson("success");
	}
	public void addUsers() throws UnsupportedEncodingException {
		getModel(user.class).save();
		findall();
	}

	// 用户更新
	public void update() throws UnsupportedEncodingException {
		getModel(user.class).update();
		renderJson("success");
	}

	// 登录
	public void ulogin() {
		user m = user.te.findManagerByusername(getPara(0));
		if (m == null)
			renderJson("1");
		else {
			if (m.getStr("pass").equals(getPara(1))) {
				setSessionAttr("u", m);
				renderJson(m);
			} else
				renderJson("1");
		}
	}
	
	public void exit(){
		user m = user.te.findById(getPara("id"));
		m.set("count", (m.getInt("count")-1)+"").update();
		renderJson("1");
		
	}
	
	public void zhaohui(){
		user m = user.te.findManagerByusername(getPara("username"));
		renderJson(m.getStr("pass"));
	}

	// 更新密码
	public void updateU() throws UnsupportedEncodingException {
		getModel(user.class).update();
		renderJson("success");
	}

	public void getalluser() {
		renderJson(user.te.find("select * from user "));
	}
	public void getallteacher() {
		renderJson(user.te.find("select * from user where type='教师'"));
	}

	public void findall() {
		setAttr("list", user.te.find("select * from user where type!='学生'"));
		render("/user_list.jsp");
	}
	
	public void fenjin(){
		user.te.findById(getPara("id")).set("type","封禁").update();
		findall();
	}

	public void toup() {
		setAttr("u", user.te.findById(getPara("id")));
		render("/user_update.jsp");
	}

	public void updateUser() {
		getModel(user.class).update();
		findall();
	}

	public void del() {
		user.te.deleteById(getPara("id"));
		renderJson("1");
	}
	
	
	
}
