package com.meiliyun.analyser.controller;

import java.util.List;
import java.util.Map;

import com.meiliyun.analyser.bean.ScpTaskV2;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.meiliyun.analyser.bean.ScpTask;
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

    @Autowired
    private ScpTask scpTask;

    @Autowired
    private ScpTaskV2 scpTask2;

    private static Logger LOGGER = Logger.getLogger(AnalyserController.class);

    @RequestMapping(value = "clicks", method = RequestMethod.GET)
    public ModelAndView analyser(@RequestParam(value = "ad", required = false) String ad,
            @RequestParam("staticTimeUnit") String staticTimeUnit, @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime, @RequestParam("url") String url,
            @RequestParam(value = "channel", required = false) String channel) {

        if (StringUtils.isBlank(ad)) {
            ad = "all";
        }

        if(StringUtils.isBlank(channel)){
            channel="all";
        }

        String currentDate = scpTask2.getCurrentDate();

        ModelAndView modelAndView = new ModelAndView("clicks");
        try {

            if(endTime.compareToIgnoreCase(currentDate)>=0){
                scpTask2.scpFile();
            }

            List<Map<String, Object>> clickCountByTime = analyserService.getClickCountByTime(url, startTime, endTime,
                    staticTimeUnit, ad,channel);

            modelAndView.addObject("clicks", clickCountByTime);
            modelAndView.addObject("status", "成功");
            modelAndView.addObject("querytime", startTime + "至" + endTime);
            return modelAndView;
        } catch (Exception e) {
            LOGGER.error("get clicks error !", e);
            modelAndView.addObject("status", "失败");
            modelAndView.addObject("querytime", startTime + "至" + endTime);
            return modelAndView;
        }
    }

    @RequestMapping(value = "pvuv", method = RequestMethod.GET)
    public ModelAndView getPvUv(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
            @RequestParam("url") String url, @RequestParam("staticTimeUnit") String staticTimeUnit,
            @RequestParam(value = "channel", required = false) String channel) {
        if(StringUtils.isBlank(channel)){
            channel = "all";
        }

        String currentDate = scpTask2.getCurrentDate();

        // 校验开始时间和结束时间
        ModelAndView modelAndView = new ModelAndView("pvuv");
        try {

            if(endTime.compareToIgnoreCase(currentDate)>=0){
                scpTask2.scpFile();
            }

            List<Map<String, Object>> pvuvByUrlAndTime = analyserService.getPvuvByUrlAndTime(url, startTime, endTime,
                    staticTimeUnit,channel);

            modelAndView.addObject("pvuv", pvuvByUrlAndTime);
            modelAndView.addObject("status", "成功");
            modelAndView.addObject("querytime", startTime + "至" + endTime);
            return modelAndView;
        } catch (Exception e) {
            LOGGER.error("get pvuv error !", e);
            modelAndView.addObject("status", "失败");
            modelAndView.addObject("querytime", startTime + "至" + endTime);
            return modelAndView;
        }

    }

    @RequestMapping(value = "scp_file", method = RequestMethod.GET)
    public @ResponseBody String test(@RequestParam("test_date") String testDate) {
        String returnMsg = null;
        try {
            returnMsg = scpTask.scpFileForTest(testDate);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return returnMsg;
        }
    }

}
