package com.meiliyun.analyser.dao;

import org.springframework.stereotype.Repository;

/**
 * activity_mobp2p 相关数据操作
 * 
 * @author yulinqu
 */
@Repository
public class MeiliyunDAO extends MeiliyunBaseDAO {

    private final static String NAME_SPACE_MODEL = "meiliyun";

    @Override
    protected String nameSpace() {
        return NAME_SPACE_MODEL;
    }

}
