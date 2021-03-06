package recurrent.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

public class RecipeSummary extends Fragment implements View.OnTouchListener {

    private Recipe recipe;
    final private GestureDetector gestureDetector;

    public static String RecipeSummaryArgKey = "RecipeForSummary";

    public RecipeSummary() {
        gestureDetector = new GestureDetector(getActivity(), new GestureListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getArguments().getParcelable(RecipeSummaryArgKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_summary, parent, false);
        TextView t = (TextView) v.findViewById(R.id.tvSummary_title);
        t.setText(recipe.getName());

        t = (TextView) v.findViewById(R.id.tvSummary_preptime_num);
        t.setText(recipe.getCookingTime()+ " mins");

        t = (TextView) v.findViewById(R.id.tvSummary_ingredients_num);
        t.setText(recipe.getIngredients().size() + "");

        t = (TextView) v.findViewById(R.id.tvSummary_cals_num);
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        if(recipe.getNum_of_rating() == 0){
            t.setText("review me");
        }else{
            t.setText(twoDForm.format(recipe.getRating()/recipe.getNum_of_rating()) + " / 5.0");
        }


        ImageView image = (ImageView) v.findViewById(R.id.ivSummary_pic);
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference mRef = mStorage.child("UploadedRecipes")
                .child(recipe.getKey()).child(recipe.getName() + ".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mRef)
                .into(image);


        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(recipe.getName());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
        //transition to recipe view
        if (recipe == null) {
            return;
        }
        Bundle args = new Bundle();
        args.putParcelable(RecipeView.RecipeArgKey, recipe);
        Fragment nextFrag = new RecipeView();
        nextFrag.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, nextFrag, "FRAG_FEED")
                .addToBackStack(null)
                .commit();
    }

    public void onSwipeBottom() {
    }
}

