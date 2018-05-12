public class Test {

    public static void main(String[] args) {
        Graph domain = GraphParser.getGraph("tests/test1.txt");

        System.out.println(domain);

        Vertex a = domain.getVertexByName("A");
        Vertex b = domain.getVertexByName("B");

        a.getEdges(domain).forEachRemaining(e -> {
            System.out.println(e.toString(domain));
        });

        System.out.println("-----");

        Graph clone = domain.clone();

        clone.getEdges().forEachRemaining(e -> {
            System.out.println(e.toString(clone));
        });

        Graph search = domain.depthFirstSearch(a, b);

        System.out.println(search);
    }

}
