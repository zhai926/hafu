/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.110_3306
Source Server Version : 50162
Source Host           : 192.168.0.110:3306
Source Database       : fuxindevice

Target Server Type    : MYSQL
Target Server Version : 50162
File Encoding         : 65001

Date: 2017-02-15 16:40:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hf_love
-- ----------------------------
DROP TABLE IF EXISTS `hf_love`;
CREATE TABLE `hf_love` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `loveType` int(10) DEFAULT NULL COMMENT '爱的类型(0:代表关爱一下 1:代表照顾爸妈 2:关爱提醒 3:代表传递思念  4:代表自定义)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hf_love
-- ----------------------------
INSERT INTO `hf_love` VALUES ('1', '平时多注意健康，保重身体哦...拥抱一下！', '1');
INSERT INTO `hf_love` VALUES ('2', '早晚天冷,小心着凉，注意保暖.....', '2');
INSERT INTO `hf_love` VALUES ('3', '记得多喝开水....', '3');
INSERT INTO `hf_love` VALUES ('4', '过得可好，想念你们了...', '4');
