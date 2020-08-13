package com.zcx.zhihuCrawler;

import com.zcx.zhihuCrawler.swing.SwingCrawlerWin;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})//不使用数据库
public class ZhihuCrawlerApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ZhihuCrawlerApplication.class);
        ApplicationContext applicationContext = builder.headless(false).run(args);
        SwingCrawlerWin swing = applicationContext.getBean(SwingCrawlerWin.class);
        swing.create();
    }
}
