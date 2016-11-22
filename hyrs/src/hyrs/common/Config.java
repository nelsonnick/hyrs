package hyrs.common;

import hyrs.controller.BaseController;
import hyrs.controller.CardController;
import hyrs.controller.ExportController;
import hyrs.controller.InformationController;
import hyrs.controller.LocationController;
import hyrs.controller.MoneyController;
import hyrs.controller.OperateController;
import hyrs.controller.PersonController;
import hyrs.controller.PrintController;
import hyrs.controller.UserController;
import hyrs.entity.Base;
import hyrs.entity.Card;
import hyrs.entity.Information;
import hyrs.entity.Location;
import hyrs.entity.Money;
import hyrs.entity.Operate;
import hyrs.entity.Person;
import hyrs.entity.User;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setError404View("/error500.html");
		me.setError500View("/error500.html");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		//me.add("/", IndexController.class, "/index");					// 第三个参数为该Controller的视图存放路径
		me.add("/information", InformationController.class);
		me.add("/money", MoneyController.class);
		me.add("/operate", OperateController.class);
		me.add("/person", PersonController.class);
		me.add("/user", UserController.class);
		me.add("/location", LocationController.class);
		me.add("/print", PrintController.class);
		me.add("/base", BaseController.class);
		me.add("/export", ExportController.class);
		me.add("/card", CardController.class);
		
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("information", Information.class);	// 映射information 表到 Information模型
		arp.addMapping("location", Location.class);	// 映射location 表到 Location模型
		arp.addMapping("money", Money.class);	// 映射money 表到 Money模型
		arp.addMapping("operate", Operate.class);	// 映射operate 表到 Operate模型
		arp.addMapping("person", Person.class);	// 映射person 表到 Person模型
		arp.addMapping("user", User.class);	// 映射user 表到 User模型
		arp.addMapping("base", Base.class);	// 映射base 表到 Base模型
		arp.addMapping("card", Card.class);	// 映射card 表到 Card模型
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("contextPath"));//设置上下文路径
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
