package hyrs.interceptor;

import hyrs.entity.Location;
import hyrs.entity.User;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class InsureInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		HttpSession session = inv.getController().getSession();
		User u = (User) session.getAttribute("user");
		if(Location.dao.findById(u.get("locationId")).get("name").equals("退管科")){
			inv.getController().setAttr("error", "职介中心目前仅授权退管科进行信息变更及办理退休操作。如需更多权限，请于职介中心联系！");
			inv.getController().setAttr("userName", u.getStr("name"));
			inv.getController().setAttr("userNumber", u.getStr("number"));
			inv.getController().render("/error.html");
		}
		else{
			inv.invoke();
		}
	}
}