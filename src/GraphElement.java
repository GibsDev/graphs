import java.util.HashMap;

public abstract class GraphElement {
    // The default value for this element
    private int value;
    // The values for this element within particular contexts
    private HashMap<Graph, Integer> values = new HashMap<>();

    public int getDefaultValue() {
        return value;
    }

    public void setDefaultValue(int value) {
        this.value = value;
    }

    public int getValue(Graph ctx) {
        if(!values.containsKey(ctx)){
            return value;
        }
        return values.get(ctx);
    }

    public void setValue(Graph ctx, int value) {
        values.put(ctx, value);
    }
}
