// Just a container for the six shared variables from Figure 2.1.
// Both threads get a reference to the same SharedData object, so
// whatever one thread writes, the other thread can see.
public class SharedData {
    long A1, A2, A3;
    long B1, B2, B3;
}
