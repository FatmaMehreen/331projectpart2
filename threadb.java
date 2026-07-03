package part2;

//This is Thread B. Same idea as ThreadA, just the "B" side of things.
public class ThreadB extends Thread {

 private SharedData data;
 private ThreadSync sync;

 public ThreadB(SharedData data, ThreadSync sync) {
     this.data = data;
     this.sync = sync;
 }

 @Override
 public void run() {
     funcB1();
     funcB2();
     funcB3();
 }

 // funcB1 does not need anything from Thread A either
 private void funcB1() {
     data.B1 = SumUtil.sum(250);
 }

 // funcB2 needs A1 first, so it waits for Thread A to signal it
 private void funcB2() {
     sync.waitForA1();
     data.B2 = data.A1 + SumUtil.sum(200);
     sync.signalB2Ready();   // let Thread A know B2 can be used now
 }

 // funcB3 needs A2 first, so it waits for Thread A to signal it
 private void funcB3() {
     sync.waitForA2();
     data.B3 = data.A2 + SumUtil.sum(400);
     sync.signalB3Ready();   // let Thread A know B3 can be used now
 }
}
