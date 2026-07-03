public class Main {

    // the values worked out by hand in the report (part a)
    static final long EXPECTED_A1 = 125_250;
    static final long EXPECTED_B1 = 31_375;
    static final long EXPECTED_B2 = 145_350;
    static final long EXPECTED_A2 = 190_500;
    static final long EXPECTED_B3 = 270_700;
    static final long EXPECTED_A3 = 350_900;

    public static void main(String[] args) throws InterruptedException {

        // part a)/c): run it once and just print the values
        SharedData firstRun = runOnce();
        System.out.println("A1 = " + firstRun.A1);
        System.out.println("B1 = " + firstRun.B1);
        System.out.println("B2 = " + firstRun.B2);
        System.out.println("A2 = " + firstRun.A2);
        System.out.println("B3 = " + firstRun.B3);
        System.out.println("A3 = " + firstRun.A3);
        System.out.println();

        // part d): run it a LOT of times to check it's not just getting
        // lucky with the thread timing. 100,000 runs means the scheduler
        // gets a lot of different chances to interleave the two threads
        // in weird orders, so this is a decent stress test of the sync.
        int totalRuns = 100000;
        int correct = 0;
        int failed = 0;
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= totalRuns; i++) {
            SharedData result = runOnce();

            if (isCorrect(result)) {
                correct++;
            } else {
                // if this ever prints, the synchronisation is broken somewhere
                failed++;
                System.out.println("Run " + i + " gave a wrong answer: "
                        + "A1=" + result.A1 + " B1=" + result.B1 + " B2=" + result.B2
                        + " A2=" + result.A2 + " B3=" + result.B3 + " A3=" + result.A3);
            }

            if (i % 10000 == 0) {
                System.out.println(i + " runs done, " + failed + " failed so far.");
            }
        }

        long elapsedMs = System.currentTimeMillis() - startTime;
        double successRate = (correct * 100.0) / totalRuns;

        System.out.println();
        System.out.println("Total runs   : " + totalRuns);
        System.out.println("Correct      : " + correct);
        System.out.println("Failed       : " + failed);
        System.out.println("Success rate : " + successRate + "%");
        System.out.println("Time taken   : " + elapsedMs + " ms");
    }

    // creates a fresh SharedData and ThreadSync, spins up Thread A and
    // Thread B, waits for both to finish, then hands back the result
    private static SharedData runOnce() throws InterruptedException {
        SharedData data = new SharedData();
        ThreadSync sync = new ThreadSync();

        ThreadA threadA = new ThreadA(data, sync);
        ThreadB threadB = new ThreadB(data, sync);

        threadA.start();
        threadB.start();

        threadA.join();   // don't move on until thread A has fully finished
        threadB.join();   // don't move on until thread B has fully finished

        return data;
    }

    private static boolean isCorrect(SharedData d) {
        return d.A1 == EXPECTED_A1 && d.B1 == EXPECTED_B1 && d.B2 == EXPECTED_B2
                && d.A2 == EXPECTED_A2 && d.B3 == EXPECTED_B3 && d.A3 == EXPECTED_A3;
    }
}
