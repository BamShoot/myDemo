package com.bamboo.demo.es;


import com.alibaba.fastjson.JSON;
import com.bamboo.demo.common.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ESHandler {

    private static final RestHighLevelClient client = SpringUtil.getBean(RestHighLevelClient.class);


    /**
     * 查询索引是否存在
     *
     * @param index 索引
     * @return
     * @throws IOException
     */
    public static boolean existsInndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param index 索引
     * @return
     * @throws IOException
     */
    public static boolean createIndex(String index) throws IOException {
        if (existsInndex(index)) {
            return true;
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index 索引
     * @return
     * @throws IOException
     */
    public static boolean deleteIndex(String index) throws IOException {
        if (!existsInndex(index)) {
            return true;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }


    /**
     * 创建或更新文档
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @param object     创建或更新的数据
     * @throws IOException
     */
    public static void insertDocument(String index, String type, String documentId, Object object) throws IOException {
        IndexRequest request = new IndexRequest(index, type, documentId).source(JSON.toJSONString(object), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.info("ES|insertDocument--" + JSON.toJSONString(response));
    }

    /**
     * 根据documentId获取数据
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getDocument(String index, String type, String documentId) throws IOException {
        GetRequest request = new GetRequest(index, type, documentId);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 根据documentId获取数据
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @param clazz      返回的实体类型
     * @return
     * @throws IOException
     */
    public static <T> T getDocument(String index, String type, String documentId, Class<T> clazz) throws IOException {
        Map<String, Object> document = getDocument(index, type, documentId);
        return JSON.parseObject(JSON.toJSONString(document), clazz);
    }

    /**
     * 判断某条数据是否存在
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @return
     * @throws IOException
     */
    public static boolean existDocument(String index, String type, String documentId) throws IOException {
        GetRequest request = new GetRequest(index, type, documentId);
        return client.exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 根据documentId删除数据
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @throws IOException
     */
    public static void deleteDocument(String index, String type, String documentId) throws IOException {
        DeleteRequest request = new DeleteRequest(index, type, documentId);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        log.info("ES|deleteDocument--" + JSON.toJSONString(response));
    }

    /**
     * 更新文档
     *
     * @param index      索引
     * @param type       Type
     * @param documentId 文档 DocumentId
     * @param object     创建或更新的数据
     * @throws IOException
     */
    public static void updateDocument(String index, String type, String documentId, Object object) throws IOException {
        UpdateRequest request = new UpdateRequest(index, type, documentId).doc(JSON.toJSONString(object), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        log.info("ES|updateDocument--" + JSON.toJSONString(response));
    }

    /**
     * 查询数据
     *
     * @param index 索引
     * @param type  type
     * @param must  符合条件
     * @return
     * @throws IOException
     */
    public static ArrayList<Map> searchDocuments(String index, String type, Map<String, Object> must) throws IOException {
        return searchDocuments(index, type, must, null, 0, Integer.MAX_VALUE, Map.class);
    }

    /**
     * 查询数据
     *
     * @param index 索引
     * @param type  type
     * @param must  符合条件
     * @param clazz 返回实体类型
     * @return
     * @throws IOException
     */
    public static <T> ArrayList<T> searchDocuments(String index, String type, Map<String, Object> must, Class<T> clazz) throws IOException {
        return searchDocuments(index, type, must, null, 0, Integer.MAX_VALUE, clazz);
    }


    /**
     * 查询数据
     *
     * @param index 索引
     * @param type  type
     * @param must  符合条件
     * @param from  从第几条获取，第一个为0
     * @param size  获取记录数
     * @return
     * @throws IOException
     */
    public static ArrayList<Map> searchDocuments(String index, String type, Map<String, Object> must, int from, int size) throws IOException {
        return searchDocuments(index, type, must, null, from, size, Map.class);
    }

    /**
     * 查询数据
     *
     * @param index 索引
     * @param type  type
     * @param must  符合条件
     * @param from  从第几条获取，第一个为0
     * @param size  获取记录数
     * @param clazz 返回实体类型
     * @return
     * @throws IOException
     */
    public static <T> ArrayList<T> searchDocuments(String index, String type, Map<String, Object> must, int from, int size, Class<T> clazz) throws IOException {
        return searchDocuments(index, type, must, null, from, size, clazz);
    }

    /**
     * 查询数据
     *
     * @param index   索引
     * @param type    type
     * @param must    符合条件
     * @param mustNot 排除条件
     * @param from    从第几条获取，第一个为0
     * @param size    获取记录数
     * @return
     * @throws IOException
     */
    public static ArrayList<Map> searchDocuments(String index, String type, Map<String, Object> must, Map<String, Object> mustNot, int from, int size) throws IOException {
        return searchDocuments(index, type, must, mustNot, from, size, Map.class);
    }

    /**
     * 查询数据
     *
     * @param index   索引
     * @param type    type
     * @param must    符合条件
     * @param mustNot 排除条件
     * @param from    从第几条获取，第一个为0
     * @param size    获取记录数
     * @param clazz   返回实体类型
     * @return
     * @throws IOException
     */
    public static <T> ArrayList<T> searchDocuments(String index, String type, Map<String, Object> must, Map<String, Object> mustNot, int from, int size, Class<T> clazz) throws IOException {
        if (clazz == null) {
            return (ArrayList<T>) searchDocuments(index, type, must, mustNot, from, size);
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //符合条件
        if (must != null) {
            for (Map.Entry<String, Object> entry : must.entrySet()) {
                boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
        }
        //不符合条件
        if (mustNot != null) {
            for (Map.Entry<String, Object> entry : mustNot.entrySet()) {
                boolQueryBuilder.mustNot(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
            }
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(from);
        sourceBuilder.size(size);//获取记录数，默认10
        SearchRequest request = new SearchRequest();
        if (!StringUtils.isEmpty(index)) {
            request.indices(index);
        }
        if (!StringUtils.isEmpty(type)) {
            request.types(type);
        }
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<T> arrayList = new ArrayList<T>();
        for (SearchHit hit : hits) {
            log.info("ES|searchDocuments -> " + hit.getSourceAsString());
            arrayList.add(JSON.parseObject(hit.getSourceAsString(), clazz));
        }
        return arrayList;
    }


}
