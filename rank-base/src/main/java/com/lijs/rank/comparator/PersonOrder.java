package com.lijs.rank.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class PersonOrder {

    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 20),
                new Person("Charlie", 30)
        );

// 方法1：使用Lambda表达式
        people.sort(Comparator.comparingInt(p -> p.age));

// 方法2：使用方法引用
        people.sort(Comparator.comparingInt(Person::getAge)); // 需添加getAge()方法
    }


    static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return 0;
        }
    }
}


