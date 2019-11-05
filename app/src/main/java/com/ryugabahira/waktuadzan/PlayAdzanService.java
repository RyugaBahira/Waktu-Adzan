package com.ryugabahira.waktuadzan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class PlayAdzanService extends Service implements OnPreparedListener, OnErrorListener {

    private static boolean alarm_adzan;
    private static boolean alarm_notif;
    private static boolean alarm_notif2;
    private static boolean alarm_off;
    public static boolean alarm_test;
    public static String waktu_adzan;
    private static boolean is_ceksilent_shalat;
    private static Uri adzanSound = null;
    private static boolean isdoa_adzan;
    private static float mGZ = 0;//gravity acceleration along the z axis
    private static int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 10;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        MainActivity.adzanPlayer = new MediaPlayer();
        initAdzanPlayer();

    }

    @SuppressWarnings("deprecation")
    public void initAdzanPlayer() {
        //set player properties
        MainActivity.adzanPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        MainActivity.adzanPlayer.setAudioStreamType(MainActivity.AdzanStreamType);
        //set listeners
        MainActivity.adzanPlayer.setOnPreparedListener(this);
        MainActivity.adzanPlayer.setOnErrorListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        try {
            if (MainActivity.adzanPlayer != null) {
                if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
                MainActivity.adzanPlayer.reset();
                MainActivity.adzanPlayer.release();
                MainActivity.adzanPlayer = null;
            }

        } catch (Exception ex) {
        }
        MainActivity.lg_adzan = false;

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        MainActivity.adzanPlayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            waktu_adzan = extras.getString("waktu_adzan");
            MainActivity.waktuadzan_global = waktu_adzan;
            alarm_adzan = extras.getBoolean("alarm_adzan");
            alarm_notif = extras.getBoolean("alarm_notif");
            alarm_notif2 = extras.getBoolean("alarm_notif2");
            alarm_off = extras.getBoolean("alarm_off");
            alarm_test = extras.getBoolean("alarm_test");
            isdoa_adzan = extras.getBoolean("isdoa_adzan");
            is_ceksilent_shalat = extras.getBoolean("is_ceksilent_shalat");

        }
        MainActivity.adzan_notif = waktu_adzan;

        //TODO STartCOmmand
        if (intent.getAction().equals(MainActivity.ACTION.INIT_PLAY_ACTION)) {
            playAdzan();
        } else if (intent.getAction().equals(MainActivity.ACTION.STOPFOREGROUND_ACTION)) {
            finishAdzan();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    public static void showNotification_adzan(final Context ctx, String msgx, String tittlex) {
        if (((alarm_adzan || alarm_notif || alarm_notif2) && !alarm_off) || alarm_test) {
            int requestID = (int) System.currentTimeMillis();

            // Using RemoteViews to bind custom layouts into Notification
            RemoteViews views;
            views = new RemoteViews(ctx.getPackageName(), R.layout.notification_adzan_light);

            Intent notificationIntent = new Intent(ctx, MainActivity.class);
            notificationIntent.putExtra("from_notif", "JADWALSHALAT");
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, requestID,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setTextViewText(R.id.status_bar_judul, tittlex);
            views.setTextViewText(R.id.status_bar_info, msgx);
            if (!alarm_test) {
                Calendar c = Calendar.getInstance();
                int Hr24 = c.get(Calendar.HOUR_OF_DAY);
                int Min = c.get(Calendar.MINUTE);
                views.setTextViewText(R.id.status_bar_time, MainActivity.pad(Hr24) + ":" + MainActivity.pad(Min));
            }else views.setTextViewText(R.id.status_bar_time, "");

            Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel mChannel = new NotificationChannel(MainActivity.NOTIFICATION_CHANNEL_ID,
                        MainActivity.NOTIFICATION_CHANNEL_NAME,  //name of the channel
                        NotificationManager.IMPORTANCE_HIGH);   //importance level
                //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
                // Configure the notification channel.
                mChannel.setDescription(MainActivity.NOTIFICATION_CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                //Sets the notification light color for notifications posted to this channel, if the device supports this feature.
                mChannel.setLightColor(Color.GREEN);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(true);
                nm.createNotificationChannel(mChannel);


                notification = new Notification.Builder(ctx, MainActivity.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setCustomContentView(views)
                        .setTicker(msgx)
                        .setAutoCancel(true)   //allow auto cancel when pressed.
                        .setOngoing(false)
                        .setGroupSummary(true)
                        .setContentIntent(pendingIntent)
                        .setDeleteIntent(createOnDismissedIntent(ctx, MainActivity.NOTIF_ADZAN))
                        .build();  //finally build and return a Notification.
            } else {
                notification = new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContent(views)
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setGroup(MainActivity.NOTIFICATION_CHANNEL_NAME)
                        .setContentIntent(pendingIntent)
                        .setDeleteIntent(createOnDismissedIntent(ctx, MainActivity.NOTIF_ADZAN))
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(MainActivity.NOTIF_ADZAN, notification);
        }

        if (!alarm_test) {
            if (MainActivity.alarmmanagerAdzan != null) {
                MainActivity.getjadwalshalat(ctx);

            } else MainActivity.start_alarm_adzan(ctx);
//            MainActivity.showNotification_biasa(ctx, MainActivity.NOTIF_MOVEFILES, "abis adzan notif, get jadwal lg", "");
        }
    }

    public static PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, DismissAdzanReceiver.class);
        intent.putExtra("com.ryugabahira.waktuadzan.notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), notificationId, intent, 0);
        return pendingIntent;
    }

    private static Uri cari_URI_adzan(Context ctx, String waktuadzan2) {
        Uri adzanSoundx = null;
        String DB_PATH = (ctx.getDatabasePath("iMoslem.db") + "").replace("/" + "iMoslem.db", "");
        File filex = null;
        if (waktuadzan2.equals(ctx.getResources().getString(R.string.shalat_subuh_name))) {//subuh

                    adzanSoundx = Uri.parse("android.resource://"
                            + ctx.getPackageName() + "/" + R.raw.sheikh_abdul_karim_omar_fatani_al_makki_adzan_subuh);
        } else {

                    adzanSoundx = Uri.parse("android.resource://"
                            + ctx.getPackageName() + "/" + R.raw.sheikh_abdul_karim_omar_fatani_al_makki_adzan);
        }

//		if (Build.VERSION.SDK_INT >= 24 && MainActivity.idxadzan != 0) {
//			adzanSoundx = FileProvider.getUriForFile(ctx, "com.ryugabahira.waktuadzan.provider", filex);
//		} internal spt database path, appsdir, dll ga ush pke uri fileprovider

        return adzanSoundx;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (MainActivity.adzanPlayer != null) {
                if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
                MainActivity.adzanPlayer.reset();
                MainActivity.adzanPlayer.release();
                MainActivity.adzanPlayer = null;
            }

        } catch (Exception ex) {
        }
        MainActivity.lg_adzan = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private void finishAdzan() {
        if (MainActivity.adzanPlayer != null) {
            if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
            MainActivity.adzanPlayer.reset();
            MainActivity.adzanPlayer.release();
            MainActivity.adzanPlayer = null;
        }

        alarm_test = false;
        MainActivity.lg_adzan = false;

        stopSelf();
    }

    private void playAdzan() {
        if (MainActivity.adzanPlayer != null) {
            if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
            MainActivity.adzanPlayer.reset();
        }

        try {
            String waktustr = "";
            if (waktu_adzan.equals(getResources().getString(R.string.syuruq_name))) {
                if (alarm_test)
                    waktustr = "Test notifikasi waktu " + waktu_adzan;
                else waktustr = "Waktu " + waktu_adzan + " telah tiba";
            }else {
                if (alarm_test)
                    waktustr = "Test notifikasi Waktu Shalat " + waktu_adzan;
                else waktustr = "Waktu Shalat " + waktu_adzan + " telah tiba";
            }

            showNotification_adzan(getApplicationContext(), "Untuk daerah " + MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")", waktustr);

            if (alarm_adzan || alarm_notif) {
                if (!waktu_adzan.equals(getResources().getString(R.string.syuruq_name))) {
                    if (!MainActivity.alarm_test) MainActivity.lg_adzan = true;

                    if (alarm_adzan)
                        adzanSound = cari_URI_adzan(getApplicationContext(), waktu_adzan);
                    else
                        adzanSound = Uri.parse("android.resource://"
                                + getPackageName() + "/" + R.raw.labbaik_allahumma_labbaik_mishari);

                    MainActivity.adzanPlayer.setDataSource(getApplicationContext(), adzanSound);
                    MainActivity.adzanPlayer.setOnCompletionListener(mp -> finishAdzan());
                    MainActivity.adzanPlayer.prepareAsync();
                }
            }

        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            try {
                if (MainActivity.adzanPlayer != null) {
                    if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
                    MainActivity.adzanPlayer.reset();
                    MainActivity.adzanPlayer.release();
                    MainActivity.adzanPlayer = null;
                }
                MainActivity.lg_adzan = false;
            } catch (Exception ex) {
            }
        }

    }

}

