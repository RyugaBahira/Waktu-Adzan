package com.ryugabahira.waktuadzan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.azan.QiblaUtils;

public class Compass implements SensorEventListener {
	private SensorManager sensorManager;
	private Sensor gsensor;
	private Sensor msensor;
	private float[] mGravity = new float[3];
	private static float[] mGeomagnetic = new float[3];
	private float azimuth = 0f;
	private float currectAzimuth = 0;
	Context mcontext;

	// compass arrow to rotate
	public ImageView arrowView = null;
	public ImageView kiblatView = null;
	public ImageView indicator = null;
	public ImageView indicator2 = null;
	public TextView textMagnetic = null;

	public Compass(Context context) {
		this.mcontext = context;
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	public void start() {
		sensorManager.registerListener(this, gsensor,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, msensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {
		sensorManager.unregisterListener(this);
	}

	private void adjustArrow() {
		if (arrowView == null) {
			//			Log.i(TAG, "arrow view is not set");
			return;
		}
		float currazzimut = currectAzimuth;
		//		Log.i("Compass", "will set rotation from " + currectAzimuth + " to " + azimuth);

		Animation an = new RotateAnimation(-currazzimut, -azimuth,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		currectAzimuth = azimuth;
		
		an.setDuration(500);
		an.setRepeatCount(0);
		an.setFillAfter(true);

		arrowView.startAnimation(an);
		
		double sudutkiblat = QiblaUtils.qibla(MainActivity.lokasiGPS.getLatitude(), MainActivity.lokasiGPS.getLongitude());
		if (kiblatView != null) {
			//add baguss kiblat 
			float azimuth2 = (float) (azimuth + (360 - sudutkiblat));
			
			float currazzimut2 = (float) (currazzimut + (360 - sudutkiblat));
			Animation an2 = new RotateAnimation(-currazzimut2, -azimuth2,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
	
			an2.setDuration(500);
			an2.setRepeatCount(0);
			an2.setFillAfter(true);
	
			if (indicator2 != null) {
				if (currazzimut >= (sudutkiblat-1) && currazzimut <= (sudutkiblat+1)) {
					if (!indicator2.isShown()) {
						kiblatView.setVisibility(View.GONE);
						indicator2.setVisibility(View.VISIBLE);
					}
				}else {
					if (indicator2.isShown()) indicator2.setVisibility(View.GONE);
					kiblatView.startAnimation(an2);
				}
			}
		}
		
		if (indicator != null) {
			if (currazzimut >= (sudutkiblat-1) && currazzimut <= (sudutkiblat+1)) {
				if (!indicator.isShown()) indicator.setVisibility(View.VISIBLE);
			}else {
				if (indicator.isShown()) indicator.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];

				// mGravity = event.values;

				// Log.e(TAG, Float.toString(mGravity[0]));
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// mGeomagnetic = event.values;

				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
						* event.values[0];
				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
						* event.values[1];
				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
						* event.values[2];
				// Log.e(TAG, Float.toString(event.values[0]));
				if (textMagnetic != null) {
					if (Math.abs(mGeomagnetic[2]) > Math.abs(mGeomagnetic[1])){
						textMagnetic.setText("Magnetic Level : " + Math.round(Math.abs(mGeomagnetic[2])) + " \u00B5T");
					}else{
						textMagnetic.setText("Magnetic Level : " + Math.round(Math.abs(mGeomagnetic[1])) + " \u00B5T");
					}
				}
			}

            float[] R = new float[9];
            float[] I = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
                float[] orientation = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Log.d(TAG, "azimuth (rad): " + azimuth);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
				
				// Log.d(TAG, "azimuth (deg): " + azimuth);
				adjustArrow();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
