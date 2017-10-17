package recurrent.recipe;

import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private DatabaseReference mRef;
    private FirebaseUser curr_user;
    private String user_id;
    private String username;
    //private String email;
    Button btnSignOut, btnEditDetails, btnMyRecipes, btnSavedRecipes;
    TextView tvUsername;


    public UserProfile(){
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if(curr_user != null) {
            this.user_id = curr_user.getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //TODO:IS IT THE RIGHT WAY TO SET TITLE???
        getActivity().setTitle("My Profile");
        //update drawer content
        NavigationView nvDrawer = (NavigationView) getActivity().findViewById(R.id.nvView);
        ((MainActivity) getActivity()).setupDrawerContent(nvDrawer);
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        btnEditDetails = (Button) view.findViewById(R.id.btnEditDetails);
        btnMyRecipes = (Button) view.findViewById(R.id.btnMyRecipes);
        btnSavedRecipes = (Button) view.findViewById(R.id.btnSavedRecipes);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        //etUsernameField = (EditText) view.findViewById(R.id.etUsernameField);
        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference();

        tvUsername.setText(username);
        //final KeyListener mKeyListener = etUsernameField.getKeyListener();
        //etUsernameField.setKeyListener(null);
        //btnSaveDetails.setVisibility(view.GONE);

        // retrieve to logged in user's details
        mRef.child("users/"+user_id).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User u = dataSnapshot.getValue(User.class);
                username = u.getUsername();
                tvUsername.setText(username);
           }

           @Override
            public void onCancelled(DatabaseError error) {
              // Failed to read value
            }
      });

        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new EditProfile();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signout();
                Fragment fragment = new Login();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        btnSavedRecipes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new SavedRecipes();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });
    }

    private void signout() {
        mAuth.signOut();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Profile");
    }

}
