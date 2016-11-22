package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Person model.

mysql> desc person;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| name        | varchar(255) | NO   |     | NULL    |                |
| number      | varchar(255) | NO   |     | NULL    |                |
| phone       | varchar(255) | NO   |     | NULL    |                |
| address     | varchar(255) | NO   |     | NULL    |                |
| radio       | varchar(255) | NO   |     | NULL    |                |
| base        | varchar(255) | NO   |     | NULL    |                |
| remark      | varchar(255) | NO   |     | NULL    |                |
| locationId  | int(11)      | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Person extends Model<Person> {
	public static final Person dao = new Person();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Person> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select person.id,person.name,person.number,person.base,person.radio,person.phone,person.address,person.remark,location.`name` as lname ", "from person inner join location on person.locationId=location.id order by person.id desc");
	}
	
	public Page<Person> paginate(int pageNumber, int pageSize,String query) {
		return paginate(pageNumber, pageSize, "select person.id,person.name,person.number,person.base,person.radio,person.phone,person.address,person.remark,location.`name` as lname ", "from person inner join location on person.locationId=location.id where person.name like '%"+query+"%' or person.number like '%"+query+"%' order by person.id desc");
	}
	
	public Page<Person> paginate(int pageNumber, int pageSize,String name,String number,String radio,String location) {
		return paginate(pageNumber, pageSize, "select person.id,person.name,person.number,person.base,person.radio,person.phone,person.address,person.remark,location.`name` as lname ", "from person inner join location on person.locationId=location.id where person.name like '%"+name+"%' and person.number like '%"+number+"%' and person.radio like '%"+radio+"%' and location.name like '%"+location+"%' order by person.id desc");
	}
}
