package recurrent.recipe;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

/**
 * Created by admin on 7/10/2017.
 */

public class SearchableActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PresentSearch(query);
        }
        finish();
    }

    @Override
    public boolean onSearchRequested() {
        // pause some stuff etc. but i dont rly think you need this
        return super.onSearchRequested();
    }

    void PresentSearch(String query){

        //do some stuff
        //firebase firebase etc
        //lets just send it to browse

//        Fragment nextFrag= new BrowseRecipes();
//        FragmentManager fm = this.getSupportFragmentManager();
//        fm.beginTransaction()
//                .replace(fm.findFragmentByTag("FRAG_FEED").getId(), nextFrag, "FRAG_FEED")
//                .addToBackStack(null)
//                .commit();

    }

}
