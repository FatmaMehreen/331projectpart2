// This class only has one job: add up numbers from 0 to n using a loop.
// The handout wants this kept in its own utility class, so it stays on
// its own instead of being mixed into the thread classes.
package part2;
public class SumUtil {

    // adds 0 + 1 + 2 + ... + n
    public static long sum(int n) {
        long total = 0;
        for (int i = 0; i <= n; i++) {
            total += i;
        }
        return total;
    }
}
