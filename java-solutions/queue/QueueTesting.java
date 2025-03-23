package queue;

import java.util.Arrays;
import java.util.Random;

import static base.Colors.*;

public class QueueTesting {

    private static final int QUANTITY = 10000;

    static Random random = new Random();
    public static void main(String[] args) {
        Queue queue = new ArrayQueue();
        testArrayQueue(queue);
    }

    public static void testArrayQueue(Queue queue) {
        long starTime = System.currentTimeMillis();
        System.out.println(YELLOW + "Test's started\n" + RESET);
        enqueueDequeueElementTest(queue);
        sizeClearTest(queue);
        isEmptyTest(queue);
        System.out.println(GREEN + "\nSuccessfully all tested." + RESET + "\nTime executing: " +
                (System.currentTimeMillis() - starTime) + " ms.");
    }
    public static void enqueueDequeueElementTest(Queue queue) {
        for (int i = 0; i <= QUANTITY; i++) {
            Object element = i % 2 == 0 ? i : i + " object";
            queue.enqueue(element);
        }
        for (int i = QUANTITY; i >= 0; i--) {
            Object first = queue.element();
            assert (i % 2 == 0) ?
                    first.equals(QUANTITY - i) : first.equals((QUANTITY - i) + " object") :
                    "Test's of element, enqueue is not passed";
            assert first == queue.dequeue() : "Test's of dequeue is not passed";
        }
        System.out.println("""
                    Successfully element tested.
                    Successfully dequeue tested.
                    Successfully enqueue tested.\
                """);
    }

    public static void sizeClearTest(Queue queue) {
        queue.clear();


        assert queue.isEmpty() :
                "Test's of clear is not passed";
        for (int i = 0; i < QUANTITY; i++) {
            int sizeExpected = random.nextInt(0, QUANTITY);
            for (int j = 0; j < sizeExpected; j++) {
                queue.enqueue(j);
            }
            assert queue.size() == sizeExpected : "Test's of size is not passed";
            queue.clear();
            assert queue.isEmpty() :
                    "Test's of clear is not passed";
        }
        System.out.println("    Successfully size and clear tested.");
    }

    public static void isEmptyTest(Queue queue) {
        assert queue.isEmpty() :
                "Test's of isEmpty is not passed";
        for (int i = 0; i < QUANTITY; i++) {
            int sizeExpected = random.nextInt(0, QUANTITY);
            for (int j = 0; j < sizeExpected; j++) {
                queue.enqueue(j);
                assert !queue.isEmpty() :
                        "Test's of isEmpty is not passed";
            }
            queue.clear();
            assert queue.isEmpty() :
                    "Test's of isEmpty is not passed";
        }
        System.out.println("    Successfully isEmpty tested.");
    }

}