package com.meiliyun.analyser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 入口
 * 
 * @author yulinqu
 */
@Controller
public class AnalyserController {

    /**
     * 测试接口
     * 
     * @return
     */
    @RequestMapping(value = "heartbeat_test", method = RequestMethod.GET)
    public @ResponseBody String heartbeatTest() {

        return "1";

    }

    @RequestMapping(value = "analyser", method = RequestMethod.GET)
    public @ResponseBody String analyser(@RequestParam("ad") String ad, @RequestParam("freq") String freq) {

        return "1";

    }

}
