package com.bamboo.demo;


public interface Engine {
    void run();
}

class HighEndEngine implements Engine {
    @Override
    public void run() {
        System.out.println("高端发动机，启动快");
    }
}

class LowEndEngine implements Engine {
    @Override
    public void run() {
        System.out.println("低端发动机，启动慢");
    }
}

interface Tyre {
    void use();
}

class HighEndTyre implements Tyre {
    @Override
    public void use() {
        System.out.println("高端轮胎，耐磨防滑");
    }
}

class LowEndTyre implements Tyre {
    @Override
    public void use() {
        System.out.println("低端轮胎");
    }
}

interface CarFactory {
    Engine createEngine();

    Tyre cteateTyre();
}

class HighEndCarFactory implements CarFactory {
    @Override
    public Engine createEngine() {
        return new HighEndEngine();
    }

    @Override
    public Tyre cteateTyre() {
        return new HighEndTyre();
    }
}

class LowEndCarFactory implements CarFactory {
    @Override
    public Engine createEngine() {
        return new LowEndEngine();
    }

    @Override
    public Tyre cteateTyre() {
        return new LowEndTyre();
    }
}

class Client {
    public static void main(String[] args) {
        CarFactory cf = new HighEndCarFactory();
        Engine engine = cf.createEngine();
        engine.run();
        Tyre tyre = cf.cteateTyre();
        tyre.use();

        for(int i=0;i<10;++i){
            System.out.println(i);
        }
    }
}