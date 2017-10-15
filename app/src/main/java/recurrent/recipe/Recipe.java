package recurrent.recipe;

/*
 * Recipe class which contains all information of a Recipe.
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

class Recipe implements Parcelable {
    //every recipe will have its own id.
    static int id = 0;

    //name of user's recipe
    private String name;
    private String instructions;
    private ArrayList<String> instructionsSteps;
    private ArrayList<String> ingredients;

    public String image = "macaroons"; //TODO fix



    public Recipe(){
        instructionsSteps = new ArrayList<String>();
        ingredients = new ArrayList<String>();

        instructionsSteps.add("cut blah blah");
        instructionsSteps.add("cook blah blah etc");
        instructionsSteps.add("something somtinhgssad");

        ingredients.add("tomato");
        ingredients.add("watermelon");
    }

    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
        instructionsSteps = new ArrayList<String>();
        ingredients = new ArrayList<String>();

        instructionsSteps.add("cut blah blah");
        instructionsSteps.add("cook blah blah etc");
        instructionsSteps.add("something somtinhgssad");

        ingredients.add("tomato");
        ingredients.add("watermelon");
    }

    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        instructions = in.readString();
    }

    //getters
    public String getName() {
        return name;
    }
    public String getInstructions(){
        return instructions;
    }
    public ArrayList<String> getInstructionsSteps() {
        return instructionsSteps;
    }
    public ArrayList<String> getIngredients() {
        return ingredients;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(this.id);
        out.writeString(this.name);
        out.writeString(this.instructions);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public ArrayList<HeaderInfo> toDisplayformat() {
        ArrayList<HeaderInfo> results = new ArrayList<HeaderInfo>();

        HeaderInfo ingredients = new HeaderInfo("Ingredients");
        Integer i = 1;
        for(String s : this.ingredients){
            ingredients.addChild(new DetailInfo(i.toString(), s));
            i++;
        }

        HeaderInfo method = new HeaderInfo("Method");
        i = 1;
        for(String s : this.instructionsSteps){
            method.addChild(new DetailInfo(i.toString(), s));
            i++;
        }

        results.add(ingredients);
        results.add(method);
        return results;
    }
}

