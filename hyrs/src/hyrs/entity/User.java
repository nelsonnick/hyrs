package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * User model.

mysql> desc user;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| name        | varchar(255) | NO   |     | NULL    |                |
| number      | varchar(255) | NO   |     | NULL    |                |
| password    | varchar(255) | NO   |     | NULL    |                |
| locationId  | int(11)      | NO   |     | NULL    |                |
| active      | int(11)      | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class User extends Model<User> {
	public static final User dao = new User();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<User> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select user.id,user.name,user.number,user.active,location.`name` as lname", "from user inner join location on user.locationId=location.id order by user.id desc");
	}
	
	public Page<User> paginate(int pageNumber, int pageSize,String query) {
		return paginate(pageNumber, pageSize, "select user.id,user.name,user.number,user.active,location.`name` as lname", "from user inner join location on user.locationId=location.id where user.name like '%"+query+"%' or user.number like '%"+query+"%' order by user.id desc");
	}
}
