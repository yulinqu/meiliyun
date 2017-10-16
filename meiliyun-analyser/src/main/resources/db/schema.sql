-- pvuv
CREATE TABLE IF NOT EXISTS pvuv
(
	ID BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
	TIME_RANGE VARCHAR(128) NOT NULL COMMENT '时间区间',
	URL VARCHAR(128) NOT NULL COMMENT '当前url',
	PV int(11) DEFAULT NULL COMMENT 'PV',
	UV int(11) DEFAULT NULL COMMENT 'UV',
	CREATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	UPDATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	CREATED_BY  VARCHAR(128) NOT NULL DEFAULT 'sys' NOT NULL COMMENT '创建人',
	UPDATED_BY  VARCHAR(128) NOT NULL DEFAULT 'sys' NOT NULL COMMENT '更新人',
	PRIMARY KEY(ID),
	KEY `INX_USER_ID` (`TIME_RANGE`),
	KEY `INX_BORROW_NID` (`URL`)
)ENGINE=MYISAM DEFAULT CHARSET=gbk AUTO_INCREMENT=1 COMMENT='pvuv';


-- pclick
CREATE TABLE IF NOT EXISTS pclick
(
	ID BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
	TIME_RANGE VARCHAR(128) NOT NULL COMMENT '时间区间',
	URL VARCHAR(128) NOT NULL COMMENT '当前url',
	AREA VARCHAR(128) NOT NULL COMMENT '主区域',
	POSITION VARCHAR(128) NOT NULL COMMENT '位置',
	PID VARCHAR(128) NOT NULL COMMENT '产品id',
	COUNT int(11) DEFAULT NULL COMMENT '点击数',
	CREATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	UPDATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	CREATED_BY  VARCHAR(128) NOT NULL DEFAULT 'sys' NOT NULL COMMENT '创建人',
	UPDATED_BY  VARCHAR(128) NOT NULL DEFAULT 'sys' NOT NULL COMMENT '更新人',
	PRIMARY KEY(ID),
	KEY `INX_USER_ID` (`TIME_RANGE`),
	KEY `INX_BORROW_NID` (`URL`)
	KEY `INX_USER_ID` (`AREA`),
	KEY `INX_BORROW_NID` (`POSITION`),
	KEY `INX_USER_ID` (`PID`)
)ENGINE=MYISAM DEFAULT CHARSET=gbk AUTO_INCREMENT=1 COMMENT='pclick';


