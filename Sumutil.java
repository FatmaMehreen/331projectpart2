// The handout specifically asks for the sum to come from a static method
// in its own utility class, so this one stays separate from everything else.
public class SumUtil {

    // adds up 0 + 1 + 2 + ... + n using a normal loop
    public static long sum(int n) {
        long total = 0;
        for (int i = 0; i <= n; i++) {
            total += i;
        }
        return total;
    }
}
