package com.bamboo.demo.test;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class StopThreadUnsafe {

    public static User u = new User();

    @Data
    public static class User {
        private int id;
        private String name;

        public User() {
            id = 0;
            name = "0";
        }
    }

    public static class ct extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("1111111");
                synchronized (u) {
                    int v = (int) (System.currentTimeMillis() / 1000);
                    System.out.println("222222");
                    u.setId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    u.setName(String.valueOf(v));
                    System.out.println("33333333");
                }
                Thread.yield();
                System.out.println("44444444");
            }
        }
    }

    public static class rt extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("555555");
                synchronized (u) {
                    System.out.println("666666");
                    if (u.getId() != Integer.parseInt(u.getName())) {
                        System.out.println(u.toString());
                    }
                }
                System.out.println("77777777");
                Thread.yield();
                System.out.println("888888");
            }
        }
    }


    public static void main(String[] args)  {
        BlockingQueue<String> blockingQueue=new SynchronousQueue<>();
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"\t put 1");
                blockingQueue.put("1");

                System.out.println(Thread.currentThread().getName()+"\t put 2");
                blockingQueue.put("2");

                System.out.println(Thread.currentThread().getName()+"\t put 3");
                blockingQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AAA").start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t"+blockingQueue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t"+blockingQueue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t"+blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();
    }


}
