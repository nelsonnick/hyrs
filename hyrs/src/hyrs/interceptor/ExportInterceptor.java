package hyrs.interceptor;

import hyrs.entity.Location;
import hyrs.entity.User;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class ExportInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		HttpSession session = inv.getController().getSession();
		User u = (User) session.getAttribute("user");
		if(!Location.dao.findById(u.get("locationId")).get("name").equals("职介中心")){
			inv.getController().setAttr("error", "您无权导出数据!");
			inv.getController().render("/error.html");
		}
		else{
			inv.invoke();
		}
	}
}