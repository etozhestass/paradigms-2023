package queue;

import java.util.Arrays;
import java.util.Random;

import static base.Colors.*;

public class ArrayQueueTesting {

    private static final int QUANTITY = 10000;
    private static final int TIMES = 1000;

    static Random random = new Random();
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        ArrayQueueADT queueADT = new ArrayQueueADT();
        testArrayQueue(queue, queueADT);
    }

    public static void testArrayQueue(ArrayQueue queue, ArrayQueueADT queueADT) {
        long starTime = System.currentTimeMillis();
        System.out.println(YELLOW + "Test's started\n" + RESET);
        enqueueDequeueElementTest(queue, queueADT);
        sizeClearTest(queue, queueADT);
        isEmptyTest(queue, queueADT);
        pushRemoveTest(queue, queueADT);
        toArrayTest(queue, queueADT);
        System.out.print(GREEN + "\nSuccessfully all tested." + RESET + "\nTime executing: " +
                (System.currentTimeMillis() - starTime) + " ms.");
    }
    public static void enqueueDequeueElementTest(ArrayQueue queue, ArrayQueueADT queueADT) {
        for (int i = 0; i <= QUANTITY; i++) {
            Object element = i % 2 == 0 ? i : i + " object";
            queue.enqueue(element);
            ArrayQueueADT.enqueue(queueADT, element);
            ArrayQueueModule.enqueue(element);
        }
        for (int i = QUANTITY; i >= 0; i--) {
            Object first = queue.element();
            Object second = ArrayQueueADT.element(queueADT);
            Object third = ArrayQueueModule.element();
            assert first == second && first == third && (i % 2 == 0) ?
                    first.equals(QUANTITY - i) : first.equals((QUANTITY - i) + " object") :
                    "Test's of element, enqueue is not passed";
            assert first == queue.dequeue() && second == ArrayQueueADT.dequeue(queueADT) &&
                    third == ArrayQueueModule.dequeue() : "Test's of dequeue is not passed";
        }
        System.out.println("""
                    Successfully element tested.
                    Successfully dequeue tested.
                    Successfully enqueue tested.\
                """);
    }
    public static void pushRemoveTest(ArrayQueue queue, ArrayQueueADT queueADT) {
        for (int i = 0; i <= QUANTITY; i++) {
            Object element = i % 2 == 0 ? i : i + " object";
            queue.push(element);
            ArrayQueueADT.push(queueADT, element);
            ArrayQueueModule.push(element);
        }
        for (int i = QUANTITY; i >= 0; i--) {
            Object first = queue.peek();
            Object second = ArrayQueueADT.peek(queueADT);
            Object third = ArrayQueueModule.peek();
            assert first == second && first == third && (i % 2 == 0) ?
                    first.equals(QUANTITY - i) : first.equals((QUANTITY - i) + " object") :
                    "Test's of peek, push is not passed";
            assert first == queue.remove() && second == ArrayQueueADT.remove(queueADT) &&
                    third == ArrayQueueModule.remove() : "Test's of remove is not passed";
        }
        System.out.println("""
                    Successfully peek tested.
                    Successfully push tested.
                    Successfully remove tested.\
                """);
    }
    public static void toArrayTest(ArrayQueue queue, ArrayQueueADT queueADT) {
        // empty Deque to array
        final Object[] emptyArray = new Object[]{};
        assert Arrays.equals(emptyArray, queue.toArray()) &&
                Arrays.equals(emptyArray, ArrayQueueADT.toArray(queueADT)) : "Test's of toArray() is not passed: " +
                "toArray not correct with empty ArrayDequeue";

        Object[] elements = new Object[QUANTITY];

        for (int i = 0; i < TIMES; i++) {
            for (int j = 0; j < QUANTITY; j++) {
                elements[j] = j % 2 == 0 ? random.nextInt() : "j " + j;
                queue.enqueue(elements[j]);
                ArrayQueueADT.enqueue(queueADT, elements[j]);
                ArrayQueueModule.enqueue(elements[j]);
                if (j == QUANTITY / 2) {
                    Object[] elementsHalf = Arrays.copyOf(elements, QUANTITY / 2 + 1);
                    assert Arrays.equals(elementsHalf, ArrayQueueADT.toArray(queueADT)) &&
                            Arrays.equals(elementsHalf, queue.toArray()) &&
                            Arrays.equals(elementsHalf, ArrayQueueModule.toArray()) :
                            "Test's of toArray() test is not passed with filled array";
                }
            }
            assert Arrays.equals(elements, ArrayQueueADT.toArray(queueADT)) && Arrays.equals(elements, queue.toArray())
                    && Arrays.equals(elements, ArrayQueueModule.toArray()) : "Test's of toArray() test not passed with"
                    + "filled array";

            ArrayQueueADT.clear(queueADT);
            queue.clear();
            ArrayQueueModule.clear();

            //after operation clear() to array
            assert Arrays.equals(emptyArray, queue.toArray()) &&
                    Arrays.equals(emptyArray, ArrayQueueADT.toArray(queueADT));
        }
        System.out.println("    Successfully toArray tested.");
    }
    public static void sizeClearTest(ArrayQueue queue, ArrayQueueADT queueADT) {
        queue.clear();
        ArrayQueueADT.clear(queueADT);
        ArrayQueueModule.clear();

        assert queue.isEmpty() && ArrayQueueADT.isEmpty(queueADT) && ArrayQueueModule.isEmpty() :
                "Test's of clear is not passed";
        for (int i = 0; i < QUANTITY; i++) {
            int sizeExpected = random.nextInt(0, QUANTITY);
            for (int j = 0; j < sizeExpected; j++) {
                queue.enqueue(j);
                ArrayQueueADT.enqueue(queueADT, j);
                ArrayQueueModule.enqueue(j);
            }
            assert queue.size() == sizeExpected && ArrayQueueADT.size(queueADT) == sizeExpected &&
                    ArrayQueueModule.size() == sizeExpected : "Test's of size is not passed";
            queue.clear();
            ArrayQueueADT.clear(queueADT);
            ArrayQueueModule.clear();
            assert queue.isEmpty() && ArrayQueueADT.isEmpty(queueADT) && ArrayQueueModule.isEmpty() :
                    "Test's of clear is not passed";
        }
        System.out.println("    Successfully size and clear tested.");
    }

    public static void isEmptyTest(ArrayQueue queue, ArrayQueueADT queueADT) {
        assert queue.isEmpty() && ArrayQueueADT.isEmpty(queueADT) && ArrayQueueModule.isEmpty() :
                "Test's of isEmpty is not passed";
        for (int i = 0; i < QUANTITY; i++) {
            int sizeExpected = random.nextInt(0, QUANTITY);
            for (int j = 0; j < sizeExpected; j++) {
                queue.enqueue(j);
                ArrayQueueADT.enqueue(queueADT, j);
                ArrayQueueModule.enqueue(j);
                assert !queue.isEmpty() && !ArrayQueueADT.isEmpty(queueADT) && !ArrayQueueModule.isEmpty() :
                        "Test's of isEmpty is not passed";
            }
            queue.clear();
            ArrayQueueADT.clear(queueADT);
            ArrayQueueModule.clear();
            assert queue.isEmpty() && ArrayQueueADT.isEmpty(queueADT) && ArrayQueueModule.isEmpty() :
                    "Test's of isEmpty is not passed";
        }
        System.out.println("    Successfully isEmpty tested.");
    }

}