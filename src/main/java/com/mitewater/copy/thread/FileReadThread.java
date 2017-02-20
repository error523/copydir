package com.mitewater.copy.thread;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.exception.OutOfBufferPoolSizeException;
import com.mitewater.copy.exception.OutOfThreadPoolSizeException;
import com.mitewater.copy.pool.BufferPool;
import com.mitewater.copy.pool.ThreadPool;
import com.mitewater.copy.read.FileReadAccessHelper;
import com.mitewater.copy.wirte.FileWriteAccessHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/16
 * Time: 10:26
 * Description: class for copydir try it self
 */
public class FileReadThread extends Thread{

    private FileReadThread(){}

    private String fileBaseUrl;

    public FileReadThread(String baseUrl){
        this.fileBaseUrl = baseUrl;
        createWriteThread();
    }

    @Override
    public void run(){
        FileReadAccessHelper fileReadAccessHelper = new FileReadAccessHelper();
        try {
            List<String> fileList = fileReadAccessHelper.listFile(fileBaseUrl);
            if(null!=fileList){
                for (String file:fileList){
                    FileChannel fileChannel = fileReadAccessHelper.readFile(file);
                    BufferPool.getInstance().setFileChannelBuffer(fileChannel);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfBufferPoolSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWriteThread(){
        //在这里创建Write线程
        String path = fileBaseUrl.replace(SystemConsts.SOURCE_PATH_URL,SystemConsts.TARGET_PATH_URL);
        ThreadPool.newInstance().setFileWriteThreads(new FileWriteThread(path));
    }
}
