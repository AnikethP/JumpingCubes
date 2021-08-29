public class hw0 {
    public static int max(int[] a)
    {
        max = a[0];
        for(int i = 0; i < a.length; i++)
        {
            if(a[i] > max)
            {
                max = a[i];
            }
        }
        return max;
    }

    public static boolean threeSum(int[] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j< a.length; j++)
            {
                for(int k = 0; k < a.length; k++)
                {
                    if (a[i] + a[j] + a[k] == 0)
                    {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public static boolean threesum(int[] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j< a.length; j++)
            {
                for(int k = 0; k < a.length; k++)
                {
                    if (a[i] + a[j] + a[k] == 0 && i!=j && i!=k && k!=j)
                    {
                        return true;
                    }
                }
            }
        }
        return false;

    }
}
