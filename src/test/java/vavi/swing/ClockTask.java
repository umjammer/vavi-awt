package vavi.swing;

import java.util.TimerTask;

public class ClockTask extends TimerTask {
    private final Updatable clock;

    public ClockTask(Updatable clock) {
        this.clock = clock;
    }

    @Override
    public void run() {
        clock.update(scheduledExecutionTime());
    }

    public interface Updatable {
        void update(long time);
    }
}
