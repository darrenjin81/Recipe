package recurrent.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadRecipes extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_upload_recipes, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        final EditText nameEditor = (EditText) view.findViewById(R.id.edit_recipe_name);
        final EditText instructionsEditor = (EditText) view.findViewById(R.id.edit_recipe_instructions);

        view.findViewById(R.id.button_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO i should make sure its not empty
                //firebase stuff
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                String name = nameEditor.getText().toString();
                String instructions = instructionsEditor.getText().toString();


                //write into database
                //writeNewUser(myRef, name, instructions);
                Recipe recipe = new Recipe(name, instructions);
                myRef.child("recipes").child(name).setValue(recipe);

                Bundle args = new Bundle();
                args.putParcelable(RecipeView.RecipeArgKey, recipe);
                Fragment nextFrag= new RecipeView();
                nextFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "r")
                        .addToBackStack(null)
                        .commit();

            }
        });
    }
}
