CREATE TABLE `Person` (

`id` int NOT NULL AUTO_INCREMENT,

`number` varchar(255) CHARACTER SET utf8 NULL,

`name` varchar(255) CHARACTER SET utf8 NULL,

`phone` varchar(255) CHARACTER SET utf8 NULL,

`address` varchar(255) CHARACTER SET utf8 NULL,

`radio` varchar(255) CHARACTER SET utf8 NULL,

`base` varchar(255) CHARACTER SET utf8 NULL,

`remark` varchar(255) CHARACTER SET utf8 NULL,

`locationId` int NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `Information` (

`id` int NOT NULL AUTO_INCREMENT,

`personId` int NULL,

`userId` int NULL,

`locationId` int NULL,

`number` varchar(255) CHARACTER SET utf8 NULL,

`name` varchar(255) CHARACTER SET utf8 NULL,

`phone` varchar(255) CHARACTER SET utf8 NULL,

`address` varchar(255) CHARACTER SET utf8 NULL,

`remark` varchar(255) CHARACTER SET utf8 NULL,

`time` datetime NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `User` (

`id` int NOT NULL AUTO_INCREMENT,

`number` varchar(255) CHARACTER SET utf8 NULL,

`name` varchar(255) CHARACTER SET utf8 NULL,

`password` varchar(255) CHARACTER SET utf8 NULL,

`locationId` int NULL,

`active` int NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `Operate` (

`id` int NOT NULL AUTO_INCREMENT,

`personId` int NULL,

`locationId` int NULL,

`userId` int NULL,

`time` datetime NULL,

`month` varchar(255) CHARACTER SET utf8 NULL,

`radioBefore` varchar(255) CHARACTER SET utf8 NULL,

`baseBefore` varchar(255) CHARACTER SET utf8 NULL,

`radioAfter` varchar(255) CHARACTER SET utf8 NULL,

`baseAfter` varchar(255) CHARACTER SET utf8 NULL,

`content` varchar(255) CHARACTER SET utf8 NULL,

`remark` varchar(255) CHARACTER SET utf8 NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `Money` (

`id` int NOT NULL AUTO_INCREMENT,

`number` varchar(255) CHARACTER SET utf8 NULL,

`name` varchar(255) CHARACTER SET utf8 NULL,

`base` varchar(255) CHARACTER SET utf8 NULL,

`radio` varchar(255) CHARACTER SET utf8 NULL,

`month` varchar(255) CHARACTER SET utf8 NULL,

`content` varchar(255) CHARACTER SET utf8 NULL,

`return` varchar(255) CHARACTER SET utf8 NULL,

`reason` varchar(255) CHARACTER SET utf8 NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `Location` (

`id` int NOT NULL AUTO_INCREMENT,

`name` varchar(255) CHARACTER SET utf8 NULL,

`phone` varchar(255) CHARACTER SET utf8 NULL,

`address` varchar(255) CHARACTER SET utf8 NULL,

PRIMARY KEY (`id`) 

);



CREATE TABLE `Base` (

`id` int NOT NULL AUTO_INCREMENT,

`base` varchar(255) CHARACTER SET utf8 NULL,

`baseLow` varchar(255) CHARACTER SET utf8 NULL,

`baseHigh` varchar(255) NULL,

`medicalLow` varchar(255) CHARACTER SET utf8 NULL,

`medicalHigh` varchar(255) CHARACTER SET utf8 NULL,

PRIMARY KEY (`id`) 

);


CREATE TABLE `Card` (

`id` int NOT NULL AUTO_INCREMENT,

`cname` varchar(255) CHARACTER SET utf8 NULL,

`ename` varchar(255) CHARACTER SET utf8 NULL,

`money` varchar(255) CHARACTER SET utf8 NULL,

`sex` varchar(255) CHARACTER SET utf8 NULL,

`type` varchar(255) CHARACTER SET utf8 NULL,

`number` varchar(255) CHARACTER SET utf8 NULL,

`phone` varchar(255) CHARACTER SET utf8 NULL,

`locationId` int NOT NULL,

PRIMARY KEY (`id`) 
);



ALTER TABLE `User` ADD CONSTRAINT `User_Location` FOREIGN KEY (`locationId`) REFERENCES `Location` (`id`);

ALTER TABLE `Person` ADD CONSTRAINT `Person_Location` FOREIGN KEY (`locationId`) REFERENCES `Location` (`id`);

ALTER TABLE `Operate` ADD CONSTRAINT `Operate_Person` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`);

ALTER TABLE `Operate` ADD CONSTRAINT `Operate_Location` FOREIGN KEY (`locationId`) REFERENCES `Location` (`id`);

ALTER TABLE `Operate` ADD CONSTRAINT `Operate_User` FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

ALTER TABLE `Information` ADD CONSTRAINT `Information_Person` FOREIGN KEY (`personId`) REFERENCES `Person` (`id`);

ALTER TABLE `Information` ADD CONSTRAINT `Information_User` FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

ALTER TABLE `Information` ADD CONSTRAINT `Information_Location` FOREIGN KEY (`locationId`) REFERENCES `Location` (`id`);



INSERT INTO base (base, baseLow, baseHigh, medicalLow, medicalHigh) VALUES ('4376', '2626','4376','258.68','437.6')