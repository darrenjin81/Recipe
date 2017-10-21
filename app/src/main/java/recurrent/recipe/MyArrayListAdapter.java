package recurrent.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyArrayListAdapter extends ArrayAdapter<String> {

    final LayoutInflater inflater;
    ArrayList<String> data;

    public MyArrayListAdapter(Context context, int layoutid, ArrayList<String> data){
        //i dont need layout id... very dodgy
        super(context, layoutid, data);
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View v = inflater.inflate(R.layout.tv_list_view, parent, false);
        ((TextView)v.findViewById(R.id.tv_sequenceNum)).setText(Integer.toString(position + 1) + ".");
        ((TextView)v.findViewById(R.id.tv_list_row)).setText(data.get(position));
        ((TextView)v.findViewById(R.id.tv_list_row)).setText(data.get(position));
        android.support.v7.widget.AppCompatImageButton removeButton = (android.support.v7.widget.AppCompatImageButton)v.findViewById(R.id.removeFromList);
        removeButton.setTag(position);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                UploadRecipes.setListViewHeightBasedOnChildren((ListView)parent);
            }
        });

        return v;

    }
}
