package part2;

// This is Thread A. It extends the Thread class directly and overrides
// run(), which is one of the two ways of making a thread that was
// shown in lecture (the other being implementing Runnable).
public class ThreadA extends Thread {

    private SharedData shared;

    public ThreadA(SharedData shared) {
        this.shared = shared;
    }

    @Override
    public void run() {
        trace("Thread A Starting");
        funcA1();
        funcA2();
        funcA3();
        trace("Thread A Done!");
    }

    // funcA1 does not need anything from Thread B, so it runs right away
    private void funcA1() {
        trace("Thread A is currently using the CPU (computing A1)");
        shared.A1 = SumUtil.sum(500);
        trace("Thread A finished A1 = " + shared.A1 + ", signalling A1 ready");
        shared.signalA1Ready();   // let Thread B know A1 can be used now
    }

    // funcA2 needs B2 first, so it waits for Thread B to signal it
    private void funcA2() {
        trace("Thread A waiting for B2...");
        shared.waitForB2();
        trace("Thread A is currently using the CPU (computing A2)");
        shared.A2 = shared.B2 + SumUtil.sum(300);
        trace("Thread A finished A2 = " + shared.A2 + ", signalling A2 ready");
        shared.signalA2Ready();   // let Thread B know A2 can be used now
    }

    // funcA3 needs B3 first, so it waits for Thread B to signal it
    private void funcA3() {
        trace("Thread A waiting for B3...");
        shared.waitForB3();
        trace("Thread A is currently using the CPU (computing A3)");
        shared.A3 = shared.B3 + SumUtil.sum(400);
        trace("Thread A finished A3 = " + shared.A3);
    }

    private void trace(String message) {
        if (SharedData.traceEnabled) {
            System.out.println(message);
        }
    }
}
