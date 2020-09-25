package bamboo.demo.aspect;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Aspect {

    @RequestMapping("test")
    public void test(int a){
        System.out.println("Aspect.........."+a);
    }
}
