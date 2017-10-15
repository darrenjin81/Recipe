package recurrent.recipe;

import android.app.SearchManager;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

/**
 * Created by admin on 7/10/2017.
 */

public class MySearchView extends SearchView {

    public MySearchView(Context context) {
        super(context);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the options menu from XML
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
//
//        return true;
//    }
}
