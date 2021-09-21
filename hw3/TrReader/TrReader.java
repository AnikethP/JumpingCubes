import java.io.Reader;
import java.io.IOException;


/** Translating Reader: a stream that is a translation of an
*  existing reader.
*  @author Aniketh Prasad
*
*  NOTE: Until you fill in the right methods, the compiler will
*        reject this file, saying that you must declare TrReader
* 	     abstract.  Don't do that; define the right methods instead!
*/
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    private Reader readerString;

    private String fromArr;
    private String toArr;

    public TrReader(Reader str, String from, String to) {
        // TODO: YOUR CODE HERE
        readerString = str;
        fromArr = from;
        toArr = to;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     */
    public int read(char[] cbuf, int off, int len) throws java.io.IOException
    {

        int a = readerString.read(cbuf, off, len);

        for(int i = off; i < off+len; i++)
        {
            for(int j = 0; j < fromArr.length(); j++)
            {
                if(cbuf[i] == fromArr.charAt(j))
                {
                    cbuf[i] = toArr.charAt(j);
                    break;
                }
            }
        }
        return a;
    }

    public void close() throws IOException
    {
        readerString.close();
    }
}
