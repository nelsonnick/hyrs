package hyrs.controller;

import hyrs.entity.Location;
import hyrs.interceptor.LoginInterceptor;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class LocationController extends Controller {
	
	/**
	 * 社保中心查询
	 * */
	@Before(LoginInterceptor.class)
	public void locationQuery() {
		
		if (getPara("query")==null){
			setSessionAttr("query",getSessionAttr("query"));
		}else{
			setSessionAttr("query",getPara("query"));
			
		}
		
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("query").toString()));
		setAttr("queryString",getSessionAttr("query").toString());

		render("/location.html");
	}
	
	/**
	 * 社保中心页面
	 * */
	@Before(LoginInterceptor.class)
	public void locationUI() {
		setSessionAttr("query","");
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 15));
		render("/location.html");
	}
	
	/**
	 * 社保中心新增页面
	 * */
	@Before(LoginInterceptor.class)
	public void locationNewUI() {
	
		render("/locationNew.html");
		
	}

	/**
	 * 社保中心新增
	 * */
	@Before(LoginInterceptor.class)
	public void locationNew() {
		List<Location> locations=Location.dao.find("select * from location where name=?",getPara("locationName"));
		if(locations.size()!=0){
			setAttr("error","该社保中心数据库中已存在，请核实后再输入!");
			render("/error2.html");
		}else if (!getPara("locationPhone").matches("\\d{8}")){
			setAttr("error","联系电话错误，请核实后再输入!");
			render("/error2.html");
		}else{
			Location l =new Location();
			l.set("name", getPara("locationName").trim())
			 .set("phone", getPara("locationPhone").trim())
			 .set("address", getPara("locationAddress").trim());
			if (l.save()){
				redirect("/location/locationUI");
			}else{
				setAttr("error","出现异常错误u，请再次输入!");
				render("/error2.html");
			}
		}
	}
	
	/**
	 * 社保中心修改页面
	 * */
	@Before(LoginInterceptor.class)
	public void locationAlterUI() {
		
		Location location=Location.dao.findById(getPara("id"));
		setAttr("locationName",location.getStr("name"));
		setAttr("locationPhone",location.getStr("phone"));
		setAttr("locationAddress",location.getStr("address"));
		setAttr("locationId",location.getInt("id"));
		render("/locationAlter.html");
		
	}

	/**
	 * 社保中心修改
	 * */
	@Before(LoginInterceptor.class)
	public void locationAlter() {
		if (!getPara("locationPhone").matches("\\d{8}")){
			setAttr("error","联系电话错误，请核实后再输入!");
			render("/error2.html");
		}else{
			if (Location.dao.findById(getPara("locationId")).set("name", getPara("locationName")).set("phone", getPara("locationPhone")).set("address", getPara("locationAddress")).update()){
				redirect("/location/locationUI");
			}else{
				setAttr("error","出现异常错误l，请再次输入!");
				render("/error2.html");
			}
		}
	}
	
}


