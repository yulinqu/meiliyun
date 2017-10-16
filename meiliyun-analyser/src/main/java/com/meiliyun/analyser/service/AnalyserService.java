package com.meiliyun.analyser.service;

import com.meiliyun.analyser.bean.PvUv;
import com.meiliyun.analyser.dao.MeiliyunDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalyserService {

    @Autowired
    private MeiliyunDAO meiliyunDAO;


    public List<PvUv> getPvuvByUrlAndTime(String url,String startTime,String endTime,String timeUnit){

        return  null;


    }


}
