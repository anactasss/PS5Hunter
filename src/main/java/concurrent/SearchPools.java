package concurrent;

import java.util.concurrent.CyclicBarrier;

public class SearchPools {

    private final CyclicBarrier barrier;

    public SearchPools(int threadCount) {
        this.barrier = new CyclicBarrier(threadCount);
    }

    public void invoke(Runnable action) {
        new Thread(
                () -> {
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    action.run();
                }).start();
    }
}
