package hyrs.controller;

import hyrs.entity.Base;
import hyrs.entity.Operate;
import hyrs.entity.Person;
import hyrs.entity.User;
import hyrs.interceptor.InsureInterceptor;
import hyrs.interceptor.LoginInterceptor;
import hyrs.interceptor.PowerInterceptor;
import hyrs.interceptor.RetireInterceptor;
import hyrs.util.IDNumber;
import hyrs.util.MonthTime;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class PersonController extends Controller {

	/**
	 * 社保新增
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureNew() {

		List<Person> persons = Person.dao.find(
				"select * from person where number=?", getPara("personNumber"));
		if (!new IDNumber().availableIDNumber(getPara("personNumber").trim())) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件号码错误，请重新输入!");
			render("/error.html");
		} else if (persons.size() != 0) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "该证件号码数据库中已存在，请办理续交或其他业务!");
			render("/error.html");
		} else if (!getPara("personPhone").trim().matches("^\\d{11}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系电话错误，请重新输入!");
			render("/error.html");
		} else if (!getPara("personEnd").trim().matches("^\\d{8}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件到期日错误，请重新输入!");
			render("/error.html");
		} else if (getPara("personAddress").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系地址未填写，请重新输入!");
			render("/error.html");
		} else if (getPara("personName").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "人员姓名未填写，请重新输入!");
			render("/error.html");
		}  else if (!getPara("personBaseAfter").trim().matches("^\\d{4}")
				|| getParaToInt("personBaseAfter") < Integer.parseInt(Base.dao
						.findById("1").getStr("baseLow"))
				|| getParaToInt("personBaseAfter") > Integer.parseInt(Base.dao
						.findById("1").getStr("baseHigh"))) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "缴费基数错误，请重新输入!");
			render("/error.html");
		} else {
			Person p = new Person();
			p.set("name", getPara("personName").trim())
					.set("number", getPara("personNumber").trim())
					.set("phone", getPara("personPhone").trim())
					.set("address", getPara("personAddress").trim())
					.set("endDate", getPara("personEnd").trim())
					.set("base", getPara("personBaseAfter").trim())
					.set("radio", getPara("personRadioAfter").trim())
					.set("remark", "")
					.set("locationId",
							((User) getSessionAttr("user"))
									.getInt("locationId"));
			if (p.save()) {
				Operate o = new Operate();
				Person pp = Person.dao.findFirst(
						"select * from person where number=?",
						getPara("personNumber"));
				o.set("personId", pp.getInt("id"))
						.set("time", new Date())
						.set("month", new MonthTime().GetMonth())
						.set("baseAfter", getPara("personBaseAfter").trim())
						.set("radioAfter", getPara("personRadioAfter").trim())
						.set("baseBefore", "无")
						.set("radioBefore", "无")
						.set("content", "社保增员")
						.set("remark", getPara("personRemark").trim())
						.set("locationId",
								((User) getSessionAttr("user"))
										.getInt("locationId"))
						.set("userId",
								((User) getSessionAttr("user")).getInt("id"));
				if (o.save()) {
					redirect("/person/insureUI");
				} else {
					setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
					setAttr("error", "出现异常错误o，请再次输入!");
					render("/error.html");
				}
			} else {
				setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
				setAttr("error", "出现异常错误p，请再次输入!");
				render("/error.html");
			}
		}
	}

	/**
	 * 社保新增页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureNewUI() {
		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("baseLow", Base.dao.findById("1").getStr("baseLow"));
		render("/insureNew.html");
	}

	/**
	 * 社保续交
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureAdd() {

		if (!getPara("personBaseAfter").trim().matches("^\\d{4}")
				|| getParaToInt("personBaseAfter") < Integer.parseInt(Base.dao
						.findById("1").getStr("baseLow"))
				|| getParaToInt("personBaseAfter") > Integer.parseInt(Base.dao
						.findById("1").getStr("baseHigh"))) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "缴费基数错误，请重新输入!");
			render("/error.html");
		} else if (!getPara("pp").trim().matches("^\\d{11}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系电话错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if((Person.dao.findById(getPara("personId")).getStr("base").equals(getPara("personBaseAfter"))||
				Person.dao.findById(getPara("personId")).getStr("radio").equals(getPara("personRadioAfter")))){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "该市民已办理参保业务，无需办理续交业务");
			render("/error.html");	
		} else if (!getPara("pend").trim().matches("^\\d{8}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件到期日错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if (getPara("pa").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系地址未填写，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else {
			if (Person.dao
					.findById(getPara("personId"))
					.set("base", getPara("personBaseAfter"))
					.set("radio", getPara("personRadioAfter"))
					.set("locationId",
							((User) getSessionAttr("user"))
									.getInt("locationId")).update()) {
				Operate o = new Operate();
				o.set("personId", getPara("personId"))
						.set("time", new Date())
						.set("month", new MonthTime().GetMonth())
						.set("baseBefore", getPara("pbb"))
						.set("radioBefore", getPara("prb"))
						.set("baseAfter", getPara("personBaseAfter").trim())
						.set("radioAfter", getPara("personRadioAfter").trim())
						.set("content", "社保增员")
						.set("remark", getPara("operateRemark"))
						.set("locationId",
								((User) getSessionAttr("user"))
										.getInt("locationId"))
						.set("userId",
								((User) getSessionAttr("user")).getInt("id"));
				if (o.save()) {
					redirect("/person/insureUI");
				} else {
					setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
					setAttr("error", "出现异常错误o，请再次输入!");
					render("/error.html");
				}
			} else {
				setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
				setAttr("error", "出现异常错误p，请再次输入!");
				render("/error.html");
			}
		}

	}

	/**
	 * 社保续交页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureAddUI() {

		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		Person person = Person.dao.findFirst("select * from person where id=?",
				getPara("id"));
		if(!(person.getStr("base").equals("无")
				||person.getStr("radio").equals("无")
				||person.getStr("base").equals("已退休")
				||person.getStr("radio").equals("已退休")
				)){
			setAttr("error", "该市民已办理增员业务，无法再次办理");
			render("/error.html");
		}else{
			setAttr("personName", person.getStr("name"));
			setAttr("personNumber", person.getStr("number"));
			setAttr("personPhone", person.getStr("phone"));
			setAttr("personAddress", person.getStr("address"));
			setAttr("personEnd", person.getStr("endDate"));
			setAttr("personBaseBefore", person.getStr("base"));
			setAttr("personRadioBefore", person.getStr("radio"));
			setAttr("personId", person.getInt("id"));
			setAttr("baseLow", Base.dao.findById("1").getStr("baseLow"));
			render("/insureAdd.html");
		}
	}

	/**
	 * 社保减员
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureReduce() {
		if (!getPara("pp").trim().matches("^\\d{11}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系电话错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if((Person.dao.findById(getPara("personId")).getStr("base").equals("无")||
				Person.dao.findById(getPara("personId")).getStr("radio").equals("无"))){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "该市民已办理减员业务，无需再次办理");
			render("/error.html");	
		} else if (!getPara("pend").trim().matches("^\\d{8}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件到期日错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if (getPara("pa").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系地址未填写，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else {
			if (((User) getSessionAttr("user")).getInt("locationId")==18){
				if (Person.dao
						.findById(getPara("personId"))
						.set("base", "无")
						.set("radio", "无")
						.update()) {
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
					.set("time", new Date())
					.set("month", new MonthTime().GetMonth())
					.set("baseBefore", getPara("pbb"))
					.set("radioBefore", getPara("prb"))
					.set("baseAfter", "无")
					.set("radioAfter", "无")
					.set("content", "正常减员")
					.set("remark", getPara("operateRemark"))
					.set("locationId", 
							((User) getSessionAttr("user"))
									.getInt("locationId"))
					.set("userId", ((User) getSessionAttr("user")).getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				}
			}else{
				if (Person.dao
						.findById(getPara("personId"))
						.set("base", "无")
						.set("radio", "无")
						.set("locationId",
								((User) getSessionAttr("user"))
										.getInt("locationId")).update()) {
				}
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
							.set("time", new Date())
							.set("month", new MonthTime().GetMonth())
							.set("baseBefore", getPara("pbb"))
							.set("radioBefore", getPara("prb"))
							.set("baseAfter", "无")
							.set("radioAfter", "无")
							.set("content", "正常减员")
							.set("remark", getPara("operateRemark"))
							.set("locationId",
									((User) getSessionAttr("user"))
											.getInt("locationId"))
							.set("userId", ((User) getSessionAttr("user")).getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				}
		}
}
	

	/**
	 * 社保减员页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureReduceUI() {

		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		Person person = Person.dao.findFirst("select * from person where id=?",
				getPara("id"));
		if(person.getStr("base").equals("无")
				||person.getStr("base").equals("已退休")
				||person.getStr("radio").equals("无")
				||person.getStr("radio").equals("已退休")){
			setAttr("error", "该市民已办理减员业务，无法办理正常减员");
			render("/error.html");
		}else{
			
			setAttr("personName", person.getStr("name"));
			setAttr("personNumber", person.getStr("number"));
			setAttr("personPhone", person.getStr("phone"));
			setAttr("personAddress", person.getStr("address"));
			setAttr("personEnd", person.getStr("endDate"));
			setAttr("personBaseBefore", person.getStr("base"));
			setAttr("personRadioBefore", person.getStr("radio"));
			setAttr("personId", person.getInt("id"));
			render("/insureReduce.html");
		}
	}

	/**
	 * 社保退休
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,RetireInterceptor.class})
	public void insureRetire() {
		if (!getPara("pp").trim().matches("^\\d{11}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系电话错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if((Person.dao.findById(getPara("personId")).getStr("base").equals("无")||
				Person.dao.findById(getPara("personId")).getStr("radio").equals("无"))){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "该市民已办理减员业务，无需再次办理");
			render("/error.html");	
		}else {
			if (((User) getSessionAttr("user"))
					.getInt("locationId") == 18) {
				if (Person.dao.findById(getPara("personId")).set("base", "已退休")
						.set("radio", "已退休").update()) {
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
							.set("time", new Date())
							.set("month", new MonthTime().GetMonth())
							.set("baseBefore", getPara("pbb"))
							.set("radioBefore", getPara("prb"))
							.set("baseAfter", "无")
							.set("radioAfter", "无")
							.set("content", "办理退休")
							.set("remark", getPara("operateRemark"))
							.set("locationId",
									((User) getSessionAttr("user"))
											.getInt("locationId"))
							.set("userId",
									((User) getSessionAttr("user"))
											.getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				}
			} else {

				if (Person.dao
						.findById(getPara("personId"))
						.set("base", "已退休")
						.set("radio", "已退休")
						.set("locationId",
								((User) getSessionAttr("user"))
										.getInt("locationId")).update()) {
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
							.set("time", new Date())
							.set("month", new MonthTime().GetMonth())
							.set("baseBefore", getPara("pbb"))
							.set("radioBefore", getPara("prb"))
							.set("baseAfter", "无")
							.set("radioAfter", "无")
							.set("content", "办理退休")
							.set("remark", getPara("operateRemark"))
							.set("locationId",
									((User) getSessionAttr("user"))
											.getInt("locationId"))
							.set("userId",
									((User) getSessionAttr("user"))
											.getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				}
			}
		}
	}

	/**
	 * 社保退休页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,RetireInterceptor.class})
	public void insureRetireUI() {

		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		Person person = Person.dao.findFirst("select * from person where id=?",
				getPara("id"));
		if(person.getStr("base").equals("无")
				||person.getStr("base").equals("已退休")
				||person.getStr("radio").equals("无")
				||person.getStr("radio").equals("已退休")){
			setAttr("error", "该市民已办理减员业务，无法办理退休业务");
			render("/error.html");
		}else{
			
			setAttr("personName", person.getStr("name"));
			setAttr("personNumber", person.getStr("number"));
			setAttr("personPhone", person.getStr("phone"));
			setAttr("personAddress", person.getStr("address"));
			setAttr("personEnd", person.getStr("endDate"));
			setAttr("personBaseBefore", person.getStr("base"));
			setAttr("personRadioBefore", person.getStr("radio"));
			setAttr("personId", person.getInt("id"));
			render("/insureRetire.html");
		}

	}

	/**
	 * 社保变更
	 * */
	@Before({ LoginInterceptor.class, PowerInterceptor.class,InsureInterceptor.class})
	public void insureAlter() {
		if (!getPara("personBaseAfter").trim().matches("^\\d{4}")
				|| getParaToInt("personBaseAfter") < Integer.parseInt(Base.dao
						.findById("1").getStr("baseLow"))
				|| getParaToInt("personBaseAfter") > Integer.parseInt(Base.dao
						.findById("1").getStr("baseHigh"))) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "缴费基数错误，请重新输入!");
			render("/error.html");
		} else if (getPara("personRadioAfter").equals(getPara("prb"))
				&& getPara("personBaseAfter").equals(getPara("pbb"))) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "未进行社保变更操作，请重新输入!");
			render("/error.html");
		} else if (!getPara("pp").trim().matches("^\\d{11}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系电话错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if (!getPara("pend").trim().matches("^\\d{8}")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件到期日错误，请修改人员信息后再重新办理手续!");
			render("/error.html");
		} else if (getPara("pa").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系地址未填写，请修改人员信息后再重新办理手续!");
			render("/error.html");
		}  else if ((Person.dao.findById(getPara("personId")).getStr("base")
				.equals(getPara("personBaseAfter")) && Person.dao
				.findById(getPara("personId")).getStr("radio")
				.equals(getPara("personRadioAfter")))) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "未进行社保变更操作，请重新输入!");
			render("/error.html");
		} else {
			if (((User) getSessionAttr("user"))
					.getInt("locationId") == 18) {
				if (Person.dao.findById(getPara("personId"))
						.set("base", getPara("personBaseAfter"))
						.set("radio", getPara("personRadioAfter")).update()) {
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
							.set("time", new Date())
							.set("month", new MonthTime().GetMonth())
							.set("baseBefore", getPara("pbb"))
							.set("radioBefore", getPara("prb"))
							.set("baseAfter", getPara("personBaseAfter"))
							.set("radioAfter", getPara("personRadioAfter"))
							.set("content", "社保变更")
							.set("remark", getPara("operateRemark"))
							.set("locationId",
									((User) getSessionAttr("user"))
											.getInt("locationId"))
							.set("userId",
									((User) getSessionAttr("user"))
											.getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				} else {
					setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
					setAttr("error", "出现异常错误p，请再次输入!");
					render("/error.html");
				}

			} else {

				if (Person.dao
						.findById(getPara("personId"))
						.set("base", getPara("personBaseAfter"))
						.set("radio", getPara("personRadioAfter"))
						.set("locationId",
								((User) getSessionAttr("user"))
										.getInt("locationId")).update()) {
					Operate o = new Operate();
					o.set("personId", getPara("personId"))
							.set("time", new Date())
							.set("month", new MonthTime().GetMonth())
							.set("baseBefore", getPara("pbb"))
							.set("radioBefore", getPara("prb"))
							.set("baseAfter", getPara("personBaseAfter"))
							.set("radioAfter", getPara("personRadioAfter"))
							.set("content", "社保变更")
							.set("remark", getPara("operateRemark"))
							.set("locationId",
									((User) getSessionAttr("user"))
											.getInt("locationId"))
							.set("userId",
									((User) getSessionAttr("user"))
											.getInt("id"));
					if (o.save()) {
						redirect("/person/insureUI");
					} else {
						setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
						setAttr("error", "出现异常错误o，请再次输入!");
						render("/error.html");
					}
				} else {
					setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
					setAttr("error", "出现异常错误p，请再次输入!");
					render("/error.html");
				}
			}
		}
	}

	/**
	 * 社保变更页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void insureAlterUI() {
		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		Person person = Person.dao.findFirst("select * from person where id=?",
				getPara("id"));
		setAttr("personName", person.getStr("name"));
		setAttr("personNumber", person.getStr("number"));
		setAttr("personPhone", person.getStr("phone"));
		setAttr("personAddress", person.getStr("address"));
		setAttr("personEnd", person.getStr("endDate"));
		setAttr("personBaseBefore", person.getStr("base"));
		setAttr("personRadioBefore", person.getStr("radio"));
		setAttr("personId", person.getInt("id"));
		render("/insureAlter.html");
	}

	/**
	 * 社保查询
	 * */
	@Before(LoginInterceptor.class)
	public void insureQuery() {

		if (getPara("query") == null) {
			setSessionAttr("query", getSessionAttr("query"));
		} else {
			setSessionAttr("query", getPara("query"));
		}
		
		if (getPara("queryName") == null) {
			setSessionAttr("queryName", getSessionAttr("queryName"));
		} else {
			setSessionAttr("queryName", getPara("queryName"));
		}

		if (getPara("queryNumber") == null) {
			setSessionAttr("queryNumber", getSessionAttr("queryNumber"));
		} else {
			setSessionAttr("queryNumber", getPara("queryNumber"));
		}

		if (getPara("queryRadio") == null) {
			setSessionAttr("queryRadio", getSessionAttr("queryRadio"));
		} else {
			setSessionAttr("queryRadio", getPara("queryRadio"));
		}

		if (getPara("queryLocation") == null) {
			setSessionAttr("queryLocation", getSessionAttr("queryLocation"));
		} else {
			setSessionAttr("queryLocation", getPara("queryLocation"));
		}

		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
//		setAttr("insurePage",
//				Person.dao.paginate(getParaToInt(0, 1), 15,
//						getSessionAttr("query").toString()));
//		setAttr("queryString", getSessionAttr("query").toString());

		
		setAttr("insurePage", Person.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryRadio").toString(), getSessionAttr("queryLocation").toString()));
		setAttr("queryName",getSessionAttr("queryName").toString());
		setAttr("queryNumber",getSessionAttr("queryNumber").toString());
		setAttr("queryRadio",getSessionAttr("queryRadio").toString());
		setAttr("queryLocation",getSessionAttr("queryLocation").toString());
		
		render("/insure.html");
	}

	/**
	 * 社保页面
	 * */
	@Before(LoginInterceptor.class)
	public void insureUI() {
		setSessionAttr("query", "");
		setSessionAttr("queryName", "");
		setSessionAttr("queryNumber", "");
		setSessionAttr("queryLocation", "");
		setSessionAttr("queryRadio", "");
		
		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("insurePage", Person.dao.paginate(getParaToInt(0, 1), 15));
		render("/insure.html");
	}

}
