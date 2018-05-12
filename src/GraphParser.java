import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GraphParser {

    public static Graph getGraph(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File does not exist!");
        }
        Graph graph = new Graph();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("v")) {
                    String[] split = line.split("\\s+");
                    graph.add(new Vertex(split[1]));
                } else if (line.startsWith("e")) {
                    String[] split = line.split("\\s+");
                    int length = Integer.parseInt(split[3]);
                    Edge e = graph.connect(graph.getVertexByName(split[1]), graph.getVertexByName(split[2]));
                    e.setDefaultValue(length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }

}
