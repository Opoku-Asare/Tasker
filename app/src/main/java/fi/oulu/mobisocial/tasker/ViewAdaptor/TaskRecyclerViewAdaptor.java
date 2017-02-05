package fi.oulu.mobisocial.tasker.ViewAdaptor;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fi.oulu.mobisocial.tasker.Contract.TaskerContract;
import fi.oulu.mobisocial.tasker.MainActivity;
import fi.oulu.mobisocial.tasker.R;

/**
 * Created by opoku on 05-Feb-17.
 */

public class TaskRecyclerViewAdaptor extends RecyclerView.Adapter<TaskRecyclerViewAdaptor.TaskEntryViewHoler> {
    private ArrayList<TaskerContract.TaskEntry> dataset;

    public static class TaskEntryViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView taskTextView, taskDueTextView;
        public View holderView;

        public TaskEntryViewHoler(View itemView) {
            super(itemView);
            holderView = itemView;
            taskTextView = (TextView) itemView.findViewById(R.id.card_item_view_task);
            taskDueTextView = (TextView) itemView.findViewById(R.id.card_item_view_task_due);
        }

        public View getHolderView() {
            return holderView;
        }

        @Override
        public void onClick(View view) {

        }
    }

    public TaskRecyclerViewAdaptor(ArrayList<TaskerContract.TaskEntry> entries) {
        dataset = entries;
    }

    @Override
    public TaskEntryViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
        TaskEntryViewHoler viewHoler = new TaskEntryViewHoler(v);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(TaskEntryViewHoler holder, int position) {
        TaskerContract.TaskEntry entry = dataset.get(position);
        holder.taskTextView.setText(entry.getTask());
        Date taskDue = new Date();
        long taskDueLong = new Long(entry.getTaskDue());
        taskDue.setTime(taskDueLong);
        SimpleDateFormat format = new SimpleDateFormat();

        holder.taskDueTextView.setText(MainActivity.formateDate(taskDue,MainActivity.TASKER_FULL_DATE_FORMAT));


        //the value 0 if the argument Date is equal to this Date;
        // a value less than 0 if this Date is before the Date argument; and a value greater than 0 if this Date is after the Date argument.

        if ((holder.taskDueTextView.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
            holder.taskDueTextView.setPaintFlags(holder.taskDueTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (taskDue.compareTo(new Date()) < 0) {

            holder.taskDueTextView.setPaintFlags(holder.taskDueTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public void insertItem(TaskerContract.TaskEntry entry, int index) {
        dataset.add(index, entry);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        dataset.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
