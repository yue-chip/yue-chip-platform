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

 Date: 10/07/2023 10:25:27
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
  `create_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '创建人',
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '修改人',
  `code` varchar(255) DEFAULT '' COMMENT '编码',
  `is_default` bit(1) DEFAULT NULL COMMENT '是否默认菜单资源,默认资源不能删除',
  `name` varchar(255) DEFAULT '' COMMENT '名称',
  `parent_id` bigint DEFAULT '-9223372036854775808' COMMENT '父节点id',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `scope` int DEFAULT NULL COMMENT '作用域(0:app,1:后台,2:前端,3:微信)',
  `sort` int DEFAULT '0' COMMENT '排序',
  `state` int DEFAULT NULL COMMENT '状态(0:禁用,1:正常)',
  `type` int DEFAULT NULL COMMENT '类型(0:目录,1:菜单,2:功能)',
  `url` varchar(255) DEFAULT '' COMMENT 'url',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f4ui5tt1x3rpsyjqrfosynfjh` (`code`),
  KEY `IDXkp65s314tx157rrow695efld2` (`parent_id`),
  KEY `IDX3781wn3krxn18h1lseu9g0uib` (`name`),
  KEY `IDX9nsn1cbf1mupifd1pf1xnnbd4` (`create_date_time`),
  KEY `IDXshjyeq1ldibeaty6iffdasw96` (`update_date_time`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单资源';

-- ----------------------------
-- Records of t_resources
-- ----------------------------
BEGIN;
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (1, NULL, NULL, '2023-06-28 08:52:06', 1, 'TEST22', b'1', '测试22', 0, NULL, 1, 1, 1, 0, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (2, '2023-03-02 07:06:32', 1, '2023-06-28 08:52:03', 1, 'SYSTEMC', b'0', '系统设置', 0, NULL, 1, 0, 1, 0, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (3, '2023-03-02 07:13:37', 1, NULL, NULL, 'MENU', b'0', '菜单管理', 2, NULL, 1, NULL, 1, 1, '/resources/');
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (4, '2023-03-02 07:17:50', 1, '2023-07-10 02:23:37', 1, 'ROLE_ADD', b'0', '新增', 5, NULL, 1, NULL, 1, 0, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (5, '2023-03-08 09:54:09', 1, NULL, NULL, 'ROLE', b'0', '角色管理', 2, NULL, 1, NULL, 1, 1, '/role/');
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (6, '2023-04-06 06:35:11', 1, NULL, NULL, 'USER', b'0', '用户管理', 2, NULL, 1, NULL, 1, 1, '/user/');
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (7, '2023-04-06 08:31:19', 1, NULL, NULL, '1212', b'0', '1212', 2, NULL, 1, NULL, 1, 2, NULL);
INSERT INTO `t_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `parent_id`, `remark`, `scope`, `sort`, `state`, `type`, `url`) VALUES (8, '2023-04-07 07:37:26', 1, NULL, NULL, '11111', b'0', '1111', 2, NULL, 1, NULL, 1, 2, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '创建人',
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '修改人',
  `code` varchar(255) DEFAULT '' COMMENT '编码',
  `is_default` bit(1) DEFAULT NULL COMMENT '是否默认角色（0：否，1：是）默认角色不能删除',
  `name` varchar(255) DEFAULT '' COMMENT '名称',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `state` int DEFAULT NULL COMMENT '状态(0:禁用,1:正常)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1kdn49aubc7bcawhth747xgfs` (`code`),
  KEY `IDXbkpm7njy2ort1yoiddc7jg8gj` (`name`),
  KEY `IDX97edp96ao0de16loery8m4dr4` (`create_date_time`),
  KEY `IDXgld5084t4f8df7qsboo35332s` (`update_date_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (1, NULL, NULL, NULL, NULL, 'admin', b'1', 'admin', NULL, 1);
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (2, '2023-03-03 07:48:37', 1, NULL, NULL, 'test', b'0', '测试', '测试', 1);
INSERT INTO `t_role` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `code`, `is_default`, `name`, `remark`, `state`) VALUES (3, '2023-03-17 07:01:29', NULL, '2023-06-26 10:07:07', 1, '11111111', b'0', '测试修改111', 'remark', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_role_resources
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resources`;
CREATE TABLE `t_role_resources` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '创建人',
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '修改人',
  `resources_id` bigint DEFAULT '-9223372036854775808' COMMENT '菜单资源id',
  `role_id` bigint DEFAULT '-9223372036854775808' COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `IDXtnjjrudo6341dt7dnean6dqo7` (`role_id`),
  KEY `IDXjr6swyeyhh3ufmd6mlchi18vn` (`resources_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-资源关联表';

-- ----------------------------
-- Records of t_role_resources
-- ----------------------------
BEGIN;
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (1, '2023-04-07 07:36:51', 1, NULL, NULL, 5, 2);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (2, '2023-04-07 07:36:51', 1, NULL, NULL, 4, 2);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (3, '2023-04-07 07:36:51', 1, NULL, NULL, 3, 2);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (4, '2023-04-07 07:36:51', 1, NULL, NULL, 2, 2);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (5, '2023-04-07 07:36:51', 1, NULL, NULL, 1, 2);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (14, '2023-07-10 02:21:54', 1, NULL, -9223372036854775808, 6, 1);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (15, '2023-07-10 02:21:54', 1, NULL, -9223372036854775808, 5, 1);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (16, '2023-07-10 02:21:55', 1, NULL, -9223372036854775808, 4, 1);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (17, '2023-07-10 02:21:55', 1, NULL, -9223372036854775808, 3, 1);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (18, '2023-07-10 02:21:55', 1, NULL, -9223372036854775808, 1, 1);
INSERT INTO `t_role_resources` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `resources_id`, `role_id`) VALUES (19, '2023-07-10 02:21:55', 1, NULL, -9223372036854775808, 2, 1);
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '创建人',
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '修改人',
  `account_non_expired` bit(1) DEFAULT b'0',
  `account_non_locked` bit(1) DEFAULT b'0',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `credentials_non_expired` bit(1) DEFAULT b'0',
  `enabled` bit(1) DEFAULT b'1',
  `name` varchar(255) DEFAULT '' COMMENT '姓名',
  `password` varchar(255) DEFAULT '' COMMENT '密码',
  `profile_photo` bigint DEFAULT '-9223372036854775808' COMMENT '头像id',
  `username` varchar(255) DEFAULT '' COMMENT '登录帐号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`),
  KEY `IDXg8gqk4e142wekcb1t6d3v2mwx` (`name`),
  KEY `IDXqpja80veh8hmy1vm3ym2325pg` (`create_date_time`),
  KEY `IDXkalk5yk3chxw8iaysrxac4f8j` (`update_date_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `account_non_expired`, `account_non_locked`, `birthday`, `credentials_non_expired`, `enabled`, `name`, `password`, `profile_photo`, `username`) VALUES (1, NULL, NULL, NULL, NULL, b'0', b'0', NULL, b'0', b'0', 'admin', '{bcrypt}$2a$10$jMPKE0MHXYQOXd02z7lbMe25Axi80nExk05tiuzDh6zbObb5lplhy', NULL, 'admin');
INSERT INTO `t_user` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `account_non_expired`, `account_non_locked`, `birthday`, `credentials_non_expired`, `enabled`, `name`, `password`, `profile_photo`, `username`) VALUES (2, '2023-05-24 07:21:21', 1, '2023-05-24 07:34:34', 1, b'0', b'0', NULL, b'0', b'0', '测试1111133', '{bcrypt}$2a$10$I7fwMdRAg6NJMhds1cngI.swo21JxhrT1A6L3Z9Xj.pS0.cZPywXu', NULL, 'test');
COMMIT;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '创建人',
  `update_date_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` bigint DEFAULT '-9223372036854775808' COMMENT '修改人',
  `role_id` bigint DEFAULT '-9223372036854775808' COMMENT '角色id',
  `user_id` bigint DEFAULT '-9223372036854775808' COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `IDXkefwen29p9h9ilvry31mgyc94` (`user_id`),
  KEY `IDX4uvv76e86ms8ru0kk9s01d3s2` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-角色关联表';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
BEGIN;
INSERT INTO `t_user_role` (`id`, `create_date_time`, `create_user_id`, `update_date_time`, `update_user_id`, `role_id`, `user_id`) VALUES (1, '2023-03-08 07:28:12', NULL, NULL, NULL, 1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
