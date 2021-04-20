package com.example.demo.java8;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

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

        Date aaa = null;
        String format = DateFormatUtils.format(aaa, "yyyy-MM-dd");

        System.out.println(format);

    }


}
