package recurrent.recipe;

/**
 * Created by admin on 15/10/2017.
 */

public class DetailInfo {
    private String sequence = "";
    private String name = "";

    public DetailInfo(String s, String n){
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
