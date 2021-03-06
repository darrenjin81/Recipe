package recurrent.recipe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeCollectionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Recipe> recipes;

    public RecipeCollectionPagerAdapter(FragmentManager fm, final String query) {
        super(fm);
        recipes = new ArrayList<Recipe>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(Constants.RecipeTable).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Recipe r = child.getValue(Recipe.class);
                    if (query == null || r.getName().toLowerCase().contains(query.toLowerCase())) {
                        recipes.add(r);
                    }
                }

                RecipeCollectionPagerAdapter.super.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public Fragment getItem(int i) {

        Bundle args = new Bundle();
        Recipe r = recipes.get(i);
        args.putParcelable(RecipeSummary.RecipeSummaryArgKey, r);

        Fragment fragment = null;
        fragment = new RecipeSummary();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}

