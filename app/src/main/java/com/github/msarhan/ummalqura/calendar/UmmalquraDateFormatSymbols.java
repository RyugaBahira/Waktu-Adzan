/*
 * The MIT License
 *
 * Copyright 2015 Mouaffak A. Sarhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.msarhan.ummalqura.calendar;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Mouaffak A. Sarhan.
 */
class UmmalquraDateFormatSymbols {

	/**
	 * The locale which is used for initializing this DateFormatSymbols object.
	 *
	 * @serial
	 */
	Locale locale = null;

	/**
	 * Month strings. For example: "Muharram", "Safar", etc.  An array
	 * of 12 strings, indexed by
	 * <code>UmmalquraCalendar.MUHARRAM</code>, <code>UmmalquraCalendar.SAFAR</code>, etc.
	 *
	 * @serial
	 */
	String[] months_EN = null;
	String[] months_ID = null;

	/**
	 * Short month strings. For example: "Muh", "Saf", etc.  An array of
	 * 12 strings, indexed by
	 * <code>UmmalquraCalendar.MUHARRAM</code>, <code>UmmalquraCalendar.SAFAR</code>, etc.
	 *
	 * @serial
	 */
	String[] shortMonths = null;

	public UmmalquraDateFormatSymbols() {
		initializeData(Locale.getDefault());
	}

	public UmmalquraDateFormatSymbols(Locale locale) {
		initializeData(locale);
	}

	/**
	 * Gets month strings. For example: "Muharram", "Safar", etc.
	 *
	 * @return the month strings.
	 */
	public String[] getMonths_EN() {
		return Arrays.copyOf(months_EN, months_EN.length);
	}
	
	public String[] getMonths_ID() {
		return Arrays.copyOf(months_ID, months_ID.length);
	}

	/**
	 * Gets short month strings. For example: "Muh", "Saf", etc.
	 *
	 * @return the short month strings.
	 */
	public String[] getShortMonths() {
		return Arrays.copyOf(shortMonths, shortMonths.length);
	}

	private void initializeData(Locale desiredLocale) {
		locale = desiredLocale;

		// Initialize the fields from the ResourceBundle for locale.
		ResourceBundle resource = ResourceBundle.getBundle("com.github.msarhan.ummalqura.calendar.text.UmmalquraFormatData", locale);

		months_EN = resource.getStringArray("MonthNames_EN");
		months_ID = resource.getStringArray("MonthNames_ID");
		shortMonths = resource.getStringArray("MonthAbbreviations");
	}

}
