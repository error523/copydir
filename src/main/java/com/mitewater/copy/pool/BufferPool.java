package com.mitewater.copy.pool;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.exception.OutOfBufferPoolSizeException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/16
 * Time: 9:57
 * Description: class for copydir try it self
 */
public class BufferPool {

    private BufferPool(){}

    private static BufferPool bufferPool = new BufferPool();

    public static BufferPool getInstance(){
        return bufferPool;
    }

    private FileChannel[] fileChannelBuffer = new FileChannel[SystemConsts.BUFFER_SIZE];

    //当前池存储Buffer的指针
    private int BUFFER_SIZE = 0;

    //存储当前未使用的Buffer对象
    private Set<Integer> WAIT_USED_FILE_CHANNEL_OBJ_SET = new HashSet<Integer>();

    /**
     *  存储一个FileChannel对象到fileChannelBuffer中
     * @param fileChannel
     * @throws OutOfBufferPoolSizeException
     */
    public synchronized  void setFileChannelBuffer(FileChannel fileChannel) throws OutOfBufferPoolSizeException {
        if(BUFFER_SIZE+1==SystemConsts.BUFFER_SIZE){
            throw new OutOfBufferPoolSizeException();
        }
        this.fileChannelBuffer[BUFFER_SIZE] = fileChannel;
        BUFFER_SIZE++;
    }

    /**
     *
     * @return
     */
    public synchronized  FileChannel getFileChannelBuffer(){
        FileChannel fileChannel = null;
        for(int i = 0;i<SystemConsts.BUFFER_SIZE;i++){
            if(null!=fileChannelBuffer[i]){
                if(WAIT_USED_FILE_CHANNEL_OBJ_SET.contains(i)){
                    fileChannel = fileChannelBuffer[i];
                    //从标志使用的Set中删除已经获取的FileChannel对象,表示已经使用
                    WAIT_USED_FILE_CHANNEL_OBJ_SET.remove(i);
                    //从pool中删除获取的FileChannel对象，表示已经使用
                    fileChannelBuffer = ArrayUtils.remove(fileChannelBuffer,i);
                    BUFFER_SIZE-- ;
                    break;
                }
            }
        }
        return fileChannel;
    }

    /**
     * 判断对象池是否已满
     * @return
     */
    public boolean isFull(){
        if(BUFFER_SIZE+1==SystemConsts.BUFFER_SIZE){
            return true;
        }else{
            return false;
        }
    }
}
