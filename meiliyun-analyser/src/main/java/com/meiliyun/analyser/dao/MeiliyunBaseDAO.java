package com.meiliyun.analyser.dao;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;

public abstract class MeiliyunBaseDAO extends BaseDAO {

    @Resource(name = "sqlMapClient_meiliyun")
    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

}
