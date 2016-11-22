package hyrs.controller;

import hyrs.entity.Information;
import hyrs.entity.Person;
import hyrs.entity.User;
import hyrs.interceptor.LoginInterceptor;
import hyrs.interceptor.PowerInterceptor;
import hyrs.util.IDNumber;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class InformationController extends Controller {
	
	/**
	 * 信息变更
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class})
	public void infoAlter() {
		List<Person> persons=Person.dao.find("select * from person where number=?",getPara("personNumber"));
		if (!new IDNumber().availableIDNumber(getPara("personNumber").trim())){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","证件号码错误，请重新输入!");
			render("/error.html");
		}else if(persons.size()!=0 && !getPara("personNumber").equals(getPara("pnumber"))){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","该证件号码数据库中已存在，无法修改成该证件号码!");
			render("/error.html");
		}else if(!getPara("personPhone").trim().matches("^\\d{11}")){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","联系电话错误，请重新输入!");
			render("/error.html");
		}else if(getPara("personAddress").equals("")){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","联系地址不能为空，请重新输入!");
			render("/error.html");
		}else if(!getPara("personEnd").trim().matches("^\\d{8}") || getPara("personEnd").equals("")){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","证件到期日错误，请重新输入!");
			render("/error.html");
		}else{
			if(Person.dao.findById(getPara("personId"))
					.set("name", getPara("personName").trim())
					.set("number", getPara("personNumber").trim())
					.set("phone", getPara("personPhone").trim())
					.set("address", getPara("personAddress").trim())
					.set("endDate", getPara("personEnd").trim())
					.set("remark", getPara("personRemark").trim())
					//.set("locationId",((User) getSessionAttr("user")).getInt("locationId"))
					.update()){
				Information i=new Information();
				i.set("personId", getPara("personId"))
				 .set("name", getPara("pname").trim())
				 .set("number", getPara("pnumber").trim())
				 .set("phone", getPara("pp").trim())
				 .set("address", getPara("pa").trim())
				 .set("time",  new Date())
				 .set("locationId",((User) getSessionAttr("user")).getInt("locationId"))
				 .set("userId",((User) getSessionAttr("user")).getInt("id"))
				 .set("remark",getPara("pr").trim());
				 if(i.save()){
					 redirect("/person/insureUI");
				 }else{
					 setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
					 setAttr("error","出现异常错误i，请再次输入!");
					 render("/error.html");
				 }
			}else{
				setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
				setAttr("error","出现异常错误p，请再次输入!");
				render("/error.html");
			}
		}
	}

	/**
	 * 信息变更页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class})
	public void infoAlterUI() {
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		Person person=Person.dao.findFirst("select * from person where id=?",getPara("id"));
		setAttr("personName",person.getStr("name"));
		setAttr("personNumber",person.getStr("number"));
		setAttr("personPhone",person.getStr("phone"));
		setAttr("personAddress",person.getStr("address"));
		setAttr("personEnd",person.getStr("endDate"));
		setAttr("personBase",person.getStr("base"));
		setAttr("personRadio",person.getStr("radio"));
		setAttr("personId",person.getInt("id"));
		
		render("/infoAlter.html");
	}
	
	/**
	 * 信息查询
	 * */
	@Before(LoginInterceptor.class)
	public void infoQuery() {

		if (getPara("query")==null){
			setSessionAttr("query",getSessionAttr("query"));
		}else{
			setSessionAttr("query",getPara("query"));
			
		}
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		setAttr("infoPage", Information.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("query").toString()));
		setAttr("queryString",getSessionAttr("query").toString());
		
		render("/info.html");
	}
	
	/**
	/**
	 * 信息页面
	 * */
	@Before(LoginInterceptor.class)
	public void infoUI() {
		setSessionAttr("query","");
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("infoPage", Information.dao.paginate(getParaToInt(0, 1), 15));
		render("/info.html");
	}
}


