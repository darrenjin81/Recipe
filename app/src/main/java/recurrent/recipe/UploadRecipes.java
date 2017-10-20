package recurrent.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class UploadRecipes extends Fragment {
    private static final int SELECT_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    StorageReference imageRef = storageRef.child("images");
    private FirebaseDatabase mdatabase;
    private DatabaseReference mRef;
    private FirebaseUser curr_user;
    private StorageReference mStorage;
    private String user_id;
    private String user_name;

    private EditText nameEditor;
    private EditText instructionsEditor;
    private ImageView ivUploadImage;
    private EditText etIngredient;
    private Button btnAddInstructionsStep;
    private TextView tvInstructionsTip;
    private Button btnAddIngredient;
    private TextView tvIngredientTip;
    private Button btnAcitivateCamera;
    private Button btnUploadImage;
    private Button btnUpload;
    private EditText etCookingTime;
    private Button btnDessert;
    private Button btnSushi;
    private Button btnEntree;
    private Button btnBreakfast;

    private ProgressDialog mProgressDialog;

    private Uri imageUri;
    private byte[] cameraData;
    private String myCurrentPhotoPath;

    Recipe recipe;
    String category = "";
    String addedIngredients = "";
    String addedInstuctions = "";
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> instructionSteps = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if (curr_user != null) {
            this.user_id = curr_user.getUid();
        }
        return inflater.inflate(R.layout.fragment_upload_recipes, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        nameEditor = (EditText) getView().findViewById(R.id.upload_recipe_name);
        instructionsEditor = (EditText) getView().findViewById(R.id.upload_recipe_instructions);
        btnAddInstructionsStep = (Button) getView().findViewById(R.id.btnAddInstructionsStep);
        tvInstructionsTip = (TextView) getView().findViewById(R.id.tv_tip_added_instruction);
        ivUploadImage = (ImageView) getView().findViewById((R.id.upload_recipe_image));
        etIngredient = (EditText) getView().findViewById(R.id.upload_recipe_ingredients);
        btnAddIngredient = (Button) getView().findViewById(R.id.btnAddIngredient);
        tvIngredientTip = (TextView) getView().findViewById(R.id.tv_tip_added_ingredient);
        btnAcitivateCamera = (Button) getView().findViewById(R.id.btnActivateCamera);
        btnUploadImage = (Button) getView().findViewById(R.id.btnUploadImage);
        btnUpload = (Button) getView().findViewById(R.id.button_upload);
        etCookingTime = (EditText) getView().findViewById(R.id.et_pick_time);
        btnDessert = (Button) getView().findViewById(R.id.btn_dessert);
        btnBreakfast = (Button) getView().findViewById(R.id.btn_breakfast);
        btnSushi = (Button) getView().findViewById(R.id.btn_sushi);
        btnEntree = (Button) getView().findViewById(R.id.btn_entree);

        final RelativeLayout tag_part = (RelativeLayout) view.findViewById(R.id.tag_part);
        final ImageButton btnAddTag = (ImageButton) view.findViewById(R.id.ib_addTag);
        final EditText etAddTag = (EditText) view.findViewById(R.id.et_addTag) ;
        final GridLayout existingTags = (GridLayout) view.findViewById(R.id.existingTags);


        btnDessert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.YELLOW);
                btnSushi.setTextColor(Color.BLACK);
                btnBreakfast.setTextColor(Color.BLACK);
                btnEntree.setTextColor(Color.BLACK);
                category = "#dessert";
            }
        });
        btnBreakfast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.BLACK);
                btnSushi.setTextColor(Color.BLACK);
                btnBreakfast.setTextColor(Color.YELLOW);
                btnEntree.setTextColor(Color.BLACK);
                category = "#breakfast";
            }
        });
        btnSushi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.BLACK);
                btnSushi.setTextColor(Color.YELLOW);
                btnBreakfast.setTextColor(Color.BLACK);
                btnEntree.setTextColor(Color.BLACK);
                category = "#sushi";
            }
        });
        btnEntree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.BLACK);
                btnSushi.setTextColor(Color.BLACK);
                btnBreakfast.setTextColor(Color.BLACK);
                btnEntree.setTextColor(Color.YELLOW);
                category = "#entree";
            }
        });

        nameEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag_part.setVisibility(View.VISIBLE);
                existingTags.setVisibility(View.VISIBLE);
            }
        });

        btnAddTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                etAddTag.setVisibility(View.VISIBLE);
                existingTags.setVisibility(View.GONE);
            }
        });

        mRef.child("users/" + user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User u = dataSnapshot.getValue(User.class);
                user_name = u.getUsername();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        btnAddInstructionsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instruction = instructionsEditor.getText().toString();
                instructionSteps.add(instruction);
                instructionsEditor.setText("");
                addedInstuctions = addedInstuctions.concat(instruction + " has been successfully added...\n");
                tvInstructionsTip.setText(addedInstuctions);
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = etIngredient.getText().toString();
                ingredients.add(ingredient);
                etIngredient.setText("");
                addedIngredients = addedIngredients.concat(ingredient + " has been successfully added...\n");
                tvIngredientTip.setText(addedIngredients);
            }
        });

        btnAcitivateCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnCamera();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                String name = nameEditor.getText().toString();
                String instructions = instructionsEditor.getText().toString();
                String cookingTime = etCookingTime.getText().toString();

                String emptyTitleError = "Please enter a title.";
                if (name.isEmpty()) {
                    Toast t = Toast.makeText(getContext(), emptyTitleError, Toast.LENGTH_SHORT);
                    t.show();
                }


                if (!name.isEmpty()) {
                    //write into database
                    mProgressDialog.setMessage("being added...");
                    mProgressDialog.show();

                    if(!etAddTag.getText().toString().isEmpty()){
                        recipe = new Recipe(name, user_id, etAddTag.getText().toString(), cookingTime, instructionSteps, ingredients);
                    }else {
                        recipe = new Recipe(name, user_id, category, cookingTime, instructionSteps, ingredients);
                    }
                    String key = myRef.child("recipes").push().getKey();
                    recipe.setKey(key);
                    myRef.child("recipes").child(key).setValue(recipe);

                    StorageReference filepath = mStorage.child("UploadedRecipes").
                            child(key).child(name + ".jpg");
                    if (imageUri != null) {
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "upload done...", Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                                Bundle args = new Bundle();
                                args.putParcelable(RecipeView.RecipeArgKey, recipe);
                                Fragment nextFrag = new RecipeView();
                                nextFrag.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(((ViewGroup) getView().getParent()).getId(), nextFrag, "r")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    } else if (cameraData != null) {
                        filepath.putBytes(cameraData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "upload done...", Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                                Bundle args = new Bundle();
                                args.putParcelable(RecipeView.RecipeArgKey, recipe);
                                Fragment nextFrag = new RecipeView();
                                nextFrag.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(((ViewGroup) getView().getParent()).getId(), nextFrag, "r")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    } else {
                        mProgressDialog.dismiss();
                        Bundle args = new Bundle();
                        args.putParcelable(RecipeView.RecipeArgKey, recipe);
                        Fragment nextFrag = new RecipeView();
                        nextFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(((ViewGroup) getView().getParent()).getId(), nextFrag, "r")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Upload Recipe");
    }

    private void turnOnCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ivUploadImage.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    cameraData = bytes.toByteArray();
                }
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        ivUploadImage.setImageURI(imageUri);
                    }
                }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
    }
}
