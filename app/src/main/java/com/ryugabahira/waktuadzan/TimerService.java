package com.ryugabahira.waktuadzan;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.azan.types.PrayersType;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint("NewApi")
public class TimerService extends Service {
//	static Handler mHandler;
//	static int cnt;
//	static int jamawal = 0, mntawal = 0;
//	static long xtime1, xtime2;
    static boolean islocked = false;
    static boolean nowaybaby = false;
    static String lasttheme = "";

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                //Do your stuff on GPS status change
                try {
                    MainActivity.silent_cek_gps_and_update(context);
                } catch (Exception e) {
                }
            }
        }
    };

    private BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_SCREEN_OFF)) islocked = true;
            else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                showNotification_jadwal(context, "Untuk daerah " + MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")",
                            "Jadwal shalat berikutnya");
            }else if (action.equals(Intent.ACTION_USER_PRESENT) || action.equals(Intent.ACTION_USER_UNLOCKED)) {
                islocked = false;
                MainActivity.cancelNotification(context, MainActivity.NOTIF_JADWAL);

            } else if (action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_DATE_CHANGED)) {
                try {
                    if (MainActivity.alarmmanagerAdzan != null) {
                        MainActivity.getjadwalshalat(context);

                    } else MainActivity.start_alarm_adzan(context);
                } catch (Exception e) {
                }

//            	if (MainActivity.alarmmanagerGPS != null) {
//        			try {
//        				MainActivity.alarmmanagerGPS.cancel(MainActivity.pendingIntentGPS);
//        			} catch (Exception e0) {
//        			}
//
//        			Calendar alarm = Calendar.getInstance();
//        			alarm.set(Calendar.SECOND, 1);
//        			alarm.set(Calendar.MILLISECOND, 0);
//        			alarm.add(Calendar.MINUTE, 1);
//
//        			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        				// Wakes up the device in Doze Mode //ga ush 
////        				MainActivity.alarmmanagerGPS.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), MainActivity.pendingIntentGPS);
//        				MainActivity.alarmmanagerGPS.setExact(AlarmManager.RTC, alarm.getTimeInMillis(), MainActivity.pendingIntentGPS);
//        			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        				// Wakes up the device in Idle Mode
////        				MainActivity.alarmmanagerGPS.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), MainActivity.pendingIntentGPS);
//        				MainActivity.alarmmanagerGPS.setExact(AlarmManager.RTC, alarm.getTimeInMillis(), MainActivity.pendingIntentGPS);
//        			} else {
//        				// Old APIs
//        				MainActivity.alarmmanagerGPS.set(AlarmManager.RTC, alarm.getTimeInMillis(), MainActivity.pendingIntentGPS);
//        			}
//            	}
            } else if (action.equals(Intent.ACTION_TIME_TICK)) {
//                	Toast.makeText(context, "Tick tock", Toast.LENGTH_SHORT).show();
                MainActivity.sudahreminder = false;

                try {
                    if (MainActivity.lokasiGPS != null && MainActivity.waktuatasjudul.equals("")
                            && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
                        if (MainActivity.alarmmanagerAdzan != null) {
                            MainActivity.getjadwalshalat(context);

                        } else MainActivity.start_alarm_adzan(context);
//                        MainActivity.showNotification_biasa(context, MainActivity.NOTIF_BIASA, "waktu atas judul ", MainActivity.waktuatasjudul);
                    }else {
                        //update jadwal doang
                        MainActivity.getupdatejadwalatas(context);
                    }

                } catch (Exception e1) {
                }

                showNotification_jadwal(context, "Untuk daerah " + MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")",
                                "Jadwal shalat berikutnya");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
//		Toast.makeText(this, "create service", Toast.LENGTH_LONG).show();

        //add the  below lines of code in to your onCreate() method,
        //soon after calling super method.[i.e super.onCreate()]
        try {
            unregisterReceiver(m_timeChangedReceiver);//matiin yg sbelumnya
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(gpsReceiver);
        } catch (Exception e) {
        }

//		Toast.makeText(this, "preferensi", Toast.LENGTH_SHORT).show();

        MainActivity.do_it_1st(getApplicationContext());

        //		jamcek = new Timer();
        //		jamgps = new Timer();
        //		PackageManager manager = getPackageManager();
        //		try {
        //			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
        //			packageName = info.packageName;
        //			//int versionCode = info.versionCode;
        //			versionName = info.versionName;
        //		} catch (NameNotFoundException e) {
        //		}

        ////////////////////////////////gmn ya caranya biar ga ada notif
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        showNotification_kosong();

        //biar notif foreground service di mashmallow ke bawah ilang
        Intent intentx = new Intent(getApplicationContext(), TimerServiceBackup.class);
//			startService(intentx);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(intentx); //di oreo yg startService error
            else // di Oreo klo manggil startforeground (notification) hrs pake startForegroundService, klo ga manggil, gausah, malah bkin error
                startService(intentx);
        } catch (Exception e) {
            startService(intentx);
        }

        //}
        ///////////////////////////
//			Toast.makeText(this, "TimerServiceBackup", Toast.LENGTH_SHORT).show();

        MainActivity.s_intentFilter = new IntentFilter();
        MainActivity.s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        MainActivity.s_intentFilter.addAction(Intent.ACTION_USER_UNLOCKED);

        registerReceiver(m_timeChangedReceiver, MainActivity.s_intentFilter);
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
//
        //jobscheduler
//		if (Build.VERSION.SDK_INT >= 21) {
//			ComponentName mServiceComponent = new ComponentName(this, RyugaBahiraJobService.class);
//			JobInfo.Builder builder = new JobInfo.Builder(MainActivity.JobServiceID, mServiceComponent);
//	
////	        builder.setMinimumLatency(Long.valueOf(delay) * 1000);
////            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//	        builder.setRequiresDeviceIdle(true);
//	        builder.setRequiresCharging(true);
//	        builder.setPeriodic(intervalMillis)
//	
//	        // Extras, work duration.
////	        PersistableBundle extras = new PersistableBundle();
////	        String workDuration = mDurationTimeEditText.getText().toString();
////	        if (TextUtils.isEmpty(workDuration)) {
////	            workDuration = "1";
////	        }
////	        extras.putLong(WORK_DURATION_KEY, Long.valueOf(workDuration) * 1000);
////	
////	        builder.setExtras(extras);
//	
//	        // Schedule job
//	        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//	        tm.schedule(builder.build());
//		}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        try {
            MainActivity.start_alarm_adzan(this);
        } catch (Exception e) {
        }

        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    private void showNotification_kosong() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views;
        views = new RemoteViews(getPackageName(), R.layout.notification_utama_light);

        views.setTextViewText(R.id.status_bar_judul, getResources().getString(R.string.judul_notif_utama_in));

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(MainActivity.NOTIFICATION_CHANNEL_ID,
                    MainActivity.NOTIFICATION_CHANNEL_NAME,  //name of the channel
                    NotificationManager.IMPORTANCE_LOW);   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.

            mChannel.setDescription(MainActivity.NOTIFICATION_CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            //Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(false);
            mChannel.setShowBadge(true);
            nm.createNotificationChannel(mChannel);

            notification = new Notification.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setGroupSummary(true)
                    .setCustomContentView(views)
                    .build();  //finally build and return a Notification.
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(views)
                    .setPriority(Notification.PRIORITY_LOW)
                    .build();
        }

        startForeground(MainActivity.NOTIF_TIMER_UTAMA, notification);
//		MainActivity.boleh_harakiri = true;
    }


    @SuppressWarnings("deprecation")
    private static void showNotification_jadwal(final Context ctx, String msgx, String tittlex) {
        KeyguardManager keyguardMgr = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
        if(keyguardMgr.isKeyguardLocked()) {
            if (MainActivity.isjadwal_adzan && islocked) {
                if (MainActivity.lokasiGPS != null && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
                    int requestID = (int) System.currentTimeMillis();

                    // Using RemoteViews to bind custom layouts into Notification
                    RemoteViews views;
                    views = new RemoteViews(ctx.getPackageName(), R.layout.notification_jadwal_light);

                    Intent notificationIntent = new Intent(ctx, MainActivity.class);
                    notificationIntent.putExtra("from_notif", "JADWALSHALAT");
                    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, requestID,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    views.setViewVisibility(R.id.lay_2, View.GONE); //blom dipake

                    views.setTextViewText(R.id.wnextjadwalshalat, MainActivity.waktuatasjudul);
                    views.setTextViewText(R.id.wnextjadwaltimehour, MainActivity.waktuatastimehour);
                    views.setTextViewText(R.id.wnextjadwaltime_h, MainActivity.waktuatastime_h);
                    views.setTextViewText(R.id.wnextjadwaltimemin, MainActivity.waktuatastimemin);
                    views.setTextViewText(R.id.wnextjadwaltime_min, MainActivity.waktuatastime_min);
                    views.setTextViewText(R.id.wjudul, MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")");

                    String msgnow = "", nexttime = "";
                    if (MainActivity.waktuatasjudul.equals(ctx.getResources().getString(R.string.syuruq_name))) {
                        if (MainActivity.wnownextjudul.equals(ctx.getResources().getString(R.string.shalat_sekarang)) ||
                                MainActivity.wnownextjudul.equals(ctx.getResources().getString(R.string.dilarang_shalat))) {
                            msgnow = "Lewat dari waktu";
                        } else {
                            msgnow = "Menuju waktu";
                        }
                        nexttime = new SimpleDateFormat("HH:mm").format(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE));

                    } else {
                        if (MainActivity.wnownextjudul.equals(ctx.getResources().getString(R.string.shalat_sekarang))) {
                            msgnow = "Lewat dari waktu shalat";
                            nexttime = MainActivity.pad(MainActivity.waktu_alarm_last.get(Calendar.HOUR_OF_DAY)) + ":" + MainActivity.pad(MainActivity.waktu_alarm_last.get(Calendar.MINUTE));
                        } else {
                            msgnow = "Menuju waktu shalat";
                            nexttime = MainActivity.pad(MainActivity.waktu_alarm.get(Calendar.HOUR_OF_DAY)) + ":" + MainActivity.pad(MainActivity.waktu_alarm.get(Calendar.MINUTE));
                        }

                    }
                    views.setTextViewText(R.id.msgnow_next, msgnow);
                    if (MainActivity.waktuatasjudul.equals("System time error")) {
                        views.setViewVisibility(R.id.wnexttimeshalat, View.GONE);
                        views.setViewVisibility(R.id.lay_menuju, View.GONE);
                    } else {
                        views.setViewVisibility(R.id.wnexttimeshalat, View.VISIBLE);
                        views.setViewVisibility(R.id.lay_menuju, View.VISIBLE);
                        views.setTextViewText(R.id.wnexttimeshalat, nexttime);
                    }

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
                        mChannel.enableVibration(false);
                        mChannel.setShowBadge(true);
                        nm.createNotificationChannel(mChannel);

                        notification = new Notification.Builder(ctx, MainActivity.NOTIFICATION_CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setCustomContentView(views)
                                .setAutoCancel(true)   //allow auto cancel when pressed.
                                .setOngoing(false)
                                .setOnlyAlertOnce(true)
                                .setGroupSummary(true)
                                .setContentIntent(pendingIntent)
                                .build();  //finally build and return a Notification.
                    } else {
                        notification = new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContent(views)
                                .setAutoCancel(true)
                                .setOngoing(false)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setOnlyAlertOnce(true)
                                .setContentIntent(pendingIntent)
                                .build();
                    }

                    NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(MainActivity.NOTIF_JADWAL, notification);
                }
            }else MainActivity.cancelNotification(ctx, MainActivity.NOTIF_JADWAL);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (nowaybaby)
            unregisterReceiver(m_timeChangedReceiver);  //matiin biar ga mati service nya
        unregisterReceiver(gpsReceiver);
        //mHandler.removeCallbacksAndMessages(null);

        //MainActivity.start_alarm_timer(this);

//		if (!MainActivity.isexitback) {
//			SharedPreferences.Editor  editor = getSharedPreferences("iMoslem", Context.MODE_PRIVATE).edit();
//			editor.putBoolean("sudah_dzikir_pagi", false);
//			editor.putBoolean("sudah_dzikir_petang", false);
//			editor.putBoolean("sudah_kahfi", false);
//			editor.putBoolean("sudah_cekversi", false);
//			editor.commit();	

//		Timer timerObj = new Timer();
//		TimerTask timerTaskObj = new TimerTask() {
//			public void run() {
        Intent broadcastIntent = new Intent("com.ryugabahira.waktuadzan.TimerRestartSensor");
        sendBroadcast(broadcastIntent);
//
//			}
//		};
//		timerObj.schedule(timerTaskObj, 3000);
//		}

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


}

