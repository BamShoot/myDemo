package com.bamboo.es;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EsSearchRepositoryImpl {

    @Autowired
    private EsSearchRepository repository;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;



    public void save() {
        Article article = new Article(3L, "srpignMVC教程", "srpignMVC", "srpignMVC入门到放弃", new Date(), 22L);
        Article article1 = new Article(4L, "srpig教程", "spring", "spring入门到放弃", new Date(), 20L);
        //单条插入
        repository.save(article);
        //批量插入
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article1);
        repository.saveAll(articles);

    }

    public void deleteIndex() {
        esTemplate.deleteIndex(Article.class);
    }

    public void findByTitle() {
        //无需实现类，spring data根据方法名，自动生成实现类
        List<Article> articles = repository.findByTitle("srpig");
        articles.forEach(System.out::println);
    }

    public void page() {
        // 分页参数:分页从0开始，clickCount倒序
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "clickCount"));
        Page<Article> pageageRsutl = repository.findByContent("入门", pageable);
        System.out.println("总页数" + pageageRsutl.getTotalPages());
        List<Article> list = pageageRsutl.getContent();//结果


        for (Article article : list) {
            System.out.println(article.toString());
        }
    }



}
