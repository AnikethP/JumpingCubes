import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testAsList() {
        ArrayList<String> array= new ArrayList(Arrays.asList("Dog", "Cat", "Reese", "Monkey"));
        ECHashStringSet tester = new ECHashStringSet();
        for(String k : array){
            tester.put(k);
        }
        List lst = tester.asList();
        assertTrue(lst.size() == array.size() && lst.containsAll(array) && array.containsAll(lst));
    }
}
