package threads;

import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadQueue {
    List<Simulation> tasksList;
    ExecutorService threadPool;
    Lock lock;


    public ThreadQueue(int queueSize) {
        this.tasksList = new ArrayList<Simulation>();
        this.lock = new ReentrantLock();
        this.threadPool = Executors.newFixedThreadPool(queueSize);
    }

    public void addTask(Simulation simulation) {
        tasksList.add(simulation);
        this.threadPool.execute(simulation);
    }

    public Lock getLock() {
        return this.lock;
    }

    public void stop() {
        this.threadPool.shutdown();
    }
}
