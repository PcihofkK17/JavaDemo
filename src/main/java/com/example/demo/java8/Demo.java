package com.example.demo.java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

/**
 * 〈一句话功能简述〉
 *
 * @author f_bao
 * @create 2018/5/15
 */
public class Demo {

    public static final String ARTICLE_CONTENT = format("%s_%s", "ArticleContent", "7.12.5");

    public static void main(String[] args) {

        System.out.println(ARTICLE_CONTENT);

        Arrays.asList("a", "b", "c").forEach(e -> System.out.println(e));


        Arrays.asList("a", "b", "c").forEach((String e) -> System.out.print(e + "_"));

        Arrays.asList("a", "b", "c").forEach((String e) -> {
            System.out.println(e);
            System.out.println("****************");
        });

        List<String> list = Arrays.asList("a", "t", "b");
        list.sort((o1, o2) -> o1.compareTo(o2));
        System.out.println(list);




        List<Integer> list1 = Arrays.asList(1, 3, 2, 5, 4);
        List<Integer> list2 = Arrays.asList(1, 3, 2, 5, 4);

        Collections.sort(list1, (a1, a2) -> a2 - a1);

        Collections.sort(list2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        System.out.println(list1);
        System.out.println(list2);


    }


}
