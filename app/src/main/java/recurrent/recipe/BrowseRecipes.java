package recurrent.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import recurrent.recipe.R;
import recurrent.recipe.RecipeSummary;

public class BrowseRecipes extends Fragment {

    RecipeCollectionPagerAdapter mRecipeCollectionPagerAdapter;
    ViewPager mViewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_browse_recipes, parent, false);

//        getActivity().setContentView(R.layout.fragment_browse_recipes);
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mRecipeCollectionPagerAdapter = new RecipeCollectionPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mRecipeCollectionPagerAdapter);

        return v;
    }

}
