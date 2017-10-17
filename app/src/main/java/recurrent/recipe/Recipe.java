package recurrent.recipe;

/*
 * Recipe class which contains all information of a Recipe.
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

class Recipe implements Parcelable {
    //every recipe will have its own id.
    private String unique_id;
    private String owner_id;

    //name of user's recipe
    private String name;
    private ArrayList<String> instructionsSteps;
    private ArrayList<String> ingredients;

    //public String image = "marcoons";

    public Recipe(){
        instructionsSteps = new ArrayList<String>();
        ingredients = new ArrayList<String>();
    }

    public Recipe(String name, String owner, ArrayList<String> instructionSteps, ArrayList<String> ingredients) {
        this.unique_id = "";
        this.owner_id = owner;
        this.name = name;
        this.instructionsSteps = instructionSteps;
        this.ingredients = ingredients;
    }

    private Recipe(Parcel in) {
        unique_id = in.readString();
        name = in.readString();
        owner_id = in.readString();
        instructionsSteps = in.readArrayList(null);
        ingredients = in.readArrayList(null);
    }

    //getters
    public String getOwner_id() { return owner_id; }
    public String getName() {
        return name;
    }
    public ArrayList<String> getInstructionsSteps() {
        return instructionsSteps;
    }
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    public String getKey(){
        return unique_id;
    }

    public void setKey(String id){
        unique_id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(this.unique_id);
        out.writeString(this.name);
        out.writeString(this.owner_id);
        out.writeList(this.instructionsSteps);
        out.writeList(this.ingredients);
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

    public ArrayList<HeaderInfo> ingredientsForDisplay() {
        ArrayList<HeaderInfo> results = new ArrayList<HeaderInfo>();

        HeaderInfo ingredients = new HeaderInfo("Ingredients");
        Integer i = 1;
        for(String s : this.ingredients){
            ingredients.addChild(new DetailInfo(i.toString(), s));
            i++;
        }

        results.add(ingredients);
        return results;
    }

    public ArrayList<HeaderInfo> methodForDisplay() {
        ArrayList<HeaderInfo> results = new ArrayList<HeaderInfo>();

        HeaderInfo method = new HeaderInfo("Method");
        Integer i = 1;
        for(String s : this.instructionsSteps){
            method.addChild(new DetailInfo(i.toString(), s));
            i++;
        }

        results.add(method);
        return results;
    }
}

