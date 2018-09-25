
-- ----------------------------
-- Table structure for act_operating_form
-- ----------------------------
DROP TABLE IF EXISTS `act_operating_form`;
CREATE TABLE `act_operating_form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `code` varchar(50) DEFAULT NULL COMMENT '审批单号',
  `module_type` varchar(50) DEFAULT NULL COMMENT '模块类型',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='流程审批单表';

-- ----------------------------
-- Table structure for act_operating
-- ----------------------------
DROP TABLE IF EXISTS `act_operating`;
CREATE TABLE `act_operating` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operating_form_id` bigint(20) DEFAULT NULL COMMENT '审批单id',
  `company_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `operating_time` timestamp NULL DEFAULT NULL COMMENT '操作时间',
  `proc_inst_id` varchar(20) DEFAULT NULL COMMENT '流程实例id',
  `task_id` varchar(20) DEFAULT NULL COMMENT '任务实例id',
  `type` tinyint(4) DEFAULT NULL COMMENT '1:审批中,2:发起,3:通过,4:驳回,5:重新发起,6:评论,7:撤销',
  `content` varchar(200) DEFAULT NULL COMMENT '批注内容',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='流程审批操作表';

-- ----------------------------
-- Table structure for act_business_form
-- ----------------------------
DROP TABLE IF EXISTS `act_business_form`;
CREATE TABLE `act_business_form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `operating_form_id` bigint(20) DEFAULT NULL COMMENT '审批单id',
  `business_form` blob DEFAULT NULL COMMENT '业务表单信息',
  `proc_inst_id` varchar(20) DEFAULT NULL COMMENT '流程实例id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='流程业务表单表';


-- ----------------------------
-- Table structure for act_cc_person
-- ----------------------------
DROP TABLE IF EXISTS `act_cc_person`;
CREATE TABLE `act_cc_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(20) DEFAULT NULL COMMENT '租户id',
  `operating_form_id` bigint(20) DEFAULT NULL COMMENT '审批单id',
  `proc_inst_id` varchar(20) DEFAULT NULL COMMENT '流程实例id',
  `module_type` varchar(50) DEFAULT NULL COMMENT '模块类型',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块id',
  `person_id` bigint(20) DEFAULT NULL COMMENT '抄送人id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='流程实例抄送人表';

-- ----------------------------
-- Table structure for act_code
-- ----------------------------
DROP TABLE IF EXISTS `act_code`;
CREATE TABLE `act_code` (
  `company_id` bigint(20) NOT NULL,
  `sequence_code` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`company_id`,`sequence_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='审批编码表';

-- ----------------------------
-- Table structure for act_audit_config
-- ----------------------------
DROP TABLE IF EXISTS `act_audit_config`;
CREATE TABLE `act_audit_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` BIGINT(20) DEFAULT NULL COMMENT '租户id',
  `module_type` VARCHAR(50) DEFAULT NULL COMMENT '模块类型',
  `status` tinyint(4) DEFAULT '1' COMMENT '0:启用,1:禁用',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `updator_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) 
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COMMENT='流程配置表';

-- ----------------------------
-- Table structure for act_audit_branch
-- ----------------------------
DROP TABLE IF EXISTS `act_audit_branch`;
CREATE TABLE `act_audit_branch` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) DEFAULT NULL COMMENT '名称',
  `act_config_id` BIGINT(20) DEFAULT NULL  COMMENT '流程配置id',
  `company_id` BIGINT(20) DEFAULT NULL COMMENT '租户id',
  `module_type` VARCHAR(50) DEFAULT NULL COMMENT '模块类型',
  `proc_def_key` VARCHAR(100) DEFAULT NULL COMMENT '流程定义key',
  `pre_condition` tinyint(4) DEFAULT '0' COMMENT '前置条件0:无,1:有',
  `status` tinyint(4) DEFAULT '0' COMMENT '0:启用,1:禁用',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `updator_id` BIGINT(20) DEFAULT NULL COMMENT '更新人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) 
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COMMENT='流程配置分支表';

-- ----------------------------
-- Table structure for act_branch_applicant
-- ----------------------------
DROP TABLE IF EXISTS `act_branch_applicant`;
CREATE TABLE `act_branch_applicant` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `act_branch_id` BIGINT(20) DEFAULT NULL  COMMENT '流程分支id',
  `company_id` BIGINT(20) DEFAULT NULL COMMENT '租户id',
  `module_type` VARCHAR(50) DEFAULT NULL COMMENT '模块类型',
  `proc_def_key` VARCHAR(100) DEFAULT NULL COMMENT '流程定义key',
  `apply_id` BIGINT(20) DEFAULT NULL COMMENT '申请者id',
  `apply_type` tinyint(4) DEFAULT NULL COMMENT '申请者类型0:用户,1:部门',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COMMENT='流程分支申请人表';

-- ----------------------------
-- Table structure for act_branch_cc
-- ----------------------------
DROP TABLE IF EXISTS `act_branch_cc`;
CREATE TABLE `act_branch_cc` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `act_branch_id` BIGINT(20) DEFAULT NULL  COMMENT '流程分支id',
  `company_id` BIGINT(20) DEFAULT NULL COMMENT '租户id',
  `module_type` VARCHAR(50) DEFAULT NULL COMMENT '模块类型',
  `proc_def_key` VARCHAR(100) DEFAULT NULL COMMENT '流程定义key',
  `person_id` bigint(20) DEFAULT NULL COMMENT '抄送人id',
  `creator_id` BIGINT(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COMMENT='流程分支抄送人表';