package part2;

public class Main {

    // the values worked out by hand in the report (part a)
    static final long EXPECTED_A1 = 125_250;
    static final long EXPECTED_B1 = 31_375;
    static final long EXPECTED_B2 = 145_350;
    static final long EXPECTED_A2 = 190_500;
    static final long EXPECTED_B3 = 270_700;
    static final long EXPECTED_A3 = 350_900;

    public static void main(String[] args) throws InterruptedException {

        // part a)/c): run it once, with full tracing on, so you can see
        // exactly which thread is doing what and where it switches
        // (mirrors the "Low/Medium/High Priority Thread ... currently
        // using the CPU" style trace, just for our functions instead).
        System.out.println("===== Single detailed run (part a / c) =====");
        SharedData.traceEnabled = true;
        SharedData firstRun = runOnce();
        System.out.println();
        System.out.println("Final shared variable values:");
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
        //
        // Printing the full "which thread is doing what" trace for all
        // 100,000 runs would produce millions of lines and be unreadable,
        // so the detailed trace is only kept on for the first couple of
        // stress-test runs (to show interleaving is still happening
        // under repeated execution), then switched off for the bulk of
        // the runs. Any run that fails automatically gets its trace
        // printed too, since that's exactly when you'd want to see it.
        System.out.println("===== Stress test (part d) =====");
        int totalRuns = 100000;
        int correct = 0;
        int failed = 0;
        int tracedDemoRuns = 2; // how many extra runs to show in full detail

        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= totalRuns; i++) {

            boolean showTrace = (i <= tracedDemoRuns);
            SharedData.traceEnabled = showTrace;

            if (showTrace) {
                System.out.println("--- Stress run " + i + " (detailed) ---");
            }

            SharedData result = runOnce();

            if (isCorrect(result)) {
                correct++;
            } else {
                // if this ever happens, the synchronisation is broken
                // somewhere - re-run it with tracing on so we can see
                // exactly which thread did what leading up to the bug
                failed++;
                SharedData.traceEnabled = true;
                System.out.println("Run " + i + " gave a WRONG answer, re-running with trace on:");
                SharedData retrace = runOnce();
                System.out.println("Wrong values were: "
                        + "A1=" + result.A1 + " B1=" + result.B1 + " B2=" + result.B2
                        + " A2=" + result.A2 + " B3=" + result.B3 + " A3=" + result.A3);
                SharedData.traceEnabled = false;
            }

            if (i % 10000 == 0) {
                System.out.println("Run " + i + " of " + totalRuns
                        + " complete - Thread A and Thread B finished and rejoined the main thread. "
                        + failed + " failure(s) so far.");
            }
        }
        long elapsedMs = System.currentTimeMillis() - startTime;
        double successRate = (correct * 100.0) / totalRuns;

        System.out.println();
        System.out.println("===== Stress test summary =====");
        System.out.println("Total runs   : " + totalRuns);
        System.out.println("Correct      : " + correct);
        System.out.println("Failed       : " + failed);
        System.out.println("Success rate : " + successRate + "%");
        System.out.println("Time taken   : " + elapsedMs + " ms");
    }

    // creates a fresh SharedData, spins up Thread A and Thread B, waits
    // for both to finish, then hands back the result
    private static SharedData runOnce() throws InterruptedException {
        SharedData shared = new SharedData();
        ThreadA threadA = new ThreadA(shared);
        ThreadB threadB = new ThreadB(shared);

        if (SharedData.traceEnabled) {
            System.out.println("main Starting Thread A");
        }
        threadA.start();

        if (SharedData.traceEnabled) {
            System.out.println("main Starting Thread B");
        }
        threadB.start();

        threadA.join();   // don't move on until thread A has fully finished
        threadB.join();   // don't move on until thread B has fully finished
        return shared;
    }

    private static boolean isCorrect(SharedData d) {
        return d.A1 == EXPECTED_A1 && d.B1 == EXPECTED_B1 && d.B2 == EXPECTED_B2
                && d.A2 == EXPECTED_A2 && d.B3 == EXPECTED_B3 && d.A3 == EXPECTED_A3;
    }
}
