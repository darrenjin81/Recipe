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
import android.widget.ImageView;
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

import static android.content.ContentValues.TAG;

public class RecipeView extends Fragment {

    final static String RecipeArgKey = "recipes";
    Recipe recipe;
    private StorageReference mStorage;
    private FirebaseUser curr_user;
    private String user_id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        recipe = (Recipe)getArguments().getParcelable(RecipeArgKey);
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

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mStorage = FirebaseStorage.getInstance().getReference();

        TextView tvName, tvInstructions;
        tvName  = (TextView) view.findViewById(R.id.tvRecipeName);
        tvName.setText(recipe.getName());


        tvInstructions = (TextView) view.findViewById(R.id.tvInstructions);
        tvInstructions.setText(recipe.getInstructions());

//        StorageReference myImagePath = mStorage.child("jin.jpg");
//
////        final long ONE_MEGABYTE = 1024 * 1024;
////        myImagePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
////            @Override
////            public void onSuccess(byte[] bytes) {
////                // Data for "images/island.jpg" is returns, use this as needed
////                ImageView ivRecipeImage;
////                ivRecipeImage = (ImageView) getView().findViewById(R.id.ivRecipeImage);
////                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                ivRecipeImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, ivRecipeImage.getWidth(),
////                        ivRecipeImage.getHeight(), false));
////            }
////        });
//        FirebaseImageLoader
//        myImagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                ImageView ivRecipeImage;
//                ivRecipeImage = (ImageView) getView().findViewById(R.id.ivRecipeImage);
//                ivRecipeImage.setImageURI(uri);
//            }
//        });
        StorageReference myImagePath = mStorage.child("UploadedRecipes").child(user_id).child(recipe.getName() + ".jpg");
        ImageView ivRecipeImage;
        ivRecipeImage = (ImageView) getView().findViewById(R.id.ivRecipeImage);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(myImagePath)
                .into(ivRecipeImage);

    }
}
