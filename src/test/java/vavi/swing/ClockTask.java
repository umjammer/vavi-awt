package vavi.swing;

import java.util.TimerTask;

public class ClockTask extends TimerTask {
    private Updatable clock;

    public ClockTask(Updatable clock) {
        this.clock = clock;
    }

    public void run() {
        clock.update(scheduledExecutionTime());
    }

    public static interface Updatable {
        void update(long time);
    }
}
