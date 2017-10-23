package com.mitewater.copy;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.thread.DefaultThreadFactory;
import com.mitewater.copy.thread.DefaultThreadPool;
import com.mitewater.copy.thread.ReadFileRunnable;
import com.mitewater.copy.thread.WriteFileRunnable;

import java.io.File;
import java.nio.channels.FileChannel;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 */
public class App {

    private static ExecutorService readExecutorService = new DefaultThreadPool(DefaultThreadFactory.ThreadType.FILE_READ_THREAD);
    private static ExecutorService writeExecutorService = new DefaultThreadPool(DefaultThreadFactory.ThreadType.FILE_WRITE_THREAD);

    public static void main(String[] args) {
        start(SystemConsts.SOURCE_PATH_URL);
    }


    public static void start(String sourcePath){
        Queue<Map<String,FileChannel>> fileChannels = new LinkedBlockingQueue<Map<String, FileChannel>>(1000);
        File file = new File(sourcePath);
        if(file.exists()) {
            LinkedList<File> dirFileList = new LinkedList<>();
            dirFileList.addFirst(file);
            while(!dirFileList.isEmpty()) {
                File pathFile = dirFileList.removeFirst();
                File[] innerFiles = pathFile.listFiles();
                readExecutorService.submit(ReadFileRunnable.newInstance(fileChannels,pathFile.getAbsolutePath()));
                System.out.println(pathFile.getAbsolutePath());
                writeExecutorService.submit(WriteFileRunnable.newInstance(fileChannels));
                if(null!=innerFiles) {
                    for(File innerFile:innerFiles){
                        if(innerFile.isDirectory()){
                            dirFileList.add(innerFile);
                        }
                        readExecutorService.submit(ReadFileRunnable.newInstance(fileChannels,innerFile.getAbsolutePath()));
                        System.out.println(innerFile.getAbsolutePath());
                        writeExecutorService.submit(WriteFileRunnable.newInstance(fileChannels));
                    }
                }
            }
        }
    }

    }
