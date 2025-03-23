package queue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    /*
    Model: elements[0]..elements[n - 1], head = 0, size = 0
    Invariant: elements != null

    let:
        1) immutable(x, y) = forall i = x..y: elements'[i] == elements[i]
        2) immutable(n) = forall i = 0..n: elements'[i] == elements[i]

    Pred: element != null
    Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 &&
          a[mod(head)] == element && mod(head)' == mod(mod(head) + 1)
        enqueue(element)

    Pred: size > 0
    Post: R == queue[head] && immutable(n - 1)
        element

    Pred: size > 0
    Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1
        dequeue

    Pred: true
    Post: R == size && size' = size
        size()

    Pred: true
    Post: R = (size == 0)
        isEmpty

    Pred: true
    Post: head' == 0 && isEmpty
        clear

    Pred: element != null
    Post: immutable(head' - 1) && immutable(head' + 1, n - 1) &&
          element[head']' = element && size' = size + 1 && head' = mod(head - 1)
        push

    Pred: size > 0
    Post: R == elements[mod(mod(head) - 1)] && immutable(n - 1)
        peek

    Pred: size > 0
    Post: R == elements[mod(head)'] && immutable(mod(head)' - 1) && immutable(mod(head)' + 1, n - 1) &&
          elements[mod(head)']' == null && size' = size - 1 && mod(head)' = mod(mod(head) - 1)
        remove
*/

    private final  int INITIAL_SIZE = 16;
    //:note: use elements size
    private int maxSize = INITIAL_SIZE;
    private int head = 0;
    //:note: mod(head)
    private  Object[] elements;
    public ArrayQueue() {
        elements = new Object[INITIAL_SIZE];
    }

    // Pred: element != null
    // Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
    //       mod(head)' == mod(mod(head) + 1)
    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[(head + size) % maxSize] = element;
        size++;
    }

    // Pred: element != null
    // Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
    //       mod(head)' == mod(mod(head) + 1)
    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapacity();
        elements[(head + size) % maxSize] = element;
    }

    // Pred: size > 0
    // Post: R == queue[head] && immutable(n - 1)
    public Object element() {
        assert size > 0;
        return elements[head];
    }
    // Pred: size > 0
    // Post: R == queue[head] && immutable(n - 1)
    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    // Pred: size > 0
    // Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1
    public Object dequeue() {
        assert size > 0;
        Object result = elements[head];
        elements[head] = null;
        head = mod(head + 1);
        size--;
        return result;
    }

    // Pred: size > 0
    // Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1
    @Override
    protected void dequeueImpl() {
        assert size > 0;
        elements[head] = null;
        head = mod(head + 1);
    }

    // Pred: true
    // Post: R == size && size' = size
    public int size() {
        return size;
    }

    // Pred: true
    // Post: R = (size == 0)
    public boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: mod(head)' == 0 && head' == 0 && isEmpty
    //:note: useless
    public void clear() {
        if (size != 0) {
            elements = new Object[INITIAL_SIZE];
            maxSize = INITIAL_SIZE;
            head = 0;
            size = 0;
        }
    }

    @Override
    protected boolean containsImpl(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[mod(head, i)].equals(element)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean removeFirstOccurrenceImpl(Object element) {
        Object[] tmp = new Object[maxSize];
        boolean condition = false;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (!elements[mod(head, i)].equals(element) || condition) {
                tmp[count++] = elements[mod(head, i)];
            } else {
                condition = true;
            }
        }
        size = count;
        elements = tmp;
        head = 0;
        return condition;
    }
    @Override
    protected void clearImpl() {
        if (size != 0) {
            elements = new Object[INITIAL_SIZE];
            maxSize = INITIAL_SIZE;
            head = 0;
        }
    }

    // Pred: element != null
    // Post: immutable(head' - 1) && immutable(head' + 1, n - 1) &&
    //       element[head']' = element && size' = size + 1 && head' = mod(head - 1)
    public void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        head = mod(head - 1);
        elements[head] = element;
        size++;
    }

    // Pred: size > 0
    // Post: R == elements[mod(mod(head) - 1)] && immutable(n - 1)
    public Object peek() {
        assert size > 0;
        return elements[(head + size - 1) % maxSize];
        // return elements[mod(mod(head) - 1)];
    }

    // Pred: size > 0
    // Post: R == elements[mod(head)'] && immutable(mod(head)' - 1) && immutable(mod(head)' + 1, n - 1) &&
    //       elements[mod(head)']' == null && size' = size - 1 && mod(head)' = mod(mod(head) - 1)
    public Object remove() {
        assert size > 0;
        size--;
        //:note:  extract to var
        Object result = elements[(head + size) % maxSize];
        elements[(head + size) % maxSize] = null;
        return result;
    }

    // Pred: true
    // Post: immutable(n - 1) && R == new Object[]{head < mod(head) ? (elements[0..size]) :
    // (elements[0..maxSize - head], elements[0..mod(head)])}
    public Object[] toArray() {
        Object[] tmp = new Object[size];
        //:note: copypaste
        copy(tmp, mod(head, size), maxSize);
        return tmp;
    }

    // Pred: true
    // Post: R == (element + maxSize) % maxSize
    private int mod(int element) {
        return (element + maxSize) % maxSize;
    }

    // Pred: true
    // Post: R == (elementFirst + elementSecond) % maxSize
    private int mod(int elementFirst, int elementSecond) {
        return (elementFirst + elementSecond) % maxSize;
    }


    // Pred: size > 0
    // Post: size < queue.length && immutable(n - 1) && n' == maxSize && maxSize' == maxSize * 2 && head' == 0
    //       && mod(head)' = size && size' = size
    private void ensureCapacity() {
        if (size == maxSize) {
            maxSize *= 2;
            Object[] tmp = new Object[maxSize];
            copy(tmp, mod(head), size);
            elements = tmp;
            head = 0;
        }
    }
    // Pred: tmp.length == size || tmp.length == maxSize
    // Post: tmp = elements where head = 0
    private void copy(Object[] tmp, int tail, int len) {
        if (head < tail) {
            System.arraycopy(elements, head, tmp, 0, size);
        } else if (!isEmpty()){
            System.arraycopy(elements, head, tmp, 0, len - head);
            System.arraycopy(elements, 0, tmp, size - tail, tail);
        }
    }
}
