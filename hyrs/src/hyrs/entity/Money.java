package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Money model.

mysql> desc money;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| name        | varchar(255) | NO   |     | NULL    |                |
| number      | varchar(255) | NO   |     | NULL    |                |
| radio       | varchar(255) | NO   |     | NULL    |                |
| base        | varchar(255) | NO   |     | NULL    |                |
| month       | varchar(255) | NO   |     | NULL    |                |
| content     | varchar(255) | NO   |     | NULL    |                |
| return      | varchar(255) | NO   |     | NULL    |                |
| reason      | varchar(255) | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Money extends Model<Money> {
	public static final Money dao = new Money();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Money> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from money order by id desc");
	}
	
	public Page<Money> paginate(int pageNumber, int pageSize,String query) {
		return paginate(pageNumber, pageSize, "select *", "from money where name like '%"+query+"%' or number like '%"+query+"%' order by id desc");
	}
}
