package com.hust.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class TimeUtil {

	public static String convert(Cell cell) {
		if (cell == null)
			return "1900-01-01 00:00:00";
		if (DateUtil.isCellDateFormatted(cell)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
		}
		return new DecimalFormat("#").format(cell.getNumericCellValue());
	}

	public static boolean isvalidate(String time) {
		String regex = "^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$";
		return Pattern.matches(regex, time);
	}

	public static String DateToStr(Date date) {
		if (null == date) {
			return StringUtils.EMPTY;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static Date getSystemDate() throws ParseException {
		Date now = new Date();
		return now;
	}
	/**
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return 1 if DATE1(后) > DATE2(前)
	 * @return -1 if DATE1(前) < DATE2(后)
	 * @return 0 if DATE1 = DATE2 
	 */
	public static int compare_date(String DATE1, String DATE2) {        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {                
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {              
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	/**
	 * 根据给定时间往前推指定时间  如 2017-11-24 11：11：0 前一分钟 2017-11-24 11：10：0
	 * @param time 微秒
	 * @param curDate 给定时间
	 * @return
	 */
	public static Date getPreTime(long time,Date curDate){
		Date result = new Date(curDate.getTime()- time);
		return result;
	}
}
