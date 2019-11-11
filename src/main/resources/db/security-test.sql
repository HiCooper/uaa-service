/*
 Navicat Premium Data Transfer

 Source Server         : 47.101.42.169
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : 47.101.42.169:3306
 Source Schema         : security-test

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 11/11/2019 11:10:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端id',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源id',
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端密码',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权角色',
  `access_token_validity` int(11) NULL DEFAULT NULL COMMENT 'access token 有效期 单位 秒',
  `refresh_token_validity` int(11) NULL DEFAULT NULL COMMENT '刷新token有效期 单位 秒',
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附加信息',
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已验证自动跳过授权',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('internal', 'order', '$2a$10$4qv/tJ1krNLKMCCX0rmmIeVNdiMfNNdnF7UWwl2wcYpVi.ypkJMXy', 'web-app', 'client_credentials,refresh_token', NULL, 'ROLE_ADMIN', 300, 604800, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('web_app', 'order', '$2a$10$4qv/tJ1krNLKMCCX0rmmIeVNdiMfNNdnF7UWwl2wcYpVi.ypkJMXy', 'select', 'password,refresh_token', NULL, 'ROLE_USER', 300, 604800, NULL, 'true');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ROLE_ADMIN', '管理员');
INSERT INTO `role` VALUES (2, 'ROLE_USER', '普通用户');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `activated` tinyint(1) NULL DEFAULT NULL COMMENT '激活状态',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '昵称',
  `enabled` bit(1) NULL DEFAULT b'1' COMMENT '启用状态',
  `expired` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `locked` bit(1) NULL DEFAULT b'0' COMMENT '是否锁定',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '上次修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$5//RRdiBhvNv8S4IgoWCqOanWqnLfOuRZ9Y2UHkIwI8/kf9uHx9z2', 1, NULL, NULL, b'1', NULL, b'0', '2018-12-03 15:07:57', NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 1, 2);

SET FOREIGN_KEY_CHECKS = 1;
