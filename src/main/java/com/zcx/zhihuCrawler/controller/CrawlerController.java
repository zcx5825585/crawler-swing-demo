package com.zcx.zhihuCrawler.controller;

import com.zcx.zhihuCrawler.service.CrawlerService;
import com.zcx.zhihuCrawler.service.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/11  10:59
 */
@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @RequestMapping("zhihu/run")
    public String zhihuRun(String id) {
        try {
            crawlerService.zhihuRun(id);
            return new JsonResult("开始运行").success().toString();
        } catch (Exception e) {
            return new JsonResult("运行失败").failure().toString();
        }
    }

    @RequestMapping("zhihu/save")
    public String zhihuSave() {
        return new JsonResult(crawlerService.zhihuSave()).success().toString();
    }

    @RequestMapping("zhihu/stop")
    public String zhihuStop() {
        return new JsonResult(crawlerService.zhihuStop()).success().toString();
    }


}
