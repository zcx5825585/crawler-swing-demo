package com.zcx.zhihuCrawler;

import com.zcx.zhihuCrawler.swing.SwingCrawlerWin;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ZhihuCrawlerApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ZhihuCrawlerApplication.class);
        ApplicationContext applicationContext = builder.headless(false).run(args);
        SwingCrawlerWin swing = applicationContext.getBean(SwingCrawlerWin.class);
        swing.create();
    }
}
