package recurrent.recipe;

import java.util.ArrayList;

public class HeaderInfo {
    String name;
    ArrayList<DetailInfo> list;

    public HeaderInfo(String name) {
        this.name = name;
        this.list = new ArrayList<>();
    }

    public void addChild(DetailInfo i) {
        list.add(i);
    }

    public String getName() {
        return name;
    }


    public int size() {
        return list.size();
    }

    public DetailInfo getChild(int i) {
        return list.get(i);
    }
}
