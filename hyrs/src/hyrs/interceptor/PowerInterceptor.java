package hyrs.interceptor;

import hyrs.entity.Location;
import hyrs.entity.User;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class PowerInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		HttpSession session = inv.getController().getSession();
		User u = (User) session.getAttribute("user");
		if(Location.dao.findById(u.get("locationId")).get("name").equals("查询")){
			inv.getController().setAttr("error", "您无权进行操作");
			inv.getController().setAttr("userName", u.getStr("name"));
			inv.getController().setAttr("userNumber", u.getStr("number"));
			inv.getController().render("/error.html");
		}
		else{
			inv.invoke();
		}
	}
}