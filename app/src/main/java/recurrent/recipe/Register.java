package recurrent.recipe;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Register extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getActivity().setTitle("Register");
        return inflater.inflate(R.layout.fragment_register, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        final EditText etUsername = (EditText) view.findViewById(R.id.etRegisterUsernameField);
        final EditText etEmail = (EditText) view.findViewById(R.id.etRegisterEmailField);
        Button btnCheckValid = (Button) view.findViewById(R.id.btnCheckValid);
        final EditText etPassword = (EditText) view.findViewById(R.id.etRegisterPasswordField);
        final EditText etPasswordCheck = (EditText) view.findViewById(R.id.etRegisterPasswordCheckField);
        final TextView tvPasswordFormatTip = (TextView) view.findViewById(R.id.etPasswordFormatTip);
        Button btnCreate = (Button) view.findViewById(R.id.btnCreate);

        btnCheckValid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: check if email is valid or not by going through database.
//                ArrayList<>recipes = new ArrayList<Recipe>();
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference();
//                myRef.child(Constants.RecipeTable).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                        for(DataSnapshot child: children){
//                            recipes.add(child.getValue(Recipe.class));
//                        }
//
//                        RecipeCollectionPagerAdapter.super.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                    }
//                });
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tvPasswordFormatTip.setVisibility(View.VISIBLE);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(etUsername.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etPasswordCheck.getText().toString());
            }
        });
    }

    private void createAccount(final String username, final String email, String password, String password1) {
        if(!password.equals(password1)){
            Toast.makeText(getActivity(), "your passwords should be the same", Toast.LENGTH_LONG).show();
        } else if(username.isEmpty()){
            Toast.makeText(getActivity(), "Your username cannot be empty", Toast.LENGTH_LONG).show();
        } else if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Your email address is empty", Toast.LENGTH_LONG).show();
        } else if (password.isEmpty()) {
            Toast.makeText(getActivity(), "Your password is empty", Toast.LENGTH_LONG).show();
        } else {
            if (!email.matches("^[\\w_]+@\\w+.\\w+$")) {
                Toast.makeText(getActivity(), "Your email address is not valid", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#$%&'*+-/=?^_`{|}~])[A-Za-z\\d!#$%&'*+-/=?^_`{|}~]{6,20}$")){
                Toast.makeText(getActivity(), "Make sure your password meets requirement", Toast.LENGTH_LONG).show();
                return;
            }
            mProgress.setMessage("signing up");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mRef.child(user_id);
                        current_user.child("username").setValue(username);
                        current_user.child("profile_pic").setValue("default");

                        mProgress.dismiss();

                        Fragment fragment = new UserProfile();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    }
                }
            });
        }
    }
}

