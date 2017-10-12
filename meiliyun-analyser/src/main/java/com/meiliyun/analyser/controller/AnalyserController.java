package com.meiliyun.analyser.controller;

import java.io.File;

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

}
