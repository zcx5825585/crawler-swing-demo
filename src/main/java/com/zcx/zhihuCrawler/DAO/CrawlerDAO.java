package com.zcx.zhihuCrawler.DAO;

import com.zcx.zhihuCrawler.VO.Question;
import org.springframework.stereotype.Repository;
//import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 类说明
 *
 * @author zcx
 * @version 创建时间：2018/10/11  11:09
 */
//@Mapper
@Repository
public interface CrawlerDAO {

    void saveQuestion(List<Question> questions);

    void createTable();

    List<String> getSavedQuestionsList();

    List<String> getLastQuestionIds();
}
