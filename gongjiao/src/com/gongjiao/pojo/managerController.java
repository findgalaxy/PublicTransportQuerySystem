package com.gongjiao.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.core.Controller;

/**
 * BlogController 所有 sql 与业务逻辑写在 Model 或 Service 中
 */
public class managerController extends Controller {

	// 登录
	public void logins() {
		manager m = manager.te.findManagerByusername(getPara("username"));
		if (m != null) {
			if (m.getStr("pass").equals(getPara("pass"))) {
				setSessionAttr("m", m);
				render("/manager_index.jsp");
			} else
				render("/manager_login.jsp");
		}else
			render("/manager_login.jsp");
	}

	// 更新密码
	public void updateManager() {
		manager m = getSessionAttr("m");
		System.out.println(m.getStr("pass"));
		if (getPara("oldpass").equals(m.getStr("pass"))) {
			manager.te.findById(getPara("id")).set("pass", getPara("newpass")).update();
			redirect("/manager_login.jsp");
		} else {
			redirect("/manager_login.jsp");
		}

	}

	// 退出
	public void exit() {
		removeSessionAttr("m");
		render("/manager_login.jsp");
	}

}
