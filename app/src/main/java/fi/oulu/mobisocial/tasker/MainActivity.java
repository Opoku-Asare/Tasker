package fi.oulu.mobisocial.tasker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentQueryMap;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static boolean USER_LOGGED_IN = false;
    public static final String USERNAME_KEY = "USERNAME_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String LOG_ID = "Tasker";
    public static SharedPreferences appSharePreference;
    public static TaskerDb database;
    private ListView listView;
    private ArrayList<String> listData;
    private TaskArrayAdaptor listViewDataAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        listView = (ListView) findViewById(R.id.mainListView);
        prepareListView();


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
                database.createTask(task, taskDue);


            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return dialog.create();
    }

    private void prepareListView() {
        ArrayList<TaskerContract.TaskEntry> entries = database.readTask();

        /*String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};*/

        listData= new ArrayList<String>();

        for (TaskerContract.TaskEntry entry : entries) {
            listData.add(entry.getTask());
        }

        listViewDataAdaptor = new TaskArrayAdaptor(this, R.layout.list_view_item, R.id.sample_list_view_item, listData);
        listView.setAdapter(listViewDataAdaptor);

    }

    private void refreshListView() {
        ArrayList<TaskerContract.TaskEntry> entries = database.readTask();
        listData.clear();


        for (TaskerContract.TaskEntry entry : entries) {
            listData.add(entry.getTask());
        }

        listViewDataAdaptor.addAll(listData);
        listViewDataAdaptor.notifyDataSetChanged();
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
