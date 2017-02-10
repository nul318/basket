package teamnova.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListView_nav_Adapter extends BaseAdapter {
    ArrayList<String> items = new ArrayList<String>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_nav_item, parent, false);
        }

        TextView folder = (TextView) convertView.findViewById(R.id.listview_nav_folder_title);
        folder.setText(items.get(position));

        return convertView;
    }

    public void addItem(String folder){
        items.add(folder);
    }
    public void clear(){
        items.clear();
    }
}
