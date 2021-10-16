import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private LinkedList<String>[] store;

    private int size = 25;
    int count = 0;
    public ECHashStringSet() {
        store = (LinkedList<String>[]) new LinkedList[size];
        for(int i = 0; i<size; i++){
            store[i] = new LinkedList<String>();
        }
    }
    @Override
    public void put(String s) {
        if(((double) count)/ ((double) size) > 0.75){
            size*=2;
            LinkedList<String>[] oldStore = store;
            store = (LinkedList<String>[]) new LinkedList[size];
            for(int i = 0; i<size; i++){
                store[i] = new LinkedList<String>();
            }
            for(LinkedList k : oldStore){
                for(int i = 0; i < k.size(); i++){
                    put((String) k.get(i));
                }
            }
        }
        int bin = getBin(s);
        store[bin].add(s);
        count+=1;
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

