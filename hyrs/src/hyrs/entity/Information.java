package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Information model.

mysql> desc information;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| personId    | int(11)      | NO   |     | NULL    |                |
| userId      | int(11)      | NO   |     | NULL    |                |
| locationId  | int(11)      | NO   |     | NULL    |                |
| name        | varchar(255) | NO   |     | NULL    |                |
| number      | varchar(255) | NO   |     | NULL    |                |
| phone       | varchar(255) | NO   |     | NULL    |                |
| address     | varchar(255) | NO   |     | NULL    |                |
| remark      | varchar(255) | NO   |     | NULL    |                |
| time        | datetime     | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Information extends Model<Information> {
	public static final Information dao = new Information();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Information> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select information.id,person.`id` as pid,person.`name` as pname,person.`number` as pnumber,person.`phone` as pphone,information.time,information.name,information.number,information.phone,location.`name` as lname,user.`name` as uname", "from (((information inner join person on information.personId=person.id) inner join user on information.userId=user.id) inner join location on information.locationId=location.id) order by information.id desc");
	}
	
	public Page<Information> paginate(int pageNumber, int pageSize,String query) {
		return paginate(pageNumber, pageSize, "select information.id,person.`id` as pid,person.`name` as pname,person.`number` as pnumber,person.`phone` as pphone,information.time,information.name,information.number,information.phone,location.`name` as lname,user.`name` as uname", "from (((information inner join person on information.personId=person.id) inner join user on information.userId=user.id) inner join location on information.locationId=location.id) where person.name like '%"+query+"%' or person.number like '%"+query+"%' or information.name like '%"+query+"%' or information.number like '%"+query+"%' order by information.id desc");
	}
}
