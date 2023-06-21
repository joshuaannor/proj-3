public class GraphStruct {
    private static class Node {
        Handle handle;
        Node next;

        Node(Handle handle) {
            this.handle = handle;
            this.next = null;
        }
    }

    private Node[] adjacencyList;

    public GraphStruct() {
        this.adjacencyList = new Node[100]; // Set initial size as per your requirement
    }

    public void addNode(Handle handle) {
        int index = getIndex(handle.getKey());
        Node newNode = new Node(handle);

        if (adjacencyList[index] == null) {
            adjacencyList[index] = newNode;
        } else {
            Node current = adjacencyList[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void addEdge(Handle source, Handle destination) {
        int sourceIndex = getIndex(source.getKey());
        int destinationIndex = getIndex(destination.getKey());

        addNode(source);
        addNode(destination);

        Node sourceNode = adjacencyList[sourceIndex];
        Node destinationNode = adjacencyList[destinationIndex];

        if (!hasNeighbor(sourceNode, destination)) {
            Node newNeighbor = new Node(destination);
            Node current = sourceNode;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNeighbor;
        }
    }

    public boolean hasNode(Handle handle) {
        int index = getIndex(handle.getKey());
        Node current = adjacencyList[index];

        while (current != null) {
            if (current.handle.equals(handle)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public boolean hasEdge(Handle source, Handle destination) {
        int sourceIndex = getIndex(source.getKey());
        Node sourceNode = adjacencyList[sourceIndex];

        return hasNeighbor(sourceNode, destination);
    }

    private boolean hasNeighbor(Node node, Handle neighbor) {
        Node current = node;

        while (current != null) {
            if (current.handle.equals(neighbor)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public void removeNode(Handle handle) {
        int index = getIndex(handle.getKey());
        adjacencyList[index] = null;
    }

    public void removeEdge(Handle source, Handle destination) {
        int sourceIndex = getIndex(source.getKey());
        Node sourceNode = adjacencyList[sourceIndex];

        if (sourceNode == null) {
            return;
        }

        Node current = sourceNode;
        Node prev = null;

        while (current != null) {
            if (current.handle.equals(destination)) {
                if (prev == null) {
                    sourceNode = current.next;
                } else {
                    prev.next = current.next;
                }
                break;
            }
            prev = current;
            current = current.next;
        }

        adjacencyList[sourceIndex] = sourceNode;
    }

    public void printGraph() {
        for (Node node : adjacencyList) {
            Node current = node;
            while (current != null) {
                System.out.print(current.handle.getKey() + " ");
                current = current.next;
            }
            System.out.println();
        }
    }

    private int getIndex(String key) {
        int hash = key.hashCode();
        return Math.abs(hash) % adjacencyList.length;
    }
}
