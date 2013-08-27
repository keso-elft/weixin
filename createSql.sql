DROP TABLE IF EXISTS `dota2live`.`advice`;
CREATE TABLE  `dota2live`.`advice` (
  `id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `content` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `dota2live`.`live`;
CREATE TABLE  `dota2live`.`live` (
  `id` int(10) unsigned NOT NULL,
  `match_id` int(10) unsigned NOT NULL,
  `progress` varchar(100) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dota2live`.`live_member`;
CREATE TABLE  `dota2live`.`live_member` (
  `id` int(10) unsigned NOT NULL,
  `match_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `time` datetime NOT NULL,
  `is_quit` varchar(45) NOT NULL DEFAULT '0' COMMENT '0:not quit ,1:quit',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dota2live`.`matches`;
CREATE TABLE  `dota2live`.`matches` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(36) NOT NULL,
  `narrator` varchar(36) NOT NULL,
  `url` varchar(100) NOT NULL,
  `score` varchar(36) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `is_live` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dota2live`.`news`;
CREATE TABLE  `dota2live`.`news` (
  `id` int(10) unsigned NOT NULL,
  `content` varchar(200) NOT NULL,
  `news_date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dota2live`.`user`;
CREATE TABLE  `dota2live`.`user` (
  `id` int(10) unsigned NOT NULL,
  `from_user_name` varchar(45) NOT NULL,
  `remark_name` varchar(45) DEFAULT NULL,
  `nick_name` varchar(45) DEFAULT NULL,
  `fake_id` varchar(45) DEFAULT NULL,
  `group_id` int(10) unsigned zerofill DEFAULT NULL,
  `last_login_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;