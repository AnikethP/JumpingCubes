/** A collection of bit twiddling exercises.
 *  @author
 */

public class BitExercise {
    
    /** Fill in the function below so that it returns 
    * the value of the argument x with all but its last 
    * (least significant) 1-bit set to 0.
    * For example, 100 in binary is 0b1100100, so lastBit(100)
    * should return 4, which in binary is 0b100.
    */
    public static int lastBit(int x) {
        return x = x&-x;
            //TODO: Your code here
    }

    /** Fill in the function below so that it returns 
    * True iff x is a power of two, otherwise False.
    * For example: 2, 32, and 8192 are powers of two.
    */
    public static boolean powerOfTwo(int x) {
        //All binary numbers are represented as 1 followed by x zeroes
        //ex. 10000, 1000, 100, 10, 1
        //If we subtract by 1, it becomes zero followed by ones only if it is a power of two.
        //Then use the and operator to make sure that it is all zeroes.
        return (x & (x-1)) == 0; //TODO: Your code here
    }
    
    /** Fill in the function below so that it returns 
    * the absolute value of x WITHOUT USING ANY IF 
    * STATEMENTS OR CALLS TO MATH.
    * For example, absolute(1) should return 1 and 
    * absolute(-1) should return 1.
    */
    public static int absolute(int x) {
        //
        int y = x>>31;
        return (y^x) - y;
        //TODO: your code here
    } 
}