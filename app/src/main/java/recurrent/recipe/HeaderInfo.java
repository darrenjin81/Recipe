package recurrent.recipe;

import android.preference.PreferenceActivity;

import java.util.ArrayList;

/**
 * Created by admin on 15/10/2017.
 */

public class HeaderInfo {
    String name;
    ArrayList<DetailInfo> list;

    public HeaderInfo(String name){
        this.name = name;
        this.list = new ArrayList<>();
    }

    public void addChild(DetailInfo i){
        list.add(i);
    }

    public String getName() {
        return name;
    }


    public int size(){
        return list.size();
    }

    public DetailInfo getChild(int i){
        return list.get(i);
    }
}
