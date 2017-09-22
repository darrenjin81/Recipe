package recurrent.recipe;

/*
 * Recipe class which contains all information of a Recipe.
 *
 */

import android.graphics.drawable.Drawable;

public class Recipe{
    //every recipe will have its own id.
    static int id = 0;

    //name of user's recipe
    private String name;
    private String instructions;

    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    //getters
    public String getName() {
        return name;
    }
    public String getInstructions(){
        return instructions;
    }

}
