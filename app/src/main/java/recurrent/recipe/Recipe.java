package recurrent.recipe;

/*
 * Recipe class which contains all information of a Recipe.
 *
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

class Recipe{
    //every recipe will have its own id.
    static int id = 0;

    //name of user's recipe
    private String name;
    private String instructions;
    //image of user's recipe - might be wrong
    private Drawable image;

    public Recipe(String name, String instructions, Drawable image) {
        this.name = name;
        this.instructions = instructions;
        this.image = image;
    }


    public String getName() {
        return name;
    }
    public String getInstructions(){
        return instructions;
    }
    public Drawable getImageDrawable() {
        return image;
    }

}
