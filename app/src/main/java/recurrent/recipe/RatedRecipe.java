package recurrent.recipe;
/**
 * Created by Jin on 21/10/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class RatedRecipe implements Parcelable {

    private String ratedRecipe_id;
    private float rating;

    public RatedRecipe() {
    }

    public RatedRecipe(String ratedRecipe_id, float rating) {
        this.ratedRecipe_id = ratedRecipe_id;
        this.rating = rating;
    }

    private RatedRecipe(Parcel in) {
        ratedRecipe_id = in.readString();
        rating = in.readFloat();
    }

    //getters
    public String getRatedRecipe_id() {
        return ratedRecipe_id;
    }

    public float getRating() {
        return rating;
    }

    public int describeContents() {
        return 0;
    }

    public void updateRating(float newRating) {
        rating = newRating;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.ratedRecipe_id);
        out.writeFloat(this.rating);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<RatedRecipe> CREATOR = new Parcelable.Creator<RatedRecipe>() {
        public RatedRecipe createFromParcel(Parcel in) {
            return new RatedRecipe(in);
        }

        public RatedRecipe[] newArray(int size) {
            return new RatedRecipe[size];
        }
    };
}
