import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private LinkedList<String>[] store;

    private int size = 5;
    int count = 0;
    public ECHashStringSet() {
        store = (LinkedList<String>[]) new LinkedList[size];
        for(int i = 0; i<size; i++){
            store[i] = new LinkedList<String>();
        }
    }
    @Override
    public void put(String s) {
        if(((double) count)/ ((double) size) > 4){
            resize();

        }
        int bin = getBin(s);
        store[bin].add(s);
        count+=1;
    }
    public void resize(){
        LinkedList<String>[] oldStore = store;
        store = new LinkedList[2*oldStore.length];
        size = 2*size;
        for(int i = 0; i<size; i++){
            store[i] = new LinkedList<String>();
        }
        count = 0;
        for(int i = 0; i < oldStore.length; i++){
            if (oldStore[i] != null){
                for(String s: oldStore[i]){
                    put(s);
                }
            }
        }
    }
    private int getBin(String s){
        int index = s.hashCode() % size;
        if (index < 0) {
            index = index & 0x7fffffff % size;
        }
        return index;
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

