package recurrent.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter {

    private ArrayList<Recipe> featured_recipes = new ArrayList<>();

    public HomeAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        featured_recipes = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();
        //loop through featured_recipes list
        Recipe curr_recipe = (Recipe) featured_recipes.get(position);

        //create image view for each recipe
        ImageView ivImage;
        ivImage = (ImageView) v.findViewById(R.id.ivHomeRecipeImage);

        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference mRef = mStorage.child("UploadedRecipes")
                .child(curr_recipe.getKey()).child(curr_recipe.getName() + ".jpg");
        Glide.with(this.getContext())
                .using(new FirebaseImageLoader())
                .load(mRef)
                .into(ivImage);

        //create text view for each recipe name
        final TextView tvName, tvUploadingUser;
        tvName = (TextView) v.findViewById(R.id.tvHomeRecipeName);
        tvName.setText(curr_recipe.getName());
        tvUploadingUser = (TextView) v.findViewById(R.id.tvUploadingUser);

        String owner_id = curr_recipe.getOwner_id();
        if (owner_id != null) {
            // retrieve owner details
            dRef.child("users/" + owner_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User u = dataSnapshot.getValue(User.class);
                    String username = u.getUsername();
                    tvUploadingUser.setText("By " + username);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }

        return v;

    }

}