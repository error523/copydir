package com.mitewater.copy.util;

import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/15
 * Time: 16:57
 * Description: class for copydir try it self
 */
public class PropUtils {
    private static Properties PROP;

    static {
        readPropDefaultPropFile();
    }

    private static void readPropDefaultPropFile(){
        readPropFile("system.properties");
    }

    public static Properties readPropFile(String propFile){
        try {
            InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(propFile);
            PROP = new Properties();
            PROP.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PROP;
    }

    /**
     * 获取properties中的对应value，默认为根目录下
     * 的system.properties文件
     * @param key
     * @return
     */
    public static String getString(String key){
        return PROP.get(key).toString();
    }

}
