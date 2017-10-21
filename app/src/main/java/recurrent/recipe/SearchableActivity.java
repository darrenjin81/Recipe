package recurrent.recipe;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class SearchableActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PresentSearch(query);
        }
        //finish();
    }

    @Override
    public boolean onSearchRequested() {
        // pause some stuff etc. but i dont rly think you need this
        return super.onSearchRequested();
    }

    void PresentSearch(String query) {

    }
}
