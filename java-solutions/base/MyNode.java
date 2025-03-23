package base;

public class MyNode {
    private final Object element;
    private MyNode next;

    public MyNode(Object element) {
        this.element = element;
    }

    public Object getElement() {
        return element;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }
}