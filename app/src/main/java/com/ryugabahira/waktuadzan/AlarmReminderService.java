package com.ryugabahira.waktuadzan;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

@SuppressLint("NewApi")
public class AlarmReminderService extends IntentService implements OnPreparedListener, OnErrorListener {
	int origionalVolumeAlarm, origionalVolumeVoiceCall, origionalVolumeRingtone, origionalVolumeNotif, origionalVolumeMusic;

	public AlarmReminderService() {
		super("AlarmReminderService");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();

		//		AudioManager.STREAM_ALARM = 4, STREAM_MUSIC = 3
		try {
			if (MainActivity.adzanPlayer != null) {
				if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
				MainActivity.adzanPlayer.reset();
				MainActivity.adzanPlayer.release();
				MainActivity.adzanPlayer = null;
			}
		} catch (IllegalStateException e) {
		}

		MainActivity.adzanPlayer = new MediaPlayer();
		MainActivity.adzanPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		MainActivity.adzanPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//set listeners
		MainActivity.adzanPlayer.setOnPreparedListener(this);
		MainActivity.adzanPlayer.setOnErrorListener(this);

	}

	@SuppressLint({ "NewApi" })
	@Override
	protected void onHandleIntent(final Intent intent) {
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

		/* if (mAudioManagerAdzan.getStreamVolume(MainActivity.AdzanStreamType) <= mAudioManagerAdzan.getStreamMaxVolume(MainActivity.AdzanStreamType) / 2) 
			 mAudioManagerAdzan.setStreamVolume(MainActivity.AdzanStreamType, (mAudioManagerAdzan.getStreamMaxVolume(MainActivity.AdzanStreamType) / 2) + 2, 0);
		 else mAudioManagerAdzan.setStreamVolume(MainActivity.AdzanStreamType, mAudioManagerAdzan.getStreamVolume(MainActivity.AdzanStreamType), 0);
*/
		Calendar waktu_now = Calendar.getInstance();
		waktu_now.set(Calendar.SECOND, 0);
		waktu_now.set(Calendar.MILLISECOND, 0);

		if (MainActivity.waktu_reminder.get(Calendar.YEAR) == waktu_now.get(Calendar.YEAR) &&
				MainActivity.waktu_reminder.get(Calendar.MONTH) == waktu_now.get(Calendar.MONTH) &&
				MainActivity.waktu_reminder.get(Calendar.DAY_OF_MONTH) == waktu_now.get(Calendar.DAY_OF_MONTH) &&
				MainActivity.waktu_reminder.get(Calendar.HOUR_OF_DAY) == waktu_now.get(Calendar.HOUR_OF_DAY) &&
				MainActivity.waktu_reminder.get(Calendar.MINUTE) == waktu_now.get(Calendar.MINUTE)) {

			if (!MainActivity.sudahreminder) {
				origionalVolumeVoiceCall = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
				origionalVolumeAlarm = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_ALARM);
				origionalVolumeRingtone = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_RING);
				origionalVolumeNotif = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
				origionalVolumeMusic = MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_MUSIC);

				if (MainActivity.mainAdzanManager.getStreamVolume(AudioManager.STREAM_MUSIC) < (MainActivity.mainAdzanManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 6)) {
					MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_MUSIC, (MainActivity.mainAdzanManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2) + 2, 0);
				}

				MainActivity.sudahreminder = true;
				showNotification_reminder(MainActivity.pesanxreminder, "Pengingat Waktu Shalat " + MainActivity.waktuadzannextshort);
			}

		}

//		AlarmReminderReceiver.completeWakefulIntent(intent);
	}

	public String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + c;
	}

	@SuppressWarnings("deprecation")
	private void showNotification_reminder(String msgx, String tittlex) {
		int requestID = (int) System.currentTimeMillis();

		// Using RemoteViews to bind custom layouts into Notification
		RemoteViews views;
		views = new RemoteViews(getPackageName(), R.layout.notification_adzan_light);

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.putExtra("from_notif", "JADWALSHALAT");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		views.setTextViewText(R.id.status_bar_judul, tittlex);
		views.setTextViewText(R.id.status_bar_info, msgx);

		views.setTextViewText(R.id.status_bar_time, pad(MainActivity.waktu_reminder.get(Calendar.HOUR_OF_DAY)) + ":" + pad(MainActivity.waktu_reminder.get(Calendar.MINUTE)));

		Notification notification = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

			notification = new Notification.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setCustomContentView(views)
					.setTicker(msgx)
					.setAutoCancel(true)   //allow auto cancel when pressed.
					.setGroupSummary(true)
					.setContentIntent(pendingIntent)
					.build();  //finally build and return a Notification.
		}else {
			notification = new NotificationCompat.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setContent(views)
					.setAutoCancel(true)
					.setOngoing(false)
					.setPriority(Notification.PRIORITY_MAX)
					.setGroup(MainActivity.NOTIFICATION_CHANNEL_NAME)
					.setContentIntent(pendingIntent)
					.build();
		}
		
		Uri adzanSound = Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.labbaik_allahumma_labbaik_mishari);

		try {
			MainActivity.adzanPlayer.setDataSource(this, adzanSound);
			MainActivity.adzanPlayer.setOnCompletionListener(mp -> {
				if (MainActivity.adzanPlayer != null) {
					if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
					MainActivity.adzanPlayer.reset();
					MainActivity.adzanPlayer.release();
					MainActivity.adzanPlayer = null;
				}
				//restore

				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (notificationManager.isNotificationPolicyAccessGranted()) {
						notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
						MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
					}
				}else MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_RING, origionalVolumeRingtone, 0);
				MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, origionalVolumeVoiceCall, 0);
				MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_ALARM, origionalVolumeAlarm, 0);
				MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_MUSIC, origionalVolumeMusic, 0);
				MainActivity.mainAdzanManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, origionalVolumeNotif, 0);

			});
			MainActivity.adzanPlayer.prepareAsync();

		} catch (Exception e) {
			if (MainActivity.adzanPlayer != null) {
				if (MainActivity.adzanPlayer.isPlaying()) MainActivity.adzanPlayer.stop();
				MainActivity.adzanPlayer.reset();
				MainActivity.adzanPlayer.release();
				MainActivity.adzanPlayer = null;
			}

		}
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(MainActivity.NOTIF_REMINDER, notification);
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

		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		MainActivity.adzanPlayer.start();

	}
}

