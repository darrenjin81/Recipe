package recurrent.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BrowseRecipes extends Fragment {

    RecipeCollectionPagerAdapter mRecipeCollectionPagerAdapter;
    ViewPager mViewPager;
    final static public String QueryArgKey = "QUERY";
    String query = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString(QueryArgKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_browse_recipes, parent, false);

        mRecipeCollectionPagerAdapter = new RecipeCollectionPagerAdapter(getActivity().getSupportFragmentManager(), query);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mRecipeCollectionPagerAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Browse Recipes");
    }
}
