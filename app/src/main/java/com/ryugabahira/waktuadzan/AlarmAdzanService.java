package com.ryugabahira.waktuadzan;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmAdzanService extends IntentService {
    Intent intentx;

    public AlarmAdzanService() {
        super("AlarmAdzanService");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        //	AudioManager.STREAM_ALARM = 4, STREAM_MUSIC = 3

    }

    @SuppressWarnings("deprecation")
    @SuppressLint({"NewApi", "SimpleDateFormat"})
    @Override
    protected void onHandleIntent(Intent intent) {
        // At this point SimpleWakefulReceiver is still holding a wake lock
        // for us.  We can do whatever we need to here and then tell it that
        // it can release the wakelock.  This sample just does some slow work,
        // but more complicated implementations could take their own wake
        // lock here before releasing the receiver's.
        //
        // Note that when using this approach you should be aware that if your
        // service gets killed and restarted while in the middle of such work
        // (so the Intent gets re-delivered to perform the work again), it will
        // at that point no longer be holding a wake lock since we are depending
        // on SimpleWakefulReceiver to that for us.  If this is a concern, you can
        // acquire a separate wake lock here.

        //set next alarm
        //		Intent alarmIntent = new Intent(this, AlarmAdzanReceiver.class);
        //		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2704, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //		AlarmManager alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        intentx = intent;

        if (MainActivity.alarmmanagerAdzan != null) {
            try {
                if (MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
                    if (!MainActivity.var_daerah.equals("") && !MainActivity.waktuatasjudul.equals("")) {
                        Calendar waktu_now = Calendar.getInstance();
                        waktu_now.set(Calendar.SECOND, 0);
                        waktu_now.set(Calendar.MILLISECOND, 0);

                        if (MainActivity.waktu_alarm.get(Calendar.YEAR) == waktu_now.get(Calendar.YEAR) &&
                                MainActivity.waktu_alarm.get(Calendar.MONTH) == waktu_now.get(Calendar.MONTH) &&
                                MainActivity.waktu_alarm.get(Calendar.DAY_OF_MONTH) == waktu_now.get(Calendar.DAY_OF_MONTH) &&
                                MainActivity.waktu_alarm.get(Calendar.HOUR_OF_DAY) == waktu_now.get(Calendar.HOUR_OF_DAY) &&
                                MainActivity.waktu_alarm.get(Calendar.MINUTE) == waktu_now.get(Calendar.MINUTE)) {
//                        MainActivity.showNotification_biasa(this, MainActivity.NOTIF_DOWNLOAD, "Hour = " + MainActivity.waktu_alarm.get(Calendar.HOUR_OF_DAY)
//                                + ", Minute = " + MainActivity.waktu_alarm.get(Calendar.MINUTE), "Adzan Service");

                            MainActivity.notif_adzan_now(this);

                        }
                    }
                }

            } catch (Exception e) {
            }
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!MainActivity.isMyServiceRunning(TimerService.class, this)) {
            Intent intentx = new Intent(this, TimerService.class);
            intentx.setAction("");

            //			startService(intentx);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(intentx); //di oreo yg startService error
                else // di Oreo klo manggil startforeground (notification) hrs pake startForegroundService, klo ga manggil, gausah, malah bkin error
                    startService(intentx);
            } catch (Exception e) {
                startService(intentx);
            }
        }

//        AlarmAdzanReceiver.completeWakefulIntent(intentx);


    }

}

