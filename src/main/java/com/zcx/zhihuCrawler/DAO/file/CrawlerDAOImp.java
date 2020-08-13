package com.zcx.zhihuCrawler.DAO.file;

import com.zcx.zhihuCrawler.DAO.CrawlerDAO;
import com.zcx.zhihuCrawler.VO.Question;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CrawlerDAOImp implements CrawlerDAO {
    private int count=0;
    @Override
    public void saveQuestion(List<Question> questions) {
        try {
            FileOutputStream out=new FileOutputStream("questions");
            PrintStream p=new PrintStream(out);
            questions.forEach(one->{
                count++;
                p.println(one.toString());
            });
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        File file=new File("questions");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSavedQuestionsList() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getLastQuestionIds() {
        return new ArrayList<>();
    }
}
