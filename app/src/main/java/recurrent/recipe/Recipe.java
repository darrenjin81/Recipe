package recurrent.recipe;

/*
 * Recipe class which contains all information of a Recipe.
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

class Recipe implements Parcelable {
    //every recipe will have its own id.
    static int id = 0;

    //name of user's recipe
    private String name;
    private String instructions;

    public Recipe(){

    }

    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
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
}
