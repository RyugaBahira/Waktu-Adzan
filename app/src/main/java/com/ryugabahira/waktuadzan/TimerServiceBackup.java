package com.ryugabahira.waktuadzan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class TimerServiceBackup extends Service {
    
	@Override
	public void onCreate() {
		super.onCreate();

		//add the  below lines of code in to your onCreate() method,
		//soon after calling super method.[i.e super.onCreate()]
		
		///////////////////////////////gmn ya caranya biar ga ada notif
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		showNotification_kosong();
		//}
		///////////////////////////
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		stopForeground(true);  
		stopSelf();
		
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

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// Used only in case of bound services.
		return null;
	}


}

