package com.example.demo;

import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author bf
 * @create 2018/3/5
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class NewVersionDemo {

    public static void main(String[] args){
        System.out.println(isNewVersion("4.5.3", "4.5"));
    }

    private static int isNewVersion(String oldVersion, String newVersion) {
        if (!Pattern.matches("\\d+(\\.\\d+)+", newVersion)) {
            return -1;
        }

        String[] split1 = oldVersion.split("\\.");
        String[] split2 = newVersion.split("\\.");

        int length=Math.min(split1.length, split2.length);

        for(int i=0;i<length;i++){
            Long iii = (Long)Long.parseLong(split2[i]);
            int c = iii.compareTo(Long.parseLong(split1[i]));
            if (c != 0) {
                return c;
            }
        }
        // 解决 4.5 版本 和 4.5.3 版本之间的检验问题
        if(split1.length > split2.length){
            return -1;
        }else if(split1.length < split2.length){
            return 1;
        }else{
            return 0;
        }
    }

}
