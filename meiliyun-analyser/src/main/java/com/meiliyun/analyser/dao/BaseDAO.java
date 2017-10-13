package com.meiliyun.analyser.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * common sqlMapClient
 * 
 * @author yulinqu
 */
@SuppressWarnings("unchecked")
public abstract class BaseDAO {

    protected static Logger logger = Logger.getLogger(BaseDAO.class);

    private SqlMapClient sqlMapClient;

    public SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    protected <T> T insert(String statement, Object param) throws SQLException {
        return (T) this.sqlMapClient.insert(generate(statement), param);
    }

    protected String generate(String statement) {
        return nameSpace() + "." + statement;
    }

    protected int update(String statement, Object param) throws SQLException {

        return this.sqlMapClient.update(generate(statement), param);

    }

    protected <T> T queryForObject(String statement, Object param) throws SQLException {
        return (T) this.sqlMapClient.queryForObject(generate(statement), param);
    }

    protected Map queryForMap(String statement, Object param, String keyProperty) {
        try {
            return this.sqlMapClient.queryForMap(generate(statement), param, keyProperty);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected <T> List<T> queryForList(String statement, Object param) throws SQLException {
        return this.sqlMapClient.queryForList(generate(statement), param);
    }

    protected Map queryForMap(String statement, Object param, String keyProperty, String valueProperty) {
        try {
            return this.sqlMapClient.queryForMap(generate(statement), param, keyProperty, valueProperty);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract String nameSpace();
}
