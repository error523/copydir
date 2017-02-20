package com.mitewater.copy;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.exception.OutOfThreadPoolSizeException;
import com.mitewater.copy.pool.ThreadPool;
import com.mitewater.copy.read.FileReadAccessHelper;
import com.mitewater.copy.thread.FileReadThread;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        start(SystemConsts.SOURCE_PATH_URL);
    }

    public static void start(String sourcePath){
        FileReadAccessHelper fileReadAccessHelper = new FileReadAccessHelper();
        try {
            List<String> fileList = fileReadAccessHelper.listFile(sourcePath);
            if(null!=fileList){
                for (String path:fileList){
                    ThreadPool.newInstance().setFileReadThreads(new FileReadThread(path));
                    ThreadPool.newInstance().customse();
                }
            }
            List<String> pathList = fileReadAccessHelper.listDir(sourcePath);
            if(null!=pathList){
                for(String dir:pathList){
                    start(dir);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
