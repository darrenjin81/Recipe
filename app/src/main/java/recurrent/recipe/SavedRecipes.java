package recurrent.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedRecipes extends Fragment {
    private GridView simpleList;
    private ArrayList<Recipe> saved_recipes = new ArrayList<>();
    private FirebaseUser curr_user;
    private String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getActivity().setTitle("Saved Recipes");
        return inflater.inflate(R.layout.fragment_saved_recipes, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //create a list of recipes
        //get recipes from database
        simpleList = (GridView) view.findViewById(R.id.simpleGridView1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = curr_user.getUid();

        myRef.child("users/" + user_id + "/saved_recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Recipe r = child.getValue(Recipe.class);
                    saved_recipes.add(r);
                }

                //display list of recipes in Gridview with adapter
                HomeAdapter homeAdapter = new HomeAdapter(getContext(), R.layout.grid_view_items,
                        saved_recipes);
                simpleList.setAdapter(homeAdapter);

                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Recipe curr_recipe = (Recipe) parent.getItemAtPosition(position);
                        //transition to recipe view
                        if (curr_recipe == null) {
                            return;
                        }
                        FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        Bundle args = new Bundle();
                        args.putParcelable(RecipeView.RecipeArgKey, curr_recipe);
                        Fragment nextFrag = new RecipeView();
                        nextFrag.setArguments(args);
                        fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(),
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Saved Recipes");
    }

}
