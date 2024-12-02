package tv.quaint.async;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

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

    public AsyncTask(long id, Consumer<AsyncTask> consumer, long delay, long period) {
        this.id = id;

        this.consumer = consumer;
        this.delay = delay;
        this.period = period;
        this.currentDelay = delay;

        this.ticksLived = 0;
        this.timesRan = 0;
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

    public void executeAsync() {
        CompletableFuture.runAsync(this::execute);
    }

    public void tick(boolean runAsync) {
        ticksLived ++;
        if (currentDelay > 0) {
            currentDelay --;
            return;
        }

        doExecute(runAsync);

        if (isCompleted()) {
            remove();
        }
    }

    public void doExecute(boolean runAsync) {
        timesRan ++;
        currentDelay = period;

        if (runAsync) {
            executeAsync();
        } else {
            execute();
        }
    }

    public long queue() {
        return AsyncUtils.queueTask(this);
    }

    public void remove() {
        AsyncUtils.removeTask(this);
    }

    public void tick() {
        tick(true);
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
