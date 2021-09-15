package arrays;
import java.util.ArrayList;
/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int totalLen = A.length + B.length;
        int[] rl = new int[totalLen];
        for(int i = 0; i < A.length; i++)
        {
            rl[i] = A[i];
        }

        for(int j = A.length; j < B.length+A.length; j++ )
        {
            rl[j] = B[j-A.length];
        }
        return rl;

    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        if (start + len > A.length-1)
        {
            return null;
        }
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        for(int i = 0; i < A.length; i++)
        {
            if(i < start || i > start+ len-1)
            {
                returnList.add(A[i]);
            }

        }
        int[] return_arr = new int[returnList.size()];
        for(int i = 0; i < returnList.size(); i++)
        {
            return_arr[i] = returnList.get(i);
        }
        return return_arr;

    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A)
    {
        /* *Replace this body with the solution. */
        int highest = Integer.MIN_VALUE;
        ArrayList<ArrayList<Integer>> master = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> curr = new ArrayList<Integer>();
        for(int num : A)
        {
            if(num <= highest)
            {

                master.add(curr);
                curr = new ArrayList<Integer>();
                //System.out.println(num);
                highest = num;
            }
            else
            {
                highest = num;
                //System.out.println(num);

            }
            curr.add(num);

        }
        master.add(curr);
        int[][] array = new int[master.size()][];
        for (int i = 0; i < master.size(); i++) {
            ArrayList<Integer> row = master.get(i);

            int[] temp = new int[row.size()];
            for (int j = 0; j < row.size(); j++)
            {

                temp[j] = row.get(j);
            }
            array[i] = temp;
        }

        return array;
    }

}
