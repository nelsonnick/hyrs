package hyrs.controller;

import hyrs.entity.Card;
import hyrs.entity.Person;
import hyrs.entity.User;
import hyrs.interceptor.InsureInterceptor;
import hyrs.interceptor.LoginInterceptor;
import hyrs.interceptor.PowerInterceptor;
import hyrs.util.IDNumber;
import hyrs.util.Trans2PinYin;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class CardController extends Controller {
	
	/**
	 * 开卡界面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void cardUI() {
		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		
		setAttr("cardPage", Card.dao.paginate(getParaToInt(0, 1), 15, ((User) getSessionAttr("user")).getInt("locationId")));
		render("/card.html");
	}
	
	/**
	 * 批量开卡
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void cardCreat() {
		Person p=Person.dao.findById(getPara("id"));
		if (!p.getStr("endDate").matches("^\\d{8}") || p.getStr("endDate").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "证件到期日错误，请修改后办理再开卡业务!");
			render("/error.html");
		} else if (p.getStr("address").equals("")) {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "联系地址未填写，请修改后办理再开卡业务!");
			render("/error.html");
		} else {
			Card c=new Card();
			c.set("cname", p.get("name"))
			 .set("ename", new Trans2PinYin().trans2PinYin(p.get("name").toString()))
			 .set("money", "0")
			 .set("sex", new IDNumber().getSex(p.getStr("number")))
			 .set("type", "A")
			 .set("number",p.getStr("number"))
			 .set("phone", p.getStr("phone"))
			 .set("address", p.getStr("address"))
			 .set("endDate", p.getStr("endDate"))
			 .set("code", "250022")
			 .set("locationId",((User) getSessionAttr("user")).getInt("locationId"));
			if (c.save()) {
				redirect("/card/cardUI");
			} else {
				setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
				setAttr("error", "出现异常错误c，请再次输入!");
				render("/error.html");
			}
		}
	}
	
	/**
	 * 逐个删除
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void cardDelete() {
		if (Card.dao.deleteById(getPara("id"))) {
			redirect("/card/cardUI");
		} else {
			setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
			setAttr("error", "出现异常错误c，请再次输入!");
			render("/error.html");
		}
	}
	
	/**
	 * 批量删除
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void cardDeleteAll() {
		Db.update("DELETE FROM Card WHERE locationId = '"+((User) getSessionAttr("user")).getInt("locationId")+"' ");
		redirect("/card/cardUI");
	}
	
}


