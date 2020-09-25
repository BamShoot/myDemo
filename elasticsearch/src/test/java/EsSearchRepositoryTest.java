import com.alibaba.fastjson.JSONObject;
import com.bamboo.AdminApplication;
import com.bamboo.es.Article;
import com.bamboo.es.EsSearchRepositoryImpl;
import com.bamboo.es.EsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class)
public class EsSearchRepositoryTest {

    @Autowired
    private EsSearchRepositoryImpl repository;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Test
    public void test1(){
        repository.save();
    }

    @Test
    public void test(){
        repository.deleteIndex();
    }

    @Test
    public void test2(){
        repository.findByTitle();
    }

    @Test
    public void test3(){
        repository.page();
    }

    @Test
    public void test4(){

        HashMap<String, Object> must = new HashMap<>();
        List<Article> search = EsUtil.search(esTemplate,must, null, null, null, null, Article.class);
        System.out.println(JSONObject.toJSONString(search));
    }
}
