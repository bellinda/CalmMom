package com.angelova.w510.calmmom.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.angelova.w510.calmmom.R;

public class NextExaminationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //you might want to check what's inside the Intent
        if(intent.getStringExtra("nextExamination") != null){
            String date = intent.getStringExtra("nextExamination");
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "examinationsChannel")
                    .setSmallIcon(R.drawable.ic_examination)
                    .setContentTitle(context.getString(R.string.examination_next_title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(date))
                    .setContentText(date)
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
//            Intent i = new Intent(context, YourTargetActivity.class);
//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(
//                            context,
//                            0,
//                            i,
//                            PendingIntent.FLAG_ONE_SHOT
//                    );
            // example for blinking LED
            builder.setLights(0xFFb71c1c, 1000, 2000);
//            builder.setSound(yourSoundUri);
//            builder.setContentIntent(pendingIntent);
            manager.notify(12345, builder.build());
        }

    }
}
