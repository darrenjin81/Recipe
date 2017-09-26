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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Fragment {
    private FirebaseAuth mAuth;
    //FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        final EditText etUsername = (EditText) view.findViewById(R.id.etRegisterUsernameField);
        final EditText etEmail = (EditText) view.findViewById(R.id.etRegisterEmailField);
        final EditText etPassword = (EditText) view.findViewById(R.id.etRegisterPasswordField);
        Button btnCreate = (Button) view.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(etUsername.getText().toString(), etEmail.getText().toString(), etPassword.toString());
            }
        });
    }

    private void createAccount(final String username, final String email, String password) {
        if(username.isEmpty()){
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
            if (!password.matches("((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")){
                Toast.makeText(getActivity(), "Your password is not valid", Toast.LENGTH_LONG).show();
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

                        Fragment fragment = new UserProfile(mAuth.getCurrentUser().getUid());
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    }
                }
            });
        }
    }
}

