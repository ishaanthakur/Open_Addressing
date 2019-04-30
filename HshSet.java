import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HshSet<E> implements Iterable<E> {
    private int Initial_Capacity= 10;    // Smallest capacity, and initial one
    private double Load_Factor= .75;     // Highest allowed load factor
    private double load_Factor_Min= .33; // Lowest allowed load factor (unless
    // capacity is Initial_Capacity)

    /* Here are main properties of the set:
     * (1)  The elements of the set are the non-null b[i] for which in[i] is true.
     * (2)  No value is in b more than once.
     * (3)  b[i] = null ==> in[i] is false.
     * (4)  If an element in the set hashes to h but was placed in b[h+k] (with
     *      wraparound), then all elements in b[h..h+k-1] are not null.
     * (5)  In order to maintain (4), removal of an element in some b[i]
     *      is done simply by setting in[i] to false. */
    private E[] b;
    private BitSet in;

    private int load= 0;              // Number of b[i] that are not null.
    private int size= 0;              // The number of elements in the set
    private int numberOfRehashes= 0;  // Number of rehashes performed on this instance

    /** Constructor: an empty set with capacity Initial_Capacity. */
    @SuppressWarnings("unchecked")
    public HshSet() {
        b= (E[])(new Object[Initial_Capacity]);
        size= 0;
        in= new BitSet(Initial_Capacity);
    }

    /** return the initial capacity. */
    public int initialCapacity() {
        return Initial_Capacity;
    }

    /** = the number of items in this set. */
    public int size() {
        return size; 
    }

    /** = the capacity of this set
        --i.e. size of the array used to hold its elements. */
    public int capacity() {
        return b.length;
    }

    /** = the load --number of elements non-null elements */
    public int load() {
        return load;
    }
    
    /** = an Iterator over this hashed set. */
    public Iterator<E> iterator() {
        return new SetIterator();
    }

    /** An iterator over the elements of the hashed set. */
    private class SetIterator implements Iterator<E> {
        // 0 <= k < b.length. Elements in b[0..k-1] have been enumerated
        // If k < b.length, b[k] may be null or an element of the set
        private int k; 
        
        /** Constructor: an iterator over the set. */
        public SetIterator() {
        }

        @Override public boolean hasNext() {
            while (k < b.length  &&  b[k] == null) k= k+1;
            return k < b.length;
        }

        @Override public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            k= k+1;
            return b[k-1];
        }

    }


    /** = the number of rehashes performed on this set
          since the last operation to set size to 0. */
    public int getRehashes() {
        return numberOfRehashes; 
    }

    /**  = "e is in the set". */
    public boolean contains(E e) {
        int h= linearProbe(e);
        return in.get(h);
    }

    /** Ensure that e is in this set and return value of sentence
        "e was added because it was not in the set." */
    public boolean add(E e) {
        int h= linearProbe(e);

        if (in.get(h)) return false; // e is already in the set

        size= size + 1;
        in.flip(h);
        if (b[h] != null) { // e was in the "removed" state. Put it back in.
            return true;
        }

        // e is not in set and b[h] is null
        b[h]= e;
        load= load + 1;
        if (load > .75 * b.length) rehash();  // b is too small

        return true;
    }

    /** Remove e from this set (if it is in it) and return value of sentence
        "e was removed because it was in the set".*/
    public boolean remove(E e) {
        int h= linearProbe(e);
        if (!in.get(h)) return false;

        in.flip(h);
        size= size - 1;
        return true;
    }

    /** Change the size of this set to zero --but do not rehash.*/
    public void clear() {
        size= 0; load= 0;
        for (int i= 0; i != b.length; i= i+1) {
            b[i]= null;
        }
        in.clear();
    }

    /** = index in array b of e or where e will be put (using linear probing).
     *    Precondition: e may not be null. */
    private int linearProbe(E e) {
        assert e != null;
        int h= Math.abs(e.hashCode() % b.length);
        int i= h;
        // inv: e is not one of b[h..k+i-1] (with wraparound)
        while (b[i] != null  &&  !e.equals(b[i])) { 
            i= (i+1) % b.length;
        }

        return i;
    }

    /** Rehash array b */
    private void rehash( ) {
        E[] oldb= b;  // copy of arrays
        BitSet oldIn= in;

        // Create a new, empty array. This is used both for increasing and
        // decreasing the capacity; hence the Math.max expression.
        b= (E[])(new Object[Math.max(3*size, Initial_Capacity)]);
        in= new BitSet(b.length);
        load= 0;
        size= 0;

        // Copy elements from oldb to b
        for (int h= 0; h != oldb.length; h= h+1) {
            if (oldb[h] != null  &&  oldIn.get(h)) {
                add(oldb[h]);
            }
        }

        numberOfRehashes= numberOfRehashes + 1;
    }
}
