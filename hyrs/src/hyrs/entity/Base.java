package hyrs.entity;

import com.jfinal.plugin.activerecord.Model;

/**
 * Base model.

mysql> desc money;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| base        | varchar(255) | NO   |     | NULL    |                |
| baseLow     | varchar(255) | NO   |     | NULL    |                |
| BaseHigh    | varchar(255) | NO   |     | NULL    |                |
| medicalLow  | varchar(255) | NO   |     | NULL    |                |
| medicalHigh | varchar(255) | NO   |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

 */
@SuppressWarnings("serial")
public class Base extends Model<Base> {
	public static final Base dao = new Base();
	
}
