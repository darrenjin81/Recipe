package recurrent.recipe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends Fragment {
    private FirebaseAuth mAuth;
    //FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private ProgressDialog mProgress;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, parent, false);
        getActivity().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        etEmail = (EditText) view.findViewById(R.id.etLoginEmailField);
        etPassword = (EditText) view.findViewById(R.id.etLoginPasswordField);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new Register();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Login");
    }

    @Override
    public void onStart() {
        super.onStart();
        //update drawer content
        NavigationView nvDrawer = (NavigationView) getActivity().findViewById(R.id.nvView);
        ((MainActivity) getActivity()).setupDrawerContent(nvDrawer);
        if (mAuth.getCurrentUser() != null){
            Fragment fragment = new UserProfile();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
    }

    private void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "empty email or empty password", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //System.out.println(mAuth.getCurrentUser().getDisplayName());
                            //Toast.makeText(getActivity(), "sign in problem", Toast.LENGTH_LONG).show();
                            //String user_id = mAuth.getCurrentUser().getUid();
                            //FirebaseUser usr = mAuth.getCurrentUser();
                            //DatabaseReference current_user = mRef.child(user_id);
                            //current_user.child("name").setValue(usr.getEmail());
                            //current_user.child("image").setValue("default");
                            Fragment fragment = new UserProfile();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        }else{
                            Toast.makeText(getActivity(), "sign in problem", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
    }
}
