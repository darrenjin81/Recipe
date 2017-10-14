package recurrent.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class RecipeView extends Fragment {

    final static String RecipeArgKey = "recipes";
    Recipe recipe;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        recipe = (Recipe)getArguments().getParcelable(RecipeArgKey);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_view, parent, false);
        getActivity().setTitle("Recipe details");
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView tvName, tvInstructions;
        tvName  = (TextView) view.findViewById(R.id.tvRecipeName);
        tvName.setText(recipe.getName());

        tvInstructions = (TextView) view.findViewById(R.id.tvInstructions);
        tvInstructions.setText(recipe.getInstructions());

        ImageView ivRecipeImage;
        ivRecipeImage = (ImageView) view.findViewById(R.id.ivRecipeImage);
        int id = getResources().getIdentifier(recipe.getName(),"drawable",getContext().getPackageName());
        ivRecipeImage.setImageResource(id);
    }
}
