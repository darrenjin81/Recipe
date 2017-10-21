package recurrent.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by kitty on 13/10/2017.
 */

public class EditProfile extends Fragment {
    private static final int SELECT_IMAGE = 1;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mRef;
    private FirebaseUser curr_user;
    private String user_id;
    private String username;
    private String email;
    private Uri imageUri;
    private User user;

    Button btnSaveDetails, btnChangePhoto, btnChangePwdPage;
    EditText etUsernameField, etEmailField;
    ImageView ivProfilePic;

    public EditProfile() {
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if (curr_user != null) {
            this.email = curr_user.getEmail();
            this.user_id = curr_user.getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
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
        btnChangePhoto = (Button) view.findViewById(R.id.btnChangePhoto);
        etUsernameField = (EditText) view.findViewById(R.id.etUsernameField);
        etEmailField = (EditText) view.findViewById(R.id.etEmailField);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference();
        mProgressDialog = new ProgressDialog(getActivity());


        //retrieve logged in user's details
        mRef.child("users/" + user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.getValue(User.class);
                username = user.getUsername();
                etUsernameField.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference mRef = mStorage.child("userDp").child(user_id).child("dp.jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.mipmap.ic_launcher)
                .into(ivProfilePic);

        //display user email
        etEmailField.setText(email);

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDp();
            }
        });

        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newUsername = etUsernameField.getText().toString();
                String newEmail = etEmailField.getText().toString();

                //update details in database
                if (!username.equals(newUsername)) {
                    if (newUsername.isEmpty()) {
                        Toast.makeText(getActivity(), "Username cannot be blank!",
                                Toast
                                        .LENGTH_LONG).show();
                    } else {
                        updateDetails(newUsername);
                        Toast.makeText(getActivity(), "Username Updated!",
                                Toast
                                        .LENGTH_LONG).show();
                    }
                }

                if (!email.equals(newEmail)) {
                    if (newEmail.isEmpty()) {
                        Toast.makeText(getActivity(), "E-mail cannot be blank!",
                                Toast
                                        .LENGTH_LONG).show();
                    } else {
                        updateEmail(newEmail);
                        Toast.makeText(getActivity(), "Email Updated!",
                                Toast
                                        .LENGTH_LONG).show();
                    }
                }

                StorageReference filepath = FirebaseStorage.getInstance().getReference().child("userDp").
                        child(user_id).child("dp.jpg");
                // Delete the file
                filepath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });
                if (imageUri != null) {
                    mProgressDialog.setMessage("being added...");
                    mProgressDialog.show();
                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "upload done...", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });

        mStorage = FirebaseStorage.getInstance().getReference();
        mRef = mStorage.child("userDp").child(user_id).child("dp.jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mRef)
                .error(R.drawable.profile_icon)
                .into(ivProfilePic);

        //transit to change password page
        btnChangePwdPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new ChangePassword();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            }
        });
    }

    private void saveDp() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        ivProfilePic.setImageURI(imageUri);
                    }
                }
        }
    }

    private void updateDetails(String newUsername) {
        mRef.child("users/" + user_id).child("username").setValue(newUsername);
    }

    private void updateEmail(String newEmail) {
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
        mRef.child("users/" + user_id).child("emailAddress").setValue(newEmail);
    }
}
