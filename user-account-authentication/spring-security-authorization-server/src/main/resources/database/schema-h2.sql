
---- Spring Security ----

/* 1. Default */
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`username`)
);

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL
);
CREATE UNIQUE INDEX ix_auth_username
  on authorities (username,authority);

/* 2. Groups */
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `group_authorities`;
CREATE TABLE `group_authorities` (
  `group_id` bigint NOT NULL ,
  `authority` varchar(50) NOT NULL
);

DROP TABLE IF EXISTS `group_members`;
CREATE TABLE `group_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `group_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
);

---- Spring Oauth2 ----

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(255),
  `token` longblob,
  `authentication_id` varchar(255),
  `user_name` varchar(255),
  `client_id` varchar(255)
);

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL,
  `resource_ids` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` integer(11) DEFAULT NULL,
  `refresh_token_validity` integer(11) DEFAULT NULL,
  `additional_information` varchar(255) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(255),
  `token` longblob,
  `authentication_id` varchar(255),
  `user_name` varchar(255),
  `client_id` varchar(255),
  `authentication` longblob,
  `refresh_token` varchar(255)
);

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`(
  `token_id` varchar(255),
  `token` longblob,
  `authentication` longblob
);

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(255),
  `authentication` blob
);

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
    `userId` varchar(255),
    `clientId` varchar(255),
    `scope` varchar(255),
    `status` varchar(10),
    `expiresAt` datetime,
    `lastModifiedAt` datetime
);

