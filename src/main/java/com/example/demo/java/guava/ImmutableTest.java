package com.example.demo.java.guava;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;


public class ImmutableTest {

    /**
     * [a, b, c]
     * [a, b, c]
     * [a, b, c]
     * unmodifiableList [0]：b
     * list add a item after list:[a, b, c, baby]
     * list add a item after unmodifiableList:[a, b, c, baby]
     *
     * java.lang.UnsupportedOperationException
     * 	at java.util.Collections$UnmodifiableCollection.add(Collections.java:1055)
     *
     * Collections.unmodifiableList 返回一个不可变的集合
     *  但是当更改原 list (集合的话，这个不可变的集合，也可以被修改)，只是不能直接操作返回出来的这个集合，直接操作，会报错
     */
    @Test
    public void testJDKImmutable(){                                                                                                                                                                                                                                    
        List<String> list=new ArrayList<String>();                                                                               
        list.add("a");                                                                                                           
        list.add("b");                                                                                                           
        list.add("c");
        
        System.out.println(list);
        
        List<String> unmodifiableList=Collections.unmodifiableList(list); 
        
        System.out.println(unmodifiableList);
        
        List<String> unmodifiableList1=Collections.unmodifiableList(Arrays.asList("a","b","c")); 
        System.out.println(unmodifiableList1);
        
        String temp=unmodifiableList.get(1);
        System.out.println("unmodifiableList [0]："+temp);
                
        list.add("baby");
        System.out.println("list add a item after list:"+list);
        System.out.println("list add a item after unmodifiableList:"+unmodifiableList);
        
        unmodifiableList1.add("bb");
        System.out.println("unmodifiableList add a item after list:"+unmodifiableList1);
        
        unmodifiableList.add("cc");
        System.out.println("unmodifiableList add a item after list:"+unmodifiableList);        
    }

    /**
     * list：[a, b, c]
     * imlist：[a, b, c]
     * imOflist：[peida, jerry, harry]
     * imSortList：[a, b, c, d]
     * list add a item after list:[a, b, c, baby]
     * list add a item after imlist:[a, b, c]
     * imColorSet:[java.awt.Color[r=0,g=255,b=255], java.awt.Color[r=0,g=191,b=255]]
     *
     * ImmutableList 的各个类的使用，且 源数据更改之后，ImmutableList.of() 返回的数据，不会被更改
     */
    @Test
    public void testGuavaImmutable(){

        List<String> list=new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println("list："+list);

        ImmutableList<String> imlist=ImmutableList.copyOf(list);
        System.out.println("imlist："+imlist);

        ImmutableList<String> imOflist=ImmutableList.of("peida","jerry","harry");
        System.out.println("imOflist："+imOflist);

        ImmutableSortedSet<String> imSortList=ImmutableSortedSet.of("a", "b", "c", "a", "d", "b");
        System.out.println("imSortList："+imSortList);

        list.add("baby");
        System.out.println("list add a item after list:"+list);
        System.out.println("list add a item after imlist:"+imlist);

        ImmutableSet<Color> imColorSet =
                ImmutableSet.<Color>builder()
                        .add(new Color(0, 255, 255))
                        .add(new Color(0, 191, 255))
                        .build();

        System.out.println("imColorSet:"+imColorSet);
    }

    /**
     * imSet：[peida, jerry, harry, lisa]
     * imlist：[peida, jerry, harry, lisa]
     * imSortSet：[harry, jerry, lisa, peida]
     * list：[0x, 1x, 2x, 3x, 4x, 5x, 6x, 7x, 8x, 9x, 10x, 11x, 12x, 13x, 14x, 15x, 16x, 17x, 18x, 19x]
     * imInfolist：[2x, 3x, 4x, 5x, 6x, 7x, 8x, 9x, 10x, 11x, 12x, 13x, 14x, 15x, 16x, 17x]
     * imInfolistSize：16
     * imInfoSet：[4x, 5x, 6x, 7x, 8x, 9x, 10x, 11x, 12x, 13x, 14x]
     *
     */
    @Test
    public void testCotyOf(){
        ImmutableSet<String> imSet=ImmutableSet.of("peida","jerry","harry","lisa");
        System.out.println("imSet："+imSet);
        ImmutableList<String> imlist=ImmutableList.copyOf(imSet);
        System.out.println("imlist："+imlist);
        ImmutableSortedSet<String> imSortSet=ImmutableSortedSet.copyOf(imSet);
        System.out.println("imSortSet："+imSortSet);

        List<String> list=new ArrayList<String>();
        for(int i=0;i<20;i++){
            list.add(i+"x");
        }
        System.out.println("list："+list);
        ImmutableList<String> imInfolist=ImmutableList.copyOf(list.subList(2, 18));
        System.out.println("imInfolist："+imInfolist);
        int imInfolistSize=imInfolist.size();
        System.out.println("imInfolistSize："+imInfolistSize);
        ImmutableSet<String> imInfoSet=ImmutableSet.copyOf(imInfolist.subList(2, imInfolistSize-3));
        System.out.println("imInfoSet："+imInfoSet);
    }
}