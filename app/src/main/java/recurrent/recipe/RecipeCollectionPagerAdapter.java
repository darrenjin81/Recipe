package recurrent.recipe;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import recurrent.recipe.RecipeSummary;

public class RecipeCollectionPagerAdapter extends FragmentStatePagerAdapter {

    public RecipeCollectionPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){

        RecipeSummary f = new RecipeSummary();
        f.i = i;
        return f;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}

