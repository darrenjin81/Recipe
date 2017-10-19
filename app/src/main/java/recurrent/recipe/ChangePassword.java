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

import static android.content.ContentValues.TAG;

/**
 * Created by kitty on 13/10/2017.
 */

public class ChangePassword extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser curr_user;
    private String user_id, email;
    Button btnChangePwd;
    EditText etChangeOldPwd, etChangeNewPwd, etChangeReNewPwd;
    TextView tvPasswordFormatTip;

    public ChangePassword() {
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if (curr_user != null) {
            this.email = curr_user.getEmail();
            this.user_id = curr_user.getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getActivity().setTitle("Change Password");
        return inflater.inflate(R.layout.fragment_change_password, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Change Password");
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        btnChangePwd = (Button) view.findViewById(R.id.btnChangePwd);
        etChangeOldPwd = (EditText) view.findViewById(R.id.etChangeOldPwd);
        etChangeNewPwd = (EditText) view.findViewById(R.id.etChangeNewPwd);
        etChangeReNewPwd = (EditText) view.findViewById(R.id.etChangeReNewPwd);
        tvPasswordFormatTip = (TextView) view.findViewById(R.id.etPasswordFormatTip);

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validatePwdForm(etChangeOldPwd.getText().toString(), etChangeNewPwd.getText().toString(), etChangeReNewPwd.getText().toString());
            }
        });

    }

    private void validatePwdForm(String oldPwd, final String newPwd, final String newPwd1) {
        if (oldPwd.isEmpty() || newPwd.isEmpty() || newPwd1.isEmpty()) {
            //check if any field is empty
            Toast.makeText(getActivity(), "Please fill in all fields!", Toast.LENGTH_LONG).show();
        } else if (newPwd.equals(oldPwd)) {
            //make sure new password is different to old password
            Toast.makeText(getActivity(), "Your new passwords should be different to your old " +
                    "password", Toast
                    .LENGTH_LONG).show();
        } else {
            //make sure old password matches record, MAY INCLUDE FORGOT PASSWORD OPTION LATER??
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd);
            curr_user.reauthenticate(credential).addOnCompleteListener
                    (new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");
                                if (!newPwd.equals(newPwd1)) {
                                    Toast.makeText(getActivity(), "Your new passwords should be the same",
                                            Toast.LENGTH_LONG).show();
                                } else if (!newPwd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=" +
                                        ".*[!#$%&'*+-/=?^_`{|}~])[A-Za-z\\d!#$%&'*+-/=?^_`{|}~]{6,20}$")) {
                                    Toast.makeText(getActivity(), "Make sure your password meets requirement", Toast.LENGTH_LONG).show();
                                } else {
                                    changePwd(newPwd);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Your old password does not match the " +
                                        "records", Toast
                                        .LENGTH_LONG).show();
                            }

                        }
                    });
        }
    }

    private void changePwd(String newPwd) {
        curr_user.updatePassword(newPwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            Toast.makeText(getActivity(), "Password updated.", Toast
                                    .LENGTH_LONG).show();
                            Fragment fragment = new UserProfile();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                        }
                    }
                });
    }
}
