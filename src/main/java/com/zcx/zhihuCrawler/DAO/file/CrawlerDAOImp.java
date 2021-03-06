package com.zcx.zhihuCrawler.DAO.file;

import com.zcx.zhihuCrawler.DAO.CrawlerDAO;
import com.zcx.zhihuCrawler.VO.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CrawlerDAOImp implements CrawlerDAO {


    @Value("${filePath}")
    private String filePath;

    @Override
    public void saveQuestion(List<Question> questions) {
        try {
            FileOutputStream out = new FileOutputStream(filePath, true);
            OutputStreamWriter osw = new OutputStreamWriter(out, "utf-8");
            PrintWriter p = new PrintWriter(osw);
            questions.forEach(one -> p.println(one.toString()));
            p.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getSavedQuestionsList() {
        List<String> savedQuestionsList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            while (reader.ready()) {
                String line = reader.readLine();
                savedQuestionsList.add(line.split(" ")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedQuestionsList;
    }

    @Override
    public List<String> getLastQuestionIds() {
        List<String> savedQuestionIdsList = this.getSavedQuestionsList();
        int endIndex = savedQuestionIdsList.size() - 1;
        return savedQuestionIdsList.subList(endIndex > 100 ? endIndex - 100 : 0, endIndex);
    }
}
