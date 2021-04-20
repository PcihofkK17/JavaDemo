package com.example.demo.java8;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author f_bao
 * @create 2018/5/18
 */
public class StreamDemo {

    @Test
    public void distinctTest() {
        Person p1 = new Person(1, "dY");
        Person p2 = new Person(1, "+0");
        Person p3 = new Person(2, "moon");
        Person p4 = new Person(3, "st");
        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);

        List<Person> collect = list.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);

    }


    @Test
    public void testList11() {
        List<Double> aaa = new ArrayList<>();
        aaa.add(1.0);
        aaa.add(0.58);
        aaa.add(9.56);
        aaa.add(1.53246);
        aaa.add(1.53247);

        List<Double> collect = aaa.stream().sorted((n1, n2) -> -new Double(n1).compareTo(new Double(n2))).collect(Collectors.toList());

        System.out.println(collect);

        List<Integer> list = Arrays.asList(new Integer[]{1,9,4,6,2,7,5,3});  //构造list，填充值

        list  = list.stream().sorted((n1,n2)->-n1.compareTo(n2)).collect(Collectors.toList());

        System.out.println(list);

    }


    @Test
    public void testList(){

        for (int i = 0; i < (double)1 / 100; i++) {
            System.out.println("22222222----------");
        }

        double i = (double)50 / 100;
        System.out.println(i);

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        localDate = localDate.minusDays(1);
        System.out.println(localDate);


        System.out.println(LocalDate.now().minusDays(-2).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    /**
     * 流的构造和 map 方法的使用
     *  map 和 flatMap 的区别
     *      map: 会将一个元素变成一个新的 stream
     *      flatMap: 会将结果打平，得到一个单个元素
     */
    @Test
    public void stream1(){

        List<Integer> tetst = new ArrayList<>();
        tetst.add(1);
        tetst.add(2);
        tetst.add(3);

        List<Boolean> collect = tetst.stream().map(c -> c >= 2).collect(Collectors.toList());
        System.out.println(collect);

        List<Integer> collect1 = tetst.stream().filter(c -> c >= 2).collect(Collectors.toList());
        System.out.println(collect);

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
