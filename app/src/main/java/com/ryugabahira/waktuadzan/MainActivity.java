package com.ryugabahira.waktuadzan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azan.PrayerTimes;
import com.azan.QiblaUtils;
import com.azan.TimeAdjustment;
import com.azan.TimeCalculator;
import com.azan.types.AngleCalculationType;
import com.azan.types.BaseTimeAdjustmentType;
import com.azan.types.PrayersType;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
    public static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "NOTIFICATION_CHANNEL_DESCRIPTION";
    public static final int NOTIF_REMINDER = 456;
    public static final int NOTIF_JADWAL = 458;
    public static final int NOTIF_TIMER_UTAMA = 459;
    public static final int NOTIF_AUTO_ON = 460;
    private static final int PERMISSION_REQUEST_CODE__LOCATION = 123;
    public static final int NOTIF_ADZAN = 457;
    private static final long UPDATE_FREQ = 10000;
    private static final long FASTEST_UPDATE_FREQ = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 654;
    public static AudioManager mainAudioManager, mainAdzanManager;
    public static boolean sudahreminder = false;
    public static String pesanxreminder;
    public static String waktuatasjudul;
    public static String var_kota;
    public static String var_negara;
    public static IntentFilter s_intentFilter;
    public static boolean isjadwal_adzan = true;
    public static String waktuatastimehour;
    public static String wnownextjudul;
    public static String waktuatastime_min;
    public static String waktuatastimemin;
    public static String waktuatastime_h;
    public static PrayerTimes prayerTimes;
    public static String adzan_notif;
    public static boolean is_ceksilentDismiss = false;
    public static boolean alarm_adzanDismiss = true;
    public static boolean isdoa_adzan = true;
    private static Dialog dialogpesan;
    private static String SDCardiMoslem = "/iMoslem/";
    private static String xfolderMurottalx = "/Murottal/";
    private static int idxadzan = 0;
    public static Location lokasiGPS = new Location("lokasi_a");
    public static String var_daerah;
    private static String last_lokasiGPS;
    public static double kiblatdegree;
    public static String arahkiblat;
    public static String jarakkekiblat;
    public static String latlangkabah;
    public static boolean lg_adzan = false;
    private static String formattglarab;
    private static boolean hanafi_asr_ratio = false;
    public static boolean alarm_adzan;
    public static boolean alarm_notif;
    public static boolean alarm_notif2;
    public static boolean alarm_off;
    private static int pickedVolume;
    public static boolean is_ceksilent;
    public static boolean sudahdoaadzan;
    public static boolean alarm_test;
    public static String waktuadzan_global;
    private static String alarmadzanskg;
    private static int waktushalat;
    private static String waktuadzanskg;
    public static int manual_hijriyyah = 2;
    public static int added_day = 0;
    private static int method_index = 4;
    private TextView wjudul, whari;
    private TextView subuh_time;
    private TextView terbit_time;
    private TextView dzuhur_time;
    private TextView ashar_time;
    private TextView magrib_time;
    private TextView isya_time;
    private TextView wmethod;
    private TextView wtanggal_lokal;
    private TextView wtanggal_arab;
    private ImageView next_date;
    private ImageView prev_date;
    private PrayerTimes lokal_prayerTimes;
    private TextView wnextjadwalshalat;
    private TextView wnextjadwaltimehour;
    private TextView wnextjadwaltime_h;
    private TextView wnextjadwaltimemin;
    private TextView wnextjadwaltime_min;
    private String hr_ini, waktusholatx;
    public boolean updateFinished = true;
    protected AutoCompleteTextView edinput;
    String infowaktushalat = "";
    int origionalVolumeAlarm, origionalVolumeRingtone, origionalVolumeVoiceCall, origionalVolumeNotif, origionalVolumeMusic;
    private ImageView refresh_day;
    private Dialog dialogpopup;
    private static Calendar waktu_now;
    public static final String[] nama_hari_arab_ID = new String[]{
            "Al-Ahad",
            "Al-Itsnain",
            "Ats-Tsulaatsaa'",
            "Al-Arbi'a'",
            "Al-Khamiis",
            "Al-Jumu'ah",
            "As-Sabt"};
    private ImageView iisyanownexttime;
    private ImageView imagribnownexttime;
    private ImageView iasharnownexttime;
    private ImageView idzuhurnownexttime;
    private ImageView isyuruqnownexttime;
    private ImageView isubuhnownexttime;
    private TextView txtback1;
    private TextView txtback2;
    private LinearLayout lay_cal;
    private Dialog dialogpesanadzan;
    private Dialog dialogpesand;
    public static MediaPlayer adzanPlayer = null;
    static LatLng newLocation = null;
    private static final int MESSAGE_ID_SAVE_CAMERA_POSITION = 1;
    private static final int MESSAGE_ID_READ_CAMERA_POSITION = 2;
    private CameraPosition lastCameraPosition;
    private Handler handler;
    private GoogleMap myMap;
    static String var_daerahx = "", var_negarax = "", var_kotax = "";
    private TextView judul_pesanx;
    static ArrayList<HashMap<String, String>> arr_list_address = new ArrayList<HashMap<String, String>>();
    List<String> list_address = new ArrayList<String>();
    private static int thnarab;
    private int blnarab;
    private int tglarab;
    public static MainActivity mContext;
    private TextView wnownextjudulx;

    public interface ACTION {
        String INIT_PLAY_ACTION = "com.ryugabahira.waktuadzan.action.init";
        String STOPFOREGROUND_ACTION = "com.ryugabahira.waktuadzan.action.stopforeground";

    }
    RadioButton cekadzan;
    RadioButton ceknotif, ceknotif2;
    RadioButton cekoff;
    private Button btntest;
    private boolean firsttime;
    private SeekBar slider;
    public static Calendar waktu_alarm = null, waktu_auto_on = null, waktu_reminder = null, waktu_alarm_last = null;
    public static AlarmManager alarmmanagerAdzan = null;
    public static PendingIntent pendingIntentAdzan;
    public static  String waktuadzannextshort;


    public static void silent_cek_gps_and_update(Context context) {
        try {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                if (isLocationEnabled(context)) {
                    update_lokasi_terbaru(context);
                }

            } else {
                //					MainActivity.showNotification_biasa(this, MainActivity.NOTIF_BIASA, "No Permission", "GPS");
            }

        } catch (Exception e) {
        }

    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static Compass compass = null;

    public static void do_it_1st(Context ctx) {
        try {
            compass = new Compass(ctx);
        } catch (Exception e) {
            compass = null;
        }

        mainAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        mainAdzanManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
    }

    public static ColorFilter colormatrix_green() {
        int iColor = Color.rgb(31, 45, 32);

        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = {0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0};

        ColorFilter cf = new ColorMatrixColorFilter(matrix);

        return cf;

    }

    private BroadcastReceiver receiver_latlang = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = (String) intent.getExtras().get("status");
            if (status.equals("SUCCESS")) {
                //				MainActivity.var_daerah = (String) intent.getExtras().get("var_daerah");
                //				MainActivity.var_negara = (String) intent.getExtras().get("var_negara");
                //				MainActivity.var_kota = (String) intent.getExtras().get("var_kota");
                //				MainActivity.kiblatdegree = (double) intent.getExtras().get("kiblatdegree");
                //				MainActivity.arahkiblat = (String) intent.getExtras().get("arahkiblat");
                //				MainActivity.jarakkekiblat = (String) intent.getExtras().get("jarakkekiblat");
                //				MainActivity.latlangkabah = (String) intent.getExtras().get("latlangkabah");

            }
            try {
                show_info();
            } catch (Exception e) {
            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(receiver_latlang);

    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.added_day = 0;
        show_info();

        registerReceiver(receiver_latlang, new IntentFilter("LATLANGRECEIVER"));
        if (MainActivity.lg_adzan) {//biar klo di klik ga ilang
            String waktustr = "";
            if (PlayAdzanService.waktu_adzan.equals(getResources().getString(R.string.syuruq_name))) {
                waktustr = "Waktu " + PlayAdzanService.waktu_adzan + " telah tiba";
            } else {
                waktustr = "Waktu Shalat " + PlayAdzanService.waktu_adzan + " telah tiba";
            }

            PlayAdzanService.showNotification_adzan(getApplicationContext(), "Untuk daerah " + MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")", waktustr);

        }

        if (!isMyServiceRunning(TimerService.class, this) ) {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.do_it_1st(getApplicationContext());
        mContext = this;
        setting_visual();

        if (MainActivity.lokasiGPS.getLatitude() == 0.0 || MainActivity.lokasiGPS.getLongitude() == 0.0)
        askSetting_cek_gps_and_update(this);

    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);

        if (notifyId == MainActivity.NOTIF_ADZAN) {
            try {
                Intent intentx = new Intent("PESANGLOBALCLOSE"); //FILTER is a string to identify this intent
                ctx.sendBroadcast(intentx);
            } catch (Exception e) {
            }
        }
    }

    public static int AdzanStreamType;
    public static void notif_adzan_now(Context ctx) {
        if (!MainActivity.lg_adzan) {
            SharedPreferences prefs = ctx.getSharedPreferences("iMoslem", Context.MODE_PRIVATE);
            if (MainActivity.waktuadzannextshort.equals(ctx.getResources().getString(R.string.syuruq_name))) {
                MainActivity.waktuatasjudul = MainActivity.waktuadzannextshort;

                MainActivity.alarm_adzan = false;
                MainActivity.alarm_notif = false;
                MainActivity.alarm_notif2 = prefs.getBoolean("alarm_notif2_" + MainActivity.waktuatasjudul, true);
                MainActivity.alarm_off = prefs.getBoolean("alarm_off_" + MainActivity.waktuatasjudul, false);
            } else {
                MainActivity.alarm_adzan = prefs.getBoolean("alarm_adzan_" + MainActivity.waktuatasjudul, true);
                MainActivity.alarm_notif = prefs.getBoolean("alarm_notif_" + MainActivity.waktuatasjudul, false);
                MainActivity.alarm_notif2 = prefs.getBoolean("alarm_notif2_" + MainActivity.waktuatasjudul, false);
                MainActivity.alarm_off = prefs.getBoolean("alarm_off_" + MainActivity.waktuatasjudul, false);
            }

            MainActivity.idxadzan = prefs.getInt("idxAdzan_" + MainActivity.waktuatasjudul, 0);
            MainActivity.pickedVolume = prefs.getInt("volAdzanNew_" + MainActivity.AdzanStreamType + "_" + MainActivity.waktuatasjudul, MainActivity.mainAdzanManager.getStreamMaxVolume(MainActivity.AdzanStreamType) * 12 / 15);

            if (MainActivity.alarm_adzan) {

								/*if (isMyServiceRunning(PlayMurottalService.class, this)) {
									Intent intentz = new Intent(this, PlayMurottalService.class);
									stopService(intentz);

									if (MainActivity.waktuadzannextshort.equals(getResources().getString(R.string.syuruq_name)))
										MainActivity.showNotification_biasa(this, MainActivity.NOTIF_BIASA, "Murottal dihentikan karena waktu " + MainActivity.waktuatasjudul + " Telah Tiba",  "Murottal Terhenti");
									else
										MainActivity.showNotification_biasa(this, MainActivity.NOTIF_BIASA, "Murottal dihentikan karena waktu shalat " + MainActivity.waktuatasjudul + " Telah Tiba",  "Murottal Terhenti");
								}*/
                

                    MainActivity.mainAdzanManager.setStreamVolume(MainActivity.AdzanStreamType, MainActivity.pickedVolume, 0);
            }/*else
								 if (MainActivity.alarm_notif) {
									 if (mAudioManagerAdzan.getStreamVolume(MainActivity.AdzanStreamType) <= mAudioManagerAdzan.getStreamMaxVolume(MainActivity.AdzanStreamType) / 2)
										 mAudioManagerAdzan.setStreamVolume(MainActivity.AdzanStreamType, (mAudioManagerAdzan.getStreamMaxVolume(MainActivity.AdzanStreamType) / 2) + 2, 0);
									 else mAudioManagerAdzan.setStreamVolume(MainActivity.AdzanStreamType, mAudioManagerAdzan.getStreamVolume(MainActivity.AdzanStreamType), 0);
								 }*/

            try {
                MainActivity.alarmmanagerReminder.cancel(MainActivity.pendingIntentReminder);
                MainActivity.cancelNotification(ctx, MainActivity.NOTIF_REMINDER);
            } catch (Exception e1) {
            }

            int ceksilent_auto_on = prefs.getInt("ceksilent_auto_on_" + MainActivity.waktuatasjudul, 5);
            boolean is_ceksilent = prefs.getBoolean("is_ceksilent_" + MainActivity.waktuatasjudul, false);

            if (MainActivity.alarm_adzan && is_ceksilent && ceksilent_auto_on > 0) {
                MainActivity.is_ceksilent = true;
                MainActivity.waktu_auto_on = Calendar.getInstance();
                MainActivity.waktu_auto_on.set(Calendar.SECOND, 0);
                MainActivity.waktu_auto_on.set(Calendar.MILLISECOND, 0);

                switch (ceksilent_auto_on) {
                    case 1:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 10);
                        break;
                    case 2:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 15);
                        break;
                    case 3:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 20);
                        break;
                    case 4:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 25);
                        break;
                    case 5:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 30);
                        break;
                    case 6:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 35);
                        break;
                    case 7:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 40);
                        break;
                    case 8:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 45);
                        break;
                    case 9:
                        MainActivity.waktu_auto_on.add(Calendar.MINUTE, 60);
                        break;
                }

            }

            if (MainActivity.waktuatasjudul.equals(ctx.getResources().getString(R.string.syuruq_name))) {
                MainActivity.sudahdoaadzan = true;
                MainActivity.lg_adzan = false;
            } else {
                MainActivity.sudahdoaadzan = false;
                if (!MainActivity.alarm_test) MainActivity.lg_adzan = true;
            }

            MainActivity.waktuadzan_global = MainActivity.waktuatasjudul;

            Intent intent = new Intent(ctx, PlayAdzanService.class);
            intent.setAction(MainActivity.ACTION.INIT_PLAY_ACTION);
            intent.putExtra("waktu_adzan", MainActivity.waktuadzan_global);
            intent.putExtra("alarm_adzan", MainActivity.alarm_adzan);
            intent.putExtra("alarm_notif", MainActivity.alarm_notif);
            intent.putExtra("alarm_notif2", MainActivity.alarm_notif2);
            intent.putExtra("alarm_off", MainActivity.alarm_off);
            intent.putExtra("alarm_test", MainActivity.alarm_test);
            intent.putExtra("isdoa_adzan", MainActivity.isdoa_adzan);
            intent.putExtra("is_ceksilent_shalat", is_ceksilent);
            ctx.startService(intent);
        }
    }

    public static void toast_info(Context ctx, String info) {
        if (dialogpesan != null && dialogpesan.isShowing()) dialogpesan.dismiss();

        dialogpesan = new Dialog(ctx);
        dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpesan.setContentView(R.layout.layout_toast);
        dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
        dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogpesan.setCancelable(true);
        dialogpesan.setCanceledOnTouchOutside(true);

        Window window = dialogpesan.getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        ImageView imoslem = dialogpesan.findViewById(R.id.imoslem);
        imoslem.setVisibility(View.GONE);
        TextView judul_pesan = dialogpesan.findViewById(R.id.judul_pesan);
        judul_pesan.setText(info);

        dialogpesan.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialogpesan.isShowing()) dialogpesan.dismiss();
        }, 3000);

    }

    @SuppressWarnings("deprecation")
    public static void toast_info(String xpos, Context ctx, String info_ar, String info, int timex) {
        if (dialogpesan != null && dialogpesan.isShowing()) dialogpesan.dismiss();

        dialogpesan = new Dialog(ctx);
        dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpesan.setContentView(R.layout.layout_toast);
        dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
        dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogpesan.setCancelable(true);
        dialogpesan.setCanceledOnTouchOutside(true);

        Window window = dialogpesan.getWindow();
        // set "origin" to top left corner, so to speak
        if (xpos.equals("TOP"))
            window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        else if (xpos.equals("MID"))
            window.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        else window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        ImageView imoslem = dialogpesan.findViewById(R.id.imoslem);
        imoslem.setVisibility(View.GONE);

        TextView judul_pesan = dialogpesan.findViewById(R.id.judul_pesan);
        judul_pesan.setText(info);

        dialogpesan.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                if (dialogpesan.isShowing()) dialogpesan.dismiss();
            } catch (Exception e) {
            }
        }, timex);

    }
    

    public static FusedLocationProviderClient mFusedLocationClient = null;
    public static LocationCallback locationCallback;

    public static boolean is_NetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean checkLocationPermission(Context ctx) {
        if (ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(ctx,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                final Dialog dialogpesan = new Dialog(ctx);
                dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesan.setContentView(R.layout.layout_dialog_message);
                dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesan.setCancelable(true);
                dialogpesan.setCanceledOnTouchOutside(false);

                TextView judul_pesan = dialogpesan.findViewById(R.id.judul_pesan);
                TextView isi_pesan = dialogpesan.findViewById(R.id.isi_pesan);
                judul_pesan.setText(R.string.shalattools_title_location_permission);
                isi_pesan.setText(R.string.shalattools_text_location_permission);

                Button btn2 = dialogpesan.findViewById(R.id.btn2);
                btn2.setText(ctx.getResources().getString(R.string.str_btnbatal));
                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogpesan.dismiss();
                    }
                });

                Button btn1 = dialogpesan.findViewById(R.id.btn1);
                btn1.setText(ctx.getResources().getString(R.string.str_btnlanjut));
                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogpesan.dismiss();

                        ActivityCompat.requestPermissions(mContext,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MainActivity.PERMISSION_REQUEST_CODE__LOCATION);
                    }
                });

                dialogpesan.show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MainActivity.PERMISSION_REQUEST_CODE__LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /////////////////////// GPS GOOGLE API CLIENT
    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    public static boolean checkPlayServices(Context ctx) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(ctx);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
//				googleAPI.getErrorDialog(this, result,
//						PLAY_SERVICES_RESOLUTION_REQUEST).show();

            }

            return false;
        }

        return true;
    }

    public static void updateLocationUI(Context ctx) {
        if (MainActivity.lokasiGPS != null && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
            MyGeocoder mygeo = new MyGeocoder(ctx);
            if (Build.VERSION.SDK_INT >= 11) {
                mygeo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                mygeo.execute();
            }

            //bwt test
            //			MainActivity.showNotification_biasa(ctx, MainActivity.NOTIF_BIASA, MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")",  "GPS");

        }
    }

    public static class MyGeocoder extends AsyncTask<String, String, String> {
        private Context contextx;
        int maxResult = 1;
        private boolean brodkes;

        public MyGeocoder(Context ctx) {
            // TODO Auto-generated constructor stub
            this.contextx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            if (is_NetworkAvailable(contextx)) {
                URL geocodeUrl;
                HttpURLConnection connection;
                int responseCode = 0;
                MainActivity.var_daerah = "";
                MainActivity.var_negara = "";
                MainActivity.var_kota = "";
                String vsub = "", var_kota2 = "";
                Geocoder geocoder = new Geocoder(contextx, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        MainActivity.var_daerah = addresses.get(0).getLocality();
                        vsub = /*addresses.get(0).getThoroughfare() + " " + addresses.get(0).getSubThoroughfare() + " " + */addresses.get(0).getSubLocality();
                        MainActivity.var_negara = addresses.get(0).getCountryName();
                        MainActivity.var_kota = addresses.get(0).getSubAdminArea();
                        var_kota2 = addresses.get(0).getAdminArea();

                    }
                } catch (IOException e) {
                    try {
                        geocodeUrl = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + MainActivity.lokasiGPS.getLatitude()
                                + "," + MainActivity.lokasiGPS.getLongitude() + "&sensor=false&key=" + contextx.getResources().getString(R.string.google_api_key_geocode)); //keygeocoding
                        connection = (HttpURLConnection) geocodeUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                        responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = null;

                            InputStream inputStream = connection.getInputStream();
                            StringBuffer buffer = new StringBuffer();
                            if (inputStream == null) {
                                // Nothing to do.
                                return null;
                            }
                            reader = new BufferedReader(new InputStreamReader(inputStream));

                            String line;
                            while ((line = reader.readLine()) != null) {

                                buffer.append(line + "\n");
                            }

                            if (buffer.length() == 0) {
                                return null;
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(buffer.toString());
                                if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                                    JSONArray results = jsonObject.getJSONArray("results");
                                    if (results.length() > 0) {
                                        for (int i = 0; i < results.length() && i < maxResult; i++) {
                                            JSONObject result = results.getJSONObject(i);
                                            JSONArray components = result.getJSONArray("address_components");
                                            for (int a = 0; a < components.length(); a++) {
                                                JSONObject component = components.getJSONObject(a);
                                                JSONArray types = component.getJSONArray("types");
                                                for (int j = 0; j < types.length(); j++) {
                                                    String type = types.getString(j);
                                                    if (type.equals("administrative_area_level_4")) {
                                                        vsub = component.getString("long_name");
                                                    } else if (type.equals("administrative_area_level_3")) {
                                                        MainActivity.var_daerah = component.getString("long_name");
                                                    } else if (type.equals("administrative_area_level_2")) {
                                                        MainActivity.var_kota = component.getString("long_name");
                                                    } else if (type.equals("administrative_area_level_1")) {
                                                        var_kota2 = component.getString("long_name");
                                                    } else if (type.equals("country")) {
                                                        MainActivity.var_negara = component.getString("long_name");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException ez) {
                            }

                        }

                    } catch (MalformedURLException e2) {
                    } catch (ProtocolException e1) {
                    } catch (IOException ec) {
                    }
                }

                if (MainActivity.var_daerah == null || MainActivity.var_daerah.equals("null"))
                    MainActivity.var_daerah = "";
                if (vsub == null || vsub.equals("null")) vsub = "";

                if (MainActivity.var_daerah.equals("") && vsub.equals(""))
                    MainActivity.var_daerah = "";
                else if (!MainActivity.var_daerah.equals("") && !vsub.equals("") && !vsub.equals(MainActivity.var_daerah))
                    MainActivity.var_daerah = vsub + ", " + MainActivity.var_daerah;
                else if (MainActivity.var_daerah.equals("") && !vsub.equals(""))
                    MainActivity.var_daerah = vsub;


                if (MainActivity.var_kota == null || MainActivity.var_kota.equals("null"))
                    MainActivity.var_kota = "";
                if (var_kota2 == null || var_kota2.equals("null")) var_kota2 = "";

                if (MainActivity.var_kota.equals("") && var_kota2.equals(""))
                    MainActivity.var_kota = "";
                else if (!MainActivity.var_kota.equals("") && !var_kota2.equals("") && !var_kota2.equals(MainActivity.var_kota))
                    MainActivity.var_kota = var_kota2 + ", " + MainActivity.var_kota;
                else if (MainActivity.var_kota.equals("") && !var_kota2.equals(""))
                    MainActivity.var_kota = var_kota2;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            brodkes = false;
            if (!(MainActivity.lokasiGPS.getLatitude() + "|" + MainActivity.lokasiGPS.getLongitude()).equals(MainActivity.last_lokasiGPS)) {
                MainActivity.last_lokasiGPS = MainActivity.lokasiGPS.getLatitude() + "|" + MainActivity.lokasiGPS.getLongitude();

                DecimalFormat df = new DecimalFormat("#.##");
                MainActivity.kiblatdegree = QiblaUtils.qibla(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude());
                MainActivity.arahkiblat = contextx.getResources().getString(R.string.shalattools_arahkiblat)
                        + " " + df.format(MainActivity.kiblatdegree)
                        + contextx.getResources().getString(R.string.shalattools_dariutara)
                        + " " + QiblaUtils.direction(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()),
                        new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE));
                MainActivity.jarakkekiblat = contextx.getResources().getString(R.string.shalattools_jarakkekiblat)
                        + " " + df.format(QiblaUtils.HitungJarak(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()) / 1000)
                        + " Km";
                MainActivity.latlangkabah = com.azan.Constants.KAABA_LATITUDE + ", " + com.azan.Constants.KAABA_LONGITUDE;

                //save lokasi disini
                SharedPreferences.Editor editor = contextx.getSharedPreferences("iMoslem", Context.MODE_PRIVATE).edit();
                editor.putString("lastlat", "" + MainActivity.lokasiGPS.getLatitude());
                editor.putString("lastlong", "" + MainActivity.lokasiGPS.getLongitude());
                editor.putString("var_daerah", MainActivity.var_daerah);
                editor.putString("var_negara", MainActivity.var_negara);
                editor.putString("var_kota", MainActivity.var_kota);
                editor.putString("arahkiblat", MainActivity.arahkiblat);
                editor.putString("jarakkekiblat", MainActivity.jarakkekiblat);
                editor.commit();

                brodkes = true;
                //Toast.makeText(contextx, "Updated", Toast.LENGTH_SHORT).show();
            }

            if (MainActivity.alarmmanagerAdzan != null) {
                MainActivity.getjadwalshalat(contextx);

            } else MainActivity.start_alarm_adzan(contextx);

            ///////////////////////////////////
            if (brodkes) {
                Intent intentx = new Intent("LATLANGRECEIVER"); //FILTER is a string to identify this intent
                intentx.putExtra("status", "SUCCESS");
                contextx.sendBroadcast(intentx);
            }
            ///////////////////////////////////////
        }
    }
    /////////////////////// GPS GOOGLE API CLIENT

    @SuppressLint("SimpleDateFormat")
    public static void start_alarm_adzan(Context ctx) {
        Intent alarmIntent = new Intent(ctx, AlarmAdzanReceiver.class);
        pendingIntentAdzan = PendingIntent.getBroadcast(ctx, 2704, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmmanagerAdzan = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        getjadwalshalat(ctx);

    }
    
    public static void getjadwalshalat(Context context) {
        if (MainActivity.lokasiGPS != null && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
            try {
                alarmmanagerAdzan.cancel(pendingIntentAdzan);
            } catch (Exception e1) {
            }

            SimpleDateFormat sdfjam = new SimpleDateFormat("HH:mm");
            //ambil jadwal isya hari kemarin
            waktu_alarm_last = Calendar.getInstance();
            waktu_alarm_last.add(Calendar.DAY_OF_MONTH, -1);

            MainActivity.calculate_adzan(context, new GregorianCalendar(
                    waktu_alarm_last.get(Calendar.YEAR),
                    waktu_alarm_last.get(Calendar.MONTH),
                    waktu_alarm_last.get(Calendar.DAY_OF_MONTH),
                    waktu_alarm_last.get(Calendar.HOUR_OF_DAY),
                    waktu_alarm_last.get(Calendar.MINUTE),
                    waktu_alarm_last.get(Calendar.SECOND)));
            waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA));
            waktu_alarm_last.set(Calendar.SECOND, 0);
            waktu_alarm_last.set(Calendar.MILLISECOND, 0);
            alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA));

            //ambil jadwal subuh hari ini
            MainActivity.calculate_adzan(context, new GregorianCalendar());
            waktu_alarm = Calendar.getInstance();
            waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));
            waktu_alarm.set(Calendar.SECOND, 0);
            waktu_alarm.set(Calendar.MILLISECOND, 0);

            waktu_now = Calendar.getInstance();
            waktu_now.set(Calendar.SECOND, 20); //biar beda dgn alarm, klo kbetulan detik sama (0) bs error ->  if (waktu_alarm.before(waktu_now)) {
            waktu_now.set(Calendar.MILLISECOND, 0);

            waktushalat = 5;
            waktuadzanskg = context.getResources().getString(R.string.shalat_isya_name);
            waktuadzannextshort = context.getResources().getString(R.string.shalat_subuh_name);

            if (waktu_alarm.before(waktu_now)) {
                waktushalat = 1;
                waktuadzanskg = context.getResources().getString(R.string.shalat_subuh_name);
                waktuadzannextshort = context.getResources().getString(R.string.syuruq_name);
                alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));

                waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE));
                waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));
                waktu_alarm.set(Calendar.SECOND, 0);
                waktu_alarm.set(Calendar.MILLISECOND, 0);
                waktu_alarm_last.set(Calendar.SECOND, 0);
                waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                if (waktu_alarm.before(waktu_now)) {
                    waktushalat = 1;
                    waktuadzanskg = context.getResources().getString(R.string.shalat_subuh_name);
                    waktuadzannextshort = context.getResources().getString(R.string.shalat_dzuhur_name);
                    alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));

                    waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR));
                    waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));
                    waktu_alarm.set(Calendar.SECOND, 0);
                    waktu_alarm.set(Calendar.MILLISECOND, 0);
                    waktu_alarm_last.set(Calendar.SECOND, 0);
                    waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                    if (waktu_alarm.before(waktu_now)) {
                        waktushalat = 2;
                        waktuadzanskg = context.getResources().getString(R.string.shalat_dzuhur_name);
                        waktuadzannextshort = context.getResources().getString(R.string.shalat_ashar_name);
                        alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR));

                        waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ASR));
                        waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR));
                        waktu_alarm.set(Calendar.SECOND, 0);
                        waktu_alarm.set(Calendar.MILLISECOND, 0);
                        waktu_alarm_last.set(Calendar.SECOND, 0);
                        waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                        if (waktu_alarm.before(waktu_now)) {
                            waktushalat = 3;
                            waktuadzanskg = context.getResources().getString(R.string.shalat_ashar_name);
                            waktuadzannextshort = context.getResources().getString(R.string.shalat_maghrib_name);
                            alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ASR));

                            waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.MAGHRIB));
                            waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ASR));
                            waktu_alarm.set(Calendar.SECOND, 0);
                            waktu_alarm.set(Calendar.MILLISECOND, 0);
                            waktu_alarm_last.set(Calendar.SECOND, 0);
                            waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                            if (waktu_alarm.before(waktu_now)) {
                                waktushalat = 4;
                                waktuadzanskg = context.getResources().getString(R.string.shalat_maghrib_name);
                                waktuadzannextshort = context.getResources().getString(R.string.shalat_isya_name);
                                alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.MAGHRIB));

                                waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA));
                                waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.MAGHRIB));
                                waktu_alarm.set(Calendar.SECOND, 0);
                                waktu_alarm.set(Calendar.MILLISECOND, 0);
                                waktu_alarm_last.set(Calendar.SECOND, 0);
                                waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                                if (waktu_alarm.before(waktu_now)) { //subuh besoknya
                                    waktushalat = 5;
                                    waktuadzanskg = context.getResources().getString(R.string.shalat_isya_name);
                                    waktuadzannextshort = context.getResources().getString(R.string.shalat_subuh_name);
                                    alarmadzanskg = sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA));
                                    waktu_alarm_last.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA));

                                    waktu_alarm = Calendar.getInstance();
                                    waktu_alarm.add(Calendar.DAY_OF_MONTH, 1);
                                    //cari adzan subuh besoknya
                                    MainActivity.calculate_adzan(context, new GregorianCalendar(
                                            waktu_alarm.get(Calendar.YEAR),
                                            waktu_alarm.get(Calendar.MONTH),
                                            waktu_alarm.get(Calendar.DAY_OF_MONTH),
                                            waktu_alarm.get(Calendar.HOUR_OF_DAY),
                                            waktu_alarm.get(Calendar.MINUTE),
                                            waktu_alarm.get(Calendar.SECOND)));
                                    waktu_alarm.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR));

                                    waktu_alarm.set(Calendar.SECOND, 0);
                                    waktu_alarm.set(Calendar.MILLISECOND, 0);
                                    waktu_alarm_last.set(Calendar.SECOND, 0);
                                    waktu_alarm_last.set(Calendar.MILLISECOND, 0);

                                }

                            }

                        }

                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Wakes up the device in Doze Mode
                MainActivity.alarmmanagerAdzan.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, MainActivity.waktu_alarm.getTimeInMillis(), MainActivity.pendingIntentAdzan);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Wakes up the device in Idle Mode
                MainActivity.alarmmanagerAdzan.setExact(AlarmManager.RTC_WAKEUP, MainActivity.waktu_alarm.getTimeInMillis(), MainActivity.pendingIntentAdzan);
            } else {
                // Old APIs
                MainActivity.alarmmanagerAdzan.set(AlarmManager.RTC, MainActivity.waktu_alarm.getTimeInMillis(), MainActivity.pendingIntentAdzan);
            }

            getupdatejadwalatas(context);
//            showNotification_biasa(context, MainActivity.NOTIF_BIASA, "get jadwal shalat ", MainActivity.waktuatasjudul);
        }
    }

    static String[] method_calculationEN = {
            "Muslim World League (MWL) Standard",
            "Islamic Society of North America (ISNA) Standard",
            "Egyptian General Authority of Survey Standard",
            "University of Islamic Sciences (Karachi) Standard",
            "South East Asia (Indonesia, Malaysia, Brunei and around)",
            "Union des Organisations Islamiques de France (UOIF)",
            "Kuwait Calc Method",
            "Umm al-Qura University, Makkah"
    };
    static String[] method_calculationID = {
            "Muslim World League (MWL) Standard",
            "Islamic Society of North America (ISNA) Standard",
            "Egyptian General Authority of Survey Standard",
            "University of Islamic Sciences (Karachi) Standard",
            "Asia Tenggara (Indonesia, Malaysia, Brunei dan sekitarnya)",
            "Union des Organisations Islamiques de France (UOIF)",
            "Kuwait Calc Method",
            "Umm al-Qura University, Makkah"
    };
    public static AlarmManager alarmmanagerReminder;
    public static PendingIntent pendingIntentReminder;
    public void set_reminder_if_requested(Context context) {
        try {
            alarmmanagerReminder.cancel(pendingIntentReminder);
        } catch (Exception e1) {
        }

        if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            if (waktuadzannextshort.equals(context.getResources().getString(R.string.shalat_dzuhur_name)))
                waktuadzannextshort = context.getResources().getString(R.string.shalat_jumat);
        }

        SharedPreferences prefs = context.getSharedPreferences("iMoslem", Context.MODE_PRIVATE);
        int notif_before = prefs.getInt("notif_before_" + waktuadzannextshort, 0);
        final boolean is_notif_before = prefs.getBoolean("is_notif_before_" + waktuadzannextshort, false);

        if (is_notif_before) {
            waktu_reminder = Calendar.getInstance();
            waktu_reminder.setTimeInMillis(waktu_alarm.getTimeInMillis());
            pesanxreminder = "";
            switch (notif_before) {
                case 0: {
                    waktu_reminder.add(Calendar.MINUTE, -3);
                    pesanxreminder = "3 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 1: {
                    waktu_reminder.add(Calendar.MINUTE, -5);
                    pesanxreminder = "5 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 2: {
                    waktu_reminder.add(Calendar.MINUTE, -10);
                    pesanxreminder = "10 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 3: {
                    waktu_reminder.add(Calendar.MINUTE, -15);
                    pesanxreminder = "15 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 4: {
                    waktu_reminder.add(Calendar.MINUTE, -30);
                    pesanxreminder = "30 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 5: {
                    waktu_reminder.add(Calendar.MINUTE, -45);
                    pesanxreminder = "45 menit lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
                case 6: {
                    waktu_reminder.add(Calendar.MINUTE, -60);
                    pesanxreminder = "1 jam lagi sebelum tiba waktu shalat " + waktuadzannextshort;
                    break;
                }
            }

            Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);
            pendingIntentReminder = PendingIntent.getBroadcast(context, 1290, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmmanagerReminder = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Wakes up the device in Doze Mode
                MainActivity.alarmmanagerReminder.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, waktu_reminder.getTimeInMillis(), MainActivity.pendingIntentReminder);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Wakes up the device in Idle Mode
                MainActivity.alarmmanagerReminder.setExact(AlarmManager.RTC_WAKEUP, waktu_reminder.getTimeInMillis(), MainActivity.pendingIntentReminder);
            } else {
                // Old APIs
                MainActivity.alarmmanagerReminder.set(AlarmManager.RTC, waktu_reminder.getTimeInMillis(), MainActivity.pendingIntentReminder);
            }
        }

    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    public static void calculate_adzan(Context ctx, GregorianCalendar date) {
        TimeAdjustment adjustments;
       adjustments = new TimeAdjustment(BaseTimeAdjustmentType.TWO_MINUTES_ADJUSTMENT);

        TimeCalculator timecalc = new TimeCalculator();

                timecalc.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH, MainActivity.hanafi_asr_ratio, adjustments);


        MainActivity.prayerTimes = timecalc
                .date(date)
                .dateRelative(0)
                .location(MainActivity.lokasiGPS.getLatitude(),
                        MainActivity.lokasiGPS.getLongitude(),
                        MainActivity.lokasiGPS.getAltitude(), 0)
                //				.timeCalculationMethod(AngleCalculationType.MWL, MainActivity.hanafi_asr_ratio , adjustments)
                //				.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH)
                .calculateTimes();
        MainActivity.prayerTimes.setUseSecond(false);

    }

    public static void getupdatejadwalatas(Context context) {
        if ((waktu_alarm != null) && (waktu_alarm_last != null)) {
            MainActivity.calculate_adzan(context, new GregorianCalendar());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.collection_adzan_widget);
            ComponentName thisWidget = new ComponentName(context, CollectionAdzanWidget.class);

            waktu_now = Calendar.getInstance();
            waktu_now.set(Calendar.SECOND, 0);
            waktu_now.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat sdfjam = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formatterid = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterid_hari = new SimpleDateFormat("EEEE");
            GregorianCalendar gCal = new GregorianCalendar();
            Calendar uCal = new UmmalquraCalendar();
            uCal.setTime(gCal.getTime());
            uCal.add(Calendar.DAY_OF_MONTH, manual_hijriyyah - 2);

            thnarab = uCal.get(Calendar.YEAR);         // 1433
            String blnarab = uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);        // 2
            int tglarab = uCal.get(Calendar.DAY_OF_MONTH); // 20d
            formattglarab = pad(tglarab) + " " + blnarab + " " + thnarab + "H"; // Tuesday 8 Rabi' al-Awwal, 1433
            String formattgllokal = formatterid.format(new Date(waktu_now.getTimeInMillis()));
            String hr_ini = "";
            int harihijriyah = gCal.get(Calendar.DAY_OF_WEEK) - 1;
//			harihijriyah = harihijriyah + ((MainActivity.manual_hijriyyah - 2) * -1);
//			if (harihijriyah == 7)
//				harihijriyah = 0;
//			else if (harihijriyah == 8)
//				harihijriyah = 1;

            hr_ini = formatterid_hari.format(new Date(waktu_now.getTimeInMillis())).replace("Jumat", "Jum’at") + " / " + nama_hari_arab_ID[harihijriyah];

            remoteViews.setTextViewText(R.id.whari, context.getResources().getString(R.string.judul_hari) + " " + hr_ini);
            remoteViews.setTextViewText(R.id.wnowtime, formattgllokal + " / " + formattglarab);
            remoteViews.setTextViewText(R.id.wjudul, MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")");

            remoteViews.setTextViewText(R.id.wsubuhnownexttime, context.getResources().getString(R.string.shalat_subuh_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.FAJR)));
            if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.shalat_jumat) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR)));
            } else {
                remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.shalat_dzuhur_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR)));
            }
            remoteViews.setTextViewText(R.id.washarnownexttime, context.getResources().getString(R.string.shalat_ashar_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ASR)));
            remoteViews.setTextViewText(R.id.wmagribnownexttime, context.getResources().getString(R.string.shalat_maghrib_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.MAGHRIB)));
            remoteViews.setTextViewText(R.id.wisyanownexttime, context.getResources().getString(R.string.shalat_isya_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ISHA)));
            remoteViews.setTextColor(R.id.wsubuhnownexttime, ContextCompat.getColor(context, R.color.hitam));
            remoteViews.setTextColor(R.id.wdzuhurnownexttime, ContextCompat.getColor(context, R.color.hitam));
            remoteViews.setTextColor(R.id.washarnownexttime, ContextCompat.getColor(context, R.color.hitam));
            remoteViews.setTextColor(R.id.wmagribnownexttime, ContextCompat.getColor(context, R.color.hitam));
            remoteViews.setTextColor(R.id.wisyanownexttime, ContextCompat.getColor(context, R.color.hitam));

            remoteViews.setTextViewText(R.id.wsubuhnownextjudul, "");
            remoteViews.setInt(R.id.wsubuhnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setTextViewText(R.id.wdzuhurnownextjudul, "");
            remoteViews.setInt(R.id.wdzuhurnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setTextViewText(R.id.washarnownextjudul, "");
            remoteViews.setInt(R.id.washarnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setTextViewText(R.id.wmagribnownextjudul, "");
            remoteViews.setInt(R.id.wmagribnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setTextViewText(R.id.wisyanownextjudul, "");
            remoteViews.setInt(R.id.wisyanownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));

            remoteViews.setInt(R.id.wsubuhnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setInt(R.id.wdzuhurnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setInt(R.id.washarnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setInt(R.id.wmagribnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
            remoteViews.setInt(R.id.wisyanownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));

            int requestID = (int) System.currentTimeMillis();
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra("from_notif", "JADWALSHALAT");
            PendingIntent pendingIntentx = PendingIntent.getActivity(context, requestID,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.layout_body, pendingIntentx);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntentx);

            waktuatasjudul = "";
            waktuatastimehour = "";
            waktuatastime_h = "";
            waktuatastimemin = "";
            waktuatastime_min = "";

            if (MainActivity.lokasiGPS == null || MainActivity.lokasiGPS.getLatitude() == 0.0 || MainActivity.lokasiGPS.getLongitude() == 0.0) {
                remoteViews.setTextViewText(R.id.wjudul, context.getResources().getString(R.string.widget_wait_title));
                remoteViews.setTextViewText(R.id.wsubuhnownexttime, context.getResources().getString(R.string.shalat_subuh_name));
                if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.shalat_jumat));
                } else
                    remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.shalat_dzuhur_name));
                remoteViews.setTextViewText(R.id.washarnownexttime, context.getResources().getString(R.string.shalat_ashar_name));
                remoteViews.setTextViewText(R.id.wmagribnownexttime, context.getResources().getString(R.string.shalat_maghrib_name));
                remoteViews.setTextViewText(R.id.wisyanownexttime, context.getResources().getString(R.string.shalat_isya_name));

                remoteViews.setTextViewText(R.id.whari, context.getResources().getString(R.string.widget_wait_message));

            } else {
                switch (waktushalat) {
                    case 1: {
                        remoteViews.setTextViewText(R.id.wsubuhnownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                        remoteViews.setInt(R.id.wsubuhnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        remoteViews.setTextViewText(R.id.wdzuhurnownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                        remoteViews.setInt(R.id.wdzuhurnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        break;
                    }
                    case 2: {
                        remoteViews.setTextViewText(R.id.wdzuhurnownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                        remoteViews.setInt(R.id.wdzuhurnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        remoteViews.setTextViewText(R.id.washarnownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                        remoteViews.setInt(R.id.washarnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        break;
                    }
                    case 3: {
                        remoteViews.setTextViewText(R.id.washarnownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                        remoteViews.setInt(R.id.washarnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        remoteViews.setTextViewText(R.id.wmagribnownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                        remoteViews.setInt(R.id.wmagribnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        break;
                    }
                    case 4: {
                        remoteViews.setTextViewText(R.id.wmagribnownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                        remoteViews.setInt(R.id.wmagribnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        remoteViews.setTextViewText(R.id.wisyanownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                        remoteViews.setInt(R.id.wisyanownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        break;
                    }
                    case 5: {
                        remoteViews.setTextViewText(R.id.wisyanownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                        remoteViews.setInt(R.id.wisyanownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        remoteViews.setTextViewText(R.id.wsubuhnownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                        remoteViews.setInt(R.id.wsubuhnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                        break;
                    }
                }

                try {
                    //			long tDelta = Math.abs(MainActivity.waktu_now.getTimeInMillis() - MainActivity.waktu_alarm_last.getTimeInMillis());
                    //			long tDeltainminutes = TimeUnit.MILLISECONDS.toMinutes(MainActivity.waktu_now.getTimeInMillis() - MainActivity.waktu_alarm_last.getTimeInMillis());
                    int tDeltainminutes = (int) (((MainActivity.waktu_now.getTimeInMillis() - MainActivity.waktu_alarm_last.getTimeInMillis()) / 1000) / 60);
                    if (((tDeltainminutes <= 30) && (waktushalat == 1 || waktushalat == 4))
                            ||
                            ((tDeltainminutes <= 59) && (waktushalat != 1 && waktushalat != 4))
                    ) {
                        waktuatasjudul = MainActivity.waktuadzanskg;
                        waktuatastimemin = "+ " + MainActivity.pad(tDeltainminutes);
                        waktuatastime_min = "menit";
                        wnownextjudul = context.getResources().getString(R.string.shalat_sekarang);

                        switch (waktushalat) {
                            case 1: {
                                remoteViews.setInt(R.id.wsubuhnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wsubuhnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 2: {
                                if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                                    waktuatasjudul = context.getResources().getString(R.string.shalat_jumat);
                                    remoteViews.setTextViewText(R.id.wdzuhurnownexttime, waktuatasjudul + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR)));
                                }

                                remoteViews.setInt(R.id.wdzuhurnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wdzuhurnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 3: {
                                remoteViews.setInt(R.id.washarnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.washarnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 4: {
                                remoteViews.setInt(R.id.wmagribnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wmagribnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 5: {
                                remoteViews.setInt(R.id.wisyanownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wisyanownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                        }
                    } else {
                        waktuatasjudul = waktuadzannextshort;
                        wnownextjudul = context.getResources().getString(R.string.shalat_berikutnya);

                        //				tDeltainminutes = TimeUnit.MILLISECONDS.toMinutes(MainActivity.waktu_alarm.getTimeInMillis() - MainActivity.waktu_now.getTimeInMillis()) + 1;
                        tDeltainminutes = (int) (((MainActivity.waktu_alarm.getTimeInMillis() - MainActivity.waktu_now.getTimeInMillis()) / 1000) / 60);
                        Calendar waktu_syuruq = Calendar.getInstance();
                        waktu_syuruq.setTime(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE));
                        waktu_syuruq.add(Calendar.MINUTE, 15);

                        int tDeltainminutes_min15 = (int) (((waktu_syuruq.getTimeInMillis() - MainActivity.waktu_now.getTimeInMillis()) / 1000) / 60);

                        if ((waktuatasjudul.equals(context.getResources().getString(R.string.shalat_dzuhur_name))
                                || waktuatasjudul.equals(context.getResources().getString(R.string.shalat_jumat)))
                                && (tDeltainminutes_min15 <= 15 && tDeltainminutes_min15 >= 0)) {
                            waktuatastimemin = "+ " + MainActivity.pad(15 - tDeltainminutes_min15);
                            waktuatastime_min = "menit";
                            waktuatasjudul = context.getResources().getString(R.string.syuruq_name);
                            wnownextjudul = context.getResources().getString(R.string.dilarang_shalat);

                        } else {
                            int Hours = tDeltainminutes / 60;
                            int Minutes = tDeltainminutes % 60;
                            //				if (tDeltainminutes <= 59) {
                            if (tDeltainminutes < 0) {
                                Hours = 0;
                                Minutes = 0;
                            }
                            if (Hours == 0) {
                                if (Minutes == 0) {
                                    waktuatastimemin = "+ 00";
                                    waktuatastime_min = "menit";
//                                    wnownextjudul = context.getResources().getString(R.string.shalat_sekarang);
//                                    if (MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
//                                        if (!MainActivity.var_daerah.equals("") && !MainActivity.waktuatasjudul.equals("")) {
//                                            MainActivity.showNotification_biasa(context, MainActivity.NOTIF_DOWNLOAD, "Hours = 0, Minute = 0", "MainActivity");
//
//                                            MainActivity.notif_adzan_now(context);
//
//                                        }
//                                    }

                                } else {
                                    waktuatastimemin = "- " + MainActivity.pad(Minutes);
                                    waktuatastime_min = "menit";

                                    if (MainActivity.sudahdoaadzan && !lg_adzan) {
                                        try {
                                            MainActivity.cancelNotification(context, MainActivity.NOTIF_ADZAN);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            } else {
                                waktuatastime_h = "jam";
                                waktuatastime_min = "menit";

                                //					int timemin = (int) tDeltainminutes - (int) (TimeUnit.MILLISECONDS.toHours(MainActivity.waktu_alarm.getTimeInMillis() - MainActivity.waktu_now.getTimeInMillis()) * 60);
                                //					waktuatastimehour = "- " + MainActivity.pad((int) TimeUnit.MILLISECONDS.toHours(MainActivity.waktu_alarm.getTimeInMillis() - MainActivity.waktu_now.getTimeInMillis()));
                                //					waktuatastimemin = MainActivity.pad(timemin);
                                waktuatastimehour = "- " + MainActivity.pad(Hours);
                                waktuatastimemin = MainActivity.pad(Minutes);

                                if (MainActivity.sudahdoaadzan && !lg_adzan) {
                                    try {
                                        MainActivity.cancelNotification(context, MainActivity.NOTIF_ADZAN);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }

                        switch (waktushalat) {
                            case 1: {
                                if (waktu_alarm.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                                    if (!waktuatasjudul.equals(context.getResources().getString(R.string.syuruq_name))) {
                                        waktuatasjudul = context.getResources().getString(R.string.shalat_jumat);
                                        waktuadzannextshort = waktuatasjudul;
                                        remoteViews.setTextViewText(R.id.wdzuhurnownexttime, waktuatasjudul + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.ZUHR)));
                                    }
                                }

                                remoteViews.setInt(R.id.wdzuhurnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wdzuhurnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 2: {
                                remoteViews.setInt(R.id.washarnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.washarnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 3: {
                                remoteViews.setInt(R.id.wmagribnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wmagribnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 4: {
                                remoteViews.setInt(R.id.wisyanownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wisyanownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                            case 5: {
                                remoteViews.setInt(R.id.wsubuhnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                                remoteViews.setTextColor(R.id.wsubuhnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
//                    MainActivity.toast_info(mContext, e.getMessage());
                }
            }

            if (MainActivity.lokasiGPS == null || (MainActivity.lokasiGPS.getLatitude() == 0.0 && MainActivity.lokasiGPS.getLongitude() == 0.0))
                remoteViews.setTextViewText(R.id.wjudul, context.getResources().getString(R.string.mainmenu_gps_unknown));

//            if (countChar(waktuatastimehour + waktuatastime_h + waktuatastimemin + waktuatastime_min, '-') == 2 ||
//                    (countChar(waktuatastimehour + waktuatastime_h + waktuatastimemin + waktuatastime_min, '+') == 1 &&
//                            countChar(waktuatastimehour + waktuatastime_h + waktuatastimemin + waktuatastime_min, '-') == 1)) {
//                waktuatasjudul = "System time error";
//                remoteViews.setViewVisibility(R.id.lay_time, View.GONE);
//                waktuatastimemin = "";
//                waktuatastime_min = "";
//            } else remoteViews.setViewVisibility(R.id.lay_time, View.VISIBLE);
//            showNotification_biasa(context, MainActivity.NOTIF_BIASA, "waktu atas judul ", waktuatasjudul);

            remoteViews.setTextViewText(R.id.wnextjadwalshalat, waktuatasjudul);
            remoteViews.setTextViewText(R.id.wnextjadwaltimehour, waktuatastimehour);
            remoteViews.setTextViewText(R.id.wnextjadwaltime_h, waktuatastime_h);
            remoteViews.setTextViewText(R.id.wnextjadwaltimemin, waktuatastimemin);
            remoteViews.setTextViewText(R.id.wnextjadwaltime_min, waktuatastime_min);

            if (waktuatasjudul.equals(context.getResources().getString(R.string.shalat_subuh_name)) && waktushalat == 1) {
                remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.syuruq_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE)));

            } else if (waktuatasjudul.equals(context.getResources().getString(R.string.syuruq_name))) {
                if (MainActivity.wnownextjudul.equals(context.getResources().getString(R.string.dilarang_shalat))) {
                    remoteViews.setTextViewText(R.id.wsubuhnownextjudul, context.getResources().getString(R.string.shalat_sekarang));
                    remoteViews.setInt(R.id.wsubuhnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));

                    remoteViews.setTextViewText(R.id.wsubuhnownexttime, context.getResources().getString(R.string.syuruq_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE)));
                    remoteViews.setInt(R.id.wsubuhnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                    remoteViews.setTextColor(R.id.wsubuhnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                    remoteViews.setTextColor(R.id.wdzuhurnownexttime, ContextCompat.getColor(context, R.color.hitam));
                    remoteViews.setInt(R.id.wdzuhurnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));
                } else {
                    remoteViews.setTextColor(R.id.wdzuhurnownexttime, ContextCompat.getColor(context, R.color.warna_backgound));
                    remoteViews.setTextViewText(R.id.wdzuhurnownexttime, context.getResources().getString(R.string.syuruq_name) + "\n" + sdfjam.format(MainActivity.prayerTimes.getPrayTime(PrayersType.SUNRISE)));
                    remoteViews.setInt(R.id.wdzuhurnownexttime, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
                }

                remoteViews.setTextViewText(R.id.wdzuhurnownextjudul, context.getResources().getString(R.string.shalat_berikutnya));
                remoteViews.setInt(R.id.wdzuhurnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.green_transx));
            } else if (waktuatasjudul.equals(context.getResources().getString(R.string.shalat_dzuhur_name)) ||
                    waktuatasjudul.equals(context.getResources().getString(R.string.shalat_jumat))) {
                remoteViews.setTextViewText(R.id.wsubuhnownextjudul, "");
                remoteViews.setInt(R.id.wsubuhnownextjudul, "setBackgroundColor", ContextCompat.getColor(context, R.color.putih_trans));

            }
            //				Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
            //				animation.setDuration(500); //1 second duration for each animation cycle
            //				animation.setInterpolator(new LinearInterpolator());
            //				animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
            //				animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
            //				remoteViews.setInt(R.id.wnextjadwalshalat,"startAnimation", animation.get);
            //				wnextjadwaltimehour.startAnimation(animation);
            //				wnextjadwaltime_h.startAnimation(animation);
            //				wnextjadwaltimemin.startAnimation(animation);
            //				wnextjadwaltime_min.startAnimation(animation);
            //				wnownextjudul.startAnimation(animation); //to start animation
            //			}else {
            //				wnextjadwalshalat.clearAnimation();
            //				wnextjadwaltimehour.clearAnimation();
            //				wnextjadwaltime_h.clearAnimation();
            //				wnextjadwaltimemin.clearAnimation();
            //				wnextjadwaltime_min.clearAnimation();
            //				wnownextjudul.clearAnimation();
            //			}


            appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            ///////////////////////////////////

            Intent intentx = new Intent("LATLANGRECEIVER"); //FILTER is a string to identify this intent
            intentx.putExtra("status", "SUCCESS");
            //		intentx.putExtra("var_daerah", var_daerah);
            //		intentx.putExtra("var_negara", var_negara);
            //		intentx.putExtra("var_kota", var_kota);
            //
            //		intentx.putExtra("kiblatdegree", kiblatdegree);
            //		intentx.putExtra("arahkiblat", arahkiblat);
            //		intentx.putExtra("jarakkekiblat", jarakkekiblat);
            //		intentx.putExtra("latlangkabah", latlangkabah);


            context.sendBroadcast(intentx);
        }
        ///////////////////////////////////////
    }

    private static void show_GPS_setting(Context ctx) {
        if (checkPlayServices(ctx)) {
            LocationRequest mLocationRequestAccuracy = LocationRequest.create();
            mLocationRequestAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequestAccuracy.setInterval(UPDATE_FREQ);
            mLocationRequestAccuracy.setFastestInterval(FASTEST_UPDATE_FREQ);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequestAccuracy);
            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(ctx).checkLocationSettings(builder.build());

            result.addOnCompleteListener(task -> {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                    //sudah nyala
                    update_lokasi_terbaru(ctx);

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }
            });

        }

    }

    public static void askSetting_cek_gps_and_update(final Context ctx) {
        if (checkLocationPermission(ctx)) {
            if (isLocationEnabled(ctx)) {
                update_lokasi_terbaru(ctx);
            } else
                show_GPS_setting(ctx);
        }
    }

    private static void update_lokasi_terbaru(Context ctx) {
        if (checkPlayServices(ctx)) {
            LocationRequest mLocationRequestAccuracy = LocationRequest.create();
            mLocationRequestAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequestAccuracy.setInterval(MainActivity.UPDATE_FREQ);
            mLocationRequestAccuracy.setFastestInterval(MainActivity.FASTEST_UPDATE_FREQ);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(mContext, location -> {
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    // Logic to handle location object
                    MainActivity.lokasiGPS = location;
                    updateLocationUI(ctx);
                }
            });

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            MainActivity.lokasiGPS = location;

                            updateLocationUI(ctx);
                        }
                    }
                }
            };

            mFusedLocationClient.requestLocationUpdates(mLocationRequestAccuracy, locationCallback, null);

        }
    }

    private class GetPlacesLatLang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch markerOptions
            StringBuilder placesBuilder = new StringBuilder();
            for (String placeSearchURL : placesURL) {
                try {
                    URL requestUrl = new URL(placeSearchURL);
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        if (inputStream == null) {
                            return "";
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            placesBuilder.append(line + "\n");
                        }

                        if (placesBuilder.length() == 0) {
                            return "";
                        }

                    } else {
                        return "";
                    }
                } catch (MalformedURLException e) {
                    return "";
                } catch (IOException e) {
                    return "";
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            LatLng placeLL = null;
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONObject placesArray = resultObject.getJSONObject("result").getJSONObject("geometry")
                        .getJSONObject("location");
                placeLL = new LatLng(Double.valueOf(placesArray.getString("lat")),
                        Double.valueOf(placesArray.getString("lng")));

            } catch (Exception e) {
                e.printStackTrace();
                    MainActivity.toast_info(MainActivity.this, "Gagal mengambil data dari server.");
            }

            if (placeLL != null)
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLL, 10));

        }

    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch markerOptions
            updateFinished = false;
            StringBuilder placesBuilder = new StringBuilder();
            for (String placeSearchURL : placesURL) {
                try {
                    URL requestUrl = new URL(placeSearchURL);
                    HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        if (inputStream == null) {
                            return "";
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            placesBuilder.append(line + "\n");
                        }

                        if (placesBuilder.length() == 0) {
                            return "";
                        }

                    } else {
                        return "";
                    }
                } catch (MalformedURLException e) {
                    return "";
                } catch (IOException e) {
                    return "";
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("predictions");

                arr_list_address.clear();
                list_address.clear();
                for (int p = 0; p < placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    String place_id = "";
                    String description = "";

                    try {
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        description = placeObject.getString("description");
                        place_id = placeObject.getString("place_id");
                    } catch (JSONException jse) {
                    }

                    //add ke list
                    if (!place_id.equals("")) {
                        HashMap<String, String> map_list = new HashMap<String, String>();
                        map_list.put("place_id", place_id);
                        map_list.put("description", description);
                        //						map_list.put("latitude", "" + placeLL.latitude);
                        //						map_list.put("longitude", "" + placeLL.longitude);

                        arr_list_address.add(map_list);
                        list_address.add(description);

                    }
                }

                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_text, list_address);
                edinput.setAdapter(dataAdapter1);
                edinput.showDropDown();

                //				dataAdapterinput.notifyDataSetChanged();
                updateFinished = true;

            } catch (Exception e) {
                e.printStackTrace();
                    MainActivity.toast_info(MainActivity.this, "Gagal mengambil data dari server.");
            }

        }

    }

    public class MyGeocoderx extends AsyncTask<String, String, String> {
        private Context contextx;
        int maxResult = 1;
        private String nama_daerah;

        public MyGeocoderx(Context ctx) {
            // TODO Auto-generated constructor stub
            this.contextx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            URL geocodeUrl;
            HttpURLConnection connection;
            int responseCode = 0;
            var_daerahx = "";
            var_negarax = "";
            var_kotax = "";
            nama_daerah = "";
            String vsub = "", var_kota2 = "";
            Geocoder geocoder = new Geocoder(contextx, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(newLocation.latitude, newLocation.longitude, 1);
                if (addresses.size() > 0) {
                    var_daerahx = addresses.get(0).getLocality();
                    vsub = addresses.get(0).getSubLocality();
                    var_negarax = addresses.get(0).getCountryName();
                    var_kotax = addresses.get(0).getSubAdminArea();
                    var_kota2 = addresses.get(0).getAdminArea();

                }
            } catch (IOException e) {
                try {
                    geocodeUrl = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + newLocation.latitude
                            + "," + newLocation.longitude + "&sensor=false&key=" + contextx.getResources().getString(R.string.google_api_key_geocode)); //keygeocoding
                    connection = (HttpURLConnection) geocodeUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null) {
                            // Nothing to do.
                            return null;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            buffer.append(line + "\n");
                        }

                        if (buffer.length() == 0) {
                            return null;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(buffer.toString());
                            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                                JSONArray results = jsonObject.getJSONArray("results");
                                if (results.length() > 0) {
                                    for (int i = 0; i < results.length() && i < maxResult; i++) {
                                        JSONObject result = results.getJSONObject(i);
                                        JSONArray components = result.getJSONArray("address_components");
                                        for (int a = 0; a < components.length(); a++) {
                                            JSONObject component = components.getJSONObject(a);
                                            JSONArray types = component.getJSONArray("types");
                                            for (int j = 0; j < types.length(); j++) {
                                                String type = types.getString(j);
                                                if (type.equals("administrative_area_level_4")) {
                                                    vsub = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_3")) {
                                                    var_daerahx = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_2")) {
                                                    var_kotax = component.getString("long_name");
                                                } else if (type.equals("administrative_area_level_1")) {
                                                    var_kota2 = component.getString("long_name");
                                                } else if (type.equals("country")) {
                                                    var_negarax = component.getString("long_name");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException ez) {
                        }

                    }

                } catch (MalformedURLException e2) {
                } catch (ProtocolException e1) {
                } catch (IOException ec) {
                }
            }

            if (var_daerahx == null || var_daerahx.equals("null")) var_daerahx = "";
            if (vsub == null || vsub.equals("null")) vsub = "";

            if (var_daerahx.equals("") && vsub.equals(""))
                var_daerahx = "";
            else if (!var_daerahx.equals("") && !vsub.equals("") && !vsub.equals(var_daerahx))
                var_daerahx = vsub + ", " + var_daerahx;
            else if (var_daerahx.equals("") && !vsub.equals(""))
                var_daerahx = vsub;


            if (var_kotax == null || var_kotax.equals("null")) var_kotax = "";
            if (var_kota2 == null || var_kota2.equals("null")) var_kota2 = "";

            if (var_kotax.equals("") && var_kota2.equals(""))
                var_kotax = "";
            else if (!var_kotax.equals("") && !var_kota2.equals("") && !var_kota2.equals(var_kotax))
                var_kotax = var_kota2 + ", " + var_kotax;
            else if (var_kotax.equals("") && !var_kota2.equals(""))
                var_kotax = var_kota2;

            if(var_daerahx.equals("") && var_kotax.equals(""))
                nama_daerah = contextx.getResources().getString(R.string.mainmenu_gps_unknown);
            else if(!var_daerahx.equals("") && !var_kotax.equals(""))
                nama_daerah = var_daerahx + ", " + var_kotax;
            else if(!var_daerahx.equals(""))
                nama_daerah = var_daerahx;
            else
                nama_daerah = var_kotax;

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                DecimalFormat df = new DecimalFormat("#.##");
                double kiblatdegree = QiblaUtils.qibla(newLocation.latitude, newLocation.longitude);

                String arahkiblat = getResources().getString(R.string.shalattools_arahkiblat)
                        + " " + df.format(kiblatdegree)
                        + getResources().getString(R.string.shalattools_dariutara)
                        + " " + QiblaUtils.direction(newLocation,
                        new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE));
                String jarakkekiblat = getResources().getString(R.string.shalattools_jarakkekiblat)
                        + " " + df.format(QiblaUtils.HitungJarak(newLocation.latitude, newLocation.longitude) / 1000)
                        + " Km";

                String ttl = getResources().getString(R.string.shalattools_manual_title);

                if (nama_daerah.equals(contextx.getResources().getString(R.string.mainmenu_gps_unknown)))
                    judul_pesanx.setText(ttl + "\n" + contextx.getResources().getString(R.string.mainmenu_gps_unknown));
                else
                    judul_pesanx.setText(ttl  + "\n" +
                            nama_daerah + " (" + var_negarax + ")\n" + arahkiblat + "\n" + jarakkekiblat);
            } catch (Exception e) {
            }
        }
        ///////////////////////////////////////
    }

    @SuppressWarnings("deprecation")
    protected void atur_manual_peta() {
        final Dialog dialogpesan = new Dialog(MainActivity.this);
        dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpesan.setContentView(R.layout.layout_dialog_message);
        dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
        dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogpesan.setCancelable(true);
        dialogpesan.setCanceledOnTouchOutside(false);

        TextView judul_pesan = dialogpesan.findViewById(R.id.judul_pesan);
        TextView isi_pesan = dialogpesan.findViewById(R.id.isi_pesan);
        judul_pesan.setText(getResources().getString(R.string.shalattools_kiblat_manualtitle));
        isi_pesan.setText(getResources().getString(R.string.shalattools_kiblat_manualinfo));

        Button btn2 = dialogpesan.findViewById(R.id.btn2);
        btn2.setText(getResources().getString(R.string.str_btnbatal));
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogpesan.dismiss();

            }
        });

        Button btn1 = dialogpesan.findViewById(R.id.btn1);
        btn1.setText(getResources().getString(R.string.str_btnlanjut));
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            public void onClick(View v) {
                dialogpesan.dismiss();

                int screenwidth;
                int screenheight;
                if (android.os.Build.VERSION.SDK_INT >= 13) {
                    Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    screenwidth = size.x;
                    screenheight = size.y;
                } else {
                    Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
                    screenwidth = display.getWidth();
                    screenheight = display.getHeight();
                }
                Double xwidth = screenwidth * 0.8;
                Double xheight = screenheight * 0.8;

                final Dialog dialogpesanx = new Dialog(MainActivity.this);
                dialogpesanx.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesanx.setContentView(R.layout.layout_peta_kiblat);
                dialogpesanx.getWindow().setLayout(xwidth.intValue(), xheight.intValue());
                dialogpesanx.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesanx.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesanx.setCancelable(false);
                dialogpesanx.setCanceledOnTouchOutside(false);

                list_address.clear();
                edinput = dialogpesanx.findViewById(R.id.edinput);
                //				dataAdapterinput = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_text ,list_address);
                //				edinput.setAdapter(dataAdapterinput);
                edinput.setVisibility(View.VISIBLE);
                edinput.setOnTouchListener((v1, event) -> {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (edinput.getRight() - edinput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            edinput.setText("");
                            list_address.clear();
                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_text, list_address);
                            edinput.setAdapter(dataAdapter1);

                            return true;
                        }

                    }

                    if (!edinput.getText().toString().trim().equals("")) edinput.showDropDown();
                    return false;
                });
                edinput.setOnItemClickListener((parent, view, position, rowId) -> {
                    String selection = (String) parent.getItemAtPosition(position);

                    if (is_NetworkAvailable(MainActivity.this)) {
                        //				        	MainActivity.toast_info(MainActivity.this, position + " - " + selection);
                        String key = "key=" + MainActivity.this.getResources().getString(R.string.google_api_key_place);//key google places

                        String placeid = "place_id=" + arr_list_address.get(position).get("place_id");

                        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/details/" +
                                "json?" + placeid + "&" + key;

                        GetPlacesLatLang myplaces = new GetPlacesLatLang();
                        if (Build.VERSION.SDK_INT >= 11) {
                            myplaces.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, placesSearchStr);
                        } else {
                            myplaces.execute(selection);
                        }
                    }

                });
                edinput.setThreshold(1);
                edinput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (edinput.getText().toString().length() >= 2)
                                if (updateFinished) {
                                    if (is_NetworkAvailable(MainActivity.this)) {
                                        // Obtain browser key from https://code.google.com/apis/console
                                        String key = "key=" + MainActivity.this.getResources().getString(R.string.google_api_key_place);//key google places

                                        String input = "";

                                        try {
                                            input = "input=" + URLEncoder.encode(edinput.getText().toString().trim(), "utf-8");
                                        } catch (UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                        }

                                        if (!input.equals("")) {
                                            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/autocomplete/" +
                                                    "json?" + input + "&" + key;

                                            GetPlaces myplaces = new GetPlaces();
                                            if (Build.VERSION.SDK_INT >= 11) {
                                                myplaces.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, placesSearchStr);
                                            } else {
                                                myplaces.execute(placesSearchStr);
                                            }
                                        }

                                    } else {
                                            MainActivity.toast_info(MainActivity.this, "Koneksi internet tidak ditemukan.\nMohon aktifkan koneksi internet anda.");
                                    }

                                    return true;
                                }

                        }
                        return false;
                    }
                });

                judul_pesanx = dialogpesanx.findViewById(R.id.judul_pesan);
                judul_pesanx.setText(MainActivity.this.getResources().getString(R.string.shalattools_manual_title));

                //				FragmentManager myFragmentManager = MainActivity.this.getFragmentManager();
                FragmentManager myFragmentManager = MainActivity.this.getSupportFragmentManager();

                //				final MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);
                final SupportMapFragment myMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.mapViewKiblat);
                //				final GoogleMap myMap = myMapFragment.getMap();
                myMapFragment.getMapAsync(arg0 -> {
                    myMap = arg0;

                    //https://mapstyle.withgoogle.com/ bikin dulu dr sini
                    //class nya MapStyleManager.java

                    final int maxResult = 1;
                    handler = new Handler() {
                        public void handleMessage(Message msg) {
                            if (msg.what == MESSAGE_ID_SAVE_CAMERA_POSITION) {
                                lastCameraPosition = myMap.getCameraPosition();
                            } else if (msg.what == MESSAGE_ID_READ_CAMERA_POSITION) {
                                if (lastCameraPosition.equals(myMap.getCameraPosition())) {
                                    //					                    Log.d(LOG, "Camera position stable");
                                    newLocation = myMap.getCameraPosition().target;

                                    if (newLocation != null)
                                        if (newLocation.latitude != 0.0 && newLocation.longitude != 0.0) {
                                            if (is_NetworkAvailable(MainActivity.this)) {
                                                MyGeocoderx mygeo = new MyGeocoderx(MainActivity.this);
                                                if (Build.VERSION.SDK_INT >= 11) {
                                                    mygeo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                } else {
                                                    mygeo.execute();
                                                }
                                            }

                                        }
                                }
                            }
                        }
                    };

                    try {
                        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
//									return;
                            } else myMap.setMyLocationEnabled(true);

                        } catch (Exception e) {
                        }
                        myMap.setTrafficEnabled(false);
                        myMap.getUiSettings().setCompassEnabled(true);
                        myMap.getUiSettings().setRotateGesturesEnabled(true);
                        myMap.getUiSettings().setTiltGesturesEnabled(true);
                        myMap.getUiSettings().setMyLocationButtonEnabled(true);
                        myMap.getUiSettings().setZoomControlsEnabled(true);
                        myMap.getUiSettings().setAllGesturesEnabled(true);

                        myMap.clear();

                        myMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition arg0) {
                                handler.removeMessages(MESSAGE_ID_SAVE_CAMERA_POSITION);
                                handler.removeMessages(MESSAGE_ID_READ_CAMERA_POSITION);
                                handler.sendEmptyMessageDelayed(MESSAGE_ID_SAVE_CAMERA_POSITION, 300);
                                handler.sendEmptyMessageDelayed(MESSAGE_ID_READ_CAMERA_POSITION, 600);
                            }
                        });

                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()), 10));

                    } catch (Exception e) {
                        MainActivity.toast_info(MainActivity.this, "Google Maps Error");
                    }
                });

                Button btn1 = dialogpesanx.findViewById(R.id.btn1);
                btn1.setText(MainActivity.this.getResources().getString(R.string.str_btnsimpan));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //simpan
                        if (newLocation != null && newLocation.latitude != 0.0 && newLocation.longitude != 0.0) {
                            //							FragmentTransaction ft2 = MainActivity.this.getFragmentManager().beginTransaction();
                            FragmentTransaction ft2 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                            ft2.remove(myMapFragment);
                            ft2.commit();

                            dialogpesanx.dismiss();

                            MainActivity.lokasiGPS.setLatitude(newLocation.latitude);
                            MainActivity.lokasiGPS.setLongitude(newLocation.longitude);

                            DecimalFormat df = new DecimalFormat("#.##");
                            MainActivity.kiblatdegree = QiblaUtils.qibla(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude());
                            MainActivity.arahkiblat = MainActivity.this.getResources().getString(R.string.shalattools_arahkiblat)
                                    + " " + df.format(MainActivity.kiblatdegree)
                                    + MainActivity.this.getResources().getString(R.string.shalattools_dariutara)
                                    + " " + QiblaUtils.direction(newLocation,
                                    new LatLng(com.azan.Constants.KAABA_LATITUDE, com.azan.Constants.KAABA_LONGITUDE));
                            MainActivity.jarakkekiblat = MainActivity.this.getResources().getString(R.string.shalattools_jarakkekiblat)
                                    + " " + df.format(QiblaUtils.HitungJarak(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude()) / 1000)
                                    + " Km";

                            if (!var_daerahx.equals(MainActivity.this.getResources().getString(R.string.mainmenu_gps_unknown))) {
                                MainActivity.var_daerah = var_daerahx;
                                MainActivity.var_negara = var_negarax;
                                MainActivity.var_kota = var_kotax;
                            }

                            SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("iMoslem", Context.MODE_PRIVATE).edit();
                            editor.putString("lastlat", "" + MainActivity.lokasiGPS.getLatitude());
                            editor.putString("lastlong", "" + MainActivity.lokasiGPS.getLongitude());
                            editor.putString("var_daerah", MainActivity.var_daerah);
                            editor.putString("var_negara", MainActivity.var_negara);
                            editor.putString("var_kota", MainActivity.var_kota);
                            editor.putString("arahkiblat", MainActivity.arahkiblat);
                            editor.putString("jarakkekiblat", MainActivity.jarakkekiblat);
                            editor.commit();

                            if (MainActivity.alarmmanagerAdzan != null) {
                                MainActivity.getjadwalshalat(MainActivity.this);

                            } else MainActivity.start_alarm_adzan(MainActivity.this);

                            Intent intentx = new Intent("LATLANGRECEIVER"); //FILTER is a string to identify this intent
                            intentx.putExtra("status", "SUCCESS");
                            //				intentx.putExtra("var_daerah", var_daerah);
                            //				intentx.putExtra("var_negara", var_negara);
                            //				intentx.putExtra("var_kota", var_kota);
                            //
                            //				intentx.putExtra("kiblatdegree", kiblatdegree);
                            //				intentx.putExtra("arahkiblat", arahkiblat);
                            //				intentx.putExtra("jarakkekiblat", jarakkekiblat);
                            //				intentx.putExtra("latlangkabah", latlangkabah);

                            MainActivity.this.sendBroadcast(intentx);
                        } else MainActivity.toast_info(MainActivity.this, "Cannot Save location");
                    }
                });
                Button btn2 = dialogpesanx.findViewById(R.id.btn2);
                btn2.setText(MainActivity.this.getResources().getString(R.string.str_btnbatal));
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //						FragmentTransaction ft2 = MainActivity.this.getFragmentManager().beginTransaction();
                        FragmentTransaction ft2 = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        ft2.remove(myMapFragment);
                        ft2.commit();

                        dialogpesanx.dismiss();
                    }
                });
                ImageView imarker = dialogpesanx.findViewById(R.id.imarker);
                imarker.setVisibility(View.VISIBLE);

                dialogpesanx.show();
            }
        });

        dialogpesan.show();

    }

    public void calculate_adzan_lokal(GregorianCalendar date) {
        TimeAdjustment adjustments;
            adjustments = new TimeAdjustment(BaseTimeAdjustmentType.TWO_MINUTES_ADJUSTMENT);

        TimeCalculator timecalc = new TimeCalculator();

                timecalc.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH, MainActivity.hanafi_asr_ratio, adjustments);

        lokal_prayerTimes = timecalc
                .date(date)
                .dateRelative(0)
                .location(MainActivity.lokasiGPS.getLatitude(),
                        MainActivity.lokasiGPS.getLongitude(),
                        MainActivity.lokasiGPS.getAltitude(), 0)
                //				.timeCalculationMethod(AngleCalculationType.MWL, MainActivity.hanafi_asr_ratio , adjustments)
                //.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH)
                .calculateTimes();
        lokal_prayerTimes.setUseSecond(false);

    }


    protected void show_info() {
        if (MainActivity.lokasiGPS != null && MainActivity.lokasiGPS.getLatitude() != 0.0 && MainActivity.lokasiGPS.getLongitude() != 0.0) {
            final SharedPreferences prefs = getSharedPreferences("iMoslem", Context.MODE_PRIVATE);

            try {
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.add(GregorianCalendar.DAY_OF_MONTH, MainActivity.added_day);
                calculate_adzan_lokal(gcal);

                SimpleDateFormat formatterid_hari = new SimpleDateFormat("EEEE");
                SimpleDateFormat formatterid = new SimpleDateFormat("dd MMMM yyyy");
                SimpleDateFormat sdfjam = new SimpleDateFormat("HH:mm");

                waktu_now = Calendar.getInstance();
                waktu_now.add(Calendar.DAY_OF_MONTH, MainActivity.added_day);
                waktu_now.set(Calendar.SECOND, 0);
                waktu_now.set(Calendar.MILLISECOND, 0);

                //			GregorianCalendar gCal = new GregorianCalendar();
                Calendar uCal = new UmmalquraCalendar();
                //			uCal.setTime(gCal.getTime());
                uCal.setTime(gcal.getTime());
                //uCal.add(GregorianCalendar.DAY_OF_MONTH, MainActivity.added_day);
                //			uCal.add(Calendar.DAY_OF_MONTH, MainActivity.added_day + MainActivity.manual_hijriyyah - 2);
                uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);


                thnarab = uCal.get(Calendar.YEAR);         // 1433
                blnarab = uCal.get(Calendar.MONTH);
                String blnarabx = "";
                int harihijriyah = gcal.get(Calendar.DAY_OF_WEEK) - 1;
//			harihijriyah = harihijriyah + ((MainActivity.manual_hijriyyah - 2) * -1);
//			if (harihijriyah == 7)
//				harihijriyah = 0;
//			else if (harihijriyah == 8)
//				harihijriyah = 1;
//			else if (harihijriyah == -1)
//				harihijriyah = 6;
//			else if (harihijriyah == -2)
//				harihijriyah = 5;

                    hr_ini = formatterid_hari.format(new Date(waktu_now.getTimeInMillis())).replace("Jumat", "Jum’at") + " / " + nama_hari_arab_ID[harihijriyah];
                    blnarabx = uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);        // 2 //long bulan in, short bulan en
                tglarab = uCal.get(Calendar.DAY_OF_MONTH); // 20d
                String formattglarab = pad(tglarab) + " " + blnarabx + " " + thnarab + "H"; // Tuesday 8 Rabi' al-Awwal, 1433
                String formattgllokal = formatterid.format(new Date(waktu_now.getTimeInMillis()));

                wnownextjudulx.setVisibility(View.VISIBLE);
                wnextjadwalshalat.setVisibility(View.VISIBLE);
                wnextjadwaltimehour.setVisibility(View.VISIBLE);
                wnextjadwaltime_h.setVisibility(View.VISIBLE);
                wnextjadwaltimemin.setVisibility(View.VISIBLE);
                wnextjadwaltime_min.setVisibility(View.VISIBLE);
                refresh_day.setVisibility(View.VISIBLE);
                txtback1.setVisibility(View.VISIBLE);
                txtback2.setVisibility(View.VISIBLE);

                if (MainActivity.added_day == 0) {
                    whari.setText(getResources().getString(R.string.judul_hari) + "\n" + hr_ini);
                    refresh_day.setVisibility(View.INVISIBLE);
                    txtback1.setVisibility(View.INVISIBLE);
                    txtback2.setVisibility(View.INVISIBLE);
                } else if (MainActivity.added_day < 1) {
                    whari.setText(getResources().getString(R.string.judul_hari2) + "\n" + hr_ini);
                    wnownextjudulx.setVisibility(View.INVISIBLE);
                    wnextjadwalshalat.setVisibility(View.INVISIBLE);
                    wnextjadwaltimehour.setVisibility(View.INVISIBLE);
                    wnextjadwaltime_h.setVisibility(View.INVISIBLE);
                    wnextjadwaltimemin.setVisibility(View.INVISIBLE);
                    wnextjadwaltime_min.setVisibility(View.INVISIBLE);
                } else {
                    whari.setText(getResources().getString(R.string.judul_hari3) + "\n" + hr_ini);
                    wnownextjudulx.setVisibility(View.INVISIBLE);
                    wnextjadwalshalat.setVisibility(View.INVISIBLE);
                    wnextjadwaltimehour.setVisibility(View.INVISIBLE);
                    wnextjadwaltime_h.setVisibility(View.INVISIBLE);
                    wnextjadwaltimemin.setVisibility(View.INVISIBLE);
                    wnextjadwaltime_min.setVisibility(View.INVISIBLE);
                }

                if (!MainActivity.var_daerah.equals(getResources().getString(R.string.mainmenu_gps_unknown))) {
                    if (!MainActivity.var_kota.equals(""))
                        wjudul.setText(MainActivity.var_daerah + ", " + MainActivity.var_kota + " (" + MainActivity.var_negara + ")");
                    else
                        wjudul.setText(MainActivity.var_daerah + ", (" + MainActivity.var_negara + ")");
                    if (wjudul.getText().toString().equals(", ()"))
                        wjudul.setText(getResources().getString(R.string.mainmenu_gps_unknown));
                } else
                    wjudul.setText(getResources().getString(R.string.mainmenu_gps_unknown));

                wnextjadwalshalat.setText(MainActivity.waktuatasjudul);
                wnextjadwaltimehour.setText(MainActivity.waktuatastimehour);
                wnextjadwaltime_h.setText(MainActivity.waktuatastime_h);
                wnextjadwaltimemin.setText(MainActivity.waktuatastimemin);
                wnextjadwaltime_min.setText(MainActivity.waktuatastime_min);
                wnownextjudulx.setText(MainActivity.wnownextjudul);

                if (MainActivity.added_day == 0) {
                    Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                    if (MainActivity.wnownextjudul.equals(getResources().getString(R.string.dilarang_shalat)))
                        animation.setDuration(200); //1 second duration for each animation cycle
                    else animation.setDuration(1000); //1 second duration for each animation cycle
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
                    animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
                    wnextjadwalshalat.startAnimation(animation); //to start animation
                    wnextjadwaltimehour.startAnimation(animation);
                    wnextjadwaltime_h.startAnimation(animation);
                    wnextjadwaltimemin.startAnimation(animation);
                    wnextjadwaltime_min.startAnimation(animation);
                    wnownextjudulx.startAnimation(animation); //to start animation
                } else {
                    wnextjadwalshalat.clearAnimation();
                    wnextjadwaltimehour.clearAnimation();
                    wnextjadwaltime_h.clearAnimation();
                    wnextjadwaltimemin.clearAnimation();
                    wnextjadwaltime_min.clearAnimation();
                    wnownextjudulx.clearAnimation();
                }

                subuh_time.setText(getResources().getString(R.string.shalat_subuh_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.FAJR)));
                terbit_time.setText(getResources().getString(R.string.syuruq_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.SUNRISE)));
                if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    dzuhur_time.setText(getResources().getString(R.string.shalat_jumat) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ZUHR)));
                } else
                    dzuhur_time.setText(getResources().getString(R.string.shalat_dzuhur_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ZUHR)));

                ashar_time.setText(getResources().getString(R.string.shalat_ashar_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ASR)));
                magrib_time.setText(getResources().getString(R.string.shalat_maghrib_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.MAGHRIB)));
                isya_time.setText(getResources().getString(R.string.shalat_isya_name) + "\n" + sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ISHA)));


                boolean alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_subuh_name), true);
                boolean alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_subuh_name), false);
                boolean alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_subuh_name), false);
                boolean alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_subuh_name), false);
                if (alarm_adzan)
                    isubuhnownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    isubuhnownexttime.setImageResource(R.drawable.alarm_notif);
                else isubuhnownexttime.setImageResource(R.drawable.alarm_off);
                //			if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //				if (alarm_adzan)
                //					isubuhnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					isubuhnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else isubuhnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_off) );
                //
                //			} else {
                //				if (alarm_adzan)
                //					isubuhnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					isubuhnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else isubuhnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_off) );
                //			}

                alarm_adzan = false;
                alarm_notif = false;
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.syuruq_name), true);
                alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.syuruq_name), false);

                if (alarm_adzan)
                    isyuruqnownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    isyuruqnownexttime.setImageResource(R.drawable.alarm_notif);
                else isyuruqnownexttime.setImageResource(R.drawable.alarm_off);


                if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_jumat), true);
                    alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_jumat), false);
                    alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_jumat), false);
                    alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_jumat), false);
                } else {
                    alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_dzuhur_name), true);
                    alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_dzuhur_name), false);
                    alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_dzuhur_name), false);
                    alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_dzuhur_name), false);
                }
                if (alarm_adzan)
                    idzuhurnownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    idzuhurnownexttime.setImageResource(R.drawable.alarm_notif);
                else idzuhurnownexttime.setImageResource(R.drawable.alarm_off);
                //			if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //				if (alarm_adzan)
                //					idzuhurnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					idzuhurnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else idzuhurnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_off) );
                //
                //			} else {
                //				if (alarm_adzan)
                //					idzuhurnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					idzuhurnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else idzuhurnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_off) );
                //			}

                alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_ashar_name), true);
                alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_ashar_name), false);
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_ashar_name), false);
                alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_ashar_name), false);
                if (alarm_adzan)
                    iasharnownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    iasharnownexttime.setImageResource(R.drawable.alarm_notif);
                else iasharnownexttime.setImageResource(R.drawable.alarm_off);
                //			if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //				if (alarm_adzan)
                //					iasharnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					iasharnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else iasharnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_off) );
                //
                //			}else {
                //				if (alarm_adzan)
                //					iasharnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					iasharnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else iasharnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_off) );
                //			}

                alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_maghrib_name), true);
                alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_maghrib_name), false);
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_maghrib_name), false);
                alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_maghrib_name), false);
                if (alarm_adzan)
                    imagribnownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    imagribnownexttime.setImageResource(R.drawable.alarm_notif);
                else imagribnownexttime.setImageResource(R.drawable.alarm_off);
                //			if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //				if (alarm_adzan)
                //					imagribnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					imagribnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else imagribnownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_off) );
                //
                //			}else {
                //				if (alarm_adzan)
                //					imagribnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					imagribnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else imagribnownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_off) );
                //			}

                alarm_adzan = prefs.getBoolean("alarm_adzan_" + getResources().getString(R.string.shalat_isya_name), true);
                alarm_notif = prefs.getBoolean("alarm_notif_" + getResources().getString(R.string.shalat_isya_name), false);
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + getResources().getString(R.string.shalat_isya_name), false);
                alarm_off = prefs.getBoolean("alarm_off_" + getResources().getString(R.string.shalat_isya_name), false);
                if (alarm_adzan)
                    iisyanownexttime.setImageResource(R.drawable.alarm_on);
                else if (alarm_notif || alarm_notif2)
                    iisyanownexttime.setImageResource(R.drawable.alarm_notif);
                else iisyanownexttime.setImageResource(R.drawable.alarm_off);
                //			if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //				if (alarm_adzan)
                //					iisyanownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					iisyanownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else iisyanownexttime.setBackgroundDrawable( getResources().getDrawable(R.drawable.alarm_off) );
                //
                //			}else {
                //				if (alarm_adzan)
                //					iisyanownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_on) );
                //				else if (alarm_notif)
                //					iisyanownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_notif) );
                //				else iisyanownexttime.setBackground( getResources().getDrawable(R.drawable.alarm_off) );
                //			}

                wtanggal_lokal.setText(formattgllokal);
                wtanggal_arab.setText(formattglarab);

                    wmethod.setText(getResources().getString(R.string.method) + " " + MainActivity.method_calculationID[MainActivity.method_index]);

                if (isMyServiceRunning(PlayAdzanService.class, MainActivity.this)) {
                    if (MainActivity.adzanPlayer != null) {
                        if (MainActivity.adzanPlayer.isPlaying()) {
                            try {
                                dialogpesanadzan.dismiss();
                            } catch (Exception e) {
                            }
                            dialogpesanadzan = new Dialog(MainActivity.this);
                            dialogpesanadzan.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogpesanadzan.setContentView(R.layout.layout_dialog_message);
                            dialogpesanadzan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            dialogpesanadzan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                            dialogpesanadzan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialogpesanadzan.setCancelable(true);
                            dialogpesanadzan.setCanceledOnTouchOutside(false);

                            TextView judul_pesan = dialogpesanadzan.findViewById(R.id.judul_pesan);
                            TextView isi_pesan = dialogpesanadzan.findViewById(R.id.isi_pesan);
                            judul_pesan.setText("Notifikasi Suara Adzan");
                            isi_pesan.setText("Hentikan suara notifikasi adzan sedang berlangsung ?");

                            Button btn2 = dialogpesanadzan.findViewById(R.id.btn2);
                            btn2.setText(getResources().getString(R.string.str_btnbatal));
                            btn2.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialogpesanadzan.dismiss();

                                }
                            });

                            Button btn1 = dialogpesanadzan.findViewById(R.id.btn1);
                            btn1.setText(getResources().getString(R.string.str_btnhentikan));
                            btn1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialogpesanadzan.dismiss();

                                    if (isMyServiceRunning(PlayAdzanService.class, MainActivity.this)) {
                                        boolean is_ceksilent = prefs.getBoolean("is_ceksilent_" + MainActivity.waktuadzan_global, false);
                                        boolean alarm_adzan = false;
                                        if (!MainActivity.waktuadzan_global.equals(getResources().getString(R.string.syuruq_name)))
                                            alarm_adzan = prefs.getBoolean("alarm_adzan_" + MainActivity.waktuadzan_global, true);

                                        Intent intentx = new Intent(MainActivity.this, PlayAdzanService.class);
                                        intentx.setAction(MainActivity.ACTION.STOPFOREGROUND_ACTION);
                                        intentx.putExtra("waktu_adzan", MainActivity.waktuadzan_global);
                                        intentx.putExtra("alarm_adzan", alarm_adzan);
                                        intentx.putExtra("alarm_notif", MainActivity.alarm_notif);
                                        intentx.putExtra("alarm_notif2", MainActivity.alarm_notif2);
                                        intentx.putExtra("alarm_off", MainActivity.alarm_off);
                                        intentx.putExtra("alarm_test", MainActivity.alarm_test);
                                        intentx.putExtra("isdoa_adzan", MainActivity.isdoa_adzan);
                                        intentx.putExtra("is_ceksilent_shalat", is_ceksilent);
                                        startService(intentx);
                                    }
                                }
                            });

                            dialogpesanadzan.show();

                        }

                    }

                }

            } catch (Exception e) {
            }
        }
    }

    private void setting_visual() {
        wjudul = findViewById(R.id.wjudul);
        wjudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogpesand = new Dialog(MainActivity.this);
                dialogpesand.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesand.setContentView(R.layout.layout_dialog_message);
                dialogpesand.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogpesand.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesand.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesand.setCancelable(true);
                dialogpesand.setCanceledOnTouchOutside(false);

                TextView judul_pesan = dialogpesand.findViewById(R.id.judul_pesan);
                TextView isi_pesan = dialogpesand.findViewById(R.id.isi_pesan);
                judul_pesan.setText("Buka Pengaturan Lokasi");
                isi_pesan.setText("Pilih Pengaturan Lokasi");

                Button btn2 = dialogpesand.findViewById(R.id.btn2);
                btn2.setText("Otomatis (GPS)");
                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogpesand.dismiss();

                        MainActivity.toast_info("MID", MainActivity.this, "", "\n" + getResources().getString(R.string.shalattools_refresh_lokasi) + "\n", 4000);
                        MainActivity.askSetting_cek_gps_and_update(MainActivity.this);
                    }
                });

                Button btn1 = dialogpesand.findViewById(R.id.btn1);
                btn1.setText("Atur Manual");
                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogpesand.dismiss();

                        atur_manual_peta();
                    }
                });

                dialogpesand.show();
            }
        });

        whari = findViewById(R.id.whari);

        wnextjadwalshalat = findViewById(R.id.wnextjadwalshalat);
        wnextjadwaltimehour = findViewById(R.id.wnextjadwaltimehour);
        wnextjadwaltime_h = findViewById(R.id.wnextjadwaltime_h);
        wnextjadwaltimemin = findViewById(R.id.wnextjadwaltimemin);
        wnextjadwaltime_min = findViewById(R.id.wnextjadwaltime_min);
        wnownextjudulx = findViewById(R.id.wnownextjudul);

        subuh_time = findViewById(R.id.wsubuhnownexttime);
        terbit_time = findViewById(R.id.wsyuruqnownexttime);
        dzuhur_time = findViewById(R.id.wdzuhurnownexttime);
        ashar_time = findViewById(R.id.washarnownexttime);
        magrib_time = findViewById(R.id.wmagribnownexttime);
        isya_time = findViewById(R.id.wisyanownexttime);

        wtanggal_lokal = findViewById(R.id.wtanggal_lokal);
        wtanggal_arab = findViewById(R.id.wtanggal_arab);

        subuh_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infowaktushalat = "<h4>Info Sholat Shubuh / Fajar</h4>"
                        + "Fajar secara bahasa berarti cahaya putih. Sholat fajar disebut juga sebagai sholat shubuh dan sholat ghodah."
                        + "<br><br>Fajar ada dua jenis yaitu fajar pertama (fajar kadzib) yang merupakan pancaran sinar putih yang mencuat ke atas kemudian hilang dan setelah itu langit kembali gelap."
                        + "<br><br>Fajar kedua adalah fajar shodiq yang merupakan cahaya putih yang memanjang di arah ufuk, cahaya ini akan terus menerus menjadi lebih terang hingga terbit matahari."
                        + "<br><br><h4>Awal Waktu Sholat Shubuh / Fajar</h4>"
                        + "Para ulama sepakat bahwa awal waktu sholat fajar dimulai sejak terbitnya fajar kedua/fajar shodiq."
                        + "<br><br><h4>Waktu Imsak di bulan Ramadhan</h4>"
                        + "Imsak yang artinya adalah menahan diri dari segala yang membatalkan puasa mulai dari terbit fajar (yaitu fajar shodiq) hingga terbenamnya matahari. Terbit fajar atau fajar shodiq ini adalah waktu dimana dikumandangkannya azan subuh. Jadi pengertian waktu imsyak itu adalah waktu dimulai sejak dikumandangkannya azan subuh sampai terbenamnya matahari (azan maghrib)."
                        + "<br><br>Hal ini berdasarkan firman Allah ta’ala,<br>"
                        + "وَكُلُوا وَاشْرَبُوا حَتَّى يَتَبَيَّنَ لَكُمُ الْخَيْطُ الْأَبْيَضُ مِنَ الْخَيْطِ الْأَسْوَدِ مِنَ الْفَجْرِ ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّيْل"
                        + "<br>“Dan makan minumlah hingga terang bagimu benang putih dari benang hitam, yaitu fajar. Kemudian sempurnakanlah puasa itu sampai (datang) malam.” (QS. Al Baqarah [2] : 187).<br><br>"
                        + "كُلُوا وَاشْرَبُوا وَلاَ يَهِيدَنَّكُمُ السَّاطِعُ الْمُصْعِدُ فَكُلُوا وَاشْرَبُوا حَتَّى يَعْتَرِضَ لَكُمُ الأَحْمَرُ"
                        + "<br>“Makan dan minumlah. Janganlah kalian menjadi takut oleh pancaran sinar (putih) yang menjulang. Makan dan minumlah sehingga tampak bagi kalian warna merah.” (HR. Tirmidzi, Abu Daud, Ibnu Khuzaimah. Dalam Shohih wa Dho’if Sunan Abu Daud, Syaikh Al Albani mengatakan hadits ini hasan shahih)<br>"
                        + "<br>Maka ayat dan hadits ini menjadi dalil bahwa waktu imsak adalah sejak terbit fajar shodiq yaitu ketika adzan shubuh dikumandangkan dan bukanlah 10 menit sebelum adzan shubuh seperti yang sering kita dapatkan dalam jadwal imsakiah."
                        + "<br><br><h4>Akhir Waktu Sholat Shubuh / Fajar</h4>"
                        + "Para ulama juga sepakat bahwa akhir waktu sholat fajar dimulai sejak terbitnya matahari (waktu syuruq/isyraq)."
                        + "<br><br><h4>Disunnahkan Menyegerakan Waktu Sholat Shubuh/Fajar Pada Saat Keadaan Gholas (Gelap yang bercampur putih)</h4>"
                        + "Jumhur ulama’ berpendapat lebih utama melaksanakan sholat fajar pada saat gholas dari pada melaksanakannya ketika ishfar (cahaya putih telah semakin terang)."
                        + "<br><br>Diantara ulama yang berpendapat demikian adalah Imam Malik, Imam Syafi’i, Imam Ahmad, Ishaq dan Abu Tsaur rohimahumullah. Diantara dalil mereka adalah hadits yang diriwayatkan dari Anas bin Malik,<br>"
                        + "أَنَّ رَسُولَ اللَّهِ – صلى الله عليه وسلم – غَزَا خَيْبَرَ ، فَصَلَّيْنَا عِنْدَهَا صَلاَةَ الْغَدَاةِ بِغَلَسٍ"
                        + "<br>“Sesungguhnya Rosulullah shallallahu ‘alaihi was sallam berperang pada perang Khoibar, maka kami sholat ghodah (fajar) di Khoibar pada saat gholas”"
                        + "<br>(HR. Bukhori No. 371, Muslim No. 1365.)"
                        + "<br><br>Diringkas dari Kitab Shohih Fiqh Sunnah karya Syaikh Abu Malik Kamal bin Said Salim hal. 237-249/I Cet. Maktabah Tauqifiyah, Kairo, Mesir";
                buka_option(MainActivity.this.getResources().getString(R.string.shalat_subuh_name));
            }
        });

        terbit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infowaktushalat = "<h4>Info Waktu Syuruq & Shalat Isyroq</h4>"
                        + "Syuruq artinya terbit. Syaraqat as-Syamsu [شَرَقَتِ الشَّمْسُ] artinya matahari terbit."
                        + "<br><br>Diantara syarat dalam pelaksanaan shalat syuruq/isyroq yang perlu diperhatikan, shalat ini dikerjakan ketika matahari sudah meninggi, kurang lebih satu tombak dalam pandangan mata manusia, sekitar 12-15 menit setelah matahari terbit. Karena ketika matahari tepat di garis terbit, kita dilarang melakukan shalat."
                        + "<br><br>Dari Uqbah bin Amir radhiallahu anhu dia berkata:<br>"
                        + "ثَلاَثُ سَاعَاتٍ كَانَ النَّبِيُّ صلى الله عليه وسلم يَنْهَانَا أَنْ نُصَلِّيَ فِيْهِنَّ أَوْ أَنْ نَقْبُرَ فِيْهِنَّ مَوْتَانَا: حِيْنَ تَطْلُعُ الشَّمْسُ بَازِغَةً حَتَّى تَرْتَفِعَ، وَحِيْنَ يَقُوْمُ قَائِمُ الظَّهِيْرَةِ حَتَّى تَمِيْلَ الشَّمْسُ، وَحِيْنَ تَضَيَّف لِلْغُرُوْبِ حَتَّى تَغْرُبَ"
                        + "<br>“Ada tiga waktu di mana Nabi Shallallahu ‘alaihi wa sallam melarang kami untuk melaksanakan shalat di tiga waktu tersebut atau menguburkan jenazah kami:"
                        + "<br>[1] ketika matahari terbit sampai tinggi,"
                        + "<br>[2] ketika seseorang berdiri di tengah bayangannya sampai matahari tergelincir dan"
                        + "<br>[3] ketika matahari miring hendak tenggelam sampai benar-benar tenggelam.” (HR. Muslim 1926)"
                        + "<br><br>Berdasarkan penjelasan di atas, berarti mulainya waktu dhuha dan waktu syuruq itu sama, yaitu ketika matahari telah terbit setinggi satu tombak, sekitar 12-15 menit setelah matahari terbit."
                        + "<br><br>Sehingga kesimpulannya “shalat syuruq/isyroq adalah shalat dhuha di waktu yang paling awal.”"
                        + "<br><br>Dari Anas bin Malik radhiyallahu ‘anhu dia berkata, Rasulullah shallallahu ‘alaihi wa sallam bersabda,<br>"
                        + "« مَنْ صَلَّى الْغَدَاةَ فِى جَمَاعَةٍ ثُمَّ قَعَدَ يَذْكُرُ اللَّهَ حَتَّى تَطْلُعَ الشَّمْسُ ثُمَّ صَلَّى رَكْعَتَيْنِ كَانَتْ لَهُ كَأَجْرِ حَجَّةٍ وَعُمْرَةٍ تَامَّةٍ تَامَّةٍ تَامَّةٍ »"
                        + "<br>“Barangsiapa yang shalat subuh berjamaah, kemudian dia duduk – dalam riwayat lain: dia menetap di mesjid [HR ath-Thabrani dalam “al-Mu’jamul kabir” (no. 7741), dinyatakan baik isnadnya oleh al-Mundziri] – untuk berzikir kepada Allah sampai matahari terbit, kemudian dia shalat dua rakaat, maka dia akan mendapatkan (pahala) seperti pahala haji dan umrah, sempurna sempurna sempurna“ [HR at-Tirmidzi (no. 586), dinyatakan hasan oleh at-Tirmidzi dan syaikh al-Albani dalam “Silsilatul ahaditsish shahihah” (no. 3403)]";
                buka_option(MainActivity.this.getResources().getString(R.string.syuruq_name));

            }
        });

        dzuhur_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waktu_now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    infowaktushalat = "<h4>Info Sholat Jum’at</h4>"
                            + "Melaksanakan shalat Jum’at adalah wajib dengan dasar Al Qur’an, Sunnah dan Ijma’. Adapun dalil dari Al Qur’an adalah firman Allah:<br>"
                            + "يَاأَيُّهَا الَّذِينَ ءَامَنُوا إِذَا نُودِيَ لِلصَّلاَةِ مِن يَوْمِ الْجُمُعَةِ فَاسْعَوْا إِلَى ذِكْرِ اللهِ وَذَرُوا الْبَيْعَ ذَلِكُمْ خَيْرُُ لَّكُمْ إِن كُنتُمْ تَعْلَمُونَ"
                            + "<br>“Hai orang-orang yang beriman, apabila diseru untuk menunaikan shalat pada hari Jum’at, maka bersegeralah kamu kepada mengingat Allah dan tinggalkanlah jual beli. Yang demikian itu lebih baik bagimu jika kamu mengetahui.” [Al Jum’ah:9]"
                            + "<br><br>Dalam ayat ini Allah memerintahkan untuk menunaikannya, padahal perintah -dalam istilah ushul fiqh- menunjukkan kewajiban. Demikian juga larangan sibuk berjual beli setelah ada panggilan shalat, menunjukkan kewajibannya; sebab seandainya bukan karena wajib, tentu hal itu tidak dilarang."
                            + "<br>Sedangkan dalil dari Sunnah, ialah sabda Rasulullah:<br>"
                            + "لَيَنْتَهِيَنَّ أَقْوَامٌ عَنْ وَدْعِهِمْ الْجُمُعَاتِ أَوْ لَيَخْتِمَنَّ اللَّهُ عَلَى قُلُوبِهِمْ ثُمَّ لَيَكُونُنَّ مِنْ الْغَافِلِينَ"
                            + "<br>“Hendaklah satu kaum berhenti dari meninggalkan shalat Jum’at, atau kalau tidak, maka Allah akan mencap hati-hati mereka, kemudian menjadikannya termasuk orang yang lalai.” [HR Imam Muslim dalam Shahih-nya, kitab Al Jum’ah, Bab At Taghlith Fi Tarki Al Jum’ah, no.1422]"
                            + "<br><br>Hal ini dikuatkan lagi dengan kesepakatan (Ijma’) kaum muslimin atas kewajibannya, sebagaimana hal itu dinukil para ulama, diantaranya: Ibnu Al Mundzir [Dinukil Imam Nawawi dalam Majmu’ Syarhu Al Muhadzab, karya Imam Nawawi, Tahqiq, Muhammad Najib Al Muthi’i, Cetakan Tahun 1415 H, Dar Ihya At Turats Al Arabi, 4/349], Ibnu Qudamah[Al Mugni, karya Ibnu Qudamah, Tahqiq, Abdullah bin Abdul Muhsin At Turki dan Abdul Fatah Muhammad Al Halwu, Cetakan Kedua, Tahun 1412 H, Penerbit Hajar, Kairo, Mesir. 3/159] dan Ibnu Taimiyah.[Majmu’ Fatawa Ibnu Taimiyah, 11/615]"
                            + "<br><br><h4>Waktu Sholat Jum’at</h4>"
                            + "Waktu shalat Jum’at dimulai dari tergelincir matahari sampai akhir waktu shalat Dhuhur. "
                            + "<br><br>Inilah waktu yang disepakati para ulama, sedangkan bila dilakukan sebelum tergelincir matahari, maka para ulama berselisih dalam dua pendapat."
                            + "<br><br>Pertama : Tidak sah"
                            + "<br>Inilah yang dikenal dari para salaf, sebagaimana dinyatakan Imam Asy Syafi’i : “Nabi Shallallahu ‘alaihi wa sallam , Abu Bakar, Umar, Utsman dan para imam setelah mereka, shalat setiap Jum’at setelah tergelincir matahari”.[Al Majmu’ Syarh Al Muhadzdzab, 4/380]"
                            + "<br><br>Kedua : Sah, shalat Jum’at sebelum tergelincir matahari"
                            + "<br>Demikian pendapat Imam Ahmad dan Ishaq, salah satunya dgn Hadits Jabir bin Abdillah ketika ia ditanya:<br>"
                            + "مَتَى كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يُصَلِّي الْجُمُعَةَ قَالَ كَانَ يُصَلِّي ثُمَّ نَذْهَبُ إِلَى جِمَالِنَا فَنُرِيحُهَا حِينَ تَزُولُ الشَّمْسُ"
                            + "<br>“Kapan Rasulullah shalat Jum’at, ia menjawab,”Beliau shalat Jum’at, kemudian kami kembali ke onta-onta kami, lalu menungganginya ketika matahari tergelincir.[HR Imam Muslim dalam Shahih-nya, kitab Al Jumu’ah, Bab Shalatul Jum’ah Hina Tazulu Asy Syamsu, no. 1421]"
                            + "<br>Syaikh Al Albani berkata,”Ini jelas menunjukkan, bahwa shalat Jum’at dilakukan sebelum tergelincir matahari.”[Al Ajwiba An Nafi’ah, op.cit 22]"
                            + "<br><br>Yang rajih adalah pendapat kedua, yaitu waktu shalat Jum’at adalah waktu Dhuhur, dan sah bila dilakukan sebelum tergelincir matahari, sebagaimana dirajihkan Imam Asy Syaukani [Nailul Authar, op.cit 3/275] dan Syaikh Al Albani.[Al Ajwibah An Nafi’ah, op.cit 22]";
                    buka_option(MainActivity.this.getResources().getString(R.string.shalat_jumat));
                } else {
                    infowaktushalat = "<h4>Info Sholat Dzuhur</h4>"
                            + "Secara bahasa Dzuhur berarti waktu Zawal yaitu waktu tergelincirnya matahari (waktu matahari bergeser dari tengah-tengah langit) menuju arah tenggelamnya (barat)."
                            + "<br><br>Sholat Dzuhur adalah sholat yang dikerjakan ketika waktu Dzuhur telah masuk. Sholat Dzuhur disebut juga sholat Al Uulaa (الأُوْلَى) karena sholat yang pertama kali dikerjakan Nabi shollallahu ‘alaihi was sallam bersama Jibril ‘Alaihis salam. Disebut juga sholat Al Hijriyah (الحِجْرِيَةُ)[Berdasarkan hadits riwayat Al Bukhori No. 541]"
                            + "<br><br><h4>Awal Waktu Sholat Dzuhur</h4>"
                            + "Awal waktu Dzuhur adalah ketika matahari telah bergeser dari tengah langit menuju arah tenggelamnya (barat). Hal ini merupakan kesepakatan seluruh kaum muslimin, dalilnya adalah hadits Nabi Shollallahu ‘alaihi was sallam dari sahabat ‘Abdullah bin ‘Amr rodhiyallahu ‘anhu,<br>"
                            + "وَقْتُ الظُّهْرِ إِذَا زَالَتِ الشَّمْسُ وَكَانَ ظِلُّ الرَّجُلِ كَطُولِهِ مَا لَمْ يَحْضُرِ الْعَصْرُ…….."
                            + "<br>“Waktu Sholat Dzuhur adalah ketika telah tergelincir matahari (menuju arah tenggelamnya) hingga bayangan seseorang sebagaimana tingginya selama belum masuk waktu ‘Ashar……….”[HR. Muslim No. 612]"
                            + "<br><br><h4>Akhir Waktu Sholat Dzuhur</h4>"
                            + "Para ulama berselisih pendapat mengenai akhir waktu Dzuhur namun pendapat yang lebih tepat dan ini adalah pendapat jumhur/mayoritas ulama adalah hingga panjang bayang-bayang seseorang semisal dengan tingginya (masuknya waktu ‘ashar). Dalil pendapat ini adalah hadits Nabi Shollallahu ‘alaihi was sallam dari sahabat ‘Abdullah bin ‘Amr rodhiyallahu ‘anhu di atas."
                            + "<br><br>Catatan : Waktu sholat Dzuhur dapat diketahui dengan menghitung waktu yaitu dengan menghitung waktu antara terbitnya matahari hingga tenggelamnya maka waktu Dzuhur dapat diketahui dengan membagi duanya."
                            + "<br><br><h4>Disunnahkan Hukumnya Menyegerakan Sholat Dzuhur di Awal Waktunya</h4>"
                            + "Hal ini berdasarkan hadits Jabir bin Samuroh rodhiyallahu ‘anhu,<br>"
                            + "كَانَ النَّبِىُّ -صلى الله عليه وسلم- يُصَلِّى الظُّهْرَ إِذَا دَحَضَتِ الشَّمْسُ"
                            + "<br>“Nabi Shollallahu ‘alaihi was sallam biasa mengerjakan sholat Dzuhur ketika matahari telah tergelincir”[HR. Muslim No. 618]."
                            + "<br><br><h4>Disunnahkan Hukumnya Mengakhirkan Sholat Dzuhur Jika Sangat Panas</h4>"
                            + "Hal ini berdasarkan hadits Nabi Shollallahu ‘alaihi was sallam,<br>"
                            + "كَانَ النَّبِىُّ – صلى الله عليه وسلم – إِذَا اشْتَدَّ الْبَرْدُ بَكَّرَ بِالصَّلاَةِ ، وَإِذَا اشْتَدَّ الْحَرُّ أَبْرَدَ بِالصَّلاَةِ"
                            + "<br>“Nabi Shollallahu ‘alaihi was sallam biasanya jika keadaan sangat dingin beliau menyegerakan sholat dan jika keadaan sangat panas/terik beliau mengakhirkan sholat”[HR. Bukhori No. 906 dan Muslim No. 615]."
                            + "<br><br>Batasan dingin berbeda-beda sesuai keadaan selama tidak terlalu panjang hingga mendekati waktu akhir sholat.";
                    buka_option(MainActivity.this.getResources().getString(R.string.shalat_dzuhur_name));
                }
            }
        });
        ashar_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infowaktushalat = "<h4>Info Sholat ‘Ashar</h4>"
                        + "‘Ashar secara bahasa diartikan sebagai waktu sore hingga matahari memerah yaitu akhir dari dalam sehari."
                        + "<br><br>Sholat ‘ashar adalah sholat ketika telah masuk waktu ‘ashar, sholat ‘ashar ini juga disebut sholat woshtho (الوُسْطَى)."
                        + "<br><br><h4>Awal Waktu Sholat ‘Ashar</h4>"
                        + "Jika panjang bayangan sesuatu telah semisal dengan tingginya (menurut pendapat jumhur ulama). Dalilnya adalah hadits Nabi shollallahu ‘alaihi was sallam,<br>"
                        + "وَقْتُ الظُّهْرِ إِذَا زَالَتِ الشَّمْسُ وَكَانَ ظِلُّ الرَّجُلِ كَطُولِهِ مَا لَمْ يَحْضُرِ الْعَصْرُ وَوَقْتُ الْعَصْرِ مَا لَمْ تَصْفَرَّ الشَّمْسُ……."
                        + "<br>“Waktu Sholat Zhuhur adalah ketika telah tergelincir matahari (menuju arah tenggelamnya) hingga bayangan seseorang sebagaimana tingginya selama belum masuk waktu ‘ashar dan waktu ‘ashar masih tetap ada selama matahari belum menguning………”[HR. Muslim No. 612]."
                        + "<br><br><h4>Akhir Waktu Sholat ‘Ashar</h4>"
                        + "Hadits-hadits tentang masalah akhir waktu ‘ashar seolah-olah terlihat saling bertentangan."
                        + "<br>Dalam hadits yang diriwayatkan dari Jabir bin ‘Abdillah rodhiyallahu ‘anhu ketika Jibril ‘alihissalam menjadi imam bagi Nabi shollallahu ‘alaihi was sallam,<br>"
                        + "جَاءَ جِبْرِيلُ عَلَيْهِ السَّلَام إِلَى النَّبِيِّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ حِينَ زَالَتْ الشَّمْسُ فَقَالَ قُمْ يَا مُحَمَّدُ فَصَلِّ الظُّهْرَ حِينَ مَالَتْ الشَّمْسُ ثُمَّ مَكَثَ حَتَّى إِذَا كَانَ فَيْءُ الرَّجُلِ مِثْلَهُ جَاءَهُ لِلْعَصْرِ فَقَالَ قُمْ يَا مُحَمَّدُ فَصَلِّ الْعَصْرَ ثُمَّ مَكَثَ حَتَّى إِذَا غَابَتْ الشَّمْسُ……مَا بَيْنَ هَذَيْنِ وَقْتٌ كُلُّهُ'"
                        + "<br>“Jibril mendatangi Nabi shollallahu ‘alaihi was sallam ketika matahari telah tergelincir ke arah tenggelamnya kemudian dia mengatakan, “Berdirilah wahai Muhammad kemudian shola zhuhur lah. Kemudian ia diam hingga saat panjang bayangan seseorang sama dengan tingginya. Jibril datang kemudian mengatakan, “Wahai Muhammad berdirilah sholat ‘ashar lah”. Kemudian ia diam hingga matahari tenggelam………….diantara dua waktu ini adalah dua waktu sholat seluruhnya”[HR. Nasa’i No. 526, hadits ini dinilai shahih oleh Al Albani rohimahullah dalam Al Irwa’ hal. 270/I]."
                        + "<br>Dalam hadits yang diriwayatkan dari sahabat Abdullah bin ‘Amr rodhiyallahu ‘anhu,<br>"
                        + "وَوَقْتُ الْعَصْرِ مَا لَمْ تَصْفَرَّ الشَّمْسُ"
                        + "<br>“Dan waktu ‘ashar masih tetap ada selama matahari belum menguning………”[HR. Muslim No. 612]."
                        + "<br>Hadits Nabi Shollallahu ‘alaihi was sallam yang diriwayatkan dari sahabat Abu Huroiroh rodhiyallahu ‘anhu,<br>"
                        + "مَنْ أَدْرَكَ رَكْعَةً مِنَ الْعَصْرِ قَبْلَ أَنْ تَغْرُبَ الشَّمْسُ فَقَدْ أَدْرَكَ الْعَصْرَ"
                        + "<br>“Barangsiapa yang mendapati satu roka’at sholat ‘ashar sebelum matahari tenggelam maka ia telah mendapatkan sholat ‘ashar”[HR. Bukhori No. 579 dan Muslim No. 608]."
                        + "<br><br>Kompromi dalam memahami ketiga hadits yang seolah-olah saling bertentangan ini adalah :"
                        + "<br>Hadits tentang sholat Nabi shollallahu ‘alaihi was sallam dan Jibril ‘Alaihissalam dipahami sebagai penjelasan tentang akhir waktu terbaik dalam melaksanakan sholat ‘ashar. Adapun hadits ‘Abdullah bin ‘Amr dipahami sebagai penjelasan atas waktu pelaksanaan sholat ‘ashar yang masih boleh. Sedangkan waktu hadits Abu Huroiroh sebagai penjelasan tentang waktu pelaksanaan sholat ‘ashar jika terdesak artinya makruh mengerjakan sholat ‘ashar pada waktu ini kecuali bagi orang yang memiliki udzur maka mengerjakan sholat ‘ashar pada waktu itu hukumnya tidak makruh. Allahu a’lam."
                        + "<br><br><h4>Disunnahkan Hukumnya Menyegerakan Sholat ‘Ashar</h4>"
                        + "Hal ini berdasarkan hadits Nabi Shollallahu ‘alaihi was sallam yang diriwayatkan dari Sahabat Anas bin Malik rodhiyallahu ‘anhu,<br>"
                        + "كَانَ رَسُولُ اللَّهِ – صلى الله عليه وسلم – يُصَلِّى الْعَصْرَ وَالشَّمْسُ مُرْتَفِعَةٌ حَيَّةٌ"
                        + "<br>“Rosulullah shollallahu ‘alaihi was sallam sering melaksanakan sholat ‘ashar ketika matahari masih tinggi”[HR. Bukhori No. 550 dan Muslim No. 621]."
                        + "<br><br>Sunnah ini lebih dikuatkan ketika mendung, hal ini berdasarkah hadits yang diriwayatkan dari Sahabat Abul Mulaih rodhiyallahu ‘anhu. Dia mengatakan,<br>"
                        + "كُنَّا مَعَ بُرَيْدَةَ فِى غَزْوَةٍ فِى يَوْمٍ ذِى غَيْمٍ فَقَالَ بَكِّرُوا بِصَلاَةِ الْعَصْرِ فَإِنَّ النَّبِىَّ – صلى الله عليه وسلم – قَالَ « مَنْ تَرَكَ صَلاَةَ الْعَصْرِ فَقَدْ حَبِطَ عَمَلُهُ"
                        + "<br>“Kami bersama Buraidah pada saat perang di hari yang mendung. Kemudian ia mengatakan, “Segerakanlah sholat ‘ashar karena Nabi shollallahu ‘alaihi was sallam mengatakan, “Barangsiapa yang meninggalkan sholat ‘ashar maka amalnya telah batal”[HR. Bukhori No. 553]."
                        + "<br>Hadits ini juga menunjukkan betapa bahayanya meninggalkan sholat ‘ashar.";
                buka_option(MainActivity.this.getResources().getString(R.string.shalat_ashar_name));
            }
        });
        magrib_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infowaktushalat = "<h4>Info Sholat Maghrib</h4>"
                        + "Secara bahasa maghrib berarti waktu dan arah tempat tenggelamnya matahari. Sholat maghrib adalah sholat yang dilaksanakan pada waktu tenggelamnya matahari."
                        + "<br><br><h4>Awal Waktu Sholat Maghrib</h4>"
                        + "Kaum Muslimin sepakat awal waktu sholat maghrib adalah ketika matahari telah tenggelam hingga matahari benar-benar tenggelam sempurna."
                        + "<br><br><h4>Akhir Waktu Sholat Maghrib</h4>"
                        + "Para ulama berselisih pendapat mengenai akhir waktu maghrib."
                        + "<br>Pendapat pertama mengatakan bahwa waktu maghrib hanya merupakan satu waktu saja yaitu sekadar waktu yang diperlukan orang yang akan sholat untuk bersuci, menutup aurot, melakukan adzan, iqomah dan melaksanakan sholat maghrib. Pendapat ini adalah pendapat Malikiyah, Al Auza’i dan Imam Syafi’i. Dalil pendapat ini adalah hadits yang diriwayatkan dari Jabir ketika Jibril mengajarkan Nabi shallallahu ‘alaihi was sallam sholat,<br>"
                        + "ثُمَّ جَاءَهُ لِلْمَغْرِبِ حِينَ غَابَتْ الشَّمْسُ وَقْتًا وَاحِدًا لَمْ يَزُلْ عَنْهُ فَقَالَ قُمْ فَصَلِّ فَصَلَّى الْمَغْرِبَ….."
                        + "<br>“Kemudian Jibril mendatangi Nabi Shallallahu ‘alaihi was sallam ketika matahari telah tenggelam (sama dengan waktu ketika Jibril mengajarkan sholat kepada Nabi pada hari sebelumnya) kemudian dia mengatakan, “Wahai Muhammad berdirilah laksanakanlah sholat maghrib………..”[HR. Nasa’i No. 526, hadits ini dinilai shahih oleh Al Albani rohimahullah dalam Al Irwa’ hal. 270/I]."
                        + "<br><br>Pendapat kedua mengatakan bahwa akhir waktu maghrib adalah ketika telah hilang sinar merah ketika matahari tenggelam. Pendapat ini adalah pendapatnya Sufyan Ats Tsauri, Imam Ahmad, Ishaq, Abu Tsaur, Mahzab Hanafi serta sebahagian mazhab Syafi’i dan inilah pendapat yang dinilai tepat oleh An Nawawi rohimahumullah. Dalilnya adalah hadits ‘Abdullah bin ‘Amr rodhiyallahu ‘anhu,<br>"
                        + "….وَقْتُ صَلاَةِ الْمَغْرِبِ مَا لَمْ يَغِبِ الشَّفَقُ….."
                        + "<br>“Waktu sholat maghrib adalah selama belum hilang sinar merah ketika matahari tenggelam”[HR. Muslim No. 612]."
                        + "<br>Pendapat inilah yang lebih tepat Allahu a’lam."
                        + "<br><br><h4>Disunnahkan Menyegerakan Sholat Maghrib</h4>"
                        + "Hal ini berdasarkan hadits Nabi shollallahu ‘alaihi was sallam dari Sahabat ‘Uqbah bin ‘Amir rodhiyallahu ‘anhu,<br>"
                        + "لاَ تَزَالُ أُمَّتِى بِخَيْرٍ – أَوْ قَالَ عَلَى الْفِطْرَةِ – مَا لَمْ يُؤَخِّرُوا الْمَغْرِبَ إِلَى أَنْ تَشْتَبِكَ النُّجُومُ"
                        + "<br>“Umatku akan senantiasa dalam kebaikan (atau fithroh) selama mereka tidak mengakhirkan waktu sholat maghrib hingga munculnya bintang (di langit)”[HR. Abu Dawud No. 414 dll. dan dinilai shohih oleh Al Albani dalam Takhrij beliau untuk Sunan Ibnu Majah].";
                buka_option(MainActivity.this.getResources().getString(R.string.shalat_maghrib_name));
            }
        });
        isya_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infowaktushalat = "<h4>Info Sholat ‘Isya’</h4>‘Isya’ adalah sebuah nama untuk saat awal langit mulai gelap (setelah maghrib) hingga sepertiga malam yang awal. Sholat ‘isya’ disebut demikian karena dikerjakan pada waktu tersebut."
                        + "<br><br><h4>Awal Waktu Sholat ‘Isya’</h4>"
                        + "Para ulama sepakat bahwa awal waktu sholat ‘isya’ adalah jika telah hilang sinar merah di langit."
                        + "<br><br><h4>Akhir Waktu Sholat ‘Isya’</h4>"
                        + "Para ulama’ berselisih pendapat mengenai akhir waktu sholat ‘isya’."
                        + "<br>Pendapat pertama mengatakan bahwa akhir waktu sholat ‘isya’ adalah sepertiga malam. Ini adalah pendapatnya Imam Syafi’i dalam al Qoul Jadid, Abu Hanifah dan pendapat yang masyhur dalam mazhab Maliki. Dalilnya adalah hadits ketika Jibril mengimami sholat Nabi shallallahu ‘alaihi was sallam,<br>"
                        + "….ثُمَّ جَاءَهُ لِلْعِشَاءِ حِينَ ذَهَبَ ثُلُثُ اللَّيْلِ الْأَوَّلُ….."
                        + "<br>“……Kemudian Jibril mendatangi Nabi Shallallahu ‘alaihi was sallam untuk melaksanakan sholat ‘isya’ ketika sepertiga malam yang pertama………..”."
                        + "<br>(HR. Nasa’i No. 526, hadits ini dinilai shahih oleh Al Albani rohimahullah dalam Al Irwa’ hal. 270/I.)"
                        + "<br><br>Pendapat kedua mengatakan bahwa akhir waktu sholat ‘isya’ adalah setengah malam. Inilah pendapatnya Sufyan Ats Tsauri, Ibnul Mubarok, Ishaq, Abu Tsaur, Mazhab Hanafi dan Ibnu Hazm rohimahumullah. Dalil pendapat ini adalah hadits yang diriwayatkan oleh ‘Abdullah bin ‘Amr rodhiyallahu ‘anhu,<br>"
                        + "…وَقْتُ صَلاَةِ الْعِشَاءِ إِلَى نِصْفِ اللَّيْلِ الأَوْسَطِ…."
                        + "<br>“Waktu sholat ‘isya’ adalah hingga setengah malam”."
                        + "<br>(HR. Muslim No. 612.)"
                        + "<br><br>Pendapat ketiga mengatakan bahwa akhir waktu sholat ‘isya’ adalah ketika terbit fajar shodiq. Inilah pendapatnya ‘Atho’, ‘Ikrimah, Dawud Adz Dzohiri, salah satu riwayat dari Ibnu Abbas, Abu Huroiroh dan Ibnul Mundzir Rohimahumullah. Dalilnya adalah hadits yang diriwayatkan dari Abu Qotadah rodhiyallahu ‘anhu,<br>"
                        + "…إِنَّمَا التَّفْرِيطُ عَلَى مَنْ لَمْ يُصَلِّ الصَّلاَةَ حَتَّى يَجِىءَ وَقْتُ الصَّلاَةِ الأُخْرَى…."
                        + "<br>“Hanyalah orang-orang yang terlalu menganggap remeh agama adalah orang yang tidak mengerjakan sholat hingga tiba waktu sholat lain”."
                        + "<br>(HR. Muslim No. 681.)"
                        + "<br><br>Pendapat yang tepat menurut Syaukani dalam masalah ini adalah akhir waktu sholat ‘isya’ yang terbaik adalah hingga setengah malam berdasarkan hadits ‘Abdullah bin ‘Amr sedangkan batas waktu bolehnya mengerjakan sholat ‘isya’ adalah hingga terbit fajar berdasarkan hadits Abu Qotadah."
                        + "<br><br>Sedangkan pendapat yang dinilai lebih kuat menurut Penulis Shahih Fiqh Sunnah adalah setengah malam jika hadits Anas adalah hadits yang tidak shohih."
                        + "<br><br><h4>Disunnahkan Mengakhirkan Sholat ‘Isya’</h4>"
                        + "Hal ini berdasarkan hadits Nabi shallallahu ‘alaihi was sallam,<br>"
                        + "لَوْلاَ أَنْ أَشُقَّ عَلَى أُمَّتِى لأَمَرْتُهُمْ أَنْ يُؤَخِّرُوا الْعِشَاءَ إِلَى ثُلُثِ اللَّيْلِ أَوْ نِصْفِهِ"
                        + "<br>“Jika sekiranya tidak memberatkan ummatku maka akan aku perintah agar mereka mengakhirkan sholat ‘isya’ hingga sepertiga atau setengah malam”."
                        + "<br>(HR. Tirmidzi No. 167, Ibnu Majah No. 691, dinyatakan shohih oleh Al Albani di Takhrij Sunan Tirmidzi.)"
                        + "<br><br>Akan tetapi hal ini tidak selalu dikerjakan Nabi shallallahu ‘alaihi was sallam, sebagaimana dalam hadits yang lain,<br>"
                        + "وَالْعِشَاءُ أَحْيَانًا يُقَدِّمُهَا ، وَأَحْيَانًا يُؤَخِّرُهَا : إذَا رَآهُمْ اجْتَمَعُوا عَجَّلَ ، وَإِذَا رَآهُمْ أَبْطَئُوا أَخَّرَ"
                        + "<br>“Terkadang (Nabi) menyegerakan sholat isya dan terkadang juga mengakhirkannya. Jika mereka telah terlihat terkumpul maka segerakanlah dan jika terlihat (lambat datang ke masjid)”."
                        + "<br>(HR. Bukhori No. 560, Muslim No. 233.)"
                        + "<br><br><h4>Dimakruhkan Tidur Sebelum Sholat ‘Isya’ dan Berbicara yang Tidak Perlu Setelahnya</h4>"
                        + "Hal ini berdasarkan hadits Nabi shallallahu ‘alaihi was sallam,<br>"
                        + "كَانَ يَكْرَهُ النَّوْمَ قَبْلَهَا وَالْحَدِيثَ بَعْدَهَا"
                        + "<br>“Nabi shallallahu ‘alaihi was sallam membenci tidur sebelum sholat ‘isya’ dan melakukan pembicaraan yang tidak berguna setelahnya”."
                        + "<br>(HR. Bukhori No. 568, Muslim No. 237.)";
                buka_option(MainActivity.this.getResources().getString(R.string.shalat_isya_name));
            }
        });

        isubuhnownexttime = findViewById(R.id.isubuhnownexttime);
        idzuhurnownexttime = findViewById(R.id.idzuhurnownexttime);
        iasharnownexttime = findViewById(R.id.iasharnownexttime);
        imagribnownexttime = findViewById(R.id.imagribnownexttime);
        iisyanownexttime = findViewById(R.id.iisyanownexttime);
        isyuruqnownexttime = findViewById(R.id.isyuruqnownexttime);

        txtback1 = findViewById(R.id.txtback1);
        txtback2 = findViewById(R.id.txtback2);
        refresh_day = findViewById(R.id.refresh_day);
        refresh_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.added_day = 0;
                show_info();
            }
        });
        prev_date = findViewById(R.id.prev_date);
        prev_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.added_day = MainActivity.added_day - 1;
                show_info();
            }
        });
        lay_cal = findViewById(R.id.lay_cal);
        lay_cal.setOnClickListener(new View.OnClickListener() {
            boolean isok = true;
            private String[] bulanhijr;
            private NumberPicker thnhijri;
            private NumberPicker blnhijri;
            private NumberPicker tglhijri;
            private ToggleButton swcalendar;
            private DatePicker datePickerMasehi;
            private LinearLayout datePickerHijriyyah;

            private void togglehijrimasehi() {
                isok = true;
                carimaxharihijri();
                if (swcalendar.isChecked()) {
                    datePickerMasehi.setVisibility(View.GONE);
                    datePickerHijriyyah.setVisibility(View.VISIBLE);

                    try {
                        GregorianCalendar gCal = new GregorianCalendar(datePickerMasehi.getYear(), datePickerMasehi.getMonth(), datePickerMasehi.getDayOfMonth());
                        Calendar uCal = new UmmalquraCalendar();
                        uCal.setTime(gCal.getTime());
                        uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);

                        tglhijri.setValue(uCal.get(Calendar.DAY_OF_MONTH));
                        blnhijri.setValue(uCal.get(Calendar.MONTH));
                        thnhijri.setValue(uCal.get(Calendar.YEAR));
                    } catch (Exception e) {
                        MainActivity.toast_info(MainActivity.this, getResources().getString(R.string.str_mindate));
                        isok = false;
                    }
                } else {
                    datePickerMasehi.setVisibility(View.VISIBLE);
                    datePickerHijriyyah.setVisibility(View.GONE);

                    try {
                        Calendar uCal = new UmmalquraCalendar(thnhijri.getValue(), blnhijri.getValue(), tglhijri.getValue());
                        GregorianCalendar gCal = new GregorianCalendar();
                        gCal.setTime(uCal.getTime());
                        gCal.add(Calendar.DAY_OF_MONTH, -1 * (MainActivity.manual_hijriyyah - 2));

                        datePickerMasehi.updateDate(gCal.get(Calendar.YEAR), gCal.get(Calendar.MONTH), gCal.get(Calendar.DAY_OF_MONTH));
                    } catch (Exception e) {
                        MainActivity.toast_info(MainActivity.this, getResources().getString(R.string.str_mindate));
                        isok = false;
                    }
                }
            }

            private void carimaxharihijri() {
                try {
                    int xval = tglhijri.getValue();
                    int maxval = UmmalquraCalendar.lengthOfMonth(thnhijri.getValue(), blnhijri.getValue());
                    tglhijri.setMaxValue(maxval);
                    tglhijri.setValue(xval);
                } catch (Exception e) {
                    isok = false;
                }
            }

            @Override
            public void onClick(View v) {
                //				MainActivity.toast_info(MainActivity.this, "Coming Soon");
                //				DialogFragment newFragment = new DatePickerFragment();
                //				newFragment.show(getFragmentManager(),"Date Picker");

                final Dialog dialogpesan = new Dialog(MainActivity.this);
                dialogpesan.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpesan.setContentView(R.layout.layout_dialog_datepicker);
                dialogpesan.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogpesan.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
                dialogpesan.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogpesan.setCancelable(true);
                dialogpesan.setCanceledOnTouchOutside(true);

                //				int hour = Integer.valueOf(edWaktuPagi.getText().toString().split(":")[0].trim());
                //				int minute = Integer.valueOf(edWaktuPagi.getText().toString().split(":")[1].trim());

                datePickerHijriyyah = dialogpesan.findViewById(R.id.datePickerHijriyyah);
                tglhijri = dialogpesan.findViewById(R.id.tglhijri);
                tglhijri.setMinValue(1);
                tglhijri.setValue(tglarab);
                tglhijri.setFormatter(new NumberPicker.Formatter() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public String format(int value) {
                        return String.format("%02d", value);
                    }
                });
                tglhijri.setWrapSelectorWheel(true);


                    bulanhijr = new String[]{
                            "Muharram",
                            "Shafar",
                            "Rabi'ul Awwal",
                            "Rabi'ul Tsani",
                            "Jumaadal Ula",
                            "Jumaadal Tsaniyah",
                            "Rajab",
                            "Sya'ban",
                            "Ramadhan",
                            "Syawwal",
                            "Dzulqa'dah",
                            "Dzulhijjah"};

                blnhijri = dialogpesan.findViewById(R.id.blnhijri);
                blnhijri.setMinValue(0); //from array first value
                blnhijri.setMaxValue(bulanhijr.length - 1); //to array last value
                blnhijri.setDisplayedValues(bulanhijr);
                blnhijri.setValue(blnarab);
                blnhijri.setWrapSelectorWheel(true);
                blnhijri.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                        //			                tv.setText("Selected Number : " + newVal);
                        //						MainActivity.toast_info(MainActivity.this, thnhijri.getValue()+"-");//+UmmalquraCalendar.lengthOfMonth(thnhijri.getValue(), newVal + 1));
                        carimaxharihijri();
                    }
                });

                thnhijri = dialogpesan.findViewById(R.id.thnhijri);
                thnhijri.setMinValue(1300);
                thnhijri.setMaxValue(1500);
                thnhijri.setValue(thnarab);
                thnhijri.setWrapSelectorWheel(true);
                thnhijri.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                        //						MainActivity.toast_info(MainActivity.this, blnhijri.getValue()+"-");//+UmmalquraCalendar.lengthOfMonth(newVal, blnhijri.getValue()));
                        carimaxharihijri();
                    }
                });
                carimaxharihijri();

                datePickerMasehi = dialogpesan.findViewById(R.id.datePickerMasehi);
                datePickerMasehi.updateDate(waktu_now.get(Calendar.YEAR), waktu_now.get(Calendar.MONTH), waktu_now.get(Calendar.DAY_OF_MONTH));
                swcalendar = dialogpesan.findViewById(R.id.swcalendar);
                swcalendar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        togglehijrimasehi();
                    }

                });
                swcalendar.setChecked(true);
                togglehijrimasehi();

                TextView thijri = dialogpesan.findViewById(R.id.thijri);
                thijri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        swcalendar.setChecked(!swcalendar.isChecked());
                        togglehijrimasehi();
                    }

                });

                TextView tmasehi = dialogpesan.findViewById(R.id.tmasehi);
                tmasehi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        swcalendar.setChecked(!swcalendar.isChecked());
                        togglehijrimasehi();
                    }
                });

                Button btn2 = dialogpesan.findViewById(R.id.btn2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogpesan.dismiss();

                    }
                });

                Button btn1 = dialogpesan.findViewById(R.id.btn1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        GregorianCalendar gcal = null;
                        GregorianCalendar gcal2 = null;
                        if (swcalendar.isChecked()) {
                            swcalendar.setChecked(false);
                            togglehijrimasehi();
                        }

                        gcal = new GregorianCalendar();
                        gcal.add(GregorianCalendar.DAY_OF_MONTH, MainActivity.added_day);
                        long skg = gcal.getTimeInMillis();
                        gcal.set(GregorianCalendar.DAY_OF_MONTH, datePickerMasehi.getDayOfMonth());
                        gcal.set(GregorianCalendar.MONTH, datePickerMasehi.getMonth());
                        gcal.set(GregorianCalendar.YEAR, datePickerMasehi.getYear());
                        long nanti = gcal.getTimeInMillis();

                        gcal2 = new GregorianCalendar();
                        gcal2.set(GregorianCalendar.DAY_OF_MONTH, 31);
                        gcal2.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
                        gcal2.set(GregorianCalendar.YEAR, 1937);

                        if (gcal.after(gcal2)) {
                            int bedax = (int) TimeUnit.MILLISECONDS.toDays(nanti) - (int) TimeUnit.MILLISECONDS.toDays(skg);
                            MainActivity.added_day = MainActivity.added_day + bedax;

                            dialogpesan.dismiss();
                            show_info();
                        } else {
                            dialogpesan.dismiss();
                            MainActivity.toast_info(MainActivity.this, getResources().getString(R.string.str_mindate));
                        }

                        //						MainActivity.toast_info(MainActivity.this, datePickerMasehi.getDayOfMonth() + "-" + (datePickerMasehi.getMonth() + 1) + "-" + datePickerMasehi.getYear());
                    }
                });

                dialogpesan.show();

                //				final Calendar c = new UmmalquraCalendar();
                //			    int mYear = c.get(Calendar.YEAR);
                //			    int mMonth = c.get(Calendar.MONTH);
                //			    int mDay = c.get(Calendar.DAY_OF_MONTH);
                //
                //			    final Calendar c1 = new UmmalquraCalendar();
                //			    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                //			        @Override
                //			        public void onDateSet(DatePicker view, int year, int month, int day) {
                //
                //			            c1.set(Calendar.YEAR, year);
                //			            c1.set(Calendar.MONTH, month);
                //			            c1.set(Calendar.DAY_OF_MONTH, day);
                //			            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //
                //			            MainActivity.toast_info(MainActivity.this, dateFormat.format(c1.getTime()));
                //
                //			            /* DateFormat datefmt= DateFormat.getDateInstance(DateFormat.MEDIUM);  // to get date format in Feb 31,3017
                //			                datebutton.setText(datefmt.format(c1.getTime())); */
                //			        }
                //			    }, mYear, mMonth, mDay);
                //			    datePickerDialog.show();
                //			    long now = System.currentTimeMillis() - 1000;
                //			    datePickerDialog.getDatePicker().setMinDate(now);// To Disable previous dates and enables from present date in datepicker
                //			    datePickerDialog.getDatePicker().setMaxDate(now+31536000000L);// for 365 days  1000*60*60*24*365  mill_sec*sec*min*hours*days

            }
        });
        next_date = findViewById(R.id.next_date);
        next_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.added_day = MainActivity.added_day + 1;
                show_info();
            }
        });

        show_info();
    }


    @SuppressWarnings("deprecation")
    protected void buka_option(final String waktushalat) {
        if (!MainActivity.lg_adzan) {
            origionalVolumeVoiceCall = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            origionalVolumeAlarm = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_ALARM);
            origionalVolumeRingtone = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_RING);
            origionalVolumeNotif = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            origionalVolumeMusic = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            waktusholatx = waktushalat;
            firsttime = true;
            final SharedPreferences prefs = MainActivity.this.getSharedPreferences("iMoslem", Context.MODE_PRIVATE);
            boolean alarm_adzan;
            boolean alarm_notif, alarm_notif2;
            boolean alarm_off;
            if (waktushalat.equals(getResources().getString(R.string.syuruq_name))) {
                alarm_adzan = false;
                alarm_notif = false;
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + waktushalat, true);
                alarm_off = prefs.getBoolean("alarm_off_" + waktushalat, false);
            } else {
                alarm_adzan = prefs.getBoolean("alarm_adzan_" + waktushalat, true);
                alarm_notif = prefs.getBoolean("alarm_notif_" + waktushalat, false);
                alarm_notif2 = prefs.getBoolean("alarm_notif2_" + waktushalat, false);
                alarm_off = prefs.getBoolean("alarm_off_" + waktushalat, false);
            }

            MainActivity.idxadzan = prefs.getInt("idxAdzan_" + waktushalat, 0);
            MainActivity.pickedVolume = prefs.getInt("volAdzanNew_" + MainActivity.AdzanStreamType + "_" + waktushalat, MainActivity.mainAdzanManager.getStreamMaxVolume(MainActivity.AdzanStreamType) * 12 / 15);
            int notif_before = prefs.getInt("notif_before_" + waktushalat, 0);
            final boolean is_notif_before = prefs.getBoolean("is_notif_before_" + waktushalat, false);
            boolean is_ceksilent = prefs.getBoolean("is_ceksilent_" + waktushalat, false);
            int ceksilent_auto_on = prefs.getInt("ceksilent_auto_on_" + waktushalat, 5);

            if (dialogpopup != null && dialogpopup.isShowing()) dialogpopup.dismiss();

            dialogpopup = new Dialog(MainActivity.this);
            dialogpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogpopup.setContentView(R.layout.layout_dialog_shalat_option);
            dialogpopup.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            dialogpopup.getWindow().getAttributes().windowAnimations = R.style.MenuLeft;
            dialogpopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogpopup.setCancelable(false);
            dialogpopup.setCanceledOnTouchOutside(false);

            final TextView txtinfoshalat = dialogpopup.findViewById(R.id.txtinfoshalat);
            final ImageView imageView1 = dialogpopup.findViewById(R.id.imageView1);
            txtinfoshalat.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout.LayoutParams layparam;
                    if (txtinfoshalat.getHeight() == (int) (40 * MainActivity.this.getResources().getDisplayMetrics().density)) {
                        layparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageView1.setImageResource(R.drawable.bottom_menu);
                    } else {
                        layparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (40 * MainActivity.this.getResources().getDisplayMetrics().density));
                        imageView1.setImageResource(R.drawable.up_menu);
                    }

                    txtinfoshalat.setLayoutParams(layparam);
                }
            });
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txtinfoshalat.setText(Html.fromHtml(infowaktushalat, Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtinfoshalat.setText(Html.fromHtml(infowaktushalat));
            }
            final TextView adzanvol = dialogpopup.findViewById(R.id.adzanvol);
            adzanvol.setText(MainActivity.this.getResources().getString(R.string.shalattools_setvolume) + " : " + MainActivity.pickedVolume + " / " + MainActivity.mainAdzanManager.getStreamMaxVolume(MainActivity.AdzanStreamType));
            slider = dialogpopup.findViewById(R.id.slider);
            slider.setMax(MainActivity.mainAdzanManager.getStreamMaxVolume(MainActivity.AdzanStreamType));
            slider.setEnabled(alarm_adzan);
            slider.setProgress(MainActivity.pickedVolume);
            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    if (progress != 0) {
                        MainActivity.pickedVolume = progress;

                    } else {
                        MainActivity.pickedVolume = 1;
                        seekBar.setProgress(MainActivity.pickedVolume);
                    }
                    adzanvol.setText(MainActivity.this.getResources().getString(R.string.shalattools_setvolume) + " : " + MainActivity.pickedVolume + " / " + MainActivity.mainAdzanManager.getStreamMaxVolume(MainActivity.AdzanStreamType));

                }
            });

            btntest = dialogpopup.findViewById(R.id.btntest);
            btntest.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(View v) {
                    if (!MainActivity.lg_adzan) {

                            MainActivity.mainAdzanManager.setStreamVolume(MainActivity.AdzanStreamType, MainActivity.pickedVolume, 0);

                        MainActivity.alarm_test = true;
                        MainActivity.alarm_adzan = false;
                        MainActivity.alarm_notif = false;
                        MainActivity.alarm_notif2 = false;
                        MainActivity.alarm_off = false;
                        MainActivity.waktuadzan_global = waktushalat;
                        MainActivity.lg_adzan = false;

                        if (cekadzan.isChecked()) MainActivity.alarm_adzan = true;
                        else if (ceknotif.isChecked()) MainActivity.alarm_notif = true;
                        else if (ceknotif2.isChecked()) MainActivity.alarm_notif2 = true;

                        MainActivity.sudahdoaadzan = false;

                        Intent intent = new Intent(MainActivity.this, PlayAdzanService.class);
                        intent.setAction(MainActivity.ACTION.INIT_PLAY_ACTION);
                        intent.putExtra("waktu_adzan", MainActivity.waktuadzan_global);
                        intent.putExtra("alarm_adzan", MainActivity.alarm_adzan);
                        intent.putExtra("alarm_notif", MainActivity.alarm_notif);
                        intent.putExtra("alarm_notif2", MainActivity.alarm_notif2);
                        intent.putExtra("alarm_off", MainActivity.alarm_off);
                        intent.putExtra("alarm_test", MainActivity.alarm_test);
                        intent.putExtra("isdoa_adzan", MainActivity.isdoa_adzan);
                        intent.putExtra("is_ceksilent_shalat", false);
                        MainActivity.this.startService(intent);

                    }
                }
            });

            cekadzan = dialogpopup.findViewById(R.id.cekadzan);
            cekadzan.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    slider.setEnabled(cekadzan.isChecked());
                    btntest.setEnabled(!cekoff.isChecked());
                }
            });
            ceknotif = dialogpopup.findViewById(R.id.ceknotif);
            ceknotif.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    slider.setEnabled(cekadzan.isChecked());
                    btntest.setEnabled(!cekoff.isChecked());
                }
            });
            ceknotif2 = dialogpopup.findViewById(R.id.ceknotif2);
            ceknotif2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    slider.setEnabled(cekadzan.isChecked());
                    btntest.setEnabled(!cekoff.isChecked());
                }
            });
            cekoff = dialogpopup.findViewById(R.id.cekoff);
            cekoff.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    slider.setEnabled(cekadzan.isChecked());
                    btntest.setEnabled(!cekoff.isChecked());
                }
            });

            cekadzan.setChecked(alarm_adzan);
            ceknotif.setChecked(alarm_notif);
            ceknotif2.setChecked(alarm_notif2);
            cekoff.setChecked(alarm_off);
            btntest.setEnabled(!alarm_off);

            TextView judul_pesan = dialogpopup.findViewById(R.id.judul_pesan);
            judul_pesan.setText(".: " + MainActivity.this.getResources().getString(R.string.opsi_notifikasi_title) + " " + waktushalat + " :.");

            Button btn1 = dialogpopup.findViewById(R.id.btn1);
            btn1.setText(MainActivity.this.getResources().getString(R.string.str_btnsimpan));
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialogpopup.dismiss();

                    SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("iMoslem", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("alarm_adzan_" + waktushalat, cekadzan.isChecked());
                    editor.putBoolean("alarm_notif_" + waktushalat, ceknotif.isChecked());
                    editor.putBoolean("alarm_notif2_" + waktushalat, ceknotif2.isChecked());
                    editor.putBoolean("alarm_off_" + waktushalat, cekoff.isChecked());
                    editor.putInt("volAdzanNew_" + MainActivity.AdzanStreamType + "_" + waktushalat, slider.getProgress());
                    editor.putBoolean("is_ceksilent_" + waktushalat, false);

                    editor.commit();

                    try {
                        MainActivity.alarmmanagerReminder.cancel(MainActivity.pendingIntentReminder);
                    } catch (Exception e1) {
                    }
                    if (MainActivity.waktuadzannextshort.equals(waktushalat)) {
                        MainActivity.cancelNotification(MainActivity.this, MainActivity.NOTIF_REMINDER);
                    }

                    Intent intentx = new Intent(MainActivity.this, PlayAdzanService.class);
                    if (isMyServiceRunning(PlayAdzanService.class, MainActivity.this)) {
                        intentx.setAction(MainActivity.ACTION.STOPFOREGROUND_ACTION);
                        intentx.putExtra("waktu_adzan", waktushalat);
                        intentx.putExtra("alarm_adzan", cekadzan.isChecked());
                        intentx.putExtra("alarm_notif", ceknotif.isChecked());
                        intentx.putExtra("alarm_notif2", ceknotif2.isChecked());
                        intentx.putExtra("alarm_off", cekoff.isChecked());
                        intentx.putExtra("alarm_test", true);
                        intentx.putExtra("isdoa_adzan", false);
                        intentx.putExtra("is_ceksilent_shalat", false);
                        MainActivity.this.startService(intentx);
                    } else {
                        if (MainActivity.adzanPlayer != null) {
                            if (MainActivity.adzanPlayer.isPlaying())
                                MainActivity.adzanPlayer.stop();
                            MainActivity.adzanPlayer.reset();
                            MainActivity.adzanPlayer.release();
                            MainActivity.adzanPlayer = null;
                        }
                        MainActivity.alarm_test = false;
                        MainActivity.lg_adzan = false;
                    }
                    MainActivity.cancelNotification(MainActivity.this, MainActivity.NOTIF_ADZAN);

                    NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (notificationManager.isNotificationPolicyAccessGranted()) {
                            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                            MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
                        }
                    } else
                        MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, origionalVolumeVoiceCall, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_ALARM, origionalVolumeAlarm, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_MUSIC, origionalVolumeMusic, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, origionalVolumeNotif, 0);

                    show_info();

                }
            });
            Button btn2 = dialogpopup.findViewById(R.id.btn2);
            btn2.setText(MainActivity.this.getResources().getString(R.string.str_btnbatal));
            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialogpopup.dismiss();

                    Intent intentx = new Intent(MainActivity.this, PlayAdzanService.class);
                    if (isMyServiceRunning(PlayAdzanService.class, MainActivity.this)) {
                        intentx.setAction(MainActivity.ACTION.STOPFOREGROUND_ACTION);
                        intentx.putExtra("waktu_adzan", waktushalat);
                        intentx.putExtra("alarm_adzan", cekadzan.isChecked());
                        intentx.putExtra("alarm_notif", ceknotif.isChecked());
                        intentx.putExtra("alarm_notif2", ceknotif2.isChecked());
                        intentx.putExtra("alarm_off", cekoff.isChecked());
                        intentx.putExtra("alarm_test", MainActivity.alarm_test);
                        intentx.putExtra("isdoa_adzan", false);
                        intentx.putExtra("is_ceksilent_shalat", false);
                        MainActivity.this.startService(intentx);
                    } else {
                        if (MainActivity.adzanPlayer != null) {
                            if (MainActivity.adzanPlayer.isPlaying())
                                MainActivity.adzanPlayer.stop();
                            MainActivity.adzanPlayer.reset();
                            MainActivity.adzanPlayer.release();
                            MainActivity.adzanPlayer = null;
                        }
                        MainActivity.alarm_test = false;
                        MainActivity.lg_adzan = false;
                    }
                    MainActivity.cancelNotification(MainActivity.this, MainActivity.NOTIF_ADZAN);

                    NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (notificationManager.isNotificationPolicyAccessGranted()) {
                            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                            MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
                        }
                    } else
                        MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, origionalVolumeVoiceCall, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_ALARM, origionalVolumeAlarm, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_MUSIC, origionalVolumeMusic, 0);
                    MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, origionalVolumeNotif, 0);

                }
            });

            if (waktushalat.equals(MainActivity.this.getResources().getString(R.string.syuruq_name))) {
                cekadzan.setVisibility(View.GONE);
                ceknotif.setVisibility(View.GONE);
                adzanvol.setVisibility(View.GONE);
                slider.setVisibility(View.GONE);
            }

            dialogpopup.show();
        }
    }

    public void arah_kiblat(View view) {
        Intent i = new Intent(MainActivity.this, ArahKiblatActivity.class);
        startActivity(i);
    }

    public void kalender_hijriyyah(View view) {
        Intent i = new Intent(MainActivity.this, KalenderHijriyyahActivity.class);
        startActivity(i);
    }

}
