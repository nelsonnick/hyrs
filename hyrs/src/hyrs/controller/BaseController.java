package hyrs.controller;

import java.util.List;

import hyrs.entity.Base;
import hyrs.entity.Person;
import hyrs.interceptor.LoginInterceptor;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class BaseController extends Controller {
	

	@Before(LoginInterceptor.class)
	public void baseAlter() {
		
		if (getPara("base").toString().equals("")||getPara("baseLow").toString().equals("")||getPara("baseHigh").toString().equals("")||getPara("medicalLow").toString().equals("")||getPara("medicalHigh").toString().equals("")){
			setAttr("error","输入内容不完整，请再次输入!");
			render("/error2.html");
		}else if(!(getPara("base").trim().matches("^\\d{4}")||getPara("base").trim().matches("^\\d{4}"))){
			setAttr("error","社平工资错误，请再次输入!");
			render("/error2.html");
		}else if(!(getPara("baseLow").trim().matches("^\\d{4}")||getPara("baseLow").trim().matches("^\\d{4}"))){
			setAttr("error","最低基数错误，请再次输入!");
			render("/error2.html");
		}else if(!(getPara("baseHigh").trim().matches("^\\d{4}")||getPara("baseHigh").trim().matches("^\\d{4}"))){
			setAttr("error","最高基数错误，请再次输入!");
			render("/error2.html");
		}else{
			if (Base.dao.findById("1")
					.set("base", getPara("base"))
					.set("baseLow", getPara("baseLow"))
					.set("baseHigh", getPara("baseHigh"))
					.set("medicalLow", getPara("medicalLow"))
					.set("medicalHigh", getPara("medicalHigh"))
					.update()){
				redirect("/user/userUI");
			}else{
				setAttr("error","出现异常错误b，请再次输入!");
				render("/error2.html");
			}
		}
	}
	
	@Before(LoginInterceptor.class)
	public void baseUI() {
		setAttr("id",Base.dao.findById("1").getInt("id"));
		setAttr("base",Base.dao.findById("1").getStr("base"));
		setAttr("baseLow",Base.dao.findById("1").getStr("baseLow"));
		setAttr("baseHigh",Base.dao.findById("1").getStr("baseHigh"));
		setAttr("medicalLow",Base.dao.findById("1").getStr("medicalLow"));
		setAttr("medicalHigh",Base.dao.findById("1").getStr("medicalHigh"));
		render("/base.html");
	}

	@Before(LoginInterceptor.class)
	public void baseChange() {
		List<Person> p = Person.dao.find("select * from person where base<>'无' and base<>'已退休'");
		for(int i=0;i<p.size();i++){
			if (Integer.parseInt((p.get(i).get("base").toString()))<Integer.parseInt(Base.dao.findById("1").getStr("baseLow").toString())){
				p.get(i).set("base", Base.dao.findById("1").getStr("baseLow").toString()).update();
			}
		}
		redirect("/base/baseUI");
	}
}


