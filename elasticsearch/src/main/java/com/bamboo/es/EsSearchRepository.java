package com.bamboo.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 1、接口继承 ElasticsearchRepository类。默认会提供很多实现，比如 CRUD 和搜索相关的实现。
 *      类似于 JPA 读取数据，是使用 CrudRepository 进行操作 ES 数据。
 *      支持的默认方法有： count(), findAll(), findOne(ID),
 *                      delete(ID), deleteAll(), exists(ID),
 *                      save(DomainObject), save(Iterable)
 * 2、接口中声明方法：无需实现类，spring data根据方法名，自动生成实现类，方法名必须符合一定的规则,（这里还扩展出一种忽略方法名，根据注解的方式查询）
 *      关键字             方法命名
 *      And             findByNameAndPwd
 *      Or              findByNameOrSex
 *      Is              findById
 *      Between         findByIdBetween
 *      Like            findByNameLike
 *      NotLike         findByNameNotLike
 *      OrderBy         findByIdOrderByXDesc
 *      Not             findByNameNot
 * 3、自定义repository：在实现类中注入elasticsearchTemplate，实现上面两种方式不易实现的查询（例如：聚合、分组、深度翻页等）
 */
public interface EsSearchRepository extends ElasticsearchRepository<Article, String>  {

    public List<Article> findByTitle(String title);


    /**
     * AND 语句查询
     *
     * @param tile
     * @param clickCount
     * @return
     */
    List<Article> findByTitleAndClickCount(String tile, Integer clickCount);
    /**
     * OR 语句查询
     *
     * @param tile
     * @param clickCount
     * @return
     */
    List<Article> findByTitleOrClickCount(String tile, Integer clickCount);
    /**
     * 查询文章内容分页
     *
     * 等同于下面代码
     * @Query("{\"bool\" : {\"must\" : {\"term\" : {\"content\" : \"?0\"}}}}")
     * Page<Article> findByContent(String content, Pageable pageable);
     *
     * @param content
     * @param page
     * @return
     */
    Page<Article> findByContent(String content, Pageable page);
    /**
     * NOT 语句查询
     *
     * @param content
     * @param page
     * @return
     */
    Page<Article> findByContentNot(String content, Pageable page);
    /**
     * LIKE 语句查询
     *
     * @param content
     * @param page
     * @return
     */
    Page<Article> findByContentLike(String content, Pageable page);
}
