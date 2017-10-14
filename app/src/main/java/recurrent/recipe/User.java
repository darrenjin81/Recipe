package recurrent.recipe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kitty on 28/9/2017.
 */

public class User implements Parcelable {

    static int user_id = 0;
    private String username;

    public User(){

    }

    public User(String username) {
        this.username = username;
    }

    private User(Parcel in) {
        user_id = in.readInt();
        username = in.readString();
    }

    //getters
    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(this.user_id);
        out.writeString(this.username);
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
}
