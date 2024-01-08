package ua.in.khol.oleh.touristweathercomparer.executor;

import static ua.in.khol.oleh.touristweathercomparer.Globals.CORE_POOL_SIZE;
import static ua.in.khol.oleh.touristweathercomparer.Globals.KEEP_ALIVE_TIME;
import static ua.in.khol.oleh.touristweathercomparer.Globals.MAXIMUM_POOL_SIZE;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobExecutor implements Executor {

    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public JobExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new JobThreadFactory()
        );
    }

    @Override
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME_PREFIX = "wa_";

        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, THREAD_NAME_PREFIX + counter++);
        }
    }
}
