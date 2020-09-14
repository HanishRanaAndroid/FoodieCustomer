package com.valle.foodieapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /*Notification recieved*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived: " + remoteMessage.toString());
        // sendNotification(remoteMessage.getFrom(), remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
           sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }

        try {
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
            if (!isScreenOn) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "myApp:notificationLock");
                wl.acquire(10000); //set your time in milliseconds
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Token Refresh*/
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    /*Send Notification*/
    private void sendNotification(String title, String body) {
        Intent intent;
        PendingIntent pendingIntent;
        intent = new Intent(this, HomeTabActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Random random = new Random();
        pendingIntent = PendingIntent.getActivity(this, random.nextInt(10), intent, 0);

        String channelId = getString(R.string.chaneel_id);
        Uri notification = null;
        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4,5})\\b");
//        StringBuffer sb = new StringBuffer();
//        Matcher m = pattern.matcher(body);
//        while (m.find()) {
//            int cp = Integer.parseInt(m.group(1), 16);
//            String added = cp < 0x10000 ? String.valueOf((char) cp) : new String(new int[]{cp}, 0, 1);
//            m.appendReplacement(sb, added);
//        }
//        m.appendTail(sb);
//        String content = sb.toString();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(getNotificationIcon())
                        .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .setVibrate(new long[]{500, 1000})
                        .setContentText(body)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setWhen(System.currentTimeMillis())
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                    getApplicationContext().getPackageName() + "/" + R.raw.notification);

            mChannel = new NotificationChannel(channelId, "ValleFood Channel", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableLights(true);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{500, 1000});
            mChannel.enableVibration(true);

            // Allow lockscreen playback control
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
//            mChannel.setSound(soundUri, audioAttributes);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        assert notificationManager != null;
        Random t = new Random();
        int notificationId = t.nextInt(10);
        notificationManager.notify(1, notificationBuilder.build());
    }

    /*Notification Icon*/
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo : R.drawable.logo;
    }
}
