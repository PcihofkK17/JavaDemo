package com.example.demo.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author f_bao
 * @create 2018/5/18
 */
public class StreamDemo {

    /**
     * 流的构造和 map 方法的使用
     *  map 和 flatMap 的区别
     *      map: 会将一个元素变成一个新的 stream
     *      flatMap: 会将结果打平，得到一个单个元素
     */
    @Test
    public void stream1(){

        String[] strings = {"a", "b", "c"};
        List<String> collect = Arrays.asList(strings).stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect);

        List<String> collect1 = Arrays.asList(strings).stream().map(n -> n + n).collect(Collectors.toList());
        System.out.println(collect1);

        /**获取单词，并且去重**/
        List<String> list = Arrays.asList("hello welcome", "world hello", "hello world",
                "hello world welcome");

        //map和flatmap的区别
        list.stream().map(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("---------- ");
        list.stream().flatMap(item -> Arrays.stream(item.split(" "))).distinct().collect(Collectors.toList()).forEach(System.out::println);
    }

    /**
     * stream filter 测试
     *  用来 过滤 数据
     */
    @Test
    public void filter(){

        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        List<Integer> collect = Stream.of(sixNums).filter(m -> m > 3).collect(Collectors.toList());
        System.out.println(collect);

    }

    /**
     * stream foreach 就相当于之前的 超级 for ，遍历
     */
    @Test
    public void foreach(){

        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Stream.of(sixNums).filter(n -> n > 2).forEach(p -> System.out.println("this is " + p));

    }

    /**
     * peek：对每一个元素进行操作，并返回一个新的 stream
     */
    @Test
    public void peek(){

        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());

    }

    /**
     * 返回 stream 第一个元素
     */
    @Test
    public void findFirst(){

        Optional<String> first = Stream.of("one", "two", "three", "four").findFirst();
        System.out.println(first.get());
    }

    @Test
    public void reduce(){

        // 字符串拼接
        String reduce = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println(reduce);

        // 求最小值

    }


}
