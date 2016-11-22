package hyrs.controller;

import java.util.ArrayList;
import java.util.List;

import hyrs.entity.Count;
import hyrs.entity.Location;
import hyrs.entity.Operate;
import hyrs.entity.User;
import hyrs.interceptor.LoginInterceptor;
import hyrs.util.MonthTime;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class OperateController extends Controller {

	/**
	 * 变更查询
	 * */
	@Before(LoginInterceptor.class)
	public void operateQuery() {
		
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

		if (getPara("queryContent") == null) {
			setSessionAttr("queryContent", getSessionAttr("queryContent"));
		} else {
			setSessionAttr("queryContent", getPara("queryContent"));
		}

		if (getPara("queryLocation") == null) {
			setSessionAttr("queryLocation", getSessionAttr("queryLocation"));
		} else {
			setSessionAttr("queryLocation", getPara("queryLocation"));
		}

		if (getPara("queryMonth") == null) {
			setSessionAttr("queryMonth", getSessionAttr("queryMonth"));
		} else {
			setSessionAttr("queryMonth", getPara("queryMonth"));
		}
	
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		//setAttr("operatePage", Operate.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("query").toString()));
		//setAttr("queryString",getSessionAttr("query").toString());
		
		setAttr("operatePage", Operate.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryContent").toString(), getSessionAttr("queryLocation").toString(), getSessionAttr("queryMonth").toString()));
		setAttr("queryName",getSessionAttr("queryName").toString());
		setAttr("queryNumber",getSessionAttr("queryNumber").toString());
		setAttr("queryContent",getSessionAttr("queryContent").toString());
		setAttr("queryLocation",getSessionAttr("queryLocation").toString());
		setAttr("queryMonth",getSessionAttr("queryMonth").toString());
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 50));
		render("/operate.html");
	}
	/**
	 * 变更页面
	 * */
	@Before(LoginInterceptor.class)
	public void operateUI() {
		setSessionAttr("query", "");
		setSessionAttr("queryName", "");
		setSessionAttr("queryNumber", "");
		setSessionAttr("queryContent", "");
		setSessionAttr("queryLocation", "");
		setSessionAttr("queryMonth", "");
		
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("operatePage", Operate.dao.paginate(getParaToInt(0, 1), 15));
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 50));
		
		render("/operate.html");
	}
	
	/**
	 * 计数查询
	 * */
	@Before(LoginInterceptor.class)
	public void operateCountQuery() {
		if (getPara("queryMonth") == null) {
			setSessionAttr("queryMonth", getSessionAttr("queryMonth"));
		} else {
			setSessionAttr("queryMonth", getPara("queryMonth"));
		}
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("queryMonth",getSessionAttr("queryMonth").toString());
		
		
		Long j=Db.queryLong("select count(*) from location");
		int[] id=new int[j.intValue()+1];
		Count count=new Count();

		count.setAll(Db.queryLong("select count(*) from operate where month like '%"+ getSessionAttr("queryMonth").toString() +"%'"));
		count.setId(0);
		count.setName("汇总");
		count.setAdd(Db.queryLong("select count(*) from operate where content='社保增员' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
		count.setAlter(Db.queryLong("select count(*) from operate where content='社保变更' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
		count.setReduce(Db.queryLong("select count(*) from operate where content='正常减员' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
		count.setRetire(Db.queryLong("select count(*) from operate where content='办理退休' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));

		List<Count> c=new ArrayList<Count>();
		c.add(count);
		for (int i=1;i<j+1;i++){
			 id[i]=Location.dao.findById(i).getInt("id");
			 Count cc=new Count();
			 cc.setAll(Db.queryLong("select count(*) from operate where locationId='"+id[i]+"' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
			 cc.setId(Location.dao.findById(i).getInt("id"));
			 cc.setName(Location.dao.findById(i).getStr("name"));
			 cc.setAdd(Db.queryLong("select count(*) from operate where content='社保增员' and locationId='"+id[i]+"' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
			 cc.setAlter(Db.queryLong("select count(*) from operate where content='社保变更' and locationId='"+id[i]+"' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
			 cc.setReduce(Db.queryLong("select count(*) from operate where content='正常减员' and locationId='"+id[i]+"' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
			 cc.setRetire(Db.queryLong("select count(*) from operate where content='办理退休' and locationId='"+id[i]+"' and month like '%"+getSessionAttr("queryMonth").toString() +"%'"));
			 c.add(cc);
		}
		
		setAttr("countPage", c);
		
		
		render("/operateCount.html");
	}
	/**
	 * 计数页面
	 * */
	@Before(LoginInterceptor.class)
	public void operateCountUI() {
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
		setAttr("userLocationId", ((User) getSessionAttr("user")).getInt("locationId"));
		setAttr("queryMonth", new MonthTime().GetMonth().toString());
		setSessionAttr("queryMonth", "");
		
		Long j=Db.queryLong("select count(*) from location");
		int[] id=new int[j.intValue()+1];
		Count count=new Count();

		count.setAll(Db.queryLong("select count(*) from operate"));
		count.setId(0);
		count.setName("汇总");
		count.setAdd(Db.queryLong("select count(*) from operate where content='社保增员'"));
		count.setAlter(Db.queryLong("select count(*) from operate where content='社保变更'"));
		count.setReduce(Db.queryLong("select count(*) from operate where content='正常减员'"));
		count.setRetire(Db.queryLong("select count(*) from operate where content='办理退休'"));
		
		
		List<Count> c=new ArrayList<Count>();
		c.add(count);
		for (int i=1;i<j+1;i++){
			 id[i]=Location.dao.findById(i).getInt("id");
			 Count cc=new Count();
			 cc.setAll(Db.queryLong("select count(*) from operate where locationId='"+id[i]+"'"));
			 cc.setId(Location.dao.findById(i).getInt("id"));
			 cc.setName(Location.dao.findById(i).getStr("name"));
			 cc.setAdd(Db.queryLong("select count(*) from operate where content='社保增员' and locationId='"+id[i]+"'"));
			 cc.setAlter(Db.queryLong("select count(*) from operate where content='社保变更' and locationId='"+id[i]+"'"));
			 cc.setReduce(Db.queryLong("select count(*) from operate where content='正常减员' and locationId='"+id[i]+"'"));
			 cc.setRetire(Db.queryLong("select count(*) from operate where content='办理退休' and locationId='"+id[i]+"'"));
			 c.add(cc);
		}
		
		setAttr("countPage", c);
		
		render("/operateCount.html");
		
		
	 }



}


