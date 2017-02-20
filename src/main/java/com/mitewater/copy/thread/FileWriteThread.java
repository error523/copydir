package com.mitewater.copy.thread;

import com.mitewater.copy.pool.BufferPool;
import com.mitewater.copy.wirte.FileWriteAccessHelper;

import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/16
 * Time: 10:55
 * Description: class for copydir try it self
 */
public class FileWriteThread extends Thread{

    private String fileBaseUrl;

    private FileWriteThread(){}

    public FileWriteThread(String baseUrl){
        this.fileBaseUrl = baseUrl;
    }

    @Override
    public void run(){
        //从Buffer池中拿到FileChannel对象
        FileChannel fileChannel = BufferPool.getInstance().getFileChannelBuffer();
        //交给write去写文件
        FileWriteAccessHelper fileWriteAccessHelper = new FileWriteAccessHelper();
        fileWriteAccessHelper.writeFile(fileBaseUrl,fileChannel);
    }
}
