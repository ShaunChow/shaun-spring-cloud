
DROP TABLE IF EXISTS `jdbc_mutex_locks`;
CREATE TABLE `jdbc_locks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(64) NOT NULL DEFAULT '',
  `desc` varchar(1024) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_method_name` (`method_name `) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;