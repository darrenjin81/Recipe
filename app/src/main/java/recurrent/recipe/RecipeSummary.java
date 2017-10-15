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

/**
 * Created by admin on 18/09/2017.
 *
 */

public class RecipeSummary extends Fragment implements View.OnTouchListener {

    private Recipe recipe;
    final private GestureDetector gestureDetector;

    public static String RecipeSummaryArgKey = "RecipeForSummary";

    public RecipeSummary(){
        gestureDetector = new GestureDetector(getActivity(), new GestureListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        recipe = (Recipe)getArguments().getParcelable(RecipeSummaryArgKey);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_recipe_summary, parent, false);
        TextView t = (TextView) v.findViewById(R.id.tvSummary_title);
        t.setText(recipe.getName());

        t = (TextView) v.findViewById(R.id.tvSummary_ingredients_num);
        t.setText("4"); //TODO fix

        t = (TextView) v.findViewById(R.id.tvSummary_preptime_num);
        t.setText("34H 92M"); //TODO fix

        t = (TextView) v.findViewById(R.id.tvSummary_cals_num);
        t.setText("499");//TODO fix


        ImageView image = (ImageView) v.findViewById(R.id.ivSummary_pic) ;
        image.setImageResource(getResources().getIdentifier(recipe.image,"drawable",getContext().getPackageName()));


        v.setOnTouchListener(this);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    //TODO I stole this is that ok...
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
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
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
        if(recipe == null){
            return;
        }
        Bundle args = new Bundle();
        args.putParcelable(RecipeView.RecipeArgKey, recipe);
        Fragment nextFrag= new RecipeView();
        nextFrag.setArguments(args);

        //TODO if we need browse, then its getParent().get()Parent()
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "FRAG_FEED")
                .addToBackStack(null)
                .commit();
    }

    public void onSwipeBottom() {
    }
}

