package com.meiliyun.analyser.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliyun.analyser.dao.MeiliyunDAO;

@Component
public class AnalyserService {

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    public List<Map<String, Object>> getPvuvByUrlAndTime(String url, String startTime, String endTime, String timeUnit)
            throws SQLException {
        List<Map<String, Object>> pvuvByTime = meiliyunDAO.getPvuvByTime(url, startTime, endTime, timeUnit);
        return pvuvByTime;
    }

    public List<Map<String, Object>> getClickCountByTime(String url, String startTime, String endTime, String timeUnit,
            String advertisment) throws SQLException {
        List<Map<String, Object>> clickCount = meiliyunDAO.getClickCount(url, startTime, endTime, timeUnit,
                advertisment);
        return clickCount;
    }

}
