package queue;

    /*
    Model: elements[0]..elements[n - 1], head = 0, mod(head) = 0, size = 0
    Invariant: elements != null

    let:
        1) immutable(x, y) = forall i = x..y: elements'[i] == elements[i]
        2) immutable(n) = forall i = 0..n: elements'[i] == elements[i]

    Pred: element != null
    Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
          mod(head)' == mod(mod(head) + 1)
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
    Post: mod(head)' == 0 && head' == 0 && isEmpty
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

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueModule {
    private final static int INITIAL_SIZE = 32;
    private static int maxSize = INITIAL_SIZE;
    private static int head = 0;
    private static int size = 0;
    private static Object[] elements = new Object[INITIAL_SIZE];

    // Pred: element != null
    // Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
    //       mod(head)' == mod(mod(head) + 1)
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[(head + size) % maxSize] = element;
        size++;
    }

    // Pred: size > 0
    // Post: R == queue[head] && immutable(n - 1)
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    // Pred: size > 0
    // Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1
    public static Object dequeue() {
        assert size > 0;
        Object result = elements[head];
        elements[head] = null;
        head = mod(head + 1);
        size--;
        return result;
    }


    // Pred: true
    // Post: R == size && size' = size
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R = (size == 0)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: mod(head)' == 0 && head' == 0 && isEmpty
    public static void clear() {
        if (size != 0) {
            elements = new Object[INITIAL_SIZE];
            maxSize = INITIAL_SIZE;
            head = 0;
            size = 0;
        }
    }

    // Pred: element != null
    // Post: immutable(head' - 1) && immutable(head' + 1, n - 1) &&
    //       element[head']' = element && size' = size + 1 && head' = mod(head - 1)
    public static void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        head = mod(head - 1);
        elements[head] = element;
        size++;
    }

    // Pred: size > 0
    // Post: R == elements[mod(mod(head) - 1)] && immutable(n - 1)
    public static Object peek() {
        assert size > 0;
        return elements[(head + size - 1) % maxSize];
        // return elements[mod(mod(head) - 1)];
    }

    // Pred: size > 0
    // Post: R == elements[mod(head)'] && immutable(mod(head)' - 1) && immutable(mod(head)' + 1, n - 1) &&
    //       elements[mod(head)']' == null && size' = size - 1 && mod(head)' = mod(mod(head) - 1)
    public static Object remove() {
        assert size > 0;
        size--;
        Object result = elements[(head + size) % maxSize];
        elements[(head + size) % maxSize] = null;
        return result;
    }

    // Pred: true
    // Post: immutable(n - 1) && R == new Object[]{head < mod(head) ? (elements[0..size]) :
    // (elements[0..maxSize - head], elements[0..mod(head)])}
    public static Object[] toArray() {
        Object[] tmp = new Object[size];
        //:note: copypaste
        if (head < (head + size) % maxSize) {
            System.arraycopy(elements, head, tmp, 0, size);
        } else if (!isEmpty()){
            System.arraycopy(elements, head, tmp, 0, maxSize - head);
            System.arraycopy(elements, 0, tmp, size - (head + size) % maxSize, (head + size) % maxSize);
        }
        return tmp;
    }

    // Pred: true
    // Post: R == (element + maxSize) % maxSize
    private static int mod(int element) {
        return (element + maxSize) % maxSize;
    }

    // Pred: size > 0
    // Post: size < queue.length && immutable(n - 1) && n' == maxSize && maxSize' == maxSize * 2 && head' == 0
    //       && mod(head)' = size && size' = size
    private static void ensureCapacity() {
        if (size == maxSize) {
            maxSize *= 2;
            Object[] tmp = new Object[maxSize];
            if (head < mod(head)) {
                System.arraycopy(elements, head, tmp, 0, size);
            } else {
                System.arraycopy(elements, head, tmp, 0, size - head);
                System.arraycopy(elements, 0, tmp, size - mod(head), mod(head));
            }
            elements = tmp;
            head = 0;
        }
    }
}