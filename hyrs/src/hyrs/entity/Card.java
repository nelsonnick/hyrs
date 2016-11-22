package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Card model.

mysql> desc money;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| cname       | varchar(255) | NO   |     | NULL    |                |
| ename       | varchar(255) | NO   |     | NULL    |                |
| money       | varchar(255) | NO   |     | NULL    |                |
| sex         | varchar(255) | NO   |     | NULL    |                |
| type        | varchar(255) | NO   |     | NULL    |                |
| number      | varchar(255) | NO   |     | NULL    |                |
| phone       | varchar(255) | NO   |     | NULL    |                |
| locationId  | int(11)      | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Card extends Model<Card> {
	public static final Card dao = new Card();
	
	public Page<Card> paginate(int pageNumber, int pageSize,int locationId) {
		return paginate(pageNumber, pageSize, "select *", "from card where locationId='" + locationId +"' order by id desc");
	}
}
