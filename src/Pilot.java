package pilot;

import java.io.IOException;

public class Pilot {

    final private static long frameNanos = 1000000000 / 60;

    private static boolean isRunning;
    private static MasterView view;

    public static void main(String[] args) {

        try {
            ConfigController config = new ConfigController();
            view = new MasterView(config);
            view.activate();
            isRunning = true;

        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
            isRunning = false;
        }

        long nextFrameNanos;
        long sleepNanos = 1;

        while (isRunning) {
            nextFrameNanos = System.nanoTime() + frameNanos;

            if (sleepNanos > 0) {
                view.render();
            } else {
                nextFrameNanos += sleepNanos;
            }

            sleepNanos = nextFrameNanos - System.nanoTime();

            if (sleepNanos > 1000000) {
                try {
                    Thread.sleep(sleepNanos / 1000000);
                } catch (InterruptedException e) {
                    System.err.println("Game loop interrupted: " + e.getMessage());
                }
            }

        }
    }

    public void stop() { isRunning = false; }

}
