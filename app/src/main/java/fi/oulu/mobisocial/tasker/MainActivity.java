package fi.oulu.mobisocial.tasker;

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
import android.widget.ListView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static boolean USER_LOGGED_IN = false;
    public static final String USERNAME_KEY = "USERNAME_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
    public static final String LOG_ID = "Tasker";
    public static SharedPreferences appSharePreference;
    private ListView listView;

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

        listView = (ListView) findViewById(R.id.mainListView);
        prepareListView();
        appSharePreference = PreferenceManager.getDefaultSharedPreferences(this);
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
        dialog.setView(getLayoutInflater().inflate(R.layout.create_task_view, null));

        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};

        ArrayList<String> data = new ArrayList<String>();
        for (String entry : values) {
            data.add(entry);
        }

        TaskArrayAdaptor adaptor = new TaskArrayAdaptor(this, R.layout.list_view_item, R.id.sample_list_view_item, data);
        listView.setAdapter(adaptor);

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!USER_LOGGED_IN) {
            startLoginActivity();
        }


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
