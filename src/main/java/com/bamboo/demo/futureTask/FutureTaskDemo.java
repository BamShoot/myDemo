package com.bamboo.demo.futureTask;



import java.util.ArrayList;
import java.util.concurrent.*;

public class FutureTaskDemo {


    public static void main(String[] args) throws Exception{
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
//        ExecutorService executor = Executors.newFixedThreadPool(10);
        ArrayList<FutureTask<Result>> list=new ArrayList();
        for(int i=0;i<50;i++){
            MyTask myTask = new MyTask(i);
            FutureTask<Result> future=new FutureTask(myTask);
            executor.execute(future);
            list.add(future);
        }
        System.out.println("放入线程池结束");
        for (FutureTask<Result> result:list) {
            Result result1 = result.get();
            System.out.println(result1.getMessage());
        }
        executor.shutdown();
    }

}
