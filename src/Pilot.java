package pilot;

public class Pilot {

    final private static long frameNanos = 1000000000 / 60;

    private static boolean isRunning;
    private static MasterView view;

    public static void main(String[] args) {
        isRunning = true;
        view = new MasterView();
        view.activate();

        long nextFrameNanos;
        long sleepNanos;
        while (isRunning) {
            nextFrameNanos = System.nanoTime() + frameNanos;
            view.render();
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
