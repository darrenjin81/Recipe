package recurrent.recipe;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by admin on 21/09/2017.
 */

public class Factory {

    // Get a reference to our posts
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();

    public List<Recipe> loadTopN(int i){
        Query queryRef = ref.orderByChild("recipes").limitToFirst(i);
        DBResultListener result = new DBResultListener();
        queryRef.addListenerForSingleValueEvent(result);

        while (result.result == null){

        }

        return result.result;
    }
}

class DBResultListener<T> implements ValueEventListener{
    public List<Recipe> result;

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        Recipe r;
        Iterable<DataSnapshot> children = snapshot.getChildren();
        for(DataSnapshot child: children){
            result.add(child.getValue(Recipe.class));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //error
        Log.w("FireBase Error", "Failed to read value.", databaseError.toException());
    }
}
