package hyrs.controller;

import hyrs.entity.Location;
import hyrs.entity.User;
import hyrs.interceptor.LoginInterceptor;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class UserController extends Controller {
	
	/**
	 * 登陆
	 * */
	public void login() {
	
		if (getPara("number").equals("admin") && getPara("password").equals("7953276")){
			setSessionAttr("user","管理员");
			setAttr("userName","管理员");
			redirect("/user/userUI");
		}else{
			User user=User.dao.findFirst("select * from user where number=? and password=? and active=1", getPara("number"),getPara("password"));
			if (user!=null){
				setSessionAttr("user",user);
				setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
				setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
				render("/home.html");
			}else{
				setAttr("error","用户名或密码错误，请重新输入!");
				render("/loginUI.html");
			}
		}

	}
	
	/**
	 * 登陆界面
	 * */
	public void loginUI() {
		render("/loginUI.html");
	}
	
	/**
	 * 联系电话界面
	 * */
	public void contactUI() {
		render("/contactUI.html");
	}
	/**
	 * 退休金计算界面
	 * */
	public void retireUI() {
		render("/retireUI.html");
	}
	/**
	 * 登出
	 * */
	@Before(LoginInterceptor.class)
	public void logout() {
		removeSessionAttr("user");
		render("/loginUI.html");
	}

	/**
	 * 主界面
	 * */
	@Before(LoginInterceptor.class)
	public void homeUI() {
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		render("/home.html");
	}

	/**
	 * 密码变更
	 * */
	@Before(LoginInterceptor.class)
	public void passwordAlter() {
		
		if (!getPara("passwordNew").trim().matches("^[a-zA-Z0-9_]{8,20}$")){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","新密码不符合规则，请重新输入!");
			render("/error.html");
		}
		else if (!getPara("passwordNew").trim().equals(getPara("passwordCheck"))){
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error","两次输入的密码不一致，请重新输入!");
			render("/error.html");
		}else{
			User.dao.findById(((User) getSessionAttr("user")).getInt("id")).set("password", getPara("passwordNew").trim()).update();
			removeSessionAttr("user");
			render("/loginUI.html");
		}
	}
	
	/**
	 * 密码变更页面
	 * */
	@Before(LoginInterceptor.class)
	public void passwordAlterUI() {
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		render("/password.html");
	}
	
	/**
	 * 用户查询
	 * */
	@Before(LoginInterceptor.class)
	public void userQuery() {
		
		if (getPara("query")==null){
			setSessionAttr("query",getSessionAttr("query"));
		}else{
			setSessionAttr("query",getPara("query"));
			
		}
		
		setAttr("userPage", User.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("query").toString()));
		setAttr("queryString",getSessionAttr("query").toString());

		render("/user.html");
	}
	
	/**
	 * 用户页面
	 * */
	@Before(LoginInterceptor.class)
	public void userUI() {
		setSessionAttr("query","");
		setAttr("userPage", User.dao.paginate(getParaToInt(0, 1), 15));
		render("/user.html");
	}
	
	/**
	 * 用户激活
	 * */
	@Before(LoginInterceptor.class)
	public void userActive() {
		if(User.dao.findById(getPara("id")).set("active", 1).update()){
			redirect("/user/userUI");
		 }else{
			 setAttr("error","出现异常错误u，请再次输入!");
			 render("/error2.html");
		}
	}
	
	/**
	 * 用户注销
	 * */
	@Before(LoginInterceptor.class)
	public void userCancel() {
		if(User.dao.findById(getPara("id")).set("active", 2).update()){
			redirect("/user/userUI");
		 }else{
			 setAttr("error","出现异常错误u，请再次输入!");
			 render("/error2.html");
		}
	}
	
	/**
	 * 用户密码重置
	 * */
	@Before(LoginInterceptor.class)
	public void userReset() {
		if(User.dao.findById(getPara("id")).set("password", "123456").update()){
			redirect("/user/userUI");
		 }else{
			 setAttr("error","出现异常错误u，请再次输入!");
			 render("/error2.html");
		}
	}
	
	/**
	 * 用户新增页面
	 * */
	@Before(LoginInterceptor.class)
	public void userNewUI() {
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 50));
		render("/userNew.html");
		
	}

	/**
	 * 用户新增
	 * */
	@Before(LoginInterceptor.class)
	public void userNew() {
		List<User> users=User.dao.find("select * from user where number=?",getPara("userNumber"));
		List<Location> locations=Location.dao.find("select * from location where name=?",getPara("locationName"));
		if(users.size()!=0){
			setAttr("error","该用户名数据库中已存在，请重新输入!");
			render("/error2.html");
		}else if(locations.size()==0){
			setAttr("error","无法识别您输入社保中心名称，请核实后再输入!");
			render("/error2.html");
		}else{
			User u=new User();
			u.set("name", getPara("userName").trim())
			 .set("number", getPara("userNumber").trim())
			 .set("password", "123456")
			 .set("active", 1)
			 .set("locationId",locations.get(0).getInt("id"));
			if (u.save()){
				redirect("/user/userUI");
			}else{
				setAttr("error","出现异常错误u，请再次输入!");
				render("/error2.html");
			}
		}
	}

	
	
	
	/**
	 * 用户修改页面
	 * */
	@Before(LoginInterceptor.class)
	public void userAlterUI() {
		
		User user=User.dao.findFirst("select user.id,user.name,user.number,user.active,location.`name` as lname from user inner join location on user.locationId=location.id  where user.id=?",getPara("id"));
		setAttr("userName",user.getStr("name"));
		setAttr("userNumber",user.getStr("number"));
		setAttr("locationName",user.getStr("lname"));
		setAttr("userId",user.getInt("id"));
		render("/userAlter.html");
		
	}

	/**
	 * 用户修改
	 * */
	@Before(LoginInterceptor.class)
	public void userAlter() {
		
		List<User> users=User.dao.find("select * from user where number=?",getPara("userNumber"));
		List<Location> locations=Location.dao.find("select * from location where name=?",getPara("locationName"));
		if(users.size()!=0 && !getPara("userNumber").equals(getPara("unumber"))){
			setAttr("error","该用户名数据库中已存在，请重新更换!");
			render("/error2.html");
		}else if(locations.size()==0){
			setAttr("error","无法识别您输入社保中心名称，请核实后再输入!");
			render("/error2.html");
		}else{
			if (User.dao.findById(getPara("userId")).set("name", getPara("userName").trim()).set("number", getPara("userNumber").trim()).set("locationId",locations.get(0).getInt("id")).update()){
				redirect("/user/userUI");
			}else{
				setAttr("error","出现异常错误u，请再次输入!");
				render("/error2.html");
			}
		}
	}
	
}


