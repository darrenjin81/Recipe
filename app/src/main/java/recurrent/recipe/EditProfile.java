package recurrent.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by kitty on 13/10/2017.
 */

public class EditProfile extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mRef;
    private FirebaseUser curr_user;
    private String user_id;
    private String username;
    private String email;
    Button btnSaveDetails, btnChangePhoto, btnChangePwdPage;
    EditText etUsernameField, etEmailField;

    public EditProfile(){
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if(curr_user != null) {
            this.email = curr_user.getEmail();
            this.user_id = curr_user.getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //TODO:IS IT THE CORRECT WAY??
        getActivity().setTitle("Edit Profile");
        return inflater.inflate(R.layout.fragment_edit_profile, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Edit Profile");
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        btnSaveDetails = (Button) view.findViewById(R.id.btnSaveDetails);
        btnChangePwdPage = (Button) view.findViewById(R.id.btnChangePwdPage);
        etUsernameField = (EditText) view.findViewById(R.id.etUsernameField);
        etEmailField = (EditText) view.findViewById(R.id.etEmailField);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference();

        //retrieve logged in user's details
        mRef.child("users/" + user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User u = dataSnapshot.getValue(User.class);
                username = u.getUsername();
                etUsernameField.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        //display user email
        etEmailField.setText(email);

        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newUsername = etUsernameField.getText().toString();
                String newEmail = etEmailField.getText().toString();

                //update details in database
                if(!username.equals(newUsername)) {
                    if (newUsername.isEmpty()){
                        Toast.makeText(getActivity(), "Username cannot be blank!",
                                Toast
                                        .LENGTH_LONG).show();
                    } else {
                        updateDetails(newUsername);
                    }
                }

                if(!email.equals(newEmail)){
                    //TODO: Reauth users when update email
                    if (newEmail.isEmpty()){
                        Toast.makeText(getActivity(), "E-mail cannot be blank!",
                                Toast
                                        .LENGTH_LONG).show();
                    }else {
                        updateEmail(newEmail);
                    }

                }
            }
        });

        //transit to change password page
        btnChangePwdPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new ChangePassword();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });
    }

    private void updateDetails(String newUsername){
        mRef.child("users/" + user_id).child("username").setValue(newUsername);
        Toast.makeText(getActivity(), "Details successfully updated.",
                Toast
                        .LENGTH_LONG).show();
    }

    private void updateEmail(String newEmail){
        curr_user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "E-mail address successfully updated.",
                                    Toast
                                    .LENGTH_LONG).show();
                            Log.d(TAG, "User email address updated.");
                        } else {
                            Log.d(TAG, "ERROR");
                        }
                    }
                });
    }


}
