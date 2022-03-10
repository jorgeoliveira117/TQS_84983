import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TqsStackTest {

    TqsStack<Integer> testStack;

    @BeforeEach
    void setUp() {
        testStack = new TqsStack<Integer>();
    }

    @Test
    @DisplayName("A stack is empty on construction.")
    void emptyOnConstruction() {
        assertEquals(true, testStack.isEmpty());
    }

    @Test
    @DisplayName("A stack has size 0 on construction.")
    void zeroSizeOnConstruction() {
        assertEquals(0, testStack.size());
    }

    @Test
    @DisplayName("After n pushes to an empty stack, the stack is n")
    void nPushesSize() {
        Random n = new Random();
        int size = 1 + n.nextInt(10);
        for(int i = 0; i <  size; i++){
            testStack.push(0);
        }
        assertEquals(size, testStack.size());
    }

    @Test
    @DisplayName("If one pushes x then pops, the value popped is x.")
    void pushAndPop() {
        int x = 5;
        testStack.push(x);
        assertEquals(x, testStack.pop());
    }

    @Test
    @DisplayName("If one pushes x then peeks, the value returned is x, but the size stays the same.")
    void pushPeekAndSize() {
        int x = 5;
        testStack.push(x);
        int size = testStack.size();
        assertAll(
                () -> assertEquals(x, testStack.peek()),
                () -> assertEquals(size, testStack.size())
        );
    }

    @Test
    @DisplayName("If the size is n, then after n pops, the stack is empty and has a size 0.")
    void zeroSizeAfterPops() {
        Random n = new Random();
        int size = 1 + n.nextInt(10);
        for(int i = 0; i <  size; i++){
            testStack.push(0);
        }
        for(int i = 0; i <  size; i++){
            testStack.pop();
        }

        assertAll(
                () -> assertEquals(true, testStack.isEmpty()),
                () -> assertEquals(0, testStack.size())
        );
    }

    @Test
    @DisplayName("Popping from an empty stack does throw a NoSuchElementException.")
    void popEmptyStack() {
        assertThrows(NoSuchElementException.class,
                () -> {
            testStack.pop();
            }
        );
    }

    @Test
    @DisplayName("Peeking into an empty stack does throw a NoSuchElementException.")
    void peekEmptyStack() {
        assertThrows(NoSuchElementException.class,
                () -> {
            testStack.peek();
            }
        );
    }

    @Test
    @DisplayName("Bounded stacks only: Pushing onto a full stack does throw an IllegalStateException.")
    void pushFullBoundedStack() {
        testStack = new TqsStack<Integer>(1);
        testStack.push(0);
        assertThrows(IllegalStateException.class,
                () -> {
            testStack.push(0);
            }
        );
    }

/*
i)
 */
}