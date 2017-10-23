package com.mitewater.copy.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory{

    private ThreadType currentType;
    private static final String THREAD_PREFIX_NAME = "copy-dir-";
    private AtomicInteger threadWriteCount = new AtomicInteger(1);
    private AtomicInteger threadReadCount = new AtomicInteger(1);

    public static DefaultThreadFactory newInstance(ThreadType type) {
        DefaultThreadFactory threadFactory = new DefaultThreadFactory();
        threadFactory.setCurrentType(type);
        return threadFactory;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread;
        String threadTypeName;
        if (this.currentType == ThreadType.FILE_READ_THREAD) {
            threadTypeName = THREAD_PREFIX_NAME + "read-" + threadWriteCount.getAndAdd(1);
            thread = new CopyDirThread(r, threadTypeName);
        } else {
            threadTypeName = THREAD_PREFIX_NAME + "write-" + threadReadCount.getAndAdd(1);
            thread = new CopyDirThread(r, threadTypeName);
        }
        return thread;
    }

    public ThreadType getCurrentType() {
        return currentType;
    }

    public void setCurrentType(ThreadType currentType) {
        this.currentType = currentType;
    }

    public enum ThreadType {
        FILE_READ_THREAD,
        FILE_WRITE_THREAD;
    }
}
