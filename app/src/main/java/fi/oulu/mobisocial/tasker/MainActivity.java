package fi.oulu.mobisocial.tasker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fi.oulu.mobisocial.tasker.BroadCast.TaskerBroadCastReciever;
import fi.oulu.mobisocial.tasker.Contract.TaskerContract;
import fi.oulu.mobisocial.tasker.Contract.TaskerDb;
import fi.oulu.mobisocial.tasker.ViewAdaptor.TaskRecyclerViewAdaptor;

public class MainActivity extends AppCompatActivity {
    public static boolean USER_LOGGED_IN = false;
    public static final String USERNAME_KEY = "USERNAME_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String LOG_ID = "Tasker";
    public static SharedPreferences appSharePreference;
    public static TaskerDb database;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TaskerContract.TaskEntry> cardDataSet;
    private TaskRecyclerViewAdaptor recyclerViewAdaptor;
    public static final String TASKER_FULL_DATE_FORMAT="EEE, d MMM yyyy HH:mm:ss";
    public static final int TASKER_NOTIFICATION_ID=198901287;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = buildTaskDialog();
                dialog.show();

            }
        });

        appSharePreference = PreferenceManager.getDefaultSharedPreferences(this);
        database = new TaskerDb(getApplicationContext());

        prepareCardView();


    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!USER_LOGGED_IN) {
            startLoginActivity();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public static void saveToSharedPreference(String key, String value) {

        appSharePreference.edit().putString(key.toString(), value.toString()).apply();
    }

    public static String retrieveFromSharePreference(String key) {

        return appSharePreference.getString(key, "");
    }

    private AlertDialog buildTaskDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.create_task_view, null);
        dialog.setView(dialogView);
        dialog.setTitle("Create Task");
        final Calendar calendar = Calendar.getInstance();
        Button pickAdate = (Button) dialogView.findViewById(R.id.pickDateButton);
        Button pickATime = (Button) dialogView.findViewById(R.id.pickTimeButton);
        final EditText taskInfo = (EditText) dialogView.findViewById(R.id.taskInfo);


        pickAdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog pickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });


        pickATime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog pickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                pickerDialog.show();
            }
        });
        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task = taskInfo.getText().toString().trim();
                String taskDue = Long.toString(calendar.getTimeInMillis());
                long result = database.createTask(task, taskDue);
                if (result > -1) {
                    TaskerContract.TaskEntry entry = new TaskerContract.TaskEntry();
                    entry.setId(Long.toString(result));
                    entry.setTask(task);
                    entry.setTaskDue(taskDue);

                    recyclerViewAdaptor.insertItem(entry, 0);

                    setAlarm(entry);
                }


            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return dialog.create();
    }

    private void setAlarm(TaskerContract.TaskEntry entry) {
       /* Intent intent = new Intent(MainActivity.this, TaskerBroadCastReciever.class);
        intent.putExtra(TaskerContract.TaskEntry.TASK, entry.getTask());
        intent.putExtra(TaskerContract.TaskEntry.TASK_DUE, formateDate(entry.getTaskDue(),TASKER_FULL_DATE_FORMAT));
        intent.setAction(Long.toString(System.currentTimeMillis()));//dummy action to keep the extras
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Calendar alarmCalendar=Calendar.getInstance();
        alarmCalendar.setTimeInMillis(new Long(entry.getTaskDue()));
        //set alarm
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set (AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+60 * 1000, pendingIntent);

        NotificationManager notificationManager = (NotificationManager)
               getSystemService(NOTIFICATION_SERVICE);

        //if notification is selected
        Intent myIntent = new Intent(this,TaskerBroadCastReciever.class);

        //Since this can happen in the future, wrap it on a pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       String formated= entry.getTask()+" "+formateDate(entry.getTaskDue(),TASKER_FULL_DATE_FORMAT);
        // build notification
        Notification notification  = new Notification.Builder(this)
                .setContentTitle("Tasker")
                .setContentText(formated )
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setWhen(dateFromLongString(entry.getTaskDue()).getTime())
                .setAutoCancel(true)
                .build();*/
        //clear automatically when clicked
//                .addAction(R.drawable.icon, "Option 1", pIntent)
//                .addAction(R.drawable.icon, "Option 2", pIntent)

//        notificationManager.notify(MainActivity.TASKER_NOTIFICATION_ID, notification);
    }

    private void prepareCardView() {
        cardDataSet = database.readTask();
        recyclerViewAdaptor = new TaskRecyclerViewAdaptor(cardDataSet);
        recyclerView.setAdapter(recyclerViewAdaptor);
    }

    public static String formateDate(String dateLongString, String format) {
        Date date = new Date();

        long dateLong = new Long(dateLongString);
        date.setTime(dateLong);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }
    public static String formateDate(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }
    public static Date dateFromLongString(String dateLongString){
        Date date = new Date();

        long dateLong = new Long(dateLongString);
        date.setTime(dateLong);
        return date;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.logoutMenuItems:
                USER_LOGGED_IN = false;
                startLoginActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
