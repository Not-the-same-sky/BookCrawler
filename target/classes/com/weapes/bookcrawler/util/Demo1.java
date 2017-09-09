package com.weapes.bookcrawler.util;

/**
 * Created by 不一样的天空 on 2017/6/20.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** * Created by jerry on 16-4-20. */
public class Demo1 {

    private static void readTxt(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //获取classpath路径
        System.out.println("classpath路径： " + Demo1.class.getClassLoader().getResource("").getPath());

        //获取当前类的加载路径
        System.out.println("当前类加载/路径： " + Demo1.class.getResource("").getPath());
        //获取classpath路径
        System.out.println("classpath/路径： " + Demo1.class.getClassLoader().getResource("./").getPath());

        //获取当前类的加载路径
        System.out.println("当前类加载路径： " + Demo1.class.getResource("/").getPath());
        // 读取文件resources目录中文件的若干种方法
        // 方法一：从classpath路径出发读取
        readTxt(Demo1.class.getClassLoader().getResource("conf/filecfg.json").getPath());
        // 方法二：从类加载路径出发，相当于使用绝对路径g
        readTxt(Demo1.class.getResource("/conf/filecfg.json").getPath());
//        // 方法三：从类加载路径出发，相当于使用相对路径
//        readTxt(Demo1.class.getResource("../../../conf/filecfg.json").getPath());
    }
}
