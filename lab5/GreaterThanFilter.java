/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        this.input = input;
        this.colName = colName;
        this.ref = ref;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int index = input.colNameToIndex(colName);
        if(_next.getValue(index).compareTo(ref) > 0)
        {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private Table input;
    private String colName;
    private String ref;
}
