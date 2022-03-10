import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack<T> {

    int maxSize = -1;
    LinkedList<T> stack;

    public TqsStack(){
        stack = new LinkedList<>();
    }

    public TqsStack(int maxSize){
        stack = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public T push(T obj){
        if(this.size() == maxSize)
            throw new IllegalStateException();
        stack.push(obj);
        return obj;
    }

    public T pop(){
        if(this.size() < 1)
            throw new NoSuchElementException();
        return stack.pop();
    }

    public T peek(){
        if(this.size() < 1)
            throw new NoSuchElementException();
        return stack.peek();
    }

    public int size(){
        return stack.size();
    }

    public boolean isEmpty(){
        return true;
    }
}
