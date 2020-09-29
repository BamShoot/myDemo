import com.bamboo.KafkaApplication;
import com.bamboo.kafka.KafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *@Author xu.xudong
 *@Date 2020/9/28 16:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
public class kafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void test(){
        kafkaProducer.sendMessage("Hello Kafka");
    }

    @Test
    public void test1(){
        kafkaProducer.sendWithCallBack("Hello Kafka");
    }
}
