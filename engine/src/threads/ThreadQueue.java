package threads;

import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadQueue {
    private List<Simulation> tasksList;
    private ExecutorService threadPool;
    private Lock lock;
    private final int maxThearsNum;


    public ThreadQueue(int queueSize) {
        this.maxThearsNum = queueSize;
        this.tasksList = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.threadPool = Executors.newFixedThreadPool(queueSize);
    }

    public void addTask(Simulation simulation) {
        tasksList.add(simulation);
        this.threadPool.execute(simulation);
    }

    public int getMaxThearsNum() {
        return maxThearsNum;
    }

    public void reset() {
        this.tasksList.clear();
        this.threadPool.shutdown();
    }
}
