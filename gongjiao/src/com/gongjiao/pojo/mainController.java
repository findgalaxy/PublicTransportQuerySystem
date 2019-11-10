package com.gongjiao.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.gongjiao.util.Time;
import com.jfinal.core.Controller;

public class mainController extends Controller{
	
	
	
	
	public void gxwz(){
		getModel(gjsj.class).update();
		renderJson("1");
	}
	public void sjlogin(){
		gjsj g=gjsj.dao.findFirst("select * from gjsj where username=?",getPara("username"));
		if(g==null){
			renderJson("1");
		}else{
			if(g.getStr("pass").equals(getPara("pass"))){
				renderJson(g);
			}else{
				renderJson("1");
			}
		}
	}
	
	public void addgjsj(){
		getModel(gjsj.class).save();
		findgjsj();
	}
	public void findgjsj(){
		setAttr("list", gjsj.dao.find("select * from gjsj"));
		render("/gjsj_list.jsp");
	}
	
	public void delgjsj(){
		gjsj.dao.deleteById(getPara("id"));
		findgjsj();
	}
	
	public void getlx(){
		renderJson(luxian.dao.find("select * from luxian where name=?",getPara("key")));
	}
	public void getzd(){
		renderJson(zd.dao.find("select * from zhandian where lid=?",getPara("lid")));
	}
	public void addlx(){
		getModel(luxian.class).save();
		findlx();
	}
	
	public void findlx(){
		setAttr("list",luxian.dao.find("select * from luxian"));
		render("/luxian_list.jsp");
	}
	
	public void delx(){
		luxian.dao.deleteById(getPara("id"));
		findlx();
	}
	
	
	public void addsc(){
		getModel(sc.class).save();
		renderJson("1");
	}
	
	public void getmysc(){
		renderJson(sc.dao.find("select * from sc where uid=?",getPara("uid")));
	}
	public void qxsc(){
		sc.dao.deleteById(getPara("id"));
		renderJson("1");
	}
	
	
public void findbyname(){
	renderJson(zd.dao.find("select * from zhandian where name like '%"+getPara("key")+"%'"));
}
	public void findzd(){
		setAttr("id", getPara("id"));
		setAttr("list", zd.dao.find("select * from zhandian where lid=?",getPara("id")));
		render("/zd_list.jsp");
	}
	
	public void toaddzd(){
		setAttr("id", getPara("id"));
		render("/zd_add.jsp");
	}
	
	public void addzd(){
		getModel(zd.class).save();
		setAttr("id",getPara("zd.lid"));
		setAttr("list", zd.dao.find("select * from zhandian where lid=?",getPara("zd.lid")));
		render("/zd_list.jsp");
	}
	public void delzd(){
		zd z=zd.dao.findById(getPara("id"));
		setAttr("id",z.getStr("lid"));
		setAttr("list", zd.dao.find("select * from zhandian where lid=?",z.getStr("lid")));
		z.delete();
		render("/zd_list.jsp");
	}

}
