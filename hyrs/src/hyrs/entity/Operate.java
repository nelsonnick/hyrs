                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Operate model.

mysql> desc operate;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| personId    | int(11)      | NO   |     | NULL    |                |
| time        | datetime     | NO   |     | NULL    |                |
| month       | varchar(255) | NO   |     | NULL    |                |
| content     | varchar(255) | NO   |     | NULL    |                |
| radio       | varchar(255) | NO   |     | NULL    |                |
| base        | varchar(255) | NO   |     | NULL    |                |
| remark      | varchar(255) | NO   |     | NULL    |                |
| userId      | int(11)      | NO   |     | NULL    |                |
| locationId  | int(11)      | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Operate extends Model<Operate> {
	public static final Operate dao = new Operate();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Operate> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select operate.id,person.`id` as pid,person.`name` as pname,person.`number` as pnumber,person.`phone` as pphone,person.`address` as paddress,person.`base` as pbase,person.`radio` as pradio,operate.baseBefore,operate.radioBefore,operate.baseAfter,operate.radioAfter,operate.month,operate.time,operate.content,operate.remark,location.`name` as lname,user.`name` as uname "," from (((operate inner join person on operate.personId=person.id) inner join user on operate.userId=user.id) inner join location on operate.locationId=location.id) order by operate.id desc");
	}
	
	public Page<Operate> paginate(int pageNumber, int pageSize,String query) {
		return paginate(pageNumber, pageSize, "select operate.id,person.`id` as pid,person.`name` as pname,person.`number` as pnumber,person.`phone` as pphone,person.`address` as paddress,person.`base` as pbase,person.`radio` as pradio,operate.baseBefore,operate.radioBefore,operate.baseAfter,operate.radioAfter,operate.month,operate.time,operate.content,operate.remark,location.`name` as lname,user.`name` as uname", "from (((operate inner join person on operate.personId=person.id) inner join user on operate.userId=user.id) inner join location on operate.locationId=location.id) where person.name like '%"+query+"%' or person.number like '%"+query+"%' order by operate.id desc");
	}
	
	public Page<Operate> paginate(int pageNumber, int pageSize,String name,String number,String content,String location,String month) {
		return paginate(pageNumber, pageSize, "select operate.id,person.`id` as pid,person.`name` as pname,person.`number` as pnumber,person.`phone` as pphone,person.`address` as paddress,person.`base` as pbase,person.`radio` as pradio,operate.baseAfter,operate.radioAfter,operate.baseBefore,operate.radioBefore,operate.month,operate.time,operate.content,operate.remark,location.`name` as lname,user.`name` as uname", 
				"from (((operate inner join person on operate.personId=person.id) inner join user on operate.userId=user.id) inner join location on operate.locationId=location.id) where person.name like '%"+name+"%' and person.number like '%"+number+"%' and operate.content like '%"+content+"%' and location.name like '%"+ location+"%' and operate.month like '%"+month+"%' order by operate.id desc");
	}
}
