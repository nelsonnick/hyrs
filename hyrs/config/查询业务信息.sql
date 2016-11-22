SELECT
	person.`id` AS 业务编号,
	person.`name` AS 人员姓名,
	person.`number` AS 证件号码,
	person.`phone` AS 联系电话,
	person.`address` AS 联系地址,
	operate.`content` AS 办理内容,
	operate.`baseAfter` AS 缴费基数,
	operate.`radioAfter` AS 医保比例
	user.`name` AS 办理人员,
	operate.`month` AS 生效年月,
	operate.`time` AS 办理时间,
	location.`name` AS 办理地点,
	operate.`remark` AS 备注,
	operate.`baseBefore` AS 原缴费基数,
	operate.`radioBefore` AS 原医保比例
FROM
	(
		(
			(
				operate
				INNER JOIN person ON operate.personId = person.id
			)
			INNER JOIN USER ON operate.userId = user.id
		)
		INNER JOIN location ON operate.locationId = location.id
	)
WHERE
	operate.`month` LIKE '%%'
AND operate.`content` LIKE '%%'
AND location.`name` LIKE '%%'
AND person.`name` LIKE '%%'
AND person.`number` LIKE '%%'
ORDER BY
	operate.id DESC