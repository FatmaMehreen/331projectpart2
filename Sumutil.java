package part2;

// This class only has one job: add up numbers from 0 to n using a loop.
// The handout specifically asks for the sum to come from a static
// method in its own dedicated utility class, so this one stays separate
// rather than being folded into SharedData.
class SumUtil {
    // adds 0 + 1 + 2 + ... + n
    public static long sum(int n) {
        long total = 0;
        for (int i = 0; i <= n; i++) {
            total += i;
        }
        return total;
    }
}
