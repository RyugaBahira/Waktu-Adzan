package com.ryugabahira.waktuadzan;

//import android.annotation.SuppressLint;
////import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
////import android.os.PowerManager;
//import androidx.legacy.content.WakefulBroadcastReceiver;
//
//@SuppressWarnings("deprecation")
//public class AlarmReminderReceiver extends WakefulBroadcastReceiver {
////	public class AlarmGPSReceiver extends BroadcastReceiver {
////	private PowerManager.WakeLock screenWakeLock;
//
//	@SuppressLint({ "NewApi", "Wakelock" })
//	@Override
//	public void onReceive(Context context, Intent intent) {
////		if (screenWakeLock == null) {
////			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
////			screenWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenLock tag GPS");
////			screenWakeLock.acquire();
////		}
////
////		context.startService(new Intent(context, AlarmGPSService.class));
////
////		if (screenWakeLock != null) screenWakeLock.release();
//
//		startWakefulService(context, new Intent(context, AlarmReminderService.class));
//	}
//
//}

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReminderReceiver extends BroadcastReceiver {
//	public class AlarmGPSReceiver extends BroadcastReceiver {
//	private PowerManager.WakeLock screenWakeLock;

	@Override
	public void onReceive(Context context, Intent intent) {
//		if (screenWakeLock == null) {
//			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//			screenWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenLock tag GPS");
//			screenWakeLock.acquire();
//		}
//
//		context.startService(new Intent(context, AlarmGPSService.class));
//
//		if (screenWakeLock != null) screenWakeLock.release();

		context.startService(new Intent(context, AlarmReminderService.class));
	}

}