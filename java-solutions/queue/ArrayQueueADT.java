package queue;

import java.util.Objects;

public class ArrayQueueADT {
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
    private final static int INITIAL_SIZE = 32;
    private int maxSize = INITIAL_SIZE;
    private int head = 0;
    private int size = 0;
    private Object[] elements;

    public ArrayQueueADT() {
        elements = new Object[INITIAL_SIZE];
    }

    // Pred: element != null
    // Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
    //       mod(head)' == mod(mod(head) + 1)
    public static void enqueue(ArrayQueueADT arrayQueueADT, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(arrayQueueADT);
        arrayQueueADT.elements[(arrayQueueADT.head + arrayQueueADT.size) % arrayQueueADT.maxSize] = element;
        arrayQueueADT.size++;
    }

    // Pred: size > 0
    // Post: R == queue[head] && immutable(n - 1)
    public static Object element(ArrayQueueADT arrayQueueADT) {
        assert arrayQueueADT.size > 0;
        return arrayQueueADT.elements[arrayQueueADT.head];
    }

    // Pred: size > 0
    // Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1
    public static Object dequeue(ArrayQueueADT arrayQueueADT) {
        assert arrayQueueADT.size > 0;
        Object result = arrayQueueADT.elements[arrayQueueADT.head];
        arrayQueueADT.elements[arrayQueueADT.head] = null;
        arrayQueueADT.head = mod(arrayQueueADT,arrayQueueADT.head + 1);
        arrayQueueADT.size--;
        return result;
    }


    // Pred: true
    // Post: R == size && size' = size
    public static int size(ArrayQueueADT arrayQueueADT) {
        return arrayQueueADT.size;
    }

    // Pred: true
    // Post: R = (size == 0)
    public static boolean isEmpty(ArrayQueueADT arrayQueueADT) {
        return arrayQueueADT.size == 0;
    }

    // Pred: true
    // Post: mod(head)' == 0 && head' == 0 && isEmpty
    public static void clear(ArrayQueueADT arrayQueueADT) {
        if (arrayQueueADT.size != 0) {
            arrayQueueADT.elements = new Object[INITIAL_SIZE];
            arrayQueueADT.maxSize = INITIAL_SIZE;
            arrayQueueADT.head = 0;
            arrayQueueADT.size = 0;
        }
    }

    // Pred: element != null
    // Post: immutable(head' - 1) && immutable(head' + 1, n - 1) &&
    //       element[head']' = element && size' = size + 1 && head' = mod(head - 1)
    public static void push(ArrayQueueADT arrayQueueADT, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(arrayQueueADT);
        arrayQueueADT.head = mod(arrayQueueADT, arrayQueueADT.head - 1);
        arrayQueueADT.elements[arrayQueueADT.head] = element;
        arrayQueueADT.size++;
    }

    // Pred: size > 0
    // Post: R == elements[mod(mod(head) - 1)] && immutable(n - 1)
    public static Object peek(ArrayQueueADT arrayQueueADT) {
        assert arrayQueueADT.size > 0;
        return arrayQueueADT.elements[(arrayQueueADT.head + arrayQueueADT.size - 1) % arrayQueueADT.maxSize];
        // return elements[mod(mod(head) - 1)];
    }

    // Pred: size > 0
    // Post: R == elements[mod(head)'] && immutable(mod(head)' - 1) && immutable(mod(head)' + 1, n - 1) &&
    //       elements[mod(head)']' == null && size' = size - 1 && mod(head)' = mod(mod(head) - 1)
    public static Object remove(ArrayQueueADT arrayQueueADT) {
        assert arrayQueueADT.size > 0;
        arrayQueueADT.size--;
        Object result = arrayQueueADT.elements[(arrayQueueADT.head + arrayQueueADT.size) % arrayQueueADT.maxSize];
        arrayQueueADT.elements[(arrayQueueADT.head + arrayQueueADT.size) % arrayQueueADT.maxSize] = null;
        return result;
    }

    // Pred: true
    // Post: immutable(n - 1) && R == new Object[]{head < mod(head) ? (elements[0..size]) :
    // (elements[0..maxSize - head], elements[0..mod(head)])}
    public static Object[] toArray(ArrayQueueADT arrayQueueADT) {
        Object[] tmp = new Object[arrayQueueADT.size];
        //:note: copypaste
        if (arrayQueueADT.head < (arrayQueueADT.head + arrayQueueADT.size) % arrayQueueADT.maxSize) {
            System.arraycopy(arrayQueueADT.elements, arrayQueueADT.head, tmp, 0, arrayQueueADT.size);
        } else if (!isEmpty(arrayQueueADT)){
            System.arraycopy(arrayQueueADT.elements, arrayQueueADT.head, tmp, 0,
                    arrayQueueADT.maxSize - arrayQueueADT.head);
            System.arraycopy(arrayQueueADT.elements, 0, tmp,
                    arrayQueueADT.size - mod(arrayQueueADT, arrayQueueADT.head, arrayQueueADT.size),
                    mod(arrayQueueADT, arrayQueueADT.head, arrayQueueADT.size));
        }
        return tmp;
    }

    // Pred: true
    // Post: R == (element + maxSize) % maxSize
    private static int mod(ArrayQueueADT arrayQueueADT, int element) {
        return (element + arrayQueueADT.maxSize) % arrayQueueADT.maxSize;
    }

    private static int mod(ArrayQueueADT arrayQueueADT, int elementFirst, int elementSecond) {
        return (elementFirst + elementSecond) % arrayQueueADT.maxSize;
    }

    // Pred: size > 0
    // Post: size < queue.length && immutable(n - 1) && n' == maxSize && maxSize' == maxSize * 2 && head' == 0
    //       && mod(head)' = size && size' = size
    private static void ensureCapacity(ArrayQueueADT arrayQueueADT) {
        if (arrayQueueADT.size == arrayQueueADT.maxSize) {
            arrayQueueADT.maxSize *= 2;
            Object[] tmp = new Object[arrayQueueADT.maxSize];
            if (arrayQueueADT.head < mod(arrayQueueADT, arrayQueueADT.head)) {
                System.arraycopy(arrayQueueADT.elements, arrayQueueADT.head, tmp, 0, arrayQueueADT.size);
            } else {
                System.arraycopy(arrayQueueADT.elements, arrayQueueADT.head, tmp, 0,
                        arrayQueueADT.size - arrayQueueADT.head);
                System.arraycopy(arrayQueueADT.elements, 0, tmp,
                        arrayQueueADT.size - mod(arrayQueueADT, arrayQueueADT.head),
                        mod(arrayQueueADT, arrayQueueADT.head));
            }
            arrayQueueADT.elements = tmp;
            arrayQueueADT.head = 0;
        }
    }
}
