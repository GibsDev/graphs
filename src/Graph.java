import java.util.HashSet;
import java.util.Iterator;

/**
 * Responsible for managing the context within the given Vertex/Edges
 */
public class Graph {

    // The edges in this graph
    private HashSet<Edge> edges = new HashSet<>();
    // Only used as a cache to generate a vertex iterator
    private HashSet<Vertex> vertices = new HashSet<>();

    /**
     * Add an edge to the graph. Edges should only be created in the connect method.
     * @param e
     */
    public void add(Edge e) {
        edges.add(e);
        // Track the vertices from the edge
        vertices.add(e.getA());
        vertices.add(e.getB());
        // Add the edge to this context
        e.getA().add(this, e);
        e.getB().add(this, e);
        // Add the edge to the global map
        e.getA().add(e);
        e.getB().add(e);
    }

    /**
     * Add and track vertex to this graph
     * @param v
     */
    public void add(Vertex v) {
        vertices.add(v);
        // Send vertex a null edge so that it can initialize the context edges set
        v.add(this, null);
    }

    /**
     * Remove a vertex and its tracking from this graph
     * @param v
     */
    public void remove(Vertex v) {
        vertices.remove(v);
        for (Iterator<Edge> edgeIterator = v.getEdges(this); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            edges.remove(edge);
            Vertex other = edge.getOpposite(v);
            other.remove(this, edge);
        }
        v.remove(this);
    }

    /**
     * Remove an edge and its tracking from this context
     * @param e
     */
    public void remove(Edge e) {
        edges.remove(e);
        e.getA().remove(this, e);
        e.getB().remove(this, e);
    }

    /**
     * Get all edges that are being tracked by this graph context
     * @return
     */
    public Iterator<Edge> getEdges() {
        return edges.iterator();
    }

    /**
     * Get all vertices being tracked by this graph context
     * @return
     */
    public Iterator<Vertex> getVertices() {
        return vertices.iterator();
    }

    /**
     * Get the edge between the given vertices. This looks at all edges for these vertices, the context does not matter.
     * @param a
     * @param b
     * @return
     */
    public Edge getEdgeBetween(Vertex a, Vertex b) {
        // TODO check to make sure the edge is in this context? Or make a separate method to do that.
        for (Iterator<Edge> edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            if (edge.hasVertices(a, b)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Get a given vertex by name. Vertex needs to be within this graph.
     * @param name
     * @return
     */
    public Vertex getVertexByName(String name) {
        for (Iterator<Vertex> vi = vertices.iterator(); vi.hasNext(); ) {
            Vertex next = vi.next();
            if (next.getLabel().equals(name)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Create an edge between these vertices
     * @param a
     * @param b
     * @return the edge that was created
     */
    public Edge connect(Vertex a, Vertex b) {
        Edge edge = this.getEdgeBetween(a, b);
        if (edge == null) {
            edge = new Edge(a, b);
        }
        this.add(edge);
        return edge;
    }

    /**
     * Create an edge between two vertices with a given weight for this context
     * @param a
     * @param b
     * @param value the length between the two vertices
     * @return the edge that was created
     */
    public Edge connect(Vertex a, Vertex b, int value) {
        Edge e = this.connect(a, b);
        e.setValue(this, value);
        return e;
    }

    /**
     * Search through the points in this graph (assumes that this graph is the search domain)
     *
     * @param start
     * @param end
     * @return a graph that contains the path between the points, or null if one does not exist
     */
    public Graph depthFirstSearch(Vertex start, Vertex end) {
        Graph unsearched = this.clone();
        Graph search = new Graph();
        boolean found = dfs(unsearched, search, start, end);
        if (found) {
            return search;
        }
        return null;
    }

    /**
     * Helper method for depth first search to allow for recursion.
     * @param unsearched the domain graph to search for a path
     * @param search the path found by the search
     * @param start the starting vertex
     * @param end the ending vertex
     * @return a boolean success of finding a path
     */
    private boolean dfs(Graph unsearched, Graph search, Vertex start, Vertex end) {
        boolean found = false;
        for (Iterator<Edge> edgeIterator = start.getEdges(unsearched); edgeIterator != null && edgeIterator.hasNext(); ) {
            Edge e = edgeIterator.next();
            Vertex v = e.getOpposite(start);
            search.add(e);
            unsearched.remove(start);
            if (v == end) {
                // found
                return true;
            } else {
                found = dfs(unsearched, search, v, end);
                if (!found) {
                    search.remove(v);
                } else {
                    return true;
                }
            }
        }
        return found;
    }

    /**
     * @param start
     * @param end
     * @return the shortest path between the two given vertices, or null if no path exists
     */
    public Graph shortestPath(Vertex start, Vertex end) {
        return null;
    }

    /**
     * Generates a graph that is the inverse of this graph within the given domain (superset of this graph).
     * Removes all edge from this graph, then removes all vertices that are no longer connected.
     * @param domain the graph that contains the domain of edges to subtract from
     * @return a clone of the domain graph with the edges removed and disconnected vertices removed.
     */
    public Graph inverse(Graph domain) {
        Graph inverse = domain.clone();
        for (Edge e : edges) {
            inverse.remove(e);
        }
        for (Vertex v : vertices) {
            if(v.getDegree(inverse) == 0){
                inverse.remove(v);
            }
        }
        return inverse;
    }

    /**
     * @param other the graph to combine with this one
     * @return a new graph that contains the edges and vertices from this and the other graph
     */
    public Graph union(Graph other) {
        Graph union = this.clone();
        for (Edge e : other.edges) {
            union.edges.add(e);
        }
        for (Vertex v : other.vertices) {
            union.vertices.add(v);
        }
        return union;
    }

    /**
     * @param other
     * @return a new graph made of this and the given graph
     */
    public Graph join(Graph other) {
        Graph join = this.clone();
        join.merge(this);
        return join;
    }

    /**
     * Merges the given graph into this one
     * @param other
     */
    public void merge(Graph other) {
        for (Edge e : other.edges) {
            this.add(e);
        }
        for (Vertex v : other.vertices) {
            this.add(v);
        }
    }

    @Override
    protected Graph clone() {
        Graph clone = new Graph();
        for (Edge e : edges) {
            clone.edges.add(e);
        }
        for (Vertex v : vertices) {
            clone.vertices.add(v);
            // transfer over edge tracking to clone
            v.getEdges(this).forEachRemaining(e -> {
                v.add(clone, e);
            });
        }
        return clone;
    }

    @Override
    public String toString() {
        String out = "Graph {\n";
        for (Iterator<Edge> edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {
            Edge edge = edgeIterator.next();
            out += "\t" + edge.toString(this);
            if (edgeIterator.hasNext()) {
                out += ",";
            }
            out += "\n";
        }
        out += "}";
        return out;
    }

    @Override
    protected void finalize() throws Throwable {
        // Remove tracking of this graph
        for (Edge edge : edges) {
            edge.remove(this);
        }
        for (Vertex vertex : vertices) {
            vertex.remove(this);
        }
        super.finalize();
    }
}
