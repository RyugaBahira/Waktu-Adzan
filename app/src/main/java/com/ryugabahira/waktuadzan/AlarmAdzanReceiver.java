package com.ryugabahira.waktuadzan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import android.os.PowerManager;
//import androidx.legacy.content.WakefulBroadcastReceiver;

////public class AlarmAdzanReceiver extends BroadcastReceiver {
//@SuppressWarnings("deprecation")
//public class AlarmAdzanReceiver extends WakefulBroadcastReceiver {
////	private PowerManager.WakeLock screenWakeLock;
//
////	@SuppressWarnings("deprecation")
////	@SuppressLint({ "NewApi", "Wakelock" })
//	@Override
//	public void onReceive(Context context, Intent intent) {
////		if (screenWakeLock == null) {
////			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
////			screenWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenLock tag Updater");
////			screenWakeLock.acquire();
////		}
////
////		context.startService(new Intent(context, AlarmAdzanService.class));
////
////		if (screenWakeLock != null) screenWakeLock.release();
//
//		startWakefulService(context, new Intent(context, AlarmAdzanService.class));
//
//	}
//
//}

public class AlarmAdzanReceiver extends BroadcastReceiver {
//	private PowerManager.WakeLock screenWakeLock;

	//	@SuppressWarnings("deprecation")
//	@SuppressLint({ "NewApi", "Wakelock" })
	@Override
	public void onReceive(Context context, Intent intent) {
//		if (screenWakeLock == null) {
//			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//			screenWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ScreenLock tag Updater");
//			screenWakeLock.acquire();
//		}
//
//		context.startService(new Intent(context, AlarmAdzanService.class));
//
//		if (screenWakeLock != null) screenWakeLock.release();

		context.startService(new Intent(context, AlarmAdzanService.class));

	}

}