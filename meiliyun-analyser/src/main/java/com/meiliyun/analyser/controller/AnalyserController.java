package com.meiliyun.analyser.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meiliyun.analyser.service.AnalyserService;
import com.meiliyun.analyser.utils.JSONUtil;

/**
 * 入口
 * 
 * @author yulinqu
 */
@Controller
@RequestMapping(value = "report")
public class AnalyserController {

    @Autowired
    private AnalyserService analyserService;

    @RequestMapping(value = "analyser", method = RequestMethod.GET)
    public @ResponseBody String analyser(@RequestParam(value = "ad", required = false) String ad,
            @RequestParam("staticTimeUnit") String staticTimeUnit, @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime, @RequestParam("url") String url) {

        /**
         * 1.根据时间获取该时间维度的文件 2.遍历这些文件 解析 每一个文件 每一行的数据 3.根据staticTimeUnit 去service 里面 调用不同的方法
         */

        File scpFile = new File("");

        return "1";

    }

    @RequestMapping(value = "pvuv", method = RequestMethod.GET)
    public @ResponseBody String getPvUv(@RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime, @RequestParam("url") String url,
            @RequestParam("staticTimeUnit") String staticTimeUnit) {

        // 校验开始时间和结束时间

        Object pvuvByUrlAndTime = analyserService.getPvuvByUrlAndTime(url, startTime, endTime, staticTimeUnit);
        String jsonStr = JSONUtil.toJsonStr(pvuvByUrlAndTime);

        return jsonStr;

    }

}
