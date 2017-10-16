package com.meiliyun.analyser.dao;

import com.meiliyun.analyser.bean.PvUv;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param pvuv
     * @throws SQLException
     */
    public void insertPvuv(List<PvUv> pvuv) throws SQLException {

        Map<String,Object> params=new HashMap<String,Object>();
        params.put("pvuv",pvuv);

        super.insert("insert_pvuv",params);
    }


    public List<PvUv> getPvuvByTime(String url,String startTime,String endTime,String timeUnit) throws SQLException {


        Map<String,Object> params=new HashMap<String,Object>();
        params.put("start_time",startTime);
        params.put("end_time",endTime);
        params.put("url",url);
       // params.put("timeUnit",timeUnit);
        if (timeUnit.equalsIgnoreCase("hour"))
            return super.queryForList("",null);
        else if(timeUnit.equalsIgnoreCase("minte"))
            return super.queryForList("",null);
        else if(timeUnit.equalsIgnoreCase("day"))
            return super.queryForList("",null);
        else if(timeUnit.equalsIgnoreCase("month"))
            return super.queryForList("",null);

        return null;

    }



}
