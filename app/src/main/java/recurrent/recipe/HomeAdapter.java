package recurrent.recipe;

/**
 * Created by kitty on 22/9/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        //loop through featured_recipes list
        Recipe curr_recipe = (Recipe)featured_recipes.get(position);

        //create text view for each recipe name
        TextView tvName;
        tvName  = (TextView) v.findViewById(R.id.tvHomeRecipeName);
        tvName.setText(curr_recipe.getName());

        //create image view for each recipe
        ImageView ivImage;
        ivImage = (ImageView) v.findViewById(R.id.ivHomeRecipeImage);
        int id = getContext().getResources().getIdentifier(curr_recipe.getName(),"drawable",
                getContext().getPackageName());
        ivImage.setImageResource(id);

        return v;

    }

}