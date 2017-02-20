package com.mitewater.copy.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MiteWater
 * Date: 2017/2/15
 * Time: 16:05
 * Description: class for copydir try it self
 */
public class FileReadAccessHelper {

    /**
     * 读取并列出当前目录下面的所有目录
     * @param pathUrl
     * @return
     * @throws FileNotFoundException
     */
    public List<String> listDir(String pathUrl) throws FileNotFoundException {
        File lookUpFileDir = new File(pathUrl);
        if(!lookUpFileDir.exists()){
            throw new FileNotFoundException();
        }
        File[] files = lookUpFileDir.listFiles();
        List<String> fileList = new ArrayList<String>();
        for(File subFile:files){
            //如果是目录则把绝对路径放到列表中
            if(subFile.isDirectory()){
                fileList.add(subFile.getAbsolutePath());
            }
        }
        return fileList;
    }

    public List<String> listFile(String pathUrl) throws FileNotFoundException {
        File lookUpFileDir = new File(pathUrl);
        if(!lookUpFileDir.exists()){
            throw new FileNotFoundException();
        }
        File[] files = lookUpFileDir.listFiles();
        List<String> fileList = new ArrayList<String>();
        if(null==files){
            return null;
        }
        for(File subFile:files){
            //如果是目录则把绝对路径放到列表中
            if(!subFile.isDirectory()){
                fileList.add(subFile.getAbsolutePath());
            }
        }
        return fileList;
    }

    public FileChannel readFile(String filePath){
        FileChannel fileChannel = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath,"rw");
            fileChannel = randomAccessFile.getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileChannel;
    }


}
