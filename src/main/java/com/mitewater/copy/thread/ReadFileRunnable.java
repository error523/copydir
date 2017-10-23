package com.mitewater.copy.thread;

import com.google.common.collect.ImmutableMap;
import com.mitewater.copy.read.FileReadAccessHelper;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ReadFileRunnable implements Runnable{

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
    public void run() {
        FileReadAccessHelper fileReadAccessHelper = new FileReadAccessHelper();
        try {
            List<String> fileList = fileReadAccessHelper.listFile(filePath);
            if(null!=fileList){
                for (String file:fileList){
                    FileChannel fileChannel = fileReadAccessHelper.readFile(file);
                    fileChannels.offer(ImmutableMap.of(file,fileChannel));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
