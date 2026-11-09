import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int[] stack = new int[n];
        int sp = 0, cnt = 0;
        stack[sp++] = 0;
        seen[0] = true;
        while (sp > 0) {
            int v = stack[--sp];
            order[cnt++] = v;
            for (int u : nb[v]) {
                if (!seen[u]) { seen[u] = true; parent[u] = v; stack[sp++] = u; }
            }
        }

        long[] yes = new long[n], no = new long[n];
        for (int idx = cnt - 1; idx >= 0; idx--) {
            int v = order[idx];
            yes[v] += cost[v];
            if (parent[v] >= 0) {
                int p = parent[v];
                yes[p] += Math.min(yes[v], no[v]);
                no[p] += yes[v];
            }
        }

        boolean[] tested = new boolean[n];
        for (int idx = 0; idx < cnt; idx++) {
            int v = order[idx];
            if (parent[v] < 0) tested[v] = yes[v] <= no[v];
            else if (!tested[parent[v]]) tested[v] = true;
            else tested[v] = yes[v] <= no[v];
        }
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] parent = new int[n];
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int[] stack = new int[n];
        int sp = 0, cnt = 0;
        stack[sp++] = 0;
        seen[0] = true;
        while (sp > 0) {
            int v = stack[--sp];
            order[cnt++] = v;
            for (int u : nb[v]) {
                if (!seen[u]) { seen[u] = true; parent[u] = v; stack[sp++] = u; }
            }
        }

        int[] best1 = new int[n], best2 = new int[n];
        int answer = 0;
        for (int idx = cnt - 1; idx >= 0; idx--) {
            int v = order[idx];
            answer = Math.max(answer, best1[v] + best2[v]);
            int p = parent[v];
            if (p >= 0) {
                int len = best1[v] + 1;
                if (len > best1[p]) { best2[p] = best1[p]; best1[p] = len; }
                else if (len > best2[p]) best2[p] = len;
            }
        }
        return answer;
    }
}