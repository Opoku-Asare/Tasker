package fi.oulu.mobisocial.tasker.BroadCast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fi.oulu.mobisocial.tasker.Contract.TaskerContract;
import fi.oulu.mobisocial.tasker.MainActivity;
import fi.oulu.mobisocial.tasker.R;

/**
 * Created by opoku on 05-Feb-17.
 */

public class TaskerBroadCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
               context. getSystemService(context. NOTIFICATION_SERVICE);

        /*//if notification is selected
        Intent myIntent = new Intent(context,TaskerBroadCastReciever.class);

        //Since this can happen in the future, wrap it on a pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        Notification notification  = new Notification.Builder(context)
                .setContentTitle("Tasker")
                .setContentText(String.format("%s \n Due: %s",intent.getStringExtra(TaskerContract.TaskEntry.TASK), intent.getStringExtra( TaskerContract.TaskEntry.TASK_DUE) ))

                .setContentIntent(pIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
                //clear automatically when clicked
//                .addAction(R.drawable.icon, "Option 1", pIntent)
//                .addAction(R.drawable.icon, "Option 2", pIntent)

        notificationManager.notify(MainActivity.TASKER_NOTIFICATION_ID, notification);*/

    }
}
