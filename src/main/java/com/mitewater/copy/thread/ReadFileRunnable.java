package com.mitewater.copy.thread;

import com.google.common.collect.ImmutableMap;
import com.mitewater.copy.read.FileReadAccessHelper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;

public class ReadFileRunnable implements Callable<Boolean>{

    private ReadFileRunnable(){}

    private Queue<Map<String,FileChannel>> fileChannels;
    private String filePath;

    public static ReadFileRunnable newInstance(Queue fileChannelQueue, String filePath){
        ReadFileRunnable readFileRunnable = new ReadFileRunnable();
        readFileRunnable.fileChannels = fileChannelQueue;
        readFileRunnable.filePath = filePath;
        return readFileRunnable;
    }

    @Override
    public Boolean call() throws Exception {
        FileReadAccessHelper fileReadAccessHelper = new FileReadAccessHelper();
        try {

            File filePathFile = new File(filePath);
            if(filePathFile.isFile()){
                RandomAccessFile randomAccessFile = new RandomAccessFile(filePath,"rw");
                FileChannel fileChannel = randomAccessFile.getChannel();
                fileChannels.offer(ImmutableMap.of(filePath,fileChannel));
            } else {
                List<String> fileList = fileReadAccessHelper.listFile(filePath);
                if(null!=fileList){
                    for (String file:fileList){
                        FileChannel fileChannel = fileReadAccessHelper.readFile(file);
                        fileChannels.offer(ImmutableMap.of(file,fileChannel));
                    }
                }
            }
            System.out.println("当前存储在内存中的文件数量为: " + fileChannels.size());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
