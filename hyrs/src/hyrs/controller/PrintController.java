package hyrs.controller;

import hyrs.entity.Base;
import hyrs.entity.Location;
import hyrs.entity.Operate;
import hyrs.entity.Person;
import hyrs.entity.User;
import hyrs.interceptor.InsureInterceptor;
import hyrs.interceptor.LoginInterceptor;
import hyrs.interceptor.PowerInterceptor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class PrintController extends Controller {
	
	/**
	 * 打印合同页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void contract() {
		
		Operate o=Operate.dao.findById(getPara("id"));
		Person p=Person.dao.findById(o.getInt("personId"));
		Location l=Location.dao.findById(o.getInt("locationId"));

		setAttr("lname",l.get("name"));
		setAttr("lphone",l.get("phone"));
		setAttr("pname",p.get("name"));
		setAttr("pnumber",p.get("number"));
		setAttr("pbase",p.get("base"));
		setAttr("pradio",p.get("radio"));
		setAttr("year",o.get("month").toString().substring(0, 4));
		setAttr("month",o.get("month").toString().substring(4, 6));
		setAttr("time",o.get("time"));
		setAttr("baseBefore",o.get("baseBefore"));
		setAttr("radioBefore",o.get("radioBefore"));
		setAttr("baseAfter",o.get("baseAfter"));
		setAttr("radioAfter",o.get("radioAfter"));
		setAttr("id",o.get("id"));
		
		DecimalFormat df=new DecimalFormat(".##");
		if(o.get("radioAfter").toString().equals("-0-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 = "+Integer.parseInt(o.get("baseAfter").toString())*0.2+"元");
		}else if(o.get("radioAfter").toString().equals("-5.5-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalLow").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalLow").toString())))+"元");
		}else if(o.get("radioAfter").toString().equals("-10-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalHigh").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalHigh").toString())))+"元");
		}else{
			setAttr("money","");
		}
		
		render("/contract.html");
	}

	/**
	 * 打印银行页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void bank() {
		
		Operate o=Operate.dao.findById(getPara("id"));
		Person p=Person.dao.findById(o.getInt("personId"));
		Location l=Location.dao.findById(o.getInt("locationId"));
		User u=User.dao.findById(o.getInt("userId"));
		
		setAttr("content",o.get("content"));
		setAttr("year",o.get("month").toString().substring(0, 4));
		setAttr("month",o.get("month").toString().substring(4, 6));
		setAttr("pname",p.get("name"));
		setAttr("pnumber",p.get("number"));
		setAttr("pphone",p.get("phone"));
		setAttr("paddress",p.get("address"));
		setAttr("pbase",p.get("base"));
		setAttr("pradio",p.get("radio"));
		
		setAttr("time",o.get("time"));
		setAttr("baseBefore",o.get("baseBefore"));
		setAttr("radioBefore",o.get("radioBefore"));
		setAttr("baseAfter",o.get("baseAfter"));
		setAttr("radioAfter",o.get("radioAfter"));
		setAttr("id",o.get("id"));
		setAttr("lname",l.get("name"));
		setAttr("uname",u.get("name"));
		
		setAttr("count",Db.queryLong("select count(*) from operate  INNER JOIN person ON operate.personId = person.id where person.number='"+p.get("number").toString()+"' and month='"+o.get("month").toString() +"'"));
		
		
		
		
		if (o.get("content").toString().equals("社保增员")){
			if (o.get("radioAfter").toString().equals("-5.5-")||o.get("radioAfter").toString().equals("-10-")){
				setAttr("st","根据《济南市人民政府第201号令》和《济政办发【2004】89号》文件规定，");
				setAttr("nd","本人自愿申请参加济南市灵活就业人员医疗保险，并同意按文件规定执行。");
			}else{
				setAttr("st","");
				setAttr("nd","");
			}
		}else if(o.get("content").toString().equals("社保变更")){
			if(o.get("radioBefore").toString().equals("-0-") && (o.get("radioAfter").toString().equals("-5.5-")||o.get("radioAfter").toString().equals("-10-"))){
				setAttr("st","根据《济南市人民政府第201号令》和《济政办发【2004】89号》文件规定，");
				setAttr("nd","本人自愿申请参加济南市灵活就业人员医疗保险，并同意按文件规定执行。");
			}else{
				setAttr("st","本人现申请变更社保信息，将缴费基数由"+o.get("baseBefore").toString()+"变更为"+o.get("baseAfter").toString()+"，");
				setAttr("nd","医保比例由"+o.get("radioBefore").toString()+"变更为"+o.get("radioAfter").toString()+"。");
			}
		}else if(o.get("content").toString().equals("正常减员")){
			setAttr("st","本人现申请办理社保正常减员业务。");
			setAttr("nd","");
		}else if(o.get("content").toString().equals("办理退休")){
			setAttr("st","本人现申请办理退休。");
			setAttr("nd","");
		}else{
			setAttr("st","");
			setAttr("nd","");
		}
		
		if (o.get("baseAfter").toString().equals("无")||o.get("radioAfter").toString().equals("无")){
			setAttr("money","0");
		}else{
			DecimalFormat df=new DecimalFormat(".##");
			if(o.get("radioAfter").toString().equals("-0-")){
				setAttr("money",o.get("baseAfter").toString()+" x 0.2 = "+Integer.parseInt(o.get("baseAfter").toString())*0.2+"元");
			}else if(o.get("radioAfter").toString().equals("-5.5-")){
				setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalLow").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalLow").toString())))+"元");
			}else if(o.get("radioAfter").toString().equals("-10-")){
				setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalHigh").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalHigh").toString())))+"元");
			}else{
				setAttr("money","");
			}
		}
		render("/bank.html");

	}

	
	/**
	 * 打印外地医保页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void medical() {
		
		Operate o=Operate.dao.findById(getPara("id"));
		Person p=Person.dao.findById(o.getInt("personId"));
		Location l=Location.dao.findById(o.getInt("locationId"));
		User u=User.dao.findById(o.getInt("userId"));
		
		setAttr("content",o.get("content"));
		setAttr("year",o.get("month").toString().substring(0, 4));
		setAttr("month",o.get("month").toString().substring(4, 6));
		setAttr("pname",p.get("name"));
		setAttr("pnumber",p.get("number"));
		setAttr("pphone",p.get("phone"));
		setAttr("paddress",p.get("address"));
		setAttr("pbase",p.get("base"));
		setAttr("pradio",p.get("radio"));
		
		setAttr("time",o.get("time"));
		setAttr("baseBefore",o.get("baseBefore"));
		setAttr("radioBefore",o.get("radioBefore"));
		setAttr("baseAfter",o.get("baseAfter"));
		setAttr("radioAfter",o.get("radioAfter"));
		setAttr("id",o.get("id"));
		setAttr("lname",l.get("name"));
		setAttr("uname",u.get("name"));
		
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		SimpleDateFormat MM = new SimpleDateFormat("MM");
		SimpleDateFormat dd = new SimpleDateFormat("dd");
		setAttr("yyyy",yyyy.format(new Date()));
		setAttr("mm",MM.format(new Date()));
		setAttr("dd",dd.format(new Date()));
		
		
		setAttr("count",Db.queryLong("select count(*) from operate  INNER JOIN person ON operate.personId = person.id where person.number='"+p.get("number").toString()+"' and month='"+o.get("month").toString() +"'"));
		
		render("/medical.html");

	}

	/**
	 * 打印减员页面
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void reduce() {
		
		Operate o=Operate.dao.findById(getPara("id"));
		Person p=Person.dao.findById(o.getInt("personId"));
		Location l=Location.dao.findById(o.getInt("locationId"));

		setAttr("lname",l.get("name"));
		setAttr("lphone",l.get("phone"));
		setAttr("pname",p.get("name"));
		setAttr("pnumber",p.get("number"));
		setAttr("pbase",p.get("base"));
		setAttr("pradio",p.get("radio"));
		setAttr("year",o.get("month").toString().substring(0, 4));
		setAttr("month",o.get("month").toString().substring(4, 6));
		setAttr("time",o.get("time"));
		setAttr("baseBefore",o.get("baseBefore"));
		setAttr("radioBefore",o.get("radioBefore"));
		setAttr("baseAfter",o.get("baseAfter"));
		setAttr("radioAfter",o.get("radioAfter"));
		setAttr("id",o.get("id"));
		
		DecimalFormat df=new DecimalFormat(".##");
		if(o.get("radioAfter").toString().equals("-0-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 = "+Integer.parseInt(o.get("baseAfter").toString())*0.2+"元");
		}else if(o.get("radioAfter").toString().equals("-5.5-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalLow").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalLow").toString())))+"元");
		}else if(o.get("radioAfter").toString().equals("-10-")){
			setAttr("money",o.get("baseAfter").toString()+" x 0.2 + "+Base.dao.findById("1").getStr("medicalHigh").toString()+" ="+df.format((Integer.parseInt(o.get("baseAfter").toString())*0.2+Double.parseDouble(Base.dao.findById("1").getStr("medicalHigh").toString())))+"元");
		}else{
			setAttr("money","");
		}
		
		render("/reduce.html");
	}

}


