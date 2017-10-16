package com.meiliyun.analyser.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliyun.analyser.bean.ProductClickBean;
import com.meiliyun.analyser.bean.PvUv;

/**
 * meiliyun 相关数据操作
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

    /**
     * 每天每分钟 的pvuv插入db
     * 
     * @param pvuv
     * @throws SQLException
     */
    public void insertPvuv(List<PvUv> pvuv) throws SQLException {
        super.insert("insert_pvuv", pvuv);
    }

    /**
     * 插入点击数
     * 
     * @param pclick
     * @throws SQLException
     */
    public void insertPclick(List<ProductClickBean> pclick) throws SQLException {
        super.insert("insert_pclick", pclick);
    }

    public Object getPvuvByTime(String url, String startTime, String endTime, String timeUnit) throws SQLException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start_time", startTime);
        params.put("end_time", endTime);
        params.put("url", url);
        if (timeUnit.equalsIgnoreCase("hour"))
            return super.queryForObject("get_hour_pvuv", params);
        else if (timeUnit.equalsIgnoreCase("minute"))
            return super.queryForObject("get_minute_pvuv", params);
        else if (timeUnit.equalsIgnoreCase("day"))
            return super.queryForObject("get_day_pvuv", params);
        else if (timeUnit.equalsIgnoreCase("month"))
            return super.queryForObject("get_month_pvuv", params);
        return null;

    }

}
