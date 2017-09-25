package recurrent.recipe;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class UserProfile extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private String userId;
    Button btnSignOut;
    TextView tvUsername;

    public UserProfile(String userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
//        DatabaseReference myRef = mdatabase.getReference();
//
//        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for(DataSnapshot child: children){
//                    recipe = child.getValue(Recipe.class);
//                    TextView tvName, tvInstructions;
//                    tvName  = (TextView) view.findViewById(R.id.tvRecipeName);
//                    tvName.setText(recipe.getName());
//
//                    tvInstructions = (TextView) view.findViewById(R.id.tvInstructions);
//                    tvInstructions.setText(recipe.getInstructions());
//
//                    ImageView ivRecipeImage;
//                    ivRecipeImage = (ImageView) view.findViewById(R.id.ivRecipeImage);
//                    int id = getResources().getIdentifier(recipe.getName(),"drawable",getContext().getPackageName());
//                    ivRecipeImage.setImageResource(id);
//                }
//                //Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvUsername.setText("welcome user" + userId);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signout();
                Fragment fragment = new Login();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });
    }

    private void signout() {
        mAuth.signOut();
    }
}
