package queue;


import base.MyNode;


public class LinkedQueue extends AbstractQueue {
    private MyNode head;
    private MyNode tail;
    @Override
    protected void enqueueImpl(Object element) {
        if (head == null) {
            head = new MyNode(element);
            tail = head;
        } else {
            head.setNext(new MyNode(element));
            head = head.getNext();
        }
    }

    @Override
    public Object elementImpl() {
        return tail.getElement();
    }

    @Override
    public boolean containsImpl(final Object element) {
        MyNode next = tail;
        while (!isEmpty() && next != null) {
            if (next.getElement().equals(element)) {
                return true;
            }
            next = next.getNext();
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrenceImpl(final Object element) {
        MyNode curr = tail;
        MyNode prev = tail;
        while (curr != null) {
            if (curr.getElement().equals(element)) {
                if (curr == tail) {
                    dequeueImpl();
                } else if (curr == head) {
                    head = prev;
                } else {
                    prev.setNext(curr.getNext());
                }
                size--;
                return true;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return false;
    }
    @Override
    protected void dequeueImpl() {
        if (head == tail) {
            head = null;
        }
        tail = tail.getNext();
    }
    @Override
    protected void clearImpl() {
        tail = null;
        head = null;
    }
}
