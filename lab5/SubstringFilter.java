/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        this.input = input;
        this.colName = colName;
        this.subStr = subStr;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int index = input.colNameToIndex(colName);
        if(_next.getValue(index).contains(subStr))
        {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private Table input;
    private String colName;
    private String subStr;
}
