package com.gongjiao.common;

import com.gongjiao.index.IndexController;
import com.gongjiao.pojo.UserController;
import com.gongjiao.pojo.gjsj;
import com.gongjiao.pojo.luxian;
import com.gongjiao.pojo.mainController;
import com.gongjiao.pojo.manager;
import com.gongjiao.pojo.managerController;
import com.gongjiao.pojo.user;
import com.gongjiao.pojo.zd;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**
 * API引导式配�?
 */
public class DemoConfig extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取�?
		loadPropertyFile("a_little_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setViewType(ViewType.JSP);
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class); // 第三个参数为该Controller的视图存放路�?
		me.add("/user", UserController.class);
		me.add("/manager", managerController.class);
		me.add("/main", mainController.class);
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("user", user.class);
		arp.addMapping("manager", manager.class);
		arp.addMapping("zhandian", zd.class);
		arp.addMapping("luxian", luxian.class);
		arp.addMapping("gjsj", gjsj.class);}

	/**
	 * 配置全局拦截
	 */
	public void configInterceptor(Interceptors me) {

	}

	/**
	 * 配置处理�?
	 */
	public void configHandler(Handlers me) {

	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项�? 运行�?main
	 * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不�?��要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebContent", 8007, "/", 5);
	}
}
