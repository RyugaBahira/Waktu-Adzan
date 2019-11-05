package com.azan;

import com.azan.types.PrayersType;

import java.util.Date;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.ceil;


public class PrayerTimes {
    private boolean useSecond = false;
    private double[] prayerTimes;
    private long millis;

    PrayerTimes(long millis, double... prayerTimes) {
        this.prayerTimes = prayerTimes;
        this.millis = millis;
        if (prayerTimes[PrayersType.SUNRISE.getIndex()] == NEGATIVE_INFINITY || prayerTimes[PrayersType.MAGHRIB.getIndex()] == POSITIVE_INFINITY) {
            prayerTimes[PrayersType.ZUHR.getIndex()] = POSITIVE_INFINITY;
            prayerTimes[PrayersType.ASR.getIndex()] = POSITIVE_INFINITY;
        }
    }

    public Date getPrayTime(PrayersType payersType) {
        double time = prayerTimes[payersType.getIndex()];
        if (time == POSITIVE_INFINITY || time == NEGATIVE_INFINITY) {
            return null;
        }
        // negative hours will raise exception
        if (isUseSecond())
            return new Date(this.millis + (long) ceil(time * Constants.MINUTE_IN_HOUR * Constants.SECOND_IN_MINUTE) * Constants.MILLIS_IN_SECOND);
        else
            return new Date(this.millis + (long) ceil(time * Constants.MINUTE_IN_HOUR) * Constants.SECOND_IN_MINUTE * Constants.MILLIS_IN_SECOND);
    }

    public Time getTimeinHoursAndMinutesAndSecounds(int timeName) {
        double time = prayerTimes[timeName];
        if (time == POSITIVE_INFINITY || time == NEGATIVE_INFINITY)
            return null;
        if (isUseSecond())
            return new Time(time);
        else
            return new Time(ceil(time * Constants.MINUTE_IN_HOUR) / Constants.SECOND_IN_MINUTE);
    }

    /**
     * Whether second is used, if not it will be zero and minute is rounded to the top (ceil).
     */
    public boolean isUseSecond() {
        return useSecond;
    }

    public void setUseSecond(boolean useSecond) {
        this.useSecond = useSecond;
    }

}
