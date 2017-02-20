package com.mitewater.copy.wirte;

import com.mitewater.copy.constant.SystemConsts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/15
 * Time: 20:38
 * Description: class for copydir try it self
 */
public class FileWriteAccessHelper {

    /**
     * 通过获取输入的FileChannel对象写入新的路径
     * @param outFilePath
     * @param inFileChannel
     */
    public void writeFile(String outFilePath, FileChannel inFileChannel){
        ByteBuffer byteBuffer = ByteBuffer.allocate(SystemConsts.BUFFER_ALLOCATE_SIZE);
        try {
            File file = new File(outFilePath);
            if(!file.exists()){
                file.createNewFile();
            }else{
                //保留做替换使用
            }
            FileChannel fileChannel = new RandomAccessFile(file,"rws").getChannel();
            while(inFileChannel.read(byteBuffer)!=-1){
                byteBuffer.flip();
                while(byteBuffer.hasRemaining()){
                    fileChannel.write(byteBuffer);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
