package teamnova.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListView_Adapter extends BaseAdapter {
    ArrayList<ListView_Item> items = new ArrayList<ListView_Item>();

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
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.Listview_title);
        TextView conten = (TextView) convertView.findViewById(R.id.Listview_conten);
//        ImageView image = (ImageView) convertView.findViewById(R.id.Listview_image);

        ListView_Item item = items.get(position);

        title.setText(item.getTitle());
        conten.setText(item.getConten());
//        image.setImageDrawable(image.getDrawable());

        return convertView;
    }

    public void addItem(String title, String conten){
        ListView_Item item = new ListView_Item();
        item.setTitle(title);
        item.setConten(conten);
        items.add(item);
    }
    public void removeItem(int position){
        items.remove(position);
    }
    public void clear(){
        items.clear();
    }
}
