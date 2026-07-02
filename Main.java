import java.util.concurrent.Semaphore;

public class Main {

    // small box to hold the 6 shared variables together - didn't need its
    // own file so it just lives in here as a nested class
    static class SharedData {
        long A1, A2, A3;
        long B1, B2, B3;
    }

    // the values I worked out by hand in the report (part a)
    static final long EXPECTED_A1 = 125_250;
    static final long EXPECTED_B1 = 31_375;
    static final long EXPECTED_B2 = 145_350;
    static final long EXPECTED_A2 = 190_500;
    static final long EXPECTED_B3 = 270_700;
    static final long EXPECTED_A3 = 350_900;

    public static void main(String[] args) throws InterruptedException {

        // ----- part a)/c): run it once and just print the values -----
        SharedData firstRun = runOnce();
        System.out.println("A1 = " + firstRun.A1);
        System.out.println("B1 = " + firstRun.B1);
        System.out.println("B2 = " + firstRun.B2);
        System.out.println("A2 = " + firstRun.A2);
        System.out.println("B3 = " + firstRun.B3);
        System.out.println("A3 = " + firstRun.A3);
        System.out.println();

        // ----- part d): run it a LOT of times to check it's not just -----
        // -----          getting lucky with the thread timing -----
        int totalRuns = 5000;
        int correct = 0;

        for (int i = 1; i <= totalRuns; i++) {
            SharedData result = runOnce();

            if (isCorrect(result)) {
                correct++;
            } else {
                // if this ever prints, the synchronisation is broken somewhere
                System.out.println("Run " + i + " gave a wrong answer -> "
                        + "A1=" + result.A1 + " B1=" + result.B1 + " B2=" + result.B2
                        + " A2=" + result.A2 + " B3=" + result.B3 + " A3=" + result.A3);
            }
        }

        System.out.println(correct + " out of " + totalRuns + " runs were correct.");
    }

    // creates two fresh threads (Thread A and Thread B), runs them once,
    // and hands back whatever values they ended up with
    private static SharedData runOnce() throws InterruptedException {
        SharedData data = new SharedData();

        // all locked at the start (0 permits) - acquire() just waits quietly
        // until the matching release() happens, no CPU wasted spinning
        Semaphore a1Ready = new Semaphore(0);
        Semaphore b2Ready = new Semaphore(0);
        Semaphore a2Ready = new Semaphore(0);
        Semaphore b3Ready = new Semaphore(0);

        // ---------------- everything Thread A does ----------------
        Thread threadA = new Thread(() -> {

            // funcA1: doesn't need anything from Thread B, runs right away
            data.A1 = SumUtil.sum(500);
            a1Ready.release();   // tell Thread B "A1 is ready, you can use it"

            // funcA2: needs B2 first
            try {
                b2Ready.acquire();   // pause until Thread B says B2 is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            data.A2 = data.B2 + SumUtil.sum(300);
            a2Ready.release();   // tell Thread B "A2 is ready"

            // funcA3: needs B3 first
            try {
                b3Ready.acquire();   // pause until Thread B says B3 is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            data.A3 = data.B3 + SumUtil.sum(400);
        });

        // ---------------- everything Thread B does ----------------
        Thread threadB = new Thread(() -> {

            // funcB1: doesn't need anything from Thread A either
            data.B1 = SumUtil.sum(250);

            // funcB2: needs A1 first
            try {
                a1Ready.acquire();   // pause until Thread A says A1 is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            data.B2 = data.A1 + SumUtil.sum(200);
            b2Ready.release();   // tell Thread A "B2 is ready"

            // funcB3: needs A2 first
            try {
                a2Ready.acquire();   // pause until Thread A says A2 is ready
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            data.B3 = data.A2 + SumUtil.sum(400);
            b3Ready.release();   // tell Thread A "B3 is ready"
        });

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
