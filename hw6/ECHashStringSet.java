import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private LinkedList<String>[] store;

    private int size = 5;
    public ECHashStringSet() {
        store = (LinkedList<String>[]) new LinkedList[size];
        for(int i = 0; i<size; i++){
            store[i] = new LinkedList();
        }
    }
    @Override
    public void put(String s) {
        int bin = getBin(s);
        store[bin].add(s);
    }
    private int getBin(String s){
        return (s.hashCode() & 0x7fffffff) % size;
    }

    @Override
    public boolean contains(String s) {
        int bin = getBin(s);
        if (store[bin].contains(s)){
            return true;
        }
        return false;
        // FIXME
    }

    @Override
    public List<String> asList() {
        List<String> lst = new ArrayList<String>();
        for(int i = 0; i < size; i++){
            List<String> aList = new ArrayList<String>(store[i]);
            for (int j = 0; j < aList.size(); j++) {
                lst.add(j, aList.get(j));
            }
        }
        return lst;
    }

}

