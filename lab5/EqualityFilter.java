/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this.input = input;
        this.colName = colName;
        this.match = match;

    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int index = input.colNameToIndex(colName);
        if(_next.getValue(index).equals(match))
        {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private String colName;
    private String match;
    private Table input;
}
