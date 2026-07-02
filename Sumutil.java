// Just a helper class so both threads can call the same sum function
// instead of copy-pasting the same loop everywhere.
public class SumUtil {

    // adds up 0 + 1 + 2 + ... + n using a normal loop (assignment says we
    // have to use a loop here, not the shortcut formula)
    public static long sum(int n) {
        long total = 0;
        for (int i = 0; i <= n; i++) {
            total += i;
        }
        return total;
    }
}
