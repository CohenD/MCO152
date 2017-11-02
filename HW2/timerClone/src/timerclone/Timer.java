package timerclone;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Timer {

    private long timeAtStart = 0;
    private long timeAtStop = 0;
    private boolean isRunning = false;

    public void start() {
        if (!isRunning) {//only start if have not already
            timeAtStart = System.nanoTime();
            isRunning = true;
        }else{
            throw new IllegalStateException("Timer is already started");
        }
    }

    public void stop() {
        if (isRunning) {
            timeAtStop = System.nanoTime();
            isRunning = false;
        }
    }

    public void reset() {
        timeAtStart = isRunning ? System.nanoTime(): 0;
        timeAtStop = 0;
    }

    public long getAccumulatedTimer() {
        //nano seconds is used to get a more accurate time. It is then
        //converted to miliseconds when shown to the user to satisfy the specs
        return TimeUnit.NANOSECONDS.toMillis(isRunning ? System.nanoTime() - timeAtStart : timeAtStop - timeAtStart);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String timeToString() {
        long time = getAccumulatedTimer();

        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        return days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
    }

}
