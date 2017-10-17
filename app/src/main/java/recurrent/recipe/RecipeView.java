package recurrent.recipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RecipeView extends Fragment {

    final static String RecipeArgKey = "recipes";
    Recipe recipe;
    private FirebaseUser curr_user;
    private String user_id;
    private DatabaseReference mRef;
    private StorageReference mStorage;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        recipe = getArguments().getParcelable(RecipeArgKey);
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_view, parent, false);
        getActivity().setTitle("Recipe details");
        this.curr_user = FirebaseAuth.getInstance().getCurrentUser();
        if (curr_user != null) {
            this.user_id = curr_user.getUid();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe details");
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView imageStrip = (ImageView) view.findViewById(R.id.ivRecipeView);
        StorageReference storageImageRef = mStorage.child("UploadedRecipes").child(recipe.getKey()).child(recipe.getName() + ".jpg");
        Glide.with(this.getContext())
                .using(new FirebaseImageLoader())
                .load(storageImageRef)
                .into(imageStrip);

        TextView tvName, tvInstructions;
        tvName  = (TextView) view.findViewById(R.id.tvRecipeName);
        tvName.setText(recipe.getName());

        final ImageButton imgBtnBookmarkOn;
        final ImageButton imgBtnBookmarkOff;

        imgBtnBookmarkOn = (ImageButton) view.findViewById(R.id.imgBtnBookmarkOn);
        imgBtnBookmarkOff = (ImageButton) view.findViewById(R.id.imgBtnBookmarkOff);

        //handle situation when user is not logged in
        if (curr_user != null) {
            user_id = curr_user.getUid();

            //default
            imgBtnBookmarkOff.setVisibility(View.VISIBLE);
            imgBtnBookmarkOn.setVisibility(View.GONE);

            //check if recipes is bookmarked
            mRef.child("users/"+user_id+"/saved_recipes")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    Recipe r = child.getValue(Recipe.class);
                                    Log.d(TAG, "KEY1: "+ r.getKey());
                                    if (r.getKey().equals(recipe.getKey())) {
                                        //is bookmarked
                                        imgBtnBookmarkOff.setVisibility(View.GONE);
                                        imgBtnBookmarkOn.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            //no bookmark function when user is NOT logged in
            imgBtnBookmarkOff.setVisibility(View.GONE);
            imgBtnBookmarkOn.setVisibility(View.GONE);
        }

        imgBtnBookmarkOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //bookmark recipes
                bookmark(mRef, user_id, recipe);
                //change to bookmark on
                imgBtnBookmarkOff.setVisibility(View.GONE);
                imgBtnBookmarkOn.setVisibility(View.VISIBLE);

            }
        });

        imgBtnBookmarkOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //unmark recipes
                unmark(mRef, user_id, recipe.getKey());
                //change to bookmark off
                imgBtnBookmarkOff.setVisibility(View.VISIBLE);
                imgBtnBookmarkOn.setVisibility(View.GONE);
            }
        });

        ExpandableListView e = (ExpandableListView) view.findViewById(R.id.elvIngredients);
        MyListAdapter adaptor = new MyListAdapter(recipe.ingredientsForDisplay(), this.getContext());
        e.setAdapter(adaptor);

        e = (ExpandableListView) view.findViewById(R.id.elvMethod);
        adaptor = new MyListAdapter(recipe.methodForDisplay(), this.getContext());
        e.setAdapter(adaptor);

    }

    private void bookmark(DatabaseReference mRef, String user_id, Recipe recipe){
        mRef.child("users/"+user_id+"/saved_recipes").push().setValue(recipe);
    }

    private void unmark(final DatabaseReference mRef, final String user_id, final String rKey){
        //TODO: CHANGE WAY OF ACCESSING DATABASE LATER
        mRef.child("users/"+user_id+"/saved_recipes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for(DataSnapshot child: children){
                            Recipe r = child.getValue(Recipe.class);
                            if (r.getKey().equals(rKey)){
                                String recipe_key = child.getKey();
                                mRef.child("users/"+user_id+"/saved_recipes/"+recipe_key)
                                        .removeValue();
                                //debugging purpose
                                Log.d(TAG, "KEY: "+ recipe_key);
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}