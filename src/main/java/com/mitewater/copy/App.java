package com.mitewater.copy;

import com.mitewater.copy.constant.SystemConsts;
import com.mitewater.copy.thread.DefaultThreadFactory;
import com.mitewater.copy.thread.DefaultThreadPool;
import com.mitewater.copy.thread.ReadFileRunnable;
import com.mitewater.copy.thread.WriteFileRunnable;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.nio.channels.FileChannel;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static sun.swing.SwingUtilities2.submit;

/**
 * Hello world!
 */
public class App {

    private static ExecutorService readExecutorService = new DefaultThreadPool(DefaultThreadFactory.ThreadType.FILE_READ_THREAD);
    private static ExecutorService writeExecutorService = new DefaultThreadPool(DefaultThreadFactory.ThreadType.FILE_WRITE_THREAD);

    public static void main(String[] args) {
        try {
            start(SystemConsts.SOURCE_PATH_URL);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void start(String sourcePath) throws ExecutionException, InterruptedException {
        Queue<Map<String,FileChannel>> fileChannels = new LinkedBlockingQueue<Map<String, FileChannel>>(1000);
        File file = new File(sourcePath);
        // 创建一个守护线程
        System.out.println("----创建守护线程----");
        Thread thread = new Thread(new DaemonRunnable(fileChannels));
        thread.setName("listen-fileChannels-daemon-Thread");
        thread.setDaemon(true);
        thread.start();
        System.out.println("----创建守护线程完成----");
        if(file.exists()) {
            LinkedList<File> dirFileList = new LinkedList<>();
            dirFileList.addFirst(file);
            while(!dirFileList.isEmpty()) {
                File pathFile = dirFileList.removeFirst();
                File[] innerFiles = pathFile.listFiles();
                if(null!=innerFiles) {
                    for(File innerFile:innerFiles){
                        if(innerFile.isDirectory()){
                            dirFileList.add(innerFile);
                        }
                        Future<Boolean> future = readExecutorService.submit(ReadFileRunnable.newInstance(fileChannels,innerFile.getAbsolutePath()));
                        System.out.println("读取" + innerFile.getAbsolutePath() + "文件" + (future.get()?"成功":"失败"));
                    }
                }
            }
        }
    }

    static class DaemonRunnable implements Runnable{

        private Queue<Map<String,FileChannel>> fileChannels;

        public DaemonRunnable(Queue<Map<String,FileChannel>> fileChannels) {
            this.fileChannels = fileChannels;
        }

        @Override
        public synchronized void run() {
            while(true) {
                // 启动一个写入线程
                if (!fileChannels.isEmpty()) {
                    Future<Boolean> booleanFuture = writeExecutorService.submit(WriteFileRunnable.newInstance(fileChannels));
                    try {
                        System.out.println("写入线程 :" + booleanFuture.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
