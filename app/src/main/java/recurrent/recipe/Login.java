package recurrent.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private ProgressDialog mProgress;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        //check is user has logged in or not
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(getActivity(), user_mainpage.class));
                }
            }
        };

        // Defines the xml file for the fragment
        final View view =  inflater.inflate(R.layout.fragment_login, parent, false);

        final EditText etUsername = (EditText) view.findViewById(R.id.etUsernameField);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPasswordField);

        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //createAccount(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                TextView etRegister = (TextView) view.findViewById(R.id.tvTestRegister);
                etRegister.setText("enter in register");
                Intent userpage = new Intent(getActivity(), user_mainpage.class);
                userpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(userpage);
            }
        });
        final Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(etUsername.getText().toString(), etPassword.getText().toString());

            }
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn(String email, String password) {
        //TODO: check if email and password are valid or not.
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getActivity(), "empty email or empty password", Toast.LENGTH_LONG).show();
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Toast.makeText(getActivity(), "sign in problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void createAccount(final String email, String password) {
        if(email.isEmpty() || password.isEmpty()){
            //Toast.makeText(getActivity(), "empty email or empty password", Toast.LENGTH_LONG).show();
        }else {

            mProgress.setMessage("signing up");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mRef.child(user_id);
                        current_user.child("name").setValue(email);
                        current_user.child("image").setValue("default");

                        mProgress.dismiss();
                    }
                }
            });
        }
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }


}
