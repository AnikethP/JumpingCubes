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
                {1, 2, 3 },
                {2, 4, 6},
                {3, 4, -3},
        };

        int[] result = {6, 12, 4};


        assertArrayEquals(MultiArr.allRowSums(nums), result);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
