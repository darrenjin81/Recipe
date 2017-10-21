package recurrent.recipe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(drawerToggle);

        // Display homepage fragment
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = Homepage.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PresentSearch(query);
        }
    }

    void PresentSearch(String query) {

        Bundle args = new Bundle();
        args.putString(BrowseRecipes.QueryArgKey, query);

        Fragment nextFrag = new BrowseRecipes();
        nextFrag.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.flContent, nextFrag, "FRAG_FEED")
                .addToBackStack(null)
                .commit();
    }

    public void setTitle(String name) {
        toolbar.setTitle(name);
    }

    public void setupDrawerContent(NavigationView navigationView) {
        //set up different drawer contents based on login status
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            navigationView.getMenu().setGroupVisible(R.id.group_loggedInItems, true);
            navigationView.getMenu().setGroupVisible(R.id.group_profile_page, true);
            navigationView.getMenu().setGroupVisible(R.id.group_login_page, false);
        } else {
            navigationView.getMenu().setGroupVisible(R.id.group_loggedInItems, false);
            navigationView.getMenu().setGroupVisible(R.id.group_profile_page, false);
            navigationView.getMenu().setGroupVisible(R.id.group_login_page, true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        Bundle args = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = Homepage.class;
                break;
            case R.id.nav_login:
                fragmentClass = Login.class;
                break;
            case R.id.nav_profile:
                fragmentClass = UserProfile.class;
                break;
            case R.id.nav_upload_recipes:
                fragmentClass = UploadRecipes.class;
                break;
            case R.id.nav_saved_recipes:
                fragmentClass = SavedRecipes.class;
                break;
            case R.id.browse_recipes:
                fragmentClass = BrowseRecipes.class;
                break;
            default:
                fragmentClass = Homepage.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_action, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQuery("", false);
        searchView.setIconified(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
