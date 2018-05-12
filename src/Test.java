public class Test {

    public static void main(String[] args) {
        Graph domain = GraphParser.getGraph("tests/test1.txt");

        System.out.println(domain);

        Vertex a = domain.getVertexByName("A");
        Vertex b = domain.getVertexByName("B");

        System.out.println("-----");

        Graph search = domain.depthFirstSearch(a, b);

        System.out.println(search);

        // Test set operations

        Graph inverseSearch = search.inverse(domain);

        System.out.println(inverseSearch);
    }

}
