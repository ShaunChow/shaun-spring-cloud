DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) NOT NULL,
  `pwd` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
);