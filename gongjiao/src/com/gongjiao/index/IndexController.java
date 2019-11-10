package com.gongjiao.index;

import com.jfinal.core.Controller;

/**
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
		render("user_login.jsp");
	}
	
	public void exit(){
		redirect("/user_login.jsp");
	}
}





