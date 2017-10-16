package com.meiliyun.analyser.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliyun.analyser.dao.MeiliyunDAO;

@Component
public class AnalyserService {

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    public Object getPvuvByUrlAndTime(String url, String startTime, String endTime, String timeUnit) {

        try {
            Object pvuvByTime = meiliyunDAO.getPvuvByTime(url, startTime, endTime, timeUnit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

}
