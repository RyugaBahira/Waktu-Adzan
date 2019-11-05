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

package com.github.msarhan.ummalqura.calendar.text;

import java.util.ListResourceBundle;

/**
 * @author Mouaffak A. Sarhan.
 */
public class UmmalquraFormatData_en extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return new Object[][]{
						{"MonthNames_EN",
										new String[]{
														"Muharram",
														"Safar",
														"Rabi' al-Awwal",
														"Rabi' al-Thani",
														"Jumaada al-Ula",
														"Jumaada al-Akhirah",
														"Rajab",
														"Sha'ban",
														"Ramadhan",
														"Shawwal",
														"Dhulqaada",
														"Dhulhijja"}
						},
						{"MonthNames_ID",
										new String[]{
														"Muharram",
														"Shafar",
														"Rabi'ul Awwal",
														"Rabi'ul Tsani",
														"Jumaadal Ula",
														"Jumaadal Tsaniyah",
														"Rajab",
														"Sya'ban",
														"Ramadhan",
														"Syawwal",
														"Dzulqa'dah",
														"Dzulhijjah"}
						},
						{"MonthAbbreviations",
										new String[]{
														"Muh"
														, "Saf"
														, "Rab-I"
														, "Rab-II"
														, "Jum-I"
														, "Jum-II"
														, "Raj"
														, "Sha"
														, "Ram"
														, "Shw"
														, "Thul-Q"
														, "Thul-H"}}
		};
	}

}
