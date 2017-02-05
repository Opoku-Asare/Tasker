package fi.oulu.mobisocial.tasker.Contract;

import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by opoku on 04-Feb-17.
 */

public final class TaskerContract {

    public TaskerContract() {
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasker_data";
        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String TASK = "task";
        public static final String TASK_DUE = "due_date";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        private String timeStamp;

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getTaskDue() {
            return taskDue;
        }

        public void setTaskDue(String taskDue) {
            this.taskDue = taskDue;
        }

        private String task;
        private String taskDue;


    }
}
