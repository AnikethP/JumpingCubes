import org.junit.Test;
import static org.junit.Assert.*;

public class TranslateTest {
    @Test
    public void testTranslate()
    {
        assertEquals(Translate.translate("abc", "abc", "ABC"), "ABC");

    }
}
