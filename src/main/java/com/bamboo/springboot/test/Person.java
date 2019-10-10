package com.bamboo.springboot.test;

public class Person {

    public Person(int id) {
        System.out.println("person(" + id + ")");
    }
}

class Build {





    static{
        System.out.println("this is static block!");
    }

    Person p1 = new Person(1);//------------1-----------
    {
        System.out.println("this is non-static block!");
    }


    public Build() {
        System.out.println("this is build's block!");
        Person p2 = new Person(2);
    }

    Person p3 = new Person(3);

    public static void main(String[] args) {
        Build b = new Build();
    }

}
