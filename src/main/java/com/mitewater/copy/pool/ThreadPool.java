package com.mitewater.copy.pool;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.exception.OutOfBufferPoolSizeException;
import com.mitewater.copy.thread.FileReadThread;
import com.mitewater.copy.thread.FileWriteThread;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/16
 * Time: 10:09
 * Description: class for copydir try it self
 */
public class ThreadPool {

    private ThreadPool(){}

    private static ThreadPool threadPool = new ThreadPool();

    //创建读线程池
    private FileReadThread[] fileReadThreads = new FileReadThread[SystemConsts.THREAD_POOL_SIZE];

    private int read_count = 0;

    //创建写线程池
    private FileWriteThread[] fileWriteThreads = new FileWriteThread[SystemConsts.THREAD_POOL_SIZE];

    private int write_count = 0;

    //存储当前未使用的Thread对象
    private Set<Integer> WAIT_USED_READ_THREAD = new HashSet<Integer>();

    //存储当前未使用的Thread对象
    private Set<Integer> WAIT_USED_WRITE_THREAD = new HashSet<Integer>();

    public static ThreadPool newInstance(){
        return threadPool;
    }

    public synchronized Thread getReadThread(){
        FileReadThread fileReadThread = null;
        for (int i = 0;i < SystemConsts.THREAD_POOL_SIZE; i++){
            if(null != fileReadThreads[i]){
                if(WAIT_USED_READ_THREAD.contains(i)){
                    fileReadThread = fileReadThreads[i];
                    WAIT_USED_READ_THREAD.remove(i);
                    ArrayUtils.remove(fileReadThreads,i);
                    read_count--;
                    break;
                }
            }
        }
        return fileReadThread;
    }

    public synchronized void setFileReadThreads(FileReadThread thread){
        if(read_count+1==SystemConsts.THREAD_POOL_SIZE){
            while(isReadThreadFull()){
                try {
                    Thread.sleep(500);
                    System.out.println("线程已满请等待");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        fileReadThreads[read_count] = thread;
        WAIT_USED_READ_THREAD.add(read_count);
        read_count++;
    }

    public synchronized Thread getWriteThread(){
        FileWriteThread fileWriteThread = null;
        for (int i = 0;i < SystemConsts.THREAD_POOL_SIZE; i++){
            if(null != fileWriteThreads[i]){
                if(WAIT_USED_WRITE_THREAD.contains(i)){
                    fileWriteThread = fileWriteThreads[i];
                    WAIT_USED_WRITE_THREAD.remove(i);
                    ArrayUtils.remove(fileWriteThreads,i);
                    write_count--;
                    break;
                }
            }
        }
        return fileWriteThread;
    }

    public synchronized void setFileWriteThreads(FileWriteThread thread){
        if(write_count+1==SystemConsts.THREAD_POOL_SIZE){
            while(isWritehreadFull()){
                try {
                    Thread.sleep(500);
                    System.out.println("线程已满请等待");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        fileWriteThreads[write_count] = thread;
        WAIT_USED_WRITE_THREAD.add(write_count);
        write_count++;
    }

    public boolean isReadThreadFull(){
        if(read_count+1==SystemConsts.THREAD_POOL_SIZE){
            return true;
        }else{
            return false;
        }
    }
    public boolean isWritehreadFull(){
        if(write_count+1==SystemConsts.THREAD_POOL_SIZE){
            return true;
        }else{
            return false;
        }
    }

    public void customse(){
        while(!WAIT_USED_WRITE_THREAD.isEmpty()||!WAIT_USED_READ_THREAD.isEmpty()){
            if(null!=fileReadThreads[0]){
                fileReadThreads[0].run();
            }
            if(null!=fileWriteThreads[0]){
                fileWriteThreads[0].run();
            }
        }
    }

}
