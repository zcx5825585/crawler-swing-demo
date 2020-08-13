package com.zcx.zhihuCrawler.VO;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/10  13:49
 */
public class Question {
    private String id;
    private String title;
    private String answerCount;
    private String commentCount;
    private String followerCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    @Override
    public String toString() {
//        return "https://www.zhihu.com/question/" + id + " " + title + " 回答数：" + answerCount + " 评论数：" + commentCount + " 回复数：" + followerCount + "\n";
        return id + " https://www.zhihu.com/question/" + id + " " + title + " " + answerCount + " " + commentCount + " " + followerCount;
    }
}
