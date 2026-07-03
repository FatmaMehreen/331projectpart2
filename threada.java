// This is Thread A. It extends the Thread class directly and overrides
// run(), which is one of the two ways of making a thread that was
// shown in lecture (the other being implementing Runnable).
public class ThreadA extends Thread {

    private SharedData data;
    private ThreadSync sync;

    public ThreadA(SharedData data, ThreadSync sync) {
        this.data = data;
        this.sync = sync;
    }

    @Override
    public void run() {
        funcA1();
        funcA2();
        funcA3();
    }

    // funcA1 does not need anything from Thread B, so it runs right away
    private void funcA1() {
        data.A1 = SumUtil.sum(500);
        sync.signalA1Ready();   // let Thread B know A1 can be used now
    }

    // funcA2 needs B2 first, so it waits for Thread B to signal it
    private void funcA2() {
        sync.waitForB2();
        data.A2 = data.B2 + SumUtil.sum(300);
        sync.signalA2Ready();   // let Thread B know A2 can be used now
    }

    // funcA3 needs B3 first, so it waits for Thread B to signal it
    private void funcA3() {
        sync.waitForB3();
        data.A3 = data.B3 + SumUtil.sum(400);
    }
}
