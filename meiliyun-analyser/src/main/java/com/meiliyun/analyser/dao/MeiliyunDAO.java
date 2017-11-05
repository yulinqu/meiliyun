package com.meiliyun.analyser.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliyun.analyser.bean.ProductClickBean;
import com.meiliyun.analyser.bean.PvUv;

/**
 * meiliyun 相关数据操作
 * 
 * @author yulinqu
 */
@Repository
public class MeiliyunDAO extends MeiliyunBaseDAO {

    private final static String NAME_SPACE_MODEL = "meiliyun";

    @Override
    protected String nameSpace() {
        return NAME_SPACE_MODEL;
    }

    /**
     * 每天每分钟 的pvuv插入db
     * 
     * @param pvuv
     * @throws SQLException
     */
    public void insertPvuv(List<PvUv> pvuv) throws SQLException {
        super.insert("insert_pvuv", pvuv);
    }

    /**
     * 插入点击数
     * 
     * @param pclick
     * @throws SQLException
     */
    public void insertPclick(List<ProductClickBean> pclick) throws SQLException {
        super.insert("insert_pclick", pclick);
    }


    public List<Map<String, Object>> getPvuvByTime(String url, String startTime, String endTime, String timeUnit,String channel)
            throws SQLException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start_time", startTime);
        params.put("end_time", endTime + " 23:59:59");
        params.put("url", url);
        params.put("channel", channel);
        if (timeUnit.equalsIgnoreCase("hour")){
            if("each".equalsIgnoreCase(channel)){
                return super.queryForList("get_hour_pvuv_channel_each", params);
            }
            if("all".equalsIgnoreCase(channel)){
                return super.queryForList("get_hour_pvuv", params);
            }
            return super.queryForList("get_hour_pvuv_channel", params);
        }

        else if (timeUnit.equalsIgnoreCase("minute")){
            if("each".equalsIgnoreCase(channel)){
                return super.queryForList("get_minute_pvuv_channel_each", params);
            }
            if("all".equalsIgnoreCase(channel)){
                return super.queryForList("get_minute_pvuv", params);
            }
            return super.queryForList("get_minute_pvuv_channel", params);
        }

        else if (timeUnit.equalsIgnoreCase("day")){
            if("each".equalsIgnoreCase(channel)){
                return super.queryForList("get_day_pvuv_channel_each", params);
            }
            if("all".equalsIgnoreCase(channel)){
                return super.queryForList("get_day_pvuv", params);
            }
            return super.queryForList("get_day_pvuv_channel", params);
        }

        else if (timeUnit.equalsIgnoreCase("week")){
            if("each".equalsIgnoreCase(channel)){
                return super.queryForList("get_week_pvuv_channel_each", params);
            }
            if("all".equalsIgnoreCase(channel)){
                return super.queryForList("get_week_pvuv", params);
            }
            return super.queryForList("get_week_pvuv_channel", params);
        }

        else if (timeUnit.equalsIgnoreCase("month")){
            if("each".equalsIgnoreCase(channel)){
                return super.queryForList("get_month_pvuv_channel_each", params);
            }
            if("all".equalsIgnoreCase(channel)){
                return super.queryForList("get_month_pvuv", params);
            }
            return super.queryForList("get_month_pvuv_channel", params);
        }

        return null;

    }

    public List<Map<String, Object>> getClickCount(String url, String startTime, String endTime, String timeUnit,
            String advertisment,String channel) throws SQLException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start_time", startTime);
        params.put("end_time", endTime + " 23:59:59");
        params.put("url", url);
        params.put("ad", advertisment);
        params.put("channel", channel);

        if (timeUnit.equalsIgnoreCase("hour")) {

            //指定产品 指定渠道
            //指定产品 全部渠道不分类
            //指定产品 全部渠道分类
            if(!advertisment.equalsIgnoreCase("all")&&!advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_hour_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_hour_click_each_channel", params);
                }
                return super.queryForList("get_hour_click_channel", params);

            }

            //全部产品不分类 指定渠道
            //全部产品不分类 全部渠道不分类
            //全部产品不分类 全部渠道分类
            if(advertisment.equalsIgnoreCase("all")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_hour_all_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_hour_all_click_each_channel", params);
                }
                return super.queryForList("get_hour_all_click_channel", params);

            }

            //全部产品分类 指定渠道
            //全部产品分类 全部渠道不分类
            //全部产品分类 全部渠道分类

            if(advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_hour_each_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_hour_each_click_each_channel", params);
                }
                return super.queryForList("get_hour_each_click_channel", params);

            }






        } else if (timeUnit.equalsIgnoreCase("minute")) {

            //指定产品 指定渠道
            //指定产品 全部渠道不分类
            //指定产品 全部渠道分类
            if(!advertisment.equalsIgnoreCase("all")&&!advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_minute_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_minute_click_each_channel", params);
                }
                return super.queryForList("get_minute_click_channel", params);

            }

            //全部产品不分类 指定渠道
            //全部产品不分类 全部渠道不分类
            //全部产品不分类 全部渠道分类
            if(advertisment.equalsIgnoreCase("all")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_minute_all_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_minute_all_click_each_channel", params);
                }
                return super.queryForList("get_minute_all_click_channel", params);

            }

            //全部产品分类 指定渠道
            //全部产品分类 全部渠道不分类
            //全部产品分类 全部渠道分类

            if(advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_minute_each_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_minute_each_click_each_channel", params);
                }
                return super.queryForList("get_minute_each_click_channel", params);

            }



        } else if (timeUnit.equalsIgnoreCase("day")) {

            //指定产品 指定渠道
            //指定产品 全部渠道不分类
            //指定产品 全部渠道分类
            if(!advertisment.equalsIgnoreCase("all")&&!advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_day_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_day_click_each_channel", params);
                }
                return super.queryForList("get_day_click_channel", params);

            }

            //全部产品不分类 指定渠道
            //全部产品不分类 全部渠道不分类
            //全部产品不分类 全部渠道分类
            if(advertisment.equalsIgnoreCase("all")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_day_all_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_day_all_click_each_channel", params);
                }
                return super.queryForList("get_day_all_click_channel", params);

            }

            //全部产品分类 指定渠道
            //全部产品分类 全部渠道不分类
            //全部产品分类 全部渠道分类

            if(advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_day_each_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_day_each_click_each_channel", params);
                }
                return super.queryForList("get_day_each_click_channel", params);

            }


        } else if (timeUnit.equalsIgnoreCase("week")) {

            //指定产品 指定渠道
            //指定产品 全部渠道不分类
            //指定产品 全部渠道分类
            if(!advertisment.equalsIgnoreCase("all")&&!advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_week_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_week_click_each_channel", params);
                }
                return super.queryForList("get_week_click_channel", params);

            }

            //全部产品不分类 指定渠道
            //全部产品不分类 全部渠道不分类
            //全部产品不分类 全部渠道分类
            if(advertisment.equalsIgnoreCase("all")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_week_all_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_week_all_click_each_channel", params);
                }
                return super.queryForList("get_week_all_click_channel", params);

            }

            //全部产品分类 指定渠道
            //全部产品分类 全部渠道不分类
            //全部产品分类 全部渠道分类

            if(advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_week_each_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_week_each_click_each_channel", params);
                }
                return super.queryForList("get_week_each_click_channel", params);

            }


        } else if (timeUnit.equalsIgnoreCase("month")) {

            //指定产品 指定渠道
            //指定产品 全部渠道不分类
            //指定产品 全部渠道分类
            if(!advertisment.equalsIgnoreCase("all")&&!advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_month_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_month_click_each_channel", params);
                }
                return super.queryForList("get_month_click_channel", params);

            }

            //全部产品不分类 指定渠道
            //全部产品不分类 全部渠道不分类
            //全部产品不分类 全部渠道分类
            if(advertisment.equalsIgnoreCase("all")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_month_all_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_month_all_click_each_channel", params);
                }
                return super.queryForList("get_month_all_click_channel", params);

            }

            //全部产品分类 指定渠道
            //全部产品分类 全部渠道不分类
            //全部产品分类 全部渠道分类

            if(advertisment.equalsIgnoreCase("each")){

                if(channel.equalsIgnoreCase("all")){
                    return super.queryForList("get_month_each_click_all_channel", params);
                }
                if(channel.equalsIgnoreCase("each")){
                    return super.queryForList("get_month_each_click_each_channel", params);
                }
                return super.queryForList("get_month_each_click_channel", params);

            }

        }

        return null;
    }

}
