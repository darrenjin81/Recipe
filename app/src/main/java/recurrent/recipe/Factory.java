package recurrent.recipe;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 21/09/2017.
 */

public class Factory {

    // Get a reference to our posts
    final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference ref = database.getReference();

    public List<Recipe> loadTopN(int n){







        Query queryRef = ref.orderByChild("recipes").limitToFirst(n);
        DBResult result = new DBResult(queryRef);
        int retry = 3;

        for(int i = 0; i < retry; i++){
            if(result.result != null){
                return result.result;
            }else{
                //failed, try again after timeout
                try {
                    Thread.sleep(1000);
                }catch(Exception e){
                    Log.w("Hell", "no sleep wake up", e);
                }
            }
        }

        return result.result;
    }
}

class DBResult{
    public List<Recipe> result;

    public DBResult(Query mRef){

        result = new ArrayList<Recipe>();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.w("FireBase", "Onchange is called yay.");

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
        });
    }
}
