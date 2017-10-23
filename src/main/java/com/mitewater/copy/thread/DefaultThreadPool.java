package com.mitewater.copy.thread;

import java.util.concurrent.*;

public class DefaultThreadPool extends ThreadPoolExecutor{

    /** The Constant CORE_POOL_NUM. */
    public final static int CORE_POOL_NUM = 5;

    /** The Constant MAX_POOL_NUM. */
    public final static int MAX_POOL_NUM = 40;

    /** The Constant KEEP_ALIVE_TIME. */
    public final static int KEEP_ALIVE_TIME = 0;

    /** The Constant TIME_UNIT. */
    public final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public final static LinkedBlockingQueue<Runnable> RUNNABLE_LINKED_BLOCKING_DEQUE = new LinkedBlockingQueue<>();

    public DefaultThreadPool(DefaultThreadFactory.ThreadType type){
        super(CORE_POOL_NUM,
                MAX_POOL_NUM,
                KEEP_ALIVE_TIME,
                TIME_UNIT,
                RUNNABLE_LINKED_BLOCKING_DEQUE,
                DefaultThreadFactory.newInstance(type));
    }

}
