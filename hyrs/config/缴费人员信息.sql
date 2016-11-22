SELECT
	person.`id` AS 业务编号,
	person.`name` AS 人员姓名,
	person.`number` AS 证件号码,
	person.`phone` AS 联系电话,
	person.`address` AS 联系地址,
	person.`base` AS 缴费基数,
	person.`radio` AS 医保比例,
	person.`remark` AS 人员备注,
	location.`name` AS 办理地点
FROM
	person
INNER JOIN location ON person.locationId = location.id
WHERE
	person.`name` LIKE '%%'
AND person.`number` LIKE '%%'
AND person.`phone` LIKE '%%'
AND person.`address` LIKE '%%'
AND person.`base` LIKE '%%'
AND person.`base` != '无'
AND person.`base` != '已退休'
AND person.`radio` LIKE '%%'
AND person.`radio` != '无'
AND person.`radio` != '已退休'
AND person.`remark` LIKE '%%'
AND location.`name` LIKE '%%'
ORDER BY
	person.id DESC