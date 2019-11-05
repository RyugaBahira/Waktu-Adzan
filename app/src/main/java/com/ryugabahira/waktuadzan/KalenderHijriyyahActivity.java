package com.ryugabahira.waktuadzan;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.sahaab.hijri.caldroid.CaldroidFragment;
import com.sahaab.hijri.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class KalenderHijriyyahActivity extends FragmentActivity {
    private CaldroidFragment caldroidFragment;
    public static int space;
    public int mainMonth, mainYear;
    private Bundle args;
    
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shalat_tools_kalender_hijriyyah_light);

        caldroidFragment = new CaldroidFragment();

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            args = new Bundle();
            //			Calendar cal = new UmmalquraCalendar();
            Calendar cal = Calendar.getInstance();

            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putString(CaldroidFragment.SHOW_LANG, "in");
            args.putInt(CaldroidFragment.MANUAL_CORRECTION, MainActivity.manual_hijriyyah - 2);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            //args.putInt(CaldroidFragment._TEXT_COLOR_FOR_DATETIME_MAP, R.color.yellow_trans2);

            caldroidFragment.setArguments(args);
        }

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {
            private Dialog dialogpesan;

            @Override
            public void onSelectDate(Date date, View view) {
                try {
                    Calendar gCal = new GregorianCalendar();
                    gCal.setTime(date);

                    final Calendar uCal = new UmmalquraCalendar();
                    uCal.setTime(date);
                    uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);

                    String blnarab = "";
                    SimpleDateFormat formatterid = new SimpleDateFormat("dd MMMM yyyy");
                    SimpleDateFormat formatterid_hari = new SimpleDateFormat("EEEE");
                    final int thnarab = uCal.get(Calendar.YEAR);         // 1433
                    final int tglarab = uCal.get(Calendar.DAY_OF_MONTH); // 20d
                    String hr_iniA = "", hr_iniI = "";
                    int harihijriyah = gCal.get(Calendar.DAY_OF_WEEK) - 1;
                    //harihijriyah = harihijriyah + ((MainActivity.manual_hijriyyah - 2) * -1);
                    //if (harihijriyah == 7)
                    //	harihijriyah = 0;
                    //else if (harihijriyah == 8)
                    //	harihijriyah = 1;

                    hr_iniA = MainActivity.nama_hari_arab_ID[harihijriyah];
                    hr_iniI = formatterid_hari.format(date).replace("Jumat", "Jum’at");
                    blnarab = uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);        // 2 //long bulan in, short bulan en

                    String formattglarab = pad(tglarab) + " " + blnarab + " " + thnarab + "H"; // Tuesday 8 Rabi' al-Awwal, 1433
                    String formattgllokal = formatterid.format(date);

                    MainActivity.toast_info(KalenderHijriyyahActivity.this, hr_iniA + ", " + formattglarab + "\n" + hr_iniI + ", " + formattgllokal);

                } catch (Exception e) {
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                //				String text = "month: " + month + " year: " + year;
                //				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                try {
                    set_hari_spesial_hijriah(month, year);
                } catch (Exception e) {
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                //				if (caldroidFragment.getLeftArrowButton() != null) {
                //					Toast.makeText(this,
                //							"Caldroid view is created", Toast.LENGTH_SHORT)
                //							.show();
                //				}
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        try {
            GregorianCalendar gCal = new GregorianCalendar();
            set_hari_spesial_hijriah(gCal.get(Calendar.MONTH), gCal.get(Calendar.YEAR));
        } catch (Exception e) {
        }

    }

    private void set_hari_spesial_hijriah(int month, int year) {
		/*27 Rajab 1439 H
		Peringatan Isra' Mi'raj Nabi Muhammad SAW 1439 H

		16 Mei 2018 01 Ramadhan 1439 H
		Awal Puasa Ramadhan 1439 H 

		15-16 Juni 2018 1-2 Syawal 1439 H
		Hari Raya Idul Fitri 1439 H

		22 Agustus 2018 10 Dzul Hijjah 1439 H
		Hari Raya Idul Adha 1439 H

		11 September 2018 01 Muharram 1440 H
		Tahun Baru Hijriah 1440 H

		20 November 2018 12 Rabiul Awal 1440 H*/

        GregorianCalendar gCal = new GregorianCalendar(year, month, 1);
        Calendar uCal = new UmmalquraCalendar();
        uCal.setTime(gCal.getTime());
        uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);

        set_puasa_senin_kamis(year, month);

        //tandai hr ini
        caldroidFragment.setBackgroundResourceForDate(R.color.green1, new Date());
        caldroidFragment.setTextColorForDate(R.color.warna_backgound, new Date());

        caldroidFragment.refreshView();
    }

    private void set_puasa_senin_kamis(int year, int month) {
        for (int i = 1; i <= 31; i++) {
            try {
                GregorianCalendar gCal = new GregorianCalendar(year, month, i);
                Calendar uCal = new UmmalquraCalendar();
                uCal.setTime(gCal.getTime());
                uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);
                int thnarab = uCal.get(Calendar.YEAR);
                int blnarab = uCal.get(Calendar.MONTH);
                int tglarab = uCal.get(Calendar.DAY_OF_MONTH);

                GregorianCalendar gCalx = new GregorianCalendar();
                Calendar hCal = new UmmalquraCalendar(thnarab, blnarab, tglarab);
                hCal.add(Calendar.DAY_OF_MONTH, (MainActivity.manual_hijriyyah - 2) * -1);
                gCalx.setTime(hCal.getTime());

                Date haribesardate = gCalx.getTime();

                if (blnarab != UmmalquraCalendar.RAMADHAN ) {
                    if (gCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || gCal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        caldroidFragment.setBackgroundResourceForDate(R.color.biru, haribesardate);
                        caldroidFragment.setTextColorForDate(R.color.warna_backgound, haribesardate);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    //	/**
    //	 * Save current states of the Caldroid here
    //	 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TOD...........................................................................................O Auto-generated method stub
        super.onSaveInstanceState(outState);
        //
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    protected void show_info() {
        Calendar waktu_now = Calendar.getInstance();
        waktu_now.set(Calendar.SECOND, 0);
        waktu_now.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat formatterid = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat formatterid_hari = new SimpleDateFormat("EEEE");
        GregorianCalendar gCal = new GregorianCalendar();
        Calendar uCal = new UmmalquraCalendar();
        uCal.setTime(gCal.getTime());
        uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);

        int thnarab = uCal.get(Calendar.YEAR);         // 1433
        String blnarab = uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);        // 2
        int tglarab = uCal.get(Calendar.DAY_OF_MONTH); // 20d
        String formattglarab = pad(tglarab) + " " + blnarab + " " + thnarab + "H"; // Tuesday 8 Rabi' al-Awwal, 1433
        String formattgllokal = formatterid.format(new Date(waktu_now.getTimeInMillis()));
        String hr_ini = "";
        int harihijriyah = gCal.get(Calendar.DAY_OF_WEEK) - 1;

            hr_ini = formatterid_hari.format(new Date(waktu_now.getTimeInMillis())).replace("Jumat", "Jum’at") + " / " + MainActivity.nama_hari_arab_ID[harihijriyah];

        TextView txtjudul_H = findViewById(R.id.txtjudul_H);
        TextView txtdetail = findViewById(R.id.txtdetail);
        txtjudul_H.setText(getResources().getString(R.string.shalattools_kalender_judul));
        txtdetail.setText(getResources().getString(R.string.judul_hari) + " " + hr_ini
                + "\n" + formattgllokal + " / " + formattglarab
        );
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    @Override
    public void onPause() {
        super.onPause();

        KalenderHijriyyahActivity.this.unregisterReceiver(receiver_latlang);

    }

    @Override
    public void onResume() {
        super.onResume();

        show_info();

        KalenderHijriyyahActivity.this.registerReceiver(receiver_latlang, new IntentFilter("LATLANGRECEIVER"));

    }
}
