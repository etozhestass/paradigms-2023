package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }
    protected abstract void enqueueImpl(Object element);
    public Object dequeue() {
        assert size > 0;
        Object res = element();
        dequeueImpl();
        size--;
        return res;
    }

    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();
    protected abstract void dequeueImpl();
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();
    public boolean contains(final Object element) {
        Objects.requireNonNull(element);
        return containsImpl(element);
    }

    protected abstract boolean containsImpl(final Object element);

    public boolean removeFirstOccurrence(final Object element) {
        Objects.requireNonNull(element);
        return removeFirstOccurrenceImpl(element);
    }

    protected abstract boolean removeFirstOccurrenceImpl(Object element);
}
