package hyrs.controller;

import hyrs.entity.Money;
import hyrs.entity.User;
import hyrs.interceptor.LoginInterceptor;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class MoneyController extends Controller {
	
	/**
	 * 扣款查询
	 * */
	@Before(LoginInterceptor.class)
	public void moneyQuery() {

		if (getPara("query")==null){
			setSessionAttr("query",getSessionAttr("query"));
		}else{
			setSessionAttr("query",getPara("query"));
			
		}
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		setAttr("moneyPage", Money.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("query").toString()));
		setAttr("queryString",getSessionAttr("query").toString());
		
		render("/money.html");
	}
	
	/**
	 * 扣款页面
	 * */
	@Before(LoginInterceptor.class)
	public void moneyUI() {
		setSessionAttr("query","");
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		setAttr("moneyPage", Money.dao.paginate(getParaToInt(0, 1), 15));
		render("/money.html");
	}
}


