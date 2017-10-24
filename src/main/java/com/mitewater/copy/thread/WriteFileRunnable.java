package com.mitewater.copy.thread;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.wirte.FileWriteAccessHelper;

import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

public class WriteFileRunnable implements Callable<Boolean>{
    private WriteFileRunnable(){}

    private Queue<Map<String,FileChannel>> fileChannels;

    public static WriteFileRunnable newInstance(Queue fileChannelQueue){
        WriteFileRunnable readFileRunnable = new WriteFileRunnable();
        readFileRunnable.fileChannels = fileChannelQueue;
        return readFileRunnable;
    }

    @Override
    public Boolean call() throws Exception {
        //交给write去写文件
        FileWriteAccessHelper fileWriteAccessHelper = new FileWriteAccessHelper();
        try {
            while(!fileChannels.isEmpty()) {
                Map<String,FileChannel> map = fileChannels.poll();
                Set<String> pathSet = map.keySet();
                if (!pathSet.isEmpty()) {
                    Iterator<String> iterator = pathSet.iterator();
                    while(iterator.hasNext()){
                        String originPath = iterator.next();
                        fileWriteAccessHelper
                                .writeFile(originPath.replace(SystemConsts.SOURCE_PATH_URL,SystemConsts.TARGET_PATH_URL),
                                        map.get(originPath));
                    }
                    return true;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
