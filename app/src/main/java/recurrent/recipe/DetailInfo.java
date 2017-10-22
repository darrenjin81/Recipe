package recurrent.recipe;

public class DetailInfo {
    private String sequence = "";
    private String name = "";

    public DetailInfo(String s, String n) {
        sequence = s;
        name = n;
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }
}
