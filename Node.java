/**
 * This class represents a node in a doubly linked list where each node holds a
 * reference to an object of type T and has access to the next and previous nodes in the list.
 * 
 * @param <T> the type of object stored in the node
 * @author Mark Fuentes, Joshua Annor
 * @version 06/07/23
 */
public class Node<T> {
    private Node<T> next;
    private Node<T> prev;
    private T data;

    /**
     * Initializes a node object that has next and prev nodes.
     *
     * @param data the data of type T
     */
    public Node(T data) {
        this.data = data;
        next = null;
        prev = null;
    }

    /**
     * Getter method for next node of node.
     *
     * @return next node
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Setter method for next node.
     *
     * @param next new node to be set next
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Getter method for the prev node.
     *
     * @return prev node of node
     */
    public Node<T> getPrev() {
        return prev;
    }

    /**
     * Setter method for prev node of a node.
     *
     * @param prev new prev node
     */
    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    /**
     * Getter method for node's data.
     *
     * @return the data of node
     */
    public T getData() {
        return data;
    }

    /**
     * Setter method for node's data.
     *
     * @param data the new data of node
     */
    public void setData(T data) {
        this.data = data;
    }
}
