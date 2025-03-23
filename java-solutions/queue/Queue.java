package queue;


public interface Queue {
    /*Pred: element != null
      Post: immutable(0, mod(head) - 1) && immutable(mod(head) + 1, n - 1) && size' == size + 1 && a[mod(head)] == element &&
            mod(head)' == mod(mod(head) + 1)*/
    void enqueue(final Object element);

    /*Pred: size > 0
      Post: R == queue[head] && immutable(n - 1)*/
    Object element();

    /*Pred: size > 0
      Post: R == queue[head] && queue'[head] == null && head' = mod(head + 1) && size' = size - 1*/
    Object dequeue();

    /*Pred: true
      Post: R == size && size' = size*/
    int size();

    /*Pred: true
      Post: R = (size == 0)*/
    boolean isEmpty();

    /*Pred: true
      Post: mod(head)' == 0 && head' == 0 && isEmpty*/
    void clear();

    // Pred: element != null
    // Post: R == elements.contains(element)
    boolean contains(final Object element);

    // Pred: element != null
    // Post: R == elements.contains(element) && elements' = elements where !contains(element)
    boolean removeFirstOccurrence(final Object element);
}
