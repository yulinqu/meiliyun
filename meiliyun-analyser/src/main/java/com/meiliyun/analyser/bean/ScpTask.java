package com.meiliyun.analyser.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliyun.analyser.dao.MeiliyunDAO;
import com.meiliyun.analyser.utils.ScpUtils;

/**
 * 每天定时去nginx服务器scp文件到本地
 * 
 * @author yulinqu
 */
public class ScpTask {

    @Autowired
    private Config config;

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    public static final String DEFAULT_LOG_FILE_NAME = "access-";

    public static final String DEFAULT_LOG_FILE_NAME_SUFFIX = ".log";

    public static String TEST_DATA = null;

    private static Logger LOGGER = Logger.getLogger(ScpTask.class);

    public String scpFileForTest(String data){
        TEST_DATA = data;
        try {
            scpFile();
            TEST_DATA=null;
            return "SUCCESS";
        } catch (Exception e) {
            LOGGER.error("scp file erroe !",e );
            TEST_DATA = null;
            return "ERROR : "+e.getMessage();
        }

    }

    public void scpFile() throws IOException, SQLException {

        // 远程服务器的文件名
        String fileName = DEFAULT_LOG_FILE_NAME + getCurrentDate() + DEFAULT_LOG_FILE_NAME_SUFFIX;
        if (StringUtils.isNotBlank(TEST_DATA)) {
            fileName = DEFAULT_LOG_FILE_NAME + TEST_DATA + DEFAULT_LOG_FILE_NAME_SUFFIX;
        }

        String remoteServerIp = config.getRemoteFileHsot();
        String remoteSeverHostname = config.getRemoteSeverUserName();
        String remoteSeverPassword = config.getRemoteSeverPwd();
        String remoteFilePath = config.getRemoteFilePath() + fileName;
        String localPath = config.getLocalFilePath();


        File file = new File(localPath + fileName);

        if(file.exists()){
            return;
        }

        // 1.从远程服务器 scp 文件到本地服务器
        ScpUtils.scpFileFromRemoteSever(remoteServerIp, remoteSeverHostname, remoteSeverPassword, remoteFilePath,
                localPath);
        LOGGER.info("from remote server scp file : " + fileName + " success !");
        // 复制到本地的日志文件
        File localFile = new File(localPath + fileName);
        LOGGER.info("start to analyser log : " + fileName);
        if (!localFile.exists()) {
            return;
        }

        // 每分钟的pv uv<time,<url,List<uuid>>>
        Map<String, Map<String, List<String>>> preMinCount = new HashMap<String, Map<String, List<String>>>();

        // 各产品每分钟的点击情况<time,<url,<position,Map<pid,count>>>>

        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preBannerClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();
        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preIconClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();
        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preButtonClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();

        // 2.分析文件 按照每分钟的来统计 解析日志文件
        if (localFile.isFile() && localFile.exists()) { // 判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(localFile));// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] split = lineTxt.split("\\|");
                // 请求ip
                String ip = split[0];
                // 请求时间
                String requestTime = split[1];
                String reTime = null;
                if (StringUtils.isNotBlank(requestTime)) {
                    // 14/Oct/2017:06:06:48
                    reTime = formateDate(requestTime);
                }
                // 埋点数据
                String data = split[2];
                if (StringUtils.isNotBlank(data) && data.contains("/img/behavior.gif?")) {
                    // uuid
                    String uuidOri = split[9].replace(" ", "");
                    String uuid = "UUID";
                    if (StringUtils.isNotBlank(uuidOri)) {
                        uuid = uuidOri;
                    }
                    ;

                    // Referer 当前页url http://www.xianjinshijie.com:8090/show_list_page.do
                    String referer = split[8].replaceAll(" ", "");
                    String url = "show_list_page.do";
                    if (referer.indexOf("8443/") != -1) {
                        url = referer.split("8443/")[1];
                    }

                    // 访问list_page
                    if (data.contains("/img/behavior.gif?rnd=")) {
                        // pvuv
                        if (preMinCount.get(reTime) == null) {
                            preMinCount.put(reTime, new HashMap<String, List<String>>());
                        }

                        if (StringUtils.isNotBlank(url)) {
                            if (preMinCount.get(reTime).get(url) != null) {
                                preMinCount.get(reTime).get(url).add(uuid);
                            } else {
                                preMinCount.get(reTime).put(url, new ArrayList<String>());
                                preMinCount.get(reTime).get(url).add(uuid);
                            }

                        }

                    }
                    // banner点击
                    else {
                        if (data.contains("/img/behavior.gif?node=")) {

                            int index = data.indexOf("msg=");
                            String pid_position = data.substring(index + 4, index + 38);
                            String position = "1";
                            String pid = "";
                            if (pid_position.indexOf("-") != -1) {
                                position = pid_position.split("-")[0];
                                pid = pid_position.split("-")[1];
                            } else {
                                pid = data.substring(index + 4, index + 36);
                            }

                            if (data.contains("/img/behavior.gif?node=banner")) {
                                // banner 统计
                                if (preBannerClick.get(reTime) == null) {
                                    preBannerClick.put(reTime,
                                            new HashMap<String, Map<String, Map<String, List<String>>>>());
                                }

                                if (data.contains("/img/behavior.gif?node=banner-new")) {
                                    url = url + "_new";
                                }
                                // 各产品每分钟的点击情况<time,<url,<position,Map<pid,count>>>>
                                if (StringUtils.isNotBlank(url)) {
                                    if (preBannerClick.get(reTime).get(url) != null) {

                                        if (preBannerClick.get(reTime).get(url).get(position) != null) {

                                            if (preBannerClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preBannerClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preBannerClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preBannerClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                            // icon点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-icon")) {

                                if (preIconClick.get(reTime) == null) {
                                    preIconClick.put(reTime,
                                            new HashMap<String, Map<String, Map<String, List<String>>>>());
                                }

                                if (data.contains("/img/behavior.gif?node=merchant-list-icon-new")) {
                                    url = url + "_new";
                                }

                                if (StringUtils.isNotBlank(url)) {
                                    if (preIconClick.get(reTime).get(url) != null) {

                                        if (preIconClick.get(reTime).get(url).get(position) != null) {

                                            if (preIconClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preIconClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preIconClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preIconClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preIconClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preIconClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preIconClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                            // button点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-button")) {

                                if (preButtonClick.get(reTime) == null) {
                                    preButtonClick.put(reTime,
                                            new HashMap<String, Map<String, Map<String, List<String>>>>());
                                }

                                if (data.contains("/img/behavior.gif?node=merchant-list-button-new")) {
                                    url = url + "_new";
                                }

                                if (StringUtils.isNotBlank(url)) {
                                    if (preButtonClick.get(reTime).get(url) != null) {

                                        if (preButtonClick.get(reTime).get(url).get(position) != null) {

                                            if (preButtonClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preButtonClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preButtonClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preButtonClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                        }
                    }

                }
            }
            read.close();
        }

        // 统计每分钟pv uv
        List<PvUv> pvuv = new ArrayList<PvUv>();
        if (!preMinCount.isEmpty()) {
            Set<String> keys = preMinCount.keySet();
            for (String key : keys) {
                Map<String, List<String>> urlMap = preMinCount.get(key);
                Set<String> urlKey = urlMap.keySet();
                for (String url : urlKey) {
                    List<String> datas = urlMap.get(url);
                    Set<String> uniqDatas = new HashSet<String>();
                    uniqDatas.addAll(datas);
                    pvuv.add(new PvUv(key, url, datas.size(), uniqDatas.size()));
                }

            }
        }

        // 插入每天的pvuv
        if(CollectionUtils.isNotEmpty(pvuv)){
            meiliyunDAO.insertPvuv(pvuv);
        }


        // 统计每分钟不同主区域的点击情况
        List<ProductClickBean> bannerClicks = new ArrayList<ProductClickBean>();
        List<ProductClickBean> iconClicks = new ArrayList<ProductClickBean>();
        List<ProductClickBean> buttonClicks = new ArrayList<ProductClickBean>();
        // banner 点击情况
        if (!preBannerClick.isEmpty()) {
            Set<String> times = preBannerClick.keySet();
            for (String time : times) {
                Map<String, Map<String, Map<String, List<String>>>> timeMap = preBannerClick.get(time);
                if (!timeMap.isEmpty()) {
                    Set<String> urls = timeMap.keySet();
                    for (String url : urls) {
                        Map<String, Map<String, List<String>>> positionMap = timeMap.get(url);
                        if (!positionMap.isEmpty()) {
                            Set<String> positions = positionMap.keySet();
                            for (String position : positions) {
                                Map<String, List<String>> pidMap = positionMap.get(position);
                                if (!pidMap.isEmpty()) {
                                    Set<String> pids = pidMap.keySet();
                                    for (String pid : pids) {
                                        List<String> list = pidMap.get(pid);
                                        if (CollectionUtils.isNotEmpty(list)) {
                                            ProductClickBean productClickBean = new ProductClickBean(time, url,
                                                    "banner", position, pid, list.size());
                                            bannerClicks.add(productClickBean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // icon 点击
        if (!preIconClick.isEmpty()) {
            Set<String> times = preIconClick.keySet();
            for (String time : times) {
                Map<String, Map<String, Map<String, List<String>>>> timeMap = preIconClick.get(time);
                if (!timeMap.isEmpty()) {
                    Set<String> urls = timeMap.keySet();
                    for (String url : urls) {
                        Map<String, Map<String, List<String>>> positionMap = timeMap.get(url);
                        if (!positionMap.isEmpty()) {
                            Set<String> positions = positionMap.keySet();
                            for (String position : positions) {
                                Map<String, List<String>> pidMap = positionMap.get(position);
                                if (!pidMap.isEmpty()) {
                                    Set<String> pids = pidMap.keySet();
                                    for (String pid : pids) {
                                        List<String> list = pidMap.get(pid);
                                        if (CollectionUtils.isNotEmpty(list)) {
                                            ProductClickBean productClickBean = new ProductClickBean(time, url, "icon",
                                                    position, pid, list.size());
                                            iconClicks.add(productClickBean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // button 点击
        if (!preButtonClick.isEmpty()) {
            Set<String> times = preButtonClick.keySet();
            for (String time : times) {
                Map<String, Map<String, Map<String, List<String>>>> timeMap = preButtonClick.get(time);
                if (!timeMap.isEmpty()) {
                    Set<String> urls = timeMap.keySet();
                    for (String url : urls) {
                        Map<String, Map<String, List<String>>> positionMap = timeMap.get(url);
                        if (!positionMap.isEmpty()) {
                            Set<String> positions = positionMap.keySet();
                            for (String position : positions) {
                                Map<String, List<String>> pidMap = positionMap.get(position);
                                if (!pidMap.isEmpty()) {
                                    Set<String> pids = pidMap.keySet();
                                    for (String pid : pids) {
                                        List<String> list = pidMap.get(pid);
                                        if (CollectionUtils.isNotEmpty(list)) {
                                            ProductClickBean productClickBean = new ProductClickBean(time, url,
                                                    "button", position, pid, list.size());
                                            buttonClicks.add(productClickBean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(CollectionUtils.isNotEmpty(buttonClicks)){
            meiliyunDAO.insertPclick(buttonClicks);
        }
        if(CollectionUtils.isNotEmpty(iconClicks)){
            meiliyunDAO.insertPclick(iconClicks);
        }
        if(CollectionUtils.isNotEmpty(bannerClicks)){
            meiliyunDAO.insertPclick(bannerClicks);
        }

        LOGGER.info("end to analyser log : " + fileName);
    }

    public String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        int YEAR = now.get(Calendar.YEAR);
        int MONTH = now.get(Calendar.MONTH);
        int DAY = now.get(Calendar.DAY_OF_MONTH);
        String dateStr = YEAR + "-" + (MONTH + 1) + "-" + (DAY - 1);

        return dateStr;
    }

    public String formateDate(String dataStr) {

        if (StringUtils.isNotBlank(dataStr)) {
            String replaceAll = dataStr.replaceAll("Jan", "01").replaceAll("Feb", "02").replaceAll("Mar", "03")
                    .replaceAll("Apr", "04").replaceAll("May", "05").replaceAll("Jun", "06").replaceAll("Jul", "07")
                    .replaceAll("Aug", "08").replaceAll("Sep", "09").replaceAll("Oct", "10").replaceAll("Nov", "11")
                    .replaceAll("Dec", "12");

            String[] split = replaceAll.replaceAll("/", ":").replaceAll(" ", "").split(":");
            String day = split[0];
            String month = split[1];
            String year = split[2];
            String hour = split[3];
            String min = split[4];
            return year + "-" + month + "-" + day + " " + hour + ":" + min + ":00";
        }
        return null;

    }

    public static void main(String[] args) throws ParseException, IOException, SQLException {

        System.out.println("https://www.xianjinshijie.com:8443/show_list_page.do".split("8443/")[1]);

    }

}
