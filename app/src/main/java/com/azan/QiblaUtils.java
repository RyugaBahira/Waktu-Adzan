package com.azan;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import static com.azan.Constants.KAABA_LATITUDE;
import static com.azan.Constants.KAABA_LONGITUDE;
import static com.azan.Constants.TOTAL_DEGREES;
import static com.azan.util.MathUtil.atan2Deg;
import static com.azan.util.MathUtil.cosDeg;
import static com.azan.util.MathUtil.sinDeg;
import static com.azan.util.MathUtil.tanDeg;


public class QiblaUtils {
    /**
     * Return qibla direction in degrees from the north (clock-wise).
     *
     * @param latitude  latitude in degrees
     * @param longitude longitude in degrees
     * @return 0 means north, 90 means east, 270 means west, etc
     */
    public static double qibla(double latitude, double longitude) {
        double degrees = atan2Deg(sinDeg(KAABA_LONGITUDE - longitude),
            cosDeg(latitude) * tanDeg(KAABA_LATITUDE)
                - sinDeg(latitude) * cosDeg(KAABA_LONGITUDE - longitude));
        return degrees >= 0 ? degrees : degrees + TOTAL_DEGREES;
    }
    
    public static Double HitungJarak(double latitude, double longitude) {
		Location lokasiA = new Location("lokasi_a");
		lokasiA.setLatitude(KAABA_LATITUDE);
		lokasiA.setLongitude(KAABA_LONGITUDE);

		Location lokasiB = new Location("lokasi_b");
		lokasiB.setLatitude(latitude);
		lokasiB.setLongitude(longitude);

		Double distance = (double) lokasiA.distanceTo(lokasiB);
		return distance;
	}

	public static String direction(LatLng latlng1, LatLng latlng2) {
		double delta = 22.5;
		String direction = "Unknown";
		double heading = SphericalUtil.computeHeading(latlng1, latlng2);

		if ((heading >= 0 && heading < delta) || (heading < 0 && heading >= -delta)) {
			direction = "Utara";
		} else if (heading >= delta && heading < 90 - delta) {
			direction = "Timur Laut";
		} else if (heading >= 90 - delta && heading < 90 + delta) {
			direction = "Timur";
		} else if (heading >= 90 + delta && heading < 180 - delta) {
			direction = "Tenggara";
		} else if (heading >= 180 - delta || heading <= -180 + delta) {
			direction = "Selatan";
		} else if (heading >= -180 + delta && heading < -90 - delta) {
			direction = "Barat Daya";
		} else if (heading >= -90 - delta && heading < -90 + delta) {
			direction = "Barat";
		} else if (heading >= -90 + delta && heading < -delta) {
			direction = "Barat Laut";
		}

		return direction;
	}
}
