package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletableFuture;

public class DatabaseState {
    public enum StateType {
        WAITING,
        COMPLETED,
    }

    @Getter @Setter
    private StateType state;

    public DatabaseState(StateType state) {
        this.state = state;
    }

    public boolean isWaiting() {
        return state == StateType.WAITING;
    }

    public boolean isCompleted() {
        return state == StateType.COMPLETED;
    }

    public boolean waitUntilOpen() {
        return CompletableFuture.supplyAsync(() -> {
            while (isWaiting()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return isCompleted();
        }).join();
    }
}
