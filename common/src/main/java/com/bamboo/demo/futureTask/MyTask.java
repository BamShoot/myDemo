package com.bamboo.demo.futureTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyTask implements Callable {
    private Integer count;

    @Override
    public Object call() throws Exception {
        int i = new Random().nextInt(5000);
        Thread.sleep(i);
        Result result = new Result();
        result.setMessage("结果消息计数：" + count + "，seleep：" + i + "，线程：" + Thread.currentThread().getName());
        System.out.println("执行结束：" + count);
        return result;
    }


    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for(int i=0;i<50;i++){
            MyTask myTask = new MyTask(i);
            executor.submit(myTask);
        }
    }
}
