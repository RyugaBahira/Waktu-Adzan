package com.ryugabahira.waktuadzan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

//import android.content.BroadcastReceiver;
//import android.os.PowerManager;

public class DismissAdzanReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
//		int notificationId = intent.getExtras().getInt("com.ryugabahira.waktuadzan.notificationId");

		/* Your code to handle the event here */

		Intent intentx = new Intent(context, PlayAdzanService.class);
		intentx.setAction(MainActivity.ACTION.STOPFOREGROUND_ACTION);
		intentx.putExtra("waktu_adzan", MainActivity.waktuadzan_global);

		SharedPreferences prefs = context.getSharedPreferences("iMoslem", Context.MODE_PRIVATE);
		if (MainActivity.waktuadzannextshort.equals(context.getResources().getString(R.string.syuruq_name))) {
			MainActivity.alarm_adzan = false;
			MainActivity.alarm_notif = false;
			MainActivity.alarm_notif2 = prefs.getBoolean("alarm_notif2_" + MainActivity.waktuadzan_global, true);
			MainActivity.alarm_off = prefs.getBoolean("alarm_off_" + MainActivity.waktuadzan_global, false);
		} else {
			MainActivity.alarm_adzan = prefs.getBoolean("alarm_adzan_" + MainActivity.waktuadzan_global, true);
			MainActivity.alarm_notif = prefs.getBoolean("alarm_notif_" + MainActivity.waktuadzan_global, false);
			MainActivity.alarm_notif2 = prefs.getBoolean("alarm_notif2_" + MainActivity.waktuadzan_global, false);
			MainActivity.alarm_off = prefs.getBoolean("alarm_off_" + MainActivity.waktuadzan_global, false);
		}

		intentx.putExtra("alarm_adzan", MainActivity.alarm_adzan);
		intentx.putExtra("alarm_notif", MainActivity.alarm_notif);
		intentx.putExtra("alarm_notif2", MainActivity.alarm_notif2);
		intentx.putExtra("alarm_off", MainActivity.alarm_off);
		intentx.putExtra("alarm_test", MainActivity.alarm_test);
		intentx.putExtra("isdoa_adzan", MainActivity.isdoa_adzan);
		intentx.putExtra("alarm_adzanDismiss", MainActivity.alarm_adzanDismiss);
		intentx.putExtra("is_ceksilent_shalat", MainActivity.is_ceksilentDismiss);
		context.startService(intentx);

	}

}