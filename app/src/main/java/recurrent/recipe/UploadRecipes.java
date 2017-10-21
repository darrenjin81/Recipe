package recurrent.recipe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class UploadRecipes extends Fragment {
    private static final int SELECT_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseDatabase mdatabase;
    private DatabaseReference mRef;
    private FirebaseUser curr_user;
    private StorageReference mStorage;
    private String user_id;
    private String user_name;

    private EditText nameEditor;
    private EditText instructionsEditor;
    private EditText etIngredient;
    private android.support.v7.widget.AppCompatImageButton btnAddInstructionsStep;
    private android.support.v7.widget.AppCompatImageButton btnAddIngredient;
    private android.support.v7.widget.AppCompatImageButton btnUploadImage;
    private Button btnUpload;
    private EditText etCookingTime;
    private Button btnDessert;
    private Button btnSushi;
    private Button btnEntree;
    private Button btnBreakfast;

    private ListView lvIngredients;
    private ListView lvInstructions;

    private ProgressDialog mProgressDialog;

    private Uri imageUri;
    private byte[] cameraData;
    private String myCurrentPhotoPath;

    Recipe recipe;
    String category = "";
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> instructionSteps = new ArrayList<>();

    ArrayAdapter<String> ingredientsAdapter;
    ArrayAdapter<String> instructionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsAdapter = new MyArrayListAdapter(this.getContext(), R.layout.tv_list_view, ingredients);
        instructionAdapter = new MyArrayListAdapter(this.getContext(), R.layout.tv_list_view, instructionSteps);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if (curr_user != null) {
            this.user_id = curr_user.getUid();
        }
        return inflater.inflate(R.layout.fragment_upload_recipes, parent, false);
    }

    public void addIngredient(View v){
        if(etIngredient.getText().toString().trim().isEmpty()) return;
        ingredients.add(etIngredient.getText().toString());
        ingredientsAdapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        etIngredient.setText("");
        etIngredient.clearFocus();
        setListViewHeightBasedOnChildren(lvIngredients);
    }

    public void addInstruction(View v){
        if(instructionsEditor.getText().toString().trim().isEmpty()) return;
        instructionSteps.add(instructionsEditor.getText().toString());
        instructionAdapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        instructionsEditor.setText("");
        instructionsEditor.clearFocus();
        setListViewHeightBasedOnChildren(lvInstructions);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());

        nameEditor = (EditText) view.findViewById(R.id.upload_recipe_name);
        instructionsEditor = (EditText) view.findViewById(R.id.upload_recipe_instructions);
        btnAddInstructionsStep = (android.support.v7.widget.AppCompatImageButton) view.findViewById(R.id.btnAddInstructionsStep);
        etIngredient = (EditText) view.findViewById(R.id.upload_recipe_ingredients);
        btnAddIngredient = (android.support.v7.widget.AppCompatImageButton) view.findViewById(R.id.btnAddIngredient);
        btnUploadImage = (android.support.v7.widget.AppCompatImageButton) view.findViewById(R.id.btnUploadImage);
        btnUpload = (Button) view.findViewById(R.id.button_upload);

        lvIngredients = (ListView) getView().findViewById(R.id.ingredients_list);
        lvInstructions = (ListView) getView().findViewById(R.id.instructions_list);

        lvIngredients.setAdapter(ingredientsAdapter);
        lvInstructions.setAdapter(instructionAdapter);

        etCookingTime = (EditText) getView().findViewById(R.id.et_pick_time);
        btnDessert = (Button) getView().findViewById(R.id.btn_dessert);
        btnBreakfast = (Button) getView().findViewById(R.id.btn_breakfast);
        btnSushi = (Button) getView().findViewById(R.id.btn_sushi);
        btnEntree = (Button) getView().findViewById(R.id.btn_entree);

        final RelativeLayout tag_part = (RelativeLayout) view.findViewById(R.id.tag_part);
        final GridLayout existingTags = (GridLayout) view.findViewById(R.id.existingTags);


        btnDessert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.WHITE);
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
                btnBreakfast.setTextColor(Color.WHITE);
                btnEntree.setTextColor(Color.BLACK);
                category = "#breakfast";
            }
        });
        btnSushi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnDessert.setTextColor(Color.BLACK);
                btnSushi.setTextColor(Color.WHITE);
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
                btnEntree.setTextColor(Color.WHITE);
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

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient(v);
            }
        });

        btnAddInstructionsStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInstruction(v);
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

                    recipe = new Recipe(name, user_id, category, cookingTime, instructionSteps, ingredients);
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

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView
     * should rly be in a utility class****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        btnUploadImage.setImageURI(imageUri);
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
