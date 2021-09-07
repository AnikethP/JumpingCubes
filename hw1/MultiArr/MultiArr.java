/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        //TODO: Your code here!
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        //TODO: Your code here!
        int maximum = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++ )
        {
            for (int j = 0; j < arr[0].length; j++)
            {
                if (arr[i][j] > maximum)
                {
                    maximum = arr[i][j];
                }
            }
        }
        return maximum;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        //TODO: Your code here!!
       int[] lst = new int[arr.length];

       for(int i = 0; i < arr.length; i++)
        {
            int runningSum = 0;
            for (int j = 0; j < arr[0].length; j++)
            {
                runningSum += arr[i][j];
            }
            lst[i] = runningSum;
        }

       return lst;
    }
}