import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] nums = {
                        {-303434, 4},
                        {5, 344},
                        {2, 3},
                        };
        assertEquals(MultiArr.maxValue(nums), 344);
    }

    @Test
    public void testAllRowSums() {
        int[][] nums = {
                {1,3,4},{1},{5,6,7,8},{7,9},
        };


        int[] result = {8, 1, 26, 16};


        assertArrayEquals(MultiArr.allRowSums(nums), result);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
