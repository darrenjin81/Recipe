package recurrent.recipe;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by admin on 21/09/2017.
 */

public class Factory {

    // Get a reference to our posts
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();


    public Factory() {

    }

    public Recipe loadPK(){
        Query queryRef = ref.orderByChild("recipes").limitToFirst(1);
        DBResultListener result = new DBResultListener();
        queryRef.addListenerForSingleValueEvent(result);
        return (Recipe) result.result;
    }
}

class DBResultListener implements ValueEventListener{
    public Object result;

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        result = snapshot.getValue(Recipe.class);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //error
    }
}
