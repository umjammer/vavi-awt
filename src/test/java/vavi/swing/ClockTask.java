package vavi.swing;

import java.util.TimerTask;

public class ClockTask extends TimerTask {
    private TransClock clock;

    public ClockTask(TransClock clock) {
        this.clock = clock;
    }

    public void run() {
        clock.update(scheduledExecutionTime());
    }
}
