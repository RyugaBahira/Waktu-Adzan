package com.sahaab.hijri.caldroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryugabahira.waktuadzan.R;
import com.sahaab.hijricalendar.HijriCalendarDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import hirondelle.date4j.DateTime;

/**
 * The CaldroidGridAdapter provides customized view for the dates gridview
 * 
 * @author thomasdao
 * 
 */
@SuppressLint("ResourceAsColor")
public class CaldroidGridAdapter extends BaseAdapter {
	protected ArrayList<DateTime> datetimeList;
	protected int month;
	protected int year;
	protected Context context;
	protected ArrayList<DateTime> disableDates;
	protected ArrayList<DateTime> selectedDates;

	// Use internally, to make the search for date faster instead of using
	// indexOf methods on ArrayList
	protected HashMap<DateTime, Integer> disableDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> selectedDatesMap = new HashMap<DateTime, Integer>();

	protected DateTime minDateTime;
	protected DateTime maxDateTime;
	protected DateTime today;
	protected int startDayOfWeek;
	protected int manualCorrection;
	protected boolean sixWeeksInCalendar;
	protected Resources resources;

	/**
	 * caldroidData belongs to Caldroid
	 */
	protected HashMap<String, Object> caldroidData;
	/**
	 * extraData belongs to client
	 */
	protected HashMap<String, Object> extraData;

	public void setAdapterDateTime(DateTime dateTime) {
		this.month = dateTime.getMonth();
		this.year = dateTime.getYear();
		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar);
	}

	// GETTERS AND SETTERS
	public ArrayList<DateTime> getDatetimeList() {
		return datetimeList;
	}

	public DateTime getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(DateTime minDateTime) {
		this.minDateTime = minDateTime;
	}

	public DateTime getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(DateTime maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public ArrayList<DateTime> getDisableDates() {
		return disableDates;
	}

	public void setDisableDates(ArrayList<DateTime> disableDates) {
		this.disableDates = disableDates;
	}

	public ArrayList<DateTime> getSelectedDates() {
		return selectedDates;
	}

	public void setSelectedDates(ArrayList<DateTime> selectedDates) {
		this.selectedDates = selectedDates;
	}

	public HashMap<String, Object> getCaldroidData() {
		return caldroidData;
	}

	public void setCaldroidData(HashMap<String, Object> caldroidData) {
		this.caldroidData = caldroidData;

		// Reset parameters
		populateFromCaldroidData();
	}

	public HashMap<String, Object> getExtraData() {
		return extraData;
	}

	public void setExtraData(HashMap<String, Object> extraData) {
		this.extraData = extraData;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param month
	 * @param year
	 * @param caldroidData
	 * @param extraData
	 */
	public CaldroidGridAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData) {
		super();
		this.month = month;
		this.year = year;
		this.context = context;
		this.caldroidData = caldroidData;
		this.extraData = extraData;
		this.resources = context.getResources();

		// Get data from caldroidData
		populateFromCaldroidData();
	}

	/**
	 * Retrieve internal parameters from caldroid data
	 */
	@SuppressWarnings("unchecked")
	private void populateFromCaldroidData() {
		disableDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.DISABLE_DATES);
		if (disableDates != null) {
			disableDatesMap.clear();
			for (DateTime dateTime : disableDates) {
				disableDatesMap.put(dateTime, 1);
			}
		}

		selectedDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.SELECTED_DATES);
		if (selectedDates != null) {
			selectedDatesMap.clear();
			for (DateTime dateTime : selectedDates) {
				selectedDatesMap.put(dateTime, 1);
			}
		}

		minDateTime = (DateTime) caldroidData
				.get(CaldroidFragment._MIN_DATE_TIME);
		maxDateTime = (DateTime) caldroidData
				.get(CaldroidFragment._MAX_DATE_TIME);
		startDayOfWeek = (Integer) caldroidData
				.get(CaldroidFragment.START_DAY_OF_WEEK);
		manualCorrection = (Integer) caldroidData
				.get(CaldroidFragment.MANUAL_CORRECTION);
		sixWeeksInCalendar = (Boolean) caldroidData
				.get(CaldroidFragment.SIX_WEEKS_IN_CALENDAR);

		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar);
	}
	
	public void updateToday() {
		today = CalendarHelper.convertDateToDateTime(new Date());		
	}

	protected DateTime getToday() {
		if (today == null) {
			today = CalendarHelper.convertDateToDateTime(new Date());
		}
		return today;
	}

	@SuppressWarnings("unchecked")
	protected void setCustomResources(DateTime dateTime, View backgroundView,
			TextView textView) {
		// Set custom background resource
		HashMap<DateTime, Integer> backgroundForDateTimeMap = (HashMap<DateTime, Integer>) caldroidData
				.get(CaldroidFragment._BACKGROUND_FOR_DATETIME_MAP);
		if (backgroundForDateTimeMap != null) {
			// Get background resource for the dateTime
			Integer backgroundResource = backgroundForDateTimeMap.get(dateTime);

			// Set it
			if (backgroundResource != null) {
				backgroundView.setBackgroundResource(backgroundResource
						.intValue());
			}
		}

		// Set custom text color
		HashMap<DateTime, Integer> textColorForDateTimeMap = (HashMap<DateTime, Integer>) caldroidData
				.get(CaldroidFragment._TEXT_COLOR_FOR_DATETIME_MAP);
		if (textColorForDateTimeMap != null) {
			// Get textColor for the dateTime
			Integer textColorResource = textColorForDateTimeMap.get(dateTime);

			// Set it
			if (textColorResource != null) {
//				textView.setTextColor(resources.getColor(textColorResource
//						.intValue()));
				textView.setTextColor(ContextCompat.getColor(this.context, textColorResource
						.intValue()));
			}
		}
	}

	/**
	 * Customize colors of text and background based on states of the cell
	 * (disabled, active, selected, etc)
	 * 
	 * To be used only in getView method
	 * 
	 * @param position
	 * @param cellView
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected void customizeTextView(int position, TextView cellView , TextView cellView2, LinearLayout cell_layout) {
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
											//CELL VIEW 1//
		//////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		cellView.setTextColor(ContextCompat.getColor(this.context, R.color.yellow2));

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			cellView.setTextColor(ContextCompat.getColor(this.context, R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDatesMap
						.containsKey(dateTime))) {

			cellView.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cell_layout.setBackgroundResource(R.drawable.disable_cell);
			} else {
				cell_layout.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cell_layout.setBackgroundResource(R.drawable.red_border_gray_bg);
			}
		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDatesMap.containsKey(dateTime)) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cell_layout.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cell_layout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.caldroid_sky_blue));
			}

//			cellView.setTextColor(CaldroidFragment.selectedTextColor);
			cellView.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cell_layout.setBackgroundResource(R.drawable.blue_border_blue_bg);
			} else {
				cell_layout.setBackgroundResource(R.drawable.cell_bg);
			}
		}

		cellView.setText(" " + dateTime.getDay());

		// Set custom color if required
		setCustomResources(dateTime, cellView, cellView);
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
											//CELL VIEW 2//
		//////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		cellView2.setTextColor(ContextCompat.getColor(this.context, R.color.green1));
		
		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			cellView2.setTextColor(ContextCompat.getColor(this.context, R.color.caldroid_darker_gray));
		}

		shouldResetDiabledView = false;
		shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDatesMap
						.containsKey(dateTime))) {

			cellView2.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cell_layout.setBackgroundResource(R.drawable.disable_cell);
			} else {
				cell_layout.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cell_layout.setBackgroundResource(R.drawable.red_border_gray_bg);
			}
		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDatesMap.containsKey(dateTime)) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cell_layout.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
				
			} else {
				cell_layout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.caldroid_sky_blue));
			}

//			cellView2.setTextColor(CaldroidFragment.selectedTextColor);
			cellView.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
			cellView2.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
//			cellView.setShadowLayer(1.0f, 1.0f, 1.0f, Color.parseColor("#000000"));
//			cellView2.setShadowLayer(1.0f, 1.0f, 1.0f, Color.parseColor("#000000"));
		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cell_layout.setBackgroundResource(R.drawable.blue_border_blue_bg);
				cellView.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
				cellView2.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
//				cellView.setShadowLayer(1.0f, 1.0f, 1.0f, Color.parseColor("#000000"));
//				cellView2.setShadowLayer(1.0f, 1.0f, 1.0f, Color.parseColor("#000000"));
			} else {
				cell_layout.setBackgroundResource(R.drawable.cell_bg);
			}
		}
				
		Calendar Qurancal = Calendar.getInstance();
		Qurancal.set(Calendar.YEAR, dateTime.getYear());
		Qurancal.set(Calendar.MONTH, dateTime.getMonth());
		Qurancal.set(Calendar.DAY_OF_YEAR, dateTime.getDayOfYear());
		Qurancal.set(Calendar.HOUR, dateTime.getHour());
		Qurancal.set(Calendar.MINUTE, dateTime.getMinute());
		Qurancal.set(Calendar.SECOND, dateTime.getSecond()); 
		
		
		
		//cellView2.setText("" + HijriCalendarDate.getSimpleDateDay(Qurancal, 0));
		cellView2.setText("" + HijriCalendarDate.getSimpleDateDayx(Qurancal, manualCorrection)
				.replace("0", "\u06F0")
				.replace("1", "\u06F1")
				.replace("2", "\u06F2")
				.replace("3", "\u06F3")
				.replace("4", "\u06F4")
				.replace("5", "\u06F5")
				.replace("6", "\u06F6")
				.replace("7", "\u06F7")
				.replace("8", "\u06F8")
				.replace("9", "\u06F9") );

		// Set custom color if required
		setCustomResources(dateTime, cellView2, cellView2);
		
		cellView2.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "fonts/qalam_majeed.ttf"));
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		cellView2.setTextLocale(new Locale("ar","SA"));
		cellView2.setTextSize(30);

		/////////////////////
//        if (!dateTime.equals(getToday()))
//            if (dateTime.getWeekDay() == MONDAY || dateTime.getWeekDay() == THURSDAY) {
//                GregorianCalendar gCal = new GregorianCalendar(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
//                Calendar uCal = new UmmalquraCalendar();
//                uCal.setTime(gCal.getTime());
//                uCal.add(Calendar.DAY_OF_MONTH, MainActivity.manual_hijriyyah - 2);
//
//                int blnarab = uCal.get(Calendar.MONTH);
//                int tglarab = uCal.get(Calendar.DAY_OF_MONTH);
//
//                if (blnarab == UmmalquraCalendar.RAMADHAN) {
////                    if (blnarab == UmmalquraCalendar.THUL_HIJJAH && tglarab == 10) {}else {
////                        if (blnarab == UmmalquraCalendar.SHAWWAL && tglarab == 1) {}else {
//                            cellView.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
//                            cellView2.setTextColor(ContextCompat.getColor(this.context, R.color.warna_backgound));
//                            cell_layout.setBackgroundColor(ContextCompat.getColor(this.context, R.color.biru));
////                        }
////                    }
//                }
//
//            }
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.datetimeList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.date_cell, null);
		}

		TextView tv1 = cellView.findViewById(R.id.calendar_tv);
		TextView tv2 = cellView.findViewById(R.id.tv1);
		LinearLayout cell_layout = cellView.findViewById(R.id.cell_layout);
		
		customizeTextView(position, tv1, tv2, cell_layout);

		return cellView;
	}

}
