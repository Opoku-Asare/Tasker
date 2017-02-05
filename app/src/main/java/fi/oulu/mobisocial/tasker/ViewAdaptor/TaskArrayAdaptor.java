package fi.oulu.mobisocial.tasker.ViewAdaptor;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by opoku on 02-Feb-17.
 */

public class TaskArrayAdaptor extends ArrayAdapter<String> {
    HashMap<String,Integer> dataMap=new HashMap<String,Integer>();
    public TaskArrayAdaptor(Context context, int layout, int resource, List<String> objects) {
        super(context,layout, resource,objects);

        for (int i = 0; i <objects.size() ; i++) {
            dataMap.put(objects.get(i),i);
        }
    }


    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return dataMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
