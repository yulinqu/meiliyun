package com.meiliyun.analyser.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliyun.analyser.dao.MeiliyunDAO;

@Component
public class AnalyserService {

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public List<Map<String, Object>> getPvuvByUrlAndTime(String url, String startTime, String endTime, String timeUnit)
            throws SQLException {
        List<Map<String, Object>> pvuvByTime = meiliyunDAO.getPvuvByTime(url, startTime, endTime, timeUnit);
        return pvuvByTime;
    }

    public List<Map<String, Object>> getClickCountByTime(String url, String startTime, String endTime, String timeUnit,
            String advertisment) throws SQLException {
        List<Map<String, Object>> clickCount = meiliyunDAO.getClickCount(url, startTime, endTime, timeUnit,
                advertisment);

        if (timeUnit.equalsIgnoreCase("week")) {
            for (Map<String, Object> map : clickCount) {
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    if (key.equalsIgnoreCase("timeRange")) {
                        String yearWeek = String.valueOf(map.get(key));
                        String parseWeekToTimeRange = parseWeekToTimeRange(yearWeek);
                        map.put(key, parseWeekToTimeRange);
                    }
                }
            }

        }

        return clickCount;
    }

    public String parseWeekToTimeRange(String yearWeek) {

        String year = yearWeek.split("-")[0];
        String week = yearWeek.split("-")[1];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week));
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time1 = cal.getTime();
        String format1 = formatter.format(time1);
        cal.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week) + 1);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        Date time2 = cal.getTime();
        String format2 = formatter.format(time2);

        return format1 + " 至 " + format2;

    }

}
