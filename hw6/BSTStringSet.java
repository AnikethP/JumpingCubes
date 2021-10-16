import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if(_root == null){
            _root = new Node(s);
        }
        // FIXME: PART A
        Node l = _root;
        while(l != null){
            if(s.compareTo(l.s) < 0){
                if(l.left == null){
                    l.left = new Node(s);
                }
                else{
                    l = l.left;
                }
            }
            else if(s.compareTo(l.s) == 0){
                return;
            }
            else{
                if(l.right == null){
                    l.right = new Node(s);
                }
                else{
                    l = l.right;
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        Node curr = _root;
        while(curr != null){
            if(curr.s.equals(s)){
                return true;
            }
            else if(s.compareTo(curr.s) < 0){
                curr = curr.left;
            }
            else{
                curr = curr.right;
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        BSTIterator iter = new BSTIterator(_root);
        ArrayList<String> stringList = new ArrayList<String>();
        while(iter.hasNext()){
            String x = iter.next();

            stringList.add(x);


        }
        //Sort stringList

        return sortArrayList(stringList);

    }

    private List<String> sortArrayList(List<String> lst){
        if(lst.size() <= 1){
            return lst;
        }

        return insertInto(lst.get(0), sortArrayList(lst.subList(1,lst.size())));
    }

    private List<String> insertInto(String k, List<String> lst){
        int j;
        for(j = 0; j < lst.size(); j++){
            if(k.compareTo(lst.get(j)) <= 0){

                lst.add(j, k);
                break;
            }
            else if(j == lst.size()-1){
                lst.add(j+1, k);
            }
        }
        return lst;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B (OPTIONAL)
    }


    /** Root node of the tree. */
    private Node _root;
}
