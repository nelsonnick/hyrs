package hyrs.interceptor;

import hyrs.entity.Location;
import hyrs.entity.User;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class RetireInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		HttpSession session = inv.getController().getSession();
		User u = (User) session.getAttribute("user");
		if(!Location.dao.findById(u.get("locationId")).get("name").equals("退管科")){
			inv.getController().setAttr("error", "退休减员已由区职介授权退管科进行操作，各社保中心无需办理");
			inv.getController().setAttr("userName", u.getStr("name"));
			inv.getController().setAttr("userNumber", u.getStr("number"));
			inv.getController().render("/error.html");
		}
		else{
			inv.invoke();
		}
	}
}