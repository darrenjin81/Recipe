package recurrent.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends BaseExpandableListAdapter {

    private ArrayList<HeaderInfo> list;
    private Context context;

    public MyListAdapter(ArrayList<HeaderInfo> list, Context c) {
        this.list = list;
        this.context = c;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getChild(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        View v;
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.groupheading, null);

        TextView heading = (TextView) v.findViewById(R.id.heading);
        heading.setText(headerInfo.getName());

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
        View v;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.listrow, null);

        TextView sequence = (TextView) v.findViewById(R.id.sequence);
        sequence.setText(detailInfo.getSequence() + ". ");
        TextView childItem = (TextView) v.findViewById(R.id.row);
        childItem.setText(detailInfo.getName());

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
