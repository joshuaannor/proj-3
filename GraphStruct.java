import java.util.*;

public class GraphStruct {
    private Map<String, List<String>> adjacencyList;

    public GraphStruct() {
        adjacencyList = new HashMap<>();
    }

    public void addNode(String node) {
        if (!adjacencyList.containsKey(node)) {
            adjacencyList.put(node, new ArrayList<>());
        }
    }

    public void addEdge(String source, String destination) {
        addNode(source);
        addNode(destination);

        List<String> neighbors = adjacencyList.get(source);
        if (!neighbors.contains(destination)) {
            neighbors.add(destination);
        }
    }

    public boolean hasNode(String node) {
        return adjacencyList.containsKey(node);
    }

    public boolean hasEdge(String source, String destination) {
        if (hasNode(source)) {
            List<String> neighbors = adjacencyList.get(source);
            return neighbors.contains(destination);
        }
        return false;
    }

    public List<String> getNeighbors(String node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public int getNumNodes() {
        return adjacencyList.size();
    }

    public int getNumEdges() {
        int count = 0;
        for (List<String> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        return count;
    }
    
    public void removeNode(String node) {
        if (hasNode(node)) {
            adjacencyList.remove(node);
            
            // Remove all edges connected to the node
            for (List<String> neighbors : adjacencyList.values()) {
                neighbors.remove(node);
            }
        }
    }
    
    public void removeEdge(String source, String destination) {
        if (hasNode(source)) {
            List<String> neighbors = adjacencyList.get(source);
            neighbors.remove(destination);
        }
    }
    
    public void printGraph() {
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String node = entry.getKey();
            List<String> neighbors = entry.getValue();
            System.out.print(node + " -> ");
            for (String neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}
