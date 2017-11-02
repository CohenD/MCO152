package timerclone;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    public TimerTest() {
    }

    /**
     * Test of start method, of class Timer.
     */
    @Test
    public void testStart() throws InterruptedException {
        System.out.println("start");
        Timer instance = new Timer();
        instance.start();

        Thread.sleep(1000);

        assertTrue(instance.getAccumulatedTimer() > 0);
    }

    /**
     * Test of start method, of class Timer.
     */
    @Test
    public void testStartException() {
        System.out.println("start");
        Timer instance = new Timer();
        boolean exceptionThrown = false;
        instance.start();

        try {
            instance.start();
        } catch (IllegalStateException ex) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    /**
     * Test of stop method, of class Timer.
     */
    @Test
    public void testStop() throws InterruptedException {
        System.out.println("stop");
        Timer instance = new Timer();
        instance.start();

        Thread.sleep(1000);

        instance.stop();

        long stopTime = instance.getAccumulatedTimer();

        Thread.sleep(2000);

        assertEquals(stopTime, instance.getAccumulatedTimer());
    }

    /**
     * Test of reset method, of class Timer.
     */
    @Test
    public void testReset() throws InterruptedException {
        System.out.println("reset");
        Timer instance = new Timer();

        instance.start();

        Thread.sleep(3000);

        long timeBeforeReset = instance.getAccumulatedTimer();

        instance.reset();

        Thread.sleep(500);

        long currentTime = instance.getAccumulatedTimer();

        assertTrue(timeBeforeReset > currentTime && currentTime > 0);

        instance.stop();
        instance.reset();

        System.out.println(instance.getAccumulatedTimer());

        assertTrue(instance.getAccumulatedTimer() == 0);
    }

    /**
     * Test of getAccumulatedTimer method, of class Timer.
     */
    @Test
    public void testGetAccumulatedTimer() throws InterruptedException {
        System.out.println("getAccumulatedTimer");
        Timer instance = new Timer();
        long expResult = 3000;
        instance.start();

        Thread.sleep(expResult);

        long result = instance.getAccumulatedTimer();

        assertTrue(result > expResult - 500 && result < expResult + 500);
    }

}
