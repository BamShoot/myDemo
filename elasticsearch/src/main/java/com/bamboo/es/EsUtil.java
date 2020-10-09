package com.bamboo.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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
     * @param must    符合条件
     * @param mustNot 排除条件
     * @param page    第几页
     * @param size    获取记录数
     * @param clazz   返回实体类型
     * @return
     * @throws IOException
     */
    public static <T> List<T> search(ElasticsearchRestTemplate esTemplate,Map<String, Object> must,
                                     Map<String, Object> mustNot, Map<String, Object> should, Integer page,
                                     Integer size, Class<T> clazz) {
        if (clazz == null) {
            return (List<T>) search( esTemplate,must, mustNot, should, page, size, Map.class);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        /**
         * matchQuery：会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到。
         *
         * termQuery：不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到。
         */
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

        //NativeSearchQueryBuilder:将连接条件和聚合函数等组合
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        //分页查询
        if (page != null && size != null) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));
        }

        //排序，先按时间倒排，再按id正排
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("postTime").order(SortOrder.DESC));
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("id"));



        SearchHits<T> searchHits = esTemplate.search(nativeSearchQueryBuilder.build(), clazz);
        List<T> collect = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());



        System.out.println(JSONObject.toJSONString(searchHits));


        return collect;
    }



}
