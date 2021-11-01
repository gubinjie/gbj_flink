package com.paat.gbj_flink.application;

import com.paat.gbj_flink.core.ApplicationContext;
import com.paat.gbj_flink.core.FlinkApplication;
import com.paat.gbj_flink.task.core.ProcessCrawlerEpmQzwTask;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年06月24日
 */
public class ProcessCrawlerEpmQzwApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext application = FlinkApplication.run(ProcessCrawlerEpmQzwTask.class);
        application.execute("paat_mongo_oplog_epm_qzw_crawling_data");
    }
}