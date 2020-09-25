package com.bamboo.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *@Author xu.xudong
 *@Date 2020/9/25 10:55
 */
public class EsUtil {



    /**
     * 查询数据
     *
     * @param index   索引
     * @param must    符合条件
     * @param mustNot 排除条件
     * @param from    从第几页开始
     * @param size    获取记录数
     * @param clazz   返回实体类型
     * @return
     * @throws IOException
     */
    public static <T> List<T> search(ElasticsearchRestTemplate esTemplate,Map<String, Object> must,
                                     Map<String, Object> mustNot, Map<String, Object> should, Integer from,
                                     Integer size, Class<T> clazz) {
        if (clazz == null) {
            return (List<T>) search( esTemplate,must, mustNot, should, from, size, Map.class);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //符合条件 AND
        if (must != null) {
            must.forEach((k, v) -> queryBuilder.must(QueryBuilders.matchQuery(k, v)));
        }
        //不符合条件 NOT
        if (mustNot != null) {
            mustNot.forEach((k, v) -> queryBuilder.mustNot(QueryBuilders.matchQuery(k, v)));
        }
        //或者 OR
        if (should != null) {
            should.forEach((k, v) -> queryBuilder.should(QueryBuilders.matchQuery(k, v)));
        }
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        //分页查询
        if (from != null && size != null) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(from, size));
        }



        SearchHits<T> searchHits = esTemplate.search(nativeSearchQueryBuilder.build(), clazz);
        List<T> collect = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());



        System.out.println(JSONObject.toJSONString(searchHits));


        return collect;
    }



}
