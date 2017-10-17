package com.meiliyun.analyser.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.meiliyun.analyser.service.AnalyserService;

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

    @RequestMapping(value = "clicks", method = RequestMethod.GET)
    public ModelAndView analyser(@RequestParam(value = "ad", required = false) String ad,
            @RequestParam("staticTimeUnit") String staticTimeUnit, @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime, @RequestParam("url") String url) {

        if (StringUtils.isBlank(ad)) {
            ad = "all";
        }

        List<Map<String, Object>> clickCountByTime = analyserService.getClickCountByTime(url, startTime, endTime,
                staticTimeUnit, ad);

        ModelAndView modelAndView = new ModelAndView("clicks");
        modelAndView.addObject("clicks", clickCountByTime);
        modelAndView.addObject("status", "成功");
        modelAndView.addObject("querytime", startTime + "-" + endTime);
        return modelAndView;
    }

    @RequestMapping(value = "pvuv", method = RequestMethod.GET)
    public ModelAndView getPvUv(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
            @RequestParam("url") String url, @RequestParam("staticTimeUnit") String staticTimeUnit) {

        // 校验开始时间和结束时间
        List<Map<String, Object>> pvuvByUrlAndTime = analyserService.getPvuvByUrlAndTime(url, startTime, endTime,
                staticTimeUnit);

        ModelAndView modelAndView = new ModelAndView("pvuv");
        modelAndView.addObject("pvuv", pvuvByUrlAndTime);
        modelAndView.addObject("status", "成功");

        return modelAndView;

    }

}
