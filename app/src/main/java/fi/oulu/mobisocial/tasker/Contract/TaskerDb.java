package fi.oulu.mobisocial.tasker.Contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by opoku on 04-Feb-17.
 */

public class TaskerDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasker.db";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TaskerContract.TaskEntry.TABLE_NAME +
            "(" +
            TaskerContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
            TaskerContract.TaskEntry.TIMESTAMP + " REAL DEFAULT 0," +
            TaskerContract.TaskEntry.TASK + " TEXT DEFAULT '' ," +
            TaskerContract.TaskEntry.TASK_DUE + " REAL DEFAULT 0" +
            ")";

    public TaskerDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long createTask(String task, String timeDue) {
        try {
            ContentValues entry = new ContentValues();
            entry.put(TaskerContract.TaskEntry.TIMESTAMP, System.currentTimeMillis());
            entry.put(TaskerContract.TaskEntry.TASK, task);
            entry.put(TaskerContract.TaskEntry.TASK_DUE, timeDue);

            long results = getWritableDatabase().insert(TaskerContract.TaskEntry.TABLE_NAME, null, entry);

            return results ;

        } catch (Exception ex) {
            return -1;
        }
    }

    public ArrayList<TaskerContract.TaskEntry> readTask() {
        try {
            String orderBy= TaskerContract.TaskEntry.TIMESTAMP+" DESC";
            Cursor data = getReadableDatabase().query(TaskerContract.TaskEntry.TABLE_NAME, null, null, null, null, null, orderBy, null);
            ArrayList<TaskerContract.TaskEntry> taskEntries = new ArrayList<TaskerContract.TaskEntry>();
            if (data != null && data.moveToFirst()) {

                do {
                    TaskerContract.TaskEntry entry = new TaskerContract.TaskEntry();

                    entry.setId(data.getString(data.getColumnIndex(TaskerContract.TaskEntry._ID)));
                    entry.setTask(data.getString(data.getColumnIndex(TaskerContract.TaskEntry.TASK)));
                    entry.setTimeStamp(Long.toString(data.getLong(data.getColumnIndex(TaskerContract.TaskEntry.TIMESTAMP))));

                    entry.setTaskDue(Long.toString(data.getLong(data.getColumnIndex(TaskerContract.TaskEntry.TASK_DUE))));

                    taskEntries.add(entry);
                } while (data.moveToNext());
                data.close();
            }
            return taskEntries;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean updateTask(String task, String timeDue, String Id) {
        try {

            ContentValues values = new ContentValues();
            if (Id == null || Id.isEmpty())
                throw new Exception("Id of Database entry to update cannot be null");
            if (task != null && !task.isEmpty()) values.put(TaskerContract.TaskEntry.TASK, task);
            if (timeDue != null && !timeDue.isEmpty())
                values.put(TaskerContract.TaskEntry.TASK_DUE, timeDue);

            String whereClause = TaskerContract.TaskEntry._ID + "=" + Id;
            int results = getWritableDatabase().update(TaskerContract.TaskEntry.TABLE_NAME, values, whereClause, null);
            return (results >= 1) ? true : false;

        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteTask(String Id) {
        try {
            if (Id == null || Id.isEmpty())
                throw new Exception("Id of Database entry to update cannot be null");

            String whereClause = TaskerContract.TaskEntry._ID + "=" + Id;
            int results = getWritableDatabase().delete(TaskerContract.TaskEntry.TABLE_NAME, whereClause, null);
            return results > 0 ? true : false;

        } catch (Exception ex) {
            return false;
        }
    }


}
