package recurrent.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kitty on 28/9/2017.
 */

public class User implements Parcelable {

    private String username;
    private String emailAddress;
    private String unique_id;

    private ArrayList<RatedRecipe> ratedRecipes;

    public User() {
        this.ratedRecipes = new ArrayList<RatedRecipe>();
    }

    public User(String username, String emailAddress, String key) {

        this.username = username;
        this.emailAddress = emailAddress;
        this.unique_id = key;
        this.ratedRecipes = new ArrayList<RatedRecipe>();
    }

    private User(Parcel in) {
        unique_id = in.readString();
        username = in.readString();
        emailAddress = in.readString();
        ratedRecipes = in.readArrayList(null);
    }

    //getters
    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public ArrayList<RatedRecipe> getRatedRecipes() {
        return ratedRecipes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.unique_id);
        out.writeString(this.emailAddress);
        out.writeString(this.username);
        out.writeList(this.ratedRecipes);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void addRatedRecipe(RatedRecipe key) {
        ratedRecipes.add(key);
    }
}
