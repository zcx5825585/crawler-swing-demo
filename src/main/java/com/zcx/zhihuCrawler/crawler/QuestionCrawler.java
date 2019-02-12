package com.zcx.zhihuCrawler.crawler;

import com.zcx.zhihuCrawler.VO.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/8  16:10
 */
public class QuestionCrawler {

    public List<String> waitList;
    public List<String> resultList;
    private List<Question> questionList = new ArrayList<>();

    public QuestionCrawler(List<String> startingURL) {
        initWaitList();
        initResultList();
        setStart(startingURL);
    }

    public void stop() {
        waitList.clear();
        resultList.clear();
    }

    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }

    public void initQuestionList() {
        questionList.clear();
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    private void setStart(List<String> startingURLs) {
        //将起始的URL添加到listOfPendingURLs，然后通过一个while循环重复处理listOfPendingURLs中每一个URL
        waitList = startingURLs;
    }

    public void run() {
        while (!waitList.isEmpty()) {
            //将列表中第一个RUL去除，如果该RUL没有被处理过则对其进行处理
            String urlString = waitList.remove(0);
            getSubURLs(urlString);
            try {
                int timeUp = 500;
                int random = (int) (Math.random() * 300);
                Thread.sleep(timeUp + random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void initWaitList() {
        this.waitList = new ArrayList<>();
    }

    public void initResultList() {
        this.resultList = new ArrayList<>();
    }

    protected boolean notExist(String id) {
        if (!resultList.contains(id)) {
            waitList.add(id);
            resultList.add(id);
            return true;
        } else {
            return false;
        }
    }

    private void getSubURLs(String questionId) {
        //该方法为每个给定的URL返回一个URL列表
        java.net.URL url = null;
        try {
            url = new java.net.URL("https://www.zhihu.com/api/v4/questions/" + questionId + "/similar-questions?limit=5");
            BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line = bReader.readLine();
            doSome(line);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doSome(String line) {
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

                    questionList.add(question);
                }
            }
        }
    }

    ;

    public Question turnToQuestion(int startIndex, String line) {
        Question question = new Question();
        int endIndex;

        System.out.println(line);
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
