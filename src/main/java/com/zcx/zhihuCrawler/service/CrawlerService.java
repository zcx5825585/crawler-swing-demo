package com.zcx.zhihuCrawler.service;

import com.zcx.zhihuCrawler.DAO.CrawlerDAO;
import com.zcx.zhihuCrawler.VO.Question;
import com.zcx.zhihuCrawler.crawler.QuestionCrawler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/11  11:09
 */
@Service
public class CrawlerService {
    @Resource
    private CrawlerDAO crawlerDAO;

    private QuestionCrawler crawler;

    private JTextArea console = new JTextArea() {
        @Override
        public void append(String str) {
            super.append(str + "\n");
            this.setCaretPosition(this.getText().length());
        }
    };

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean isRunning = false;
    private boolean saveThreadRunning = false;

    public void zhihuRun(String id) throws Exception {
        crawlerDAO.createTable();
        console.paintImmediately(console.getBounds());
        String msg;
        if (isRunning) {
            console.append("正在运行");
            throw new Exception("正在运行");
        }

        List<String> savedQuestionIdsList = crawlerDAO.getSavedQuestionsList();
        msg = "已有数据：" + savedQuestionIdsList.size() + "条";
        console.append(msg);
        List<String> questionIds;
        if (id == null) {
            if (savedQuestionIdsList.size() > 0) {
                questionIds = crawlerDAO.getLastQuestionIds();
                console.append("最新数据ID：" + questionIds.get(0) + "...");
            } else {
                console.append("ID不能为空");
                throw new Exception("ID不能为空");
            }
        } else {
            questionIds = new ArrayList<>();
            questionIds.add(id);
        }

        new Thread(() -> {
            try {
                console.append(formatter.format(new Date()) + "  开始爬取");
                if (questionIds.size() == 0) {
                    throw new Exception("请输入ID");
                }
                crawler = new QuestionCrawler(questionIds, savedQuestionIdsList);
                crawler.run();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            console.append(formatter.format(new Date()) + "  结束爬取");
            isRunning = false;
        }).start();
        isRunning = true;

        if (!saveThreadRunning) {
            new Thread(() -> {
                saveThreadRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(5 * 60 * 1000);
                        zhihuSave();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        zhihuStop();
                        return;
                    }
                }
                saveThreadRunning = false;
            }).start();
        }
    }

    public String zhihuSave() {
        String msg;
        try {
            List<Question> questions = crawler.getQuestionList();
            msg = formatter.format(new Date()) + "  添加" + questions.size() + "条数据";
            if (questions.size() != 0) {
                crawlerDAO.saveQuestion(questions);
                crawler.initQuestionList();
            }
        } catch (NullPointerException e) {
            msg = "未启动！！！";
        } catch (Exception e) {
            msg = e.getMessage();
        }
        console.append(msg);
        return msg;
    }

    public String zhihuStop() {
        String msg;
        try {
            crawler.stop();
            zhihuSave();
            isRunning = false;
            msg = formatter.format(new Date()) + "  停止爬取";
        } catch (NullPointerException e) {
            msg = "未启动！！！";
        }
        console.append(msg);
        return msg;
    }

    public JTextArea getConsole() {
        return console;
    }

}
