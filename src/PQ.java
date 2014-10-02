import java.util.NoSuchElementException;

public abstract class PQ
// Priority Queue for PQItems, based on minimum values
//    It is assumed that class PQItem implements Comparable <PQItem>
//    PQItem's method compareTo determines the relative priorities items
// 
// The Priority Queue is implemented by a heap,
// with the maximum size given in the constructor.
{
    // invariant:  currentSize of array <= heapArray.size();
    int currentSize;
    PQItem heapArray [];

    // constructor defines max size of underlying heap
    // and creates the underlying heap array
    public PQ (int k)
    {
        heapArray = new PQItem[k];
        currentSize = 0;
    }

    // returns true if priority queue is empty
    public Boolean isEmpty ()
    {
        return currentSize == 0;
    }

    // returns true if priority queue is full
    public Boolean isFull ()
    {
        return currentSize == heapArray.length;
    }

    // inserts item into the priority queue, 
    // pre-condition:  the priority queue is not full
    public abstract void insert (PQItem item);

    // returns the top priority item (the minimum value)
    // pre-condition:  the priority queue is not empty
    // post-condition: the priority queue is not changed
    public PQItem top () 
    {
        if (isEmpty ())
            throw new NoSuchElementException ();
        else
            return heapArray[0];
    }

    // returns the top priority item (the minimum value)
    // pre-condition:  the priority queue is not empty
    // post-condition:  the returned item is removed from the priority queue
public abstract PQItem remove (); 


}
