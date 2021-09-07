import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(CompoundInterest.numYears(2021), 0);
        assertEquals(CompoundInterest.numYears(2022), 1);
        assertEquals(CompoundInterest.numYears(2121), 100);
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(0, 15, 2025), 0, tolerance);
        assertEquals(CompoundInterest.futureValue(10, 12, 2023), 12.544, tolerance);
        assertEquals(CompoundInterest.futureValue(10, -12, 2022), 8.8, tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2023, 3), 11.8026496, tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavings(5000, 2023, 10), 16550, tolerance);


    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavingsReal(5000, 2023, 10, 3), 15571.895, tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
