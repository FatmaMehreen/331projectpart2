package part2;

// This is Thread B. Same idea as ThreadA, just the "B" side of things.
public class ThreadB extends Thread {

    private SharedData shared;

    public ThreadB(SharedData shared) {
        this.shared = shared;
    }

    @Override
    public void run() {
        trace("Thread B Starting");
        funcB1();
        funcB2();
        funcB3();
        trace("Thread B Done!");
    }

    // funcB1 does not need anything from Thread A either
    private void funcB1() {
        trace("Thread B is currently using the CPU (computing B1)");
        shared.B1 = SumUtil.sum(250);
        trace("Thread B finished B1 = " + shared.B1);
    }

    // funcB2 needs A1 first, so it waits for Thread A to signal it
    private void funcB2() {
        trace("Thread B waiting for A1...");
        shared.waitForA1();
        trace("Thread B is currently using the CPU (computing B2)");
        shared.B2 = shared.A1 + SumUtil.sum(200);
        trace("Thread B finished B2 = " + shared.B2 + ", signalling B2 ready");
        shared.signalB2Ready();   // let Thread A know B2 can be used now
    }

    // funcB3 needs A2 first, so it waits for Thread A to signal it
    private void funcB3() {
        trace("Thread B waiting for A2...");
        shared.waitForA2();
        trace("Thread B is currently using the CPU (computing B3)");
        shared.B3 = shared.A2 + SumUtil.sum(400);
        trace("Thread B finished B3 = " + shared.B3 + ", signalling B3 ready");
        shared.signalB3Ready();   // let Thread A know B3 can be used now
    }

    private void trace(String message) {
        if (SharedData.traceEnabled) {
            System.out.println(message);
        }
    }
}
