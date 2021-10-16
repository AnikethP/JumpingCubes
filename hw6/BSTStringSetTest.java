import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Test of a BST-based String Set.
 * @author Aniketh Prasad
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testAsList() {
        BSTStringSet sTree = new BSTStringSet();
        String[] s = {"Bhavye", "Carlo", "Chaplain", "Cooking by the book", "Amogus", "Rezy"};
        for(String k : s){
            sTree.put(k);
        }
        List<String> k = sTree.asList();
        String[] l = new String[6];
        for(int i = 0; i< l.length; i++){
            l[i] = k.get(i);
        }

        String[] actual = {"Amogus", "Bhavye", "Carlo", "Chaplain", "Cooking by the book", "Rezy"};
        assertArrayEquals(actual, l);
        assertTrue(sTree.contains("Rezy"));
        // FIXME: Delete this function and add your own tests
    }
}
