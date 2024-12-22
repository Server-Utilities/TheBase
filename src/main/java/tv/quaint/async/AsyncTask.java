package tv.quaint.async;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter @Setter
public class AsyncTask implements TaskLike<AsyncTask> {
    private long id;

    private Consumer<AsyncTask> consumer;
    private long delay;
    private long period;
    private long currentDelay;

    private long ticksLived;

    private int timesRan;

    private Timer timer;

    public AsyncTask(long id, Consumer<AsyncTask> consumer, long delay, long period) {
        this.id = id;

        this.consumer = consumer;
        this.delay = delay;
        this.period = period;
        this.currentDelay = delay;

        this.ticksLived = 0;
        this.timesRan = 0;

        this.timer = createTimer();
    }

    public Timer createTimer() {
        return new Timer(50, e -> {
            try {
                tick();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });
    }

    public void start() {
        if (timer.isRunning()) return;

        timer.start();
    }

    public void stop() {
        if (! timer.isRunning()) return;

        timer.stop();
    }

    public void reset() {
        ticksLived = 0;
        timesRan = 0;
    }

    public void restart() {
        stop();
        reset();
        start();
    }

    public AsyncTask(Consumer<AsyncTask> consumer, long delay, long period) {
        this(AsyncUtils.getNextTaskId(), consumer, delay, period);
    }

    public AsyncTask(long id, Consumer<AsyncTask> consumer, long delay) {
        this(id, consumer, delay, -1);
    }

    public AsyncTask(Consumer<AsyncTask> consumer, long delay) {
        this(AsyncUtils.getNextTaskId(), consumer, delay);
    }

    public AsyncTask(long id, Consumer<AsyncTask> consumer) {
        this(id, consumer, 0, -1);
    }

    public AsyncTask(Consumer<AsyncTask> consumer) {
        this(AsyncUtils.getNextTaskId(), consumer);
    }

    public long getWarpedTicks() {
        if (timesRan == 0) {
            return ticksLived;
        }

        return ticksLived - (period * timesRan);
    }

    public long getNeededTicks() {
        if (delay > 0 && timesRan == 0) {
            return delay;
        }

        if (period > 0) {
            return period;
        }

        return -1;
    }

    public CompletableFuture<Void> executeAsync() {
        return AsyncUtils.executeAsync(this::execute);
    }

    public CompletableFuture<Void> tick(boolean runAsync) {
        ticksLived ++;
        if (currentDelay > 0) {
            currentDelay --;
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Void> future = doExecute(runAsync);

        if (isCompleted()) {
            remove();
        }

        return future;
    }

    public CompletableFuture<Void> doExecute(boolean runAsync) {
        timesRan ++;
        currentDelay = period;

        if (runAsync) {
            return executeAsync();
        } else {
            execute();

            return CompletableFuture.completedFuture(null);
        }
    }

    public long queue() {
        start();
        return AsyncUtils.queueTask(this);
    }

    public void remove() {
        stop();
        AsyncUtils.removeTask(this);
    }

    public CompletableFuture<Void> tick() {
        return tick(true);
    }

    public void execute() {
        consumer.accept(this);
    }

    public boolean completedAtLeast(int times) {
        return timesRan >= times;
    }

    public boolean completedAtLeastOnce() {
        return completedAtLeast(1);
    }

    public boolean isCompleted() {
        if (isRepeatable()) return false;

        return completedAtLeastOnce();
    }

    public boolean isRepeatable() {
        return period > -1;
    }
}
