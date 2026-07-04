package part2;

// This class used to be two separate classes: SharedData (just the six
// shared variables) and ThreadSync (the wait/notify synchronisation).
// They're merged here because every run always creates exactly one of
// each and passes both to Thread A and Thread B together anyway - so
// there was never really a reason for them to be two objects.
//
// Each dependency arrow in Figure 2.1 gets its own boolean flag and its
// own pair of methods: one that waits for the flag, one that sets the
// flag and wakes everyone up.
//
// wait() and notify() only work from inside a synchronized method or
// block, because they need to hold the object's intrinsic lock first.
// That is why every sync method below is synchronized.
class SharedData {

    // ---- shared integer variables from Figure 2.1 ----
    long A1, A2, A3;
    long B1, B2, B3;

    // ---- synchronisation state ----

    // Controls whether ThreadA/ThreadB/SharedData print their detailed
    // "who is doing what" trace messages. Main flips this on for the
    // demonstration run(s) and off for the bulk of the 100,000-run
    // stress test (part d), so the console stays readable.
    static volatile boolean traceEnabled = true;

    private boolean a1Ready = false;
    private boolean b2Ready = false;
    private boolean a2Ready = false;
    private boolean b3Ready = false;

    // called by Thread A once A1 has been worked out
    public synchronized void signalA1Ready() {
        a1Ready = true;
        notifyAll();   // wake up any thread waiting on this lock
    }

    // called by Thread B, pauses here until A1 is ready
    public synchronized void waitForA1() {
        // the while loop matters here, wait() can sometimes wake up
        // even though nothing actually changed, so we check again
        while (!a1Ready) {
            try {
                wait();   // releases the lock and pauses this thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // called by Thread B once B2 has been worked out
    public synchronized void signalB2Ready() {
        b2Ready = true;
        notifyAll();
    }

    // called by Thread A, pauses here until B2 is ready
    public synchronized void waitForB2() {
        while (!b2Ready) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // called by Thread A once A2 has been worked out
    public synchronized void signalA2Ready() {
        a2Ready = true;
        notifyAll();
    }

    // called by Thread B, pauses here until A2 is ready
    public synchronized void waitForA2() {
        while (!a2Ready) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // called by Thread B once B3 has been worked out
    public synchronized void signalB3Ready() {
        b3Ready = true;
        notifyAll();
    }

    // called by Thread A, pauses here until B3 is ready
    public synchronized void waitForB3() {
        while (!b3Ready) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
