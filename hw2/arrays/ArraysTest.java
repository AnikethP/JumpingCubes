package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void testCatenate()
    {
        int[] a = {1, 2, 3, 4};
        int[] b = {4, 5, 6};
        int[] c = {1, 2, 3, 4, 4, 5, 6};
        Arrays.catenate(a, b);
        assertArrayEquals(Arrays.catenate(a, b), c);
    }

    @Test
    public void testRemove()
    {
        int[] a = {0, 1, 2, 3};
        int[] b = {0,3};

        assertArrayEquals(Arrays.remove(a, 1, 2), b);

    }

    @Test
    public void testNaturalRuns()
    {
        int[] a = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] b = {{1, 3, 7}, {5}, {4, 6, 9, 10}};

        assertArrayEquals(Arrays.naturalRuns(a), b);
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
