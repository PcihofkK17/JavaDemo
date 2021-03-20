package com.example.demo.java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 〈一句话功能简述〉
 *
 * @author f_bao
 * @create 2018/5/15
 */
public class Demo {

    public static void main(String[] args) {
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
