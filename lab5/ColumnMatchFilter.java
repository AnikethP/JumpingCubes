/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {



    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this.colName1 = colName1;
        this.colName2 = colName2;
        this.input = input;
        // FIXME: Add your code here.
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int index1 = input.colNameToIndex(colName1);
        int index2 = input.colNameToIndex(colName2);
        if(_next.getValue(index1).equals(_next.getValue(index2)))
        {

            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    private String colName1;
    private String colName2;
    private Table input;
}
