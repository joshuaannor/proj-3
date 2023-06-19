/**
 * This Queue class represents a fixed-size queue that stores Buffer objects. If the
 * queue reaches its maximum size, the LRU element is automatically removed to make space.
 * 
 * @param <T> the type of Buffer objects stored in the queue
 * @author Mark Fuentes, Joshua Annor
 * @version 06/07/23
 */
public class Queue<T extends Buffer> {
    private Node<T> front = new Node<>(null);
    private Node<T> rear = new Node<>(null);
    private int size;
    private int maxSize;

    /**
     * Creates a new Queue object with the specified maximum size
     *
     * @param maxSize the maximum size of the queue
     */
    public Queue(int maxSize) {
        this.maxSize = maxSize;
        front.setNext(rear);
        rear.setPrev(front);
        size = 0;
    }

    /**
     * Removes a Buffer from the front of the queue
     *
     * @return the removed Buffer, or null if the queue is empty
     */
    public T dequeue() {
        if (size == 0) {
            return null;
        }

        T removed = front.getNext().getData();
        front.setNext(front.getNext().getNext());
        front.getNext().setPrev(front);
        size--;
        return removed;
    }

    /**
     * Adds a Buffer to the rear of the queue using LRU. If the queue is already full,
     * the least recently used Buffer is removed and returned.
     *
     * @param buff the Buffer to be added
     * @return the removed Buffer if the queue was full, null otherwise
     */
    public T enqueue(T buff) {
        T removed = null;

        if (size == maxSize) {
            removed = dequeue();
        }

        Node<T> newBuffer = new Node<>(buff);
        newBuffer.setPrev(rear.getPrev());
        rear.getPrev().setNext(newBuffer);
        rear.setPrev(newBuffer);
        newBuffer.setNext(rear);
        size++;
        return removed;
    }

    /**
     * Searches for a Buffer with the specified block in the queue. If found, the Buffer
     * is promoted to the rear of the queue.
     *
     * @param block the block number to search for
     * @return true if the Buffer was found and promoted, false otherwise
     */
    public boolean search(int block) {
        if (size == 0) {
            return false;
        }

        Node<T> current = front.getNext();
        int index = 1;

        while (index < size) {
            if (current.getData().getBlock() == block) {
                current.getNext().setPrev(current.getPrev());
                current.getPrev().setNext(current.getNext());
                current.setPrev(rear.getPrev());
                rear.getPrev().setNext(current);
                rear.setPrev(current);
                current.setNext(rear);
                return true;
            }

            current = current.getNext();
            index++;
        }

        return index == size && current.getData().getBlock() == block;
    }

    /**
     * Returns the Buffer at the rear of the queue without removing it.
     *
     * @return the rear Buffer, or null if the queue is empty
     */
    public T lastBuffer() {
        if (size == 0) {
            return null;
        }

        return rear.getPrev().getData();
    }

    /**
     * Returns the maximum size of the queue.
     *
     * @return the maximum size
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns the current size of the queue.
     *
     * @return the current size
     */
    public int getSize() {
        return size;
    }
}
