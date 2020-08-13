package com.zcx.zhihuCrawler.crawler;

import com.google.gson.Gson;
import com.zcx.zhihuCrawler.VO.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/8  16:10
 */
public class QuestionCrawler {

    public List<String> waitList;//已找到链接但未保存的数据
    public List<String> resultList;//已爬取列表，用于判断是否再次爬取
    private List<Question> questionList = new ArrayList<>();//已爬取并等待保存的数据

    Gson gson = null;

    DecimalFormat df = new DecimalFormat("###0");

    Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    //初始化
    public QuestionCrawler(List<String> startingURL) {
        //将起始的URL添加到waitList，然后通过一个while循环重复处理waitList中每一个URL
        this.waitList = startingURL;
    }

    //停止
    public void stop() {
        waitList.clear();
        resultList.clear();
    }

    //设置已爬取列表
    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }

    //清空待保存数据
    public void initQuestionList() {
        questionList.clear();
    }

    //获取待保存数据
    public List<Question> getQuestionList() {
        return questionList;
    }

    public void run() {
        while (!waitList.isEmpty()) {
            //将列表中第一个RUL去除,并对其进行处理
            String urlString = waitList.remove(0);
            sendAndGetSubURLs(urlString);
            try {
                //设置爬取间隔
                int timeUp = 500;
                int random = (int) (Math.random() * 300);
                Thread.sleep(timeUp + random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAndGetSubURLs(String questionId) {
        java.net.URL url = null;
        try {
            //发送请求
            url = new java.net.URL("https://www.zhihu.com/api/v4/questions/" + questionId + "/similar-questions?limit=5");
            BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String line = bReader.readLine();//返回的结果 为一行数据 其中包含多个下级数据
            System.out.println(line);
//            getSubURLsByOrigin(line);
            getSubURLsByJson(line);
            //将id添加到已爬取列表
            resultList.add(questionId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将返回的数据(必须为json格式)转为map，从中获取下级数据
    protected void getSubURLsByJson(String line) {
        Map<String, Object> jsonMap = this.getGson().fromJson(line, Map.class);
        List<Object> data = (List<Object>) jsonMap.get("data");
        for (Object datum : data) {
            Map<String, Object> map = (Map<String, Object>) datum;
            Double doubleId = (Double) map.get("id");
            String id = df.format(doubleId);
            if (notExist(id)) {
                //转为Question对象
                Question question = turnToQuestion(map);
                question.setId(id);
                //添加到待保存列表
                questionList.add(question);
            }
        }
    }

    private Question turnToQuestion(Map<String, Object> map) {
        Question question = new Question();

        String title = (String) map.get("title");
        question.setTitle(title);

        Double answerCount = (Double) map.get("answer_count");
        question.setAnswerCount(df.format(answerCount));

        Double commentCount = (Double) map.get("comment_count");
        question.setCommentCount(df.format(commentCount));

        Double followerCount = (Double) map.get("follower_count");
        question.setFollowerCount(df.format(followerCount));

        return question;
    }

    //返回的数据获取下级数据
    protected void getSubURLsByOrigin(String line) {
        int startIndex = 0;
        while (startIndex >= 0) {
            startIndex = line.indexOf("\"id\":", startIndex);//寻找该行中的URL
            if (startIndex > 0) {
                startIndex = startIndex + "\"id\":".length();
                int endIndex = line.indexOf(",", startIndex);
                String id = line.substring(startIndex, endIndex);
                if (notExist(id)) {
                    Question question = turnToQuestion(startIndex, line);
                    question.setId(id);
                    //将
                    questionList.add(question);
                }
            }
        }
    }

    //判断是否已经获得了下级链接
    protected boolean notExist(String id) {
        //若未爬取过，添加到待爬取列表
        if (!resultList.contains(id)) {
            waitList.add(id);
            return true;
        } else {
            return false;
        }
    }

    //将string转为Question对象
    public Question turnToQuestion(int startIndex, String line) {
        Question question = new Question();
        int endIndex;

        startIndex = line.indexOf("\"title\":\"", startIndex);
        startIndex = startIndex + "\"title\":\"".length();
        endIndex = line.indexOf("\",", startIndex);
        String title = line.substring(startIndex, endIndex);
        question.setTitle(title);

        startIndex = line.indexOf("\"answer_count\":", startIndex);
        startIndex = startIndex + "\"answer_count\":".length();
        endIndex = line.indexOf(",", startIndex);
        String answerCount = line.substring(startIndex, endIndex);
        question.setAnswerCount(answerCount);

        startIndex = line.indexOf("\"comment_count\":", startIndex);
        startIndex = startIndex + "\"comment_count\":".length();
        endIndex = line.indexOf(",", startIndex);
        String commentCount = line.substring(startIndex, endIndex);
        question.setCommentCount(commentCount);


        startIndex = line.indexOf("\"follower_count\":", startIndex);
        startIndex = startIndex + "\"follower_count\":".length();
        endIndex = line.indexOf(",", startIndex);
        String followerCount = line.substring(startIndex, endIndex);
        question.setFollowerCount(followerCount);

        return question;
    }

}
