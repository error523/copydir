package com.mitewater.copy.thread;

public class CopyDirThread extends Thread{

    public CopyDirThread() {
        super();
    }

    public CopyDirThread(Runnable target) {
        super(target);
    }

    public CopyDirThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public CopyDirThread(String name) {
        super(name);
    }

    public CopyDirThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public CopyDirThread(Runnable target, String name) {
        super(target, name);
    }

    public CopyDirThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public CopyDirThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }
}
