package recurrent.recipe;

/**
 * Created by kitty on 22/9/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class Homepage extends Fragment {
    private GridView simpleList;
    private ArrayList featured_recipes = new ArrayList<>();
    private String test;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, parent, false);

        //create a list of recipes
        //configure with firebase later
        simpleList = (GridView) view.findViewById(R.id.simpleGridView);
        featured_recipes.add(new Recipe("pizza","Test"));
        featured_recipes.add(new Recipe("bread","Test"));
        featured_recipes.add(new Recipe("pudding","Test"));

        //display list of recipes in Gridview with adapter
        HomeAdapter homeAdapter = new HomeAdapter(this.getContext(),R.layout.grid_view_items, featured_recipes);
        simpleList.setAdapter(homeAdapter);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
