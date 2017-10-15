package recurrent.recipe;

/**
 * Created by kitty on 22/9/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Homepage extends Fragment {
    private GridView simpleList;
    private ArrayList <Recipe> featured_recipes = new ArrayList<>();

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, parent, false);

        //create a list of recipes
        //get recipes from database
        simpleList = (GridView) view.findViewById(R.id.simpleGridView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children){
                    Recipe r = child.getValue(Recipe.class);
                    //TODO fix
                    if(r.getName().equals("pizza") || r.getName().equals("chicken") || r.getName().equals("bread")){
                        featured_recipes.add(r);
                    }
                }

                //display list of recipes in Gridview with adapter
                HomeAdapter homeAdapter = new HomeAdapter(getContext(),R.layout.grid_view_items, featured_recipes);
                simpleList.setAdapter(homeAdapter);

                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Recipe curr_recipe = (Recipe)parent.getItemAtPosition(position);
                        //transition to recipe view
                        if(curr_recipe == null){
                            return;
                        }
                        FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();


                        Bundle args = new Bundle();
                        args.putParcelable(RecipeSummary.RecipeSummaryArgKey, curr_recipe);

                        Fragment nextFrag= new RecipeSummary();
                        nextFrag.setArguments(args);
                        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(),
                                nextFrag);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

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
