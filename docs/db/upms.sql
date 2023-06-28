/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.177.129
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 192.168.177.129:3306
 Source Schema         : upms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 28/06/2023 16:48:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_resources
-- ----------------------------
DROP TABLE IF EXISTS `t_resources`;
CREATE TABLE `t_resources` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT NULL,
  `is_delete` int DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `is_default` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint NOT NULL,
  `scope` int NOT NULL,
  `state` int NOT NULL,
  `type` int NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f4ui5tt1x3rpsyjqrfosynfjh` (`code`),
  KEY `IDX79h1ofru6x7uosltx60s8b9j1` (`parent_id`),
  KEY `IDXf4ui5tt1x3rpsyjqrfosynfjh` (`code`),
  KEY `IDX3781wn3krxn18h1lseu9g0uib` (`name`),
  KEY `IDXkp65s314tx157rrow695efld2` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1093922493053272065 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_resources
-- ----------------------------
BEGIN;
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1, NULL, NULL, NULL, NULL, '2023-04-04 09:19:32', 1, 4, 'TEST22', b'1', '测试22', 0, 1, 1, 0, NULL, NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1080868750330167296, '2023-03-02 07:06:32', 1, NULL, 10000, NULL, NULL, 0, 'SYSTEMC', b'0', '系统设置', 0, 1, 1, 0, NULL, NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1080870534809387008, '2023-03-02 07:13:37', 1, NULL, 10000, NULL, NULL, 0, 'MENU', b'0', '菜单管理', 1080868750330167296, 1, 1, 1, '/resources/', NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1080871595733417984, '2023-03-02 07:17:50', 1, NULL, 10000, NULL, NULL, 0, 'ADD', b'0', '新增', 1080870534809387008, 1, 1, 2, NULL, NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1083085259152556032, '2023-03-08 09:54:09', 1, NULL, 10000, NULL, NULL, 0, 'ROLE', b'0', '角色管理', 1080868750330167296, 1, 1, 1, '/role/', NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1093544436983398400, '2023-04-06 06:35:11', 1, NULL, 10000, NULL, NULL, 0, 'USER', b'0', '用户管理', 1080868750330167296, 1, 1, 1, '/user/', NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1093573663162105856, '2023-04-06 08:31:19', 1, NULL, 10000, NULL, NULL, 0, '1212', b'0', '1212', 1093544436983398400, 1, 1, 2, NULL, NULL, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `parent_id`, `scope`, `state`, `type`, `url`, `remark`, `sort`) VALUES (1093922493053272064, '2023-04-07 07:37:26', 1, NULL, 10000, NULL, NULL, 0, '11111', b'0', '1111', 1093544436983398400, 1, 1, 2, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT NULL,
  `is_delete` int DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `is_default` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `state` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1kdn49aubc7bcawhth747xgfs` (`code`),
  KEY `IDX1kdn49aubc7bcawhth747xgfs` (`code`),
  KEY `IDXbkpm7njy2ort1yoiddc7jg8gj` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1086303299415572482 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (1, NULL, NULL, NULL, 10000, NULL, NULL, 0, 'admin', b'1', 'admin', NULL, 1);
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (1081241729299906560, '2023-03-03 07:48:37', 1, NULL, 10000, NULL, NULL, 0, 'test', b'0', '测试', '测试', 1);
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (1086303299415572480, '2023-03-17 07:01:29', NULL, 0, 10000, '2023-06-26 10:07:07', 1, 0, '11111111', b'0', '测试修改111', 'remark', 1);
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (1086303299415572481, '2023-06-28 08:46:54', 1, NULL, NULL, NULL, NULL, NULL, 'sdfsd', b'0', 'dfsdfds', 'fsdfsd', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_role_resources
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resources`;
CREATE TABLE `t_role_resources` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT NULL,
  `is_delete` int DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `resources_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `is_checked` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX962dj1muky31p9s93pw66m4l8` (`role_id`),
  KEY `IDXqswe4grm2fycj29viabfxdqam` (`resources_id`),
  KEY `IDXtnjjrudo6341dt7dnean6dqo7` (`role_id`),
  KEY `IDXjr6swyeyhh3ufmd6mlchi18vn` (`resources_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1093943318988980225 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_role_resources
-- ----------------------------
BEGIN;
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093922342830080000, '2023-04-07 07:36:51', 1, 0, 10000, NULL, NULL, 0, 1093544436983398400, 1086303299415572480, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093922342842662912, '2023-04-07 07:36:51', 1, 0, 10000, NULL, NULL, 0, 1093573663162105856, 1086303299415572480, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093922342855245824, '2023-04-07 07:36:51', 1, 0, 10000, NULL, NULL, 0, 1080871595733417984, 1086303299415572480, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093922342863634432, '2023-04-07 07:36:51', 1, 0, 10000, NULL, NULL, 0, 1080870534809387008, 1086303299415572480, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093922342876217344, '2023-04-07 07:36:51', 1, 0, 10000, NULL, NULL, 0, 1080868750330167296, 1086303299415572480, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318934454272, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1083085259152556032, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318947037184, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1093544436983398400, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318955425792, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1093573663162105856, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318959620096, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1093922493053272064, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318968008704, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1080870534809387008, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318976397312, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1080871595733417984, 1, NULL);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `resources_id`, `role_id`, `is_checked`) VALUES (1093943318988980224, '2023-04-07 09:00:12', 1, 0, 10000, NULL, NULL, 0, 1080868750330167296, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT NULL,
  `is_delete` int DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `birthday` date DEFAULT NULL,
  `profile_photo` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`),
  KEY `IDXg8gqk4e142wekcb1t6d3v2mwx` (`name`),
  KEY `IDXjhib4legehrm4yscx9t3lirqi` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1110950673215651841 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `name`, `password`, `username`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`, `birthday`, `profile_photo`) VALUES (1, NULL, NULL, 0, 10000, NULL, NULL, 0, 'admin', '{bcrypt}$2a$10$jMPKE0MHXYQOXd02z7lbMe25Axi80nExk05tiuzDh6zbObb5lplhy', 'admin', b'0', b'0', b'0', b'0', NULL, NULL);
INSERT INTO `t_user` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `name`, `password`, `username`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`, `birthday`, `profile_photo`) VALUES (1110950673215651840, '2023-05-24 07:21:21', 1, NULL, 10000, '2023-05-24 07:34:34', 1, 5, '测试1111133', '{bcrypt}$2a$10$I7fwMdRAg6NJMhds1cngI.swo21JxhrT1A6L3Z9Xj.pS0.cZPywXu', 'test', b'0', b'0', b'0', b'0', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT NULL,
  `is_delete` int DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXakj61lp0wul5h73yq0xrq89cq` (`user_id`),
  KEY `IDXkjp9c6hki8a1p70x44bwqex2v` (`role_id`),
  KEY `IDXkefwen29p9h9ilvry31mgyc94` (`user_id`),
  KEY `IDX4uvv76e86ms8ru0kk9s01d3s2` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1083048530496454657 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
BEGIN;
INSERT INTO `t_user_role` (`id`, `create_date_time`, `create_user_id`, `is_delete`, `tenant_id`, `update_date_time`, `update_user_id`, `version`, `role_id`, `user_id`) VALUES (1083048530496454656, '2023-03-08 07:28:12', NULL, 0, 10000, NULL, NULL, 0, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
