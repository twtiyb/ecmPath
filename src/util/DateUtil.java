package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class DateUtil {
	private static final String format = "yyyy-MM-dd HH:mm:ss";
	public static SimpleDateFormat FMT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String DATE_REGEXP = "^[1-2]\\d{3}(\\-\\d{1,2}){2}( (\\d{1,2}:){2}\\d{1,2})?";
	private static Pattern DATE_Pattern = Pattern.compile(DATE_REGEXP);
	private static final int MODIFY_TRUNCATE = 0;
	private static final int MODIFY_ROUND = 1;
	private static final int MODIFY_CEILING = 2;
	public static final int SEMI_MONTH = 1001;
	
	private static final int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.HOUR},
        {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM 
            /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
        },
        {Calendar.MONTH, DateUtil.SEMI_MONTH},
        {Calendar.YEAR},
        {Calendar.ERA}};
	
	public static long getDayDiff(Date dateStart, Date dateEnd) {
		return (dateEnd.getTime() - dateStart.getTime()) / 86400000;
	}
	
	public static double getHoursDiff(Date dateStart, Date dateEnd) {
		if (dateEnd == null || dateEnd == null) return 0;
		return (dateEnd.getTime() - dateStart.getTime()) / 3600000.0;
	}
	/**
	 * 取当前日期与2012-1-1的差
	 * date yymmdd
	 * @return  天数
	 */
	public static long getDay(String date) {
		Date now = toDate(date, "yyMMdd");
		if (now == null) {
			now = Calendar.getInstance().getTime();
		} else {
			now.setSeconds(30);
		}
		Calendar c = Calendar.getInstance();
		c.set(2012, 0, 1, 0, 0, 0);

		return getDayDiff(c.getTime(), now);
	}
	
	public static String toDateTimeString(Date date) {
		return toDateString(date, format, null);
	}

	public static String toDateString(Date date) {
		return toDateString(date, "yyyy-MM-dd", null);
	}

	public static String toTimeString(Date date) {
		return toDateString(date, "HH:mm:ss", null);
	}

	public static String toDateString(Date date, String format) {
		return toDateString(date, format, null);
	}
	public static String toDateString(Date date, String format, TimeZone zone) {
		if (StringUtil.isEmpty(format)) {
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat f = new SimpleDateFormat(format);
		if(zone != null) f.setTimeZone(zone);
		return date == null ? "" : f.format(date);
	}
	
	public static String toTimeString(Date date,String format) {
		if (StringUtil.isEmpty(format)) {
			format = "HH:mm:ss";
		}
		SimpleDateFormat f = new SimpleDateFormat(format);
		return date == null ? "" : f.format(date);
	}
	/** 
	* 字符串转换成日期   format: yyyy-MM-dd HH:mm:ss, 如2012-01-01 13:05:09
	* @param str 
	* @return date 
	*/ 
	public static Date toDate(String str) {
		Date date = null;
		try {
			SimpleDateFormat f = new SimpleDateFormat(format);
			date = f.parse(str);
		} catch (Exception e) {
		}
		return date;
	}
	
	public static Date toShortDate(String str) {
		Date date = null;
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			date = f.parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	public static Date toDate(String str, String format) {
		Date date = null;
		try {
			SimpleDateFormat f = new SimpleDateFormat(format);
			date = f.parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 判断是否日期格式的字符串
	 * @param dateStr
	 * @return
	 */
	public static boolean isDateStr(String dateStr) {
   		if(dateStr == null || dateStr.length() == 0) return false;
   		return DATE_Pattern.matcher(dateStr).matches();
    }
	
	public static boolean isDateTime(String str) {
		return isDateTime(str, format);
	}
	
	public static boolean isShortDate(String str) {
		return isDateTime(str, "yyyy-MM-dd");
	}
	
	private static boolean isDateTime(String str,String format) {
		if (StringUtil.isEmpty(str) || StringUtil.isEmpty(format)) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			sdf.parse(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
	/**
	 * 日期增加天数
	 * @param date
	 * @param amount 天数
	 * @return
	 */
	public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }
	/**
	 * 日期增加小时数
	 * @param date
	 * @param amount 小时数
	 * @return
	 */
	public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }
	/**
	 * 日期增加毫秒数
	 * @param date
	 * @param amount 毫秒数
	 * @return
	 */
	public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }
	/**
	 * 日期增加分钟数
	 * @param date
	 * @param amount 分钟数
	 * @return
	 */
	public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }
	/**
	 * 日期增加月份
	 * @param date
	 * @param amount 月份数
	 * @return
	 */
	public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }
	/**
	 * 日期增加秒
	 * @param date
	 * @param amount 秒数
	 * @return
	 */
	public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }
	/**
	 * 日期增加星期数
	 * @param date
	 * @param amount 星期数
	 * @return
	 */
	public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }
	/**
	 * 日期增加年
	 * @param date
	 * @param amount 年数
	 * @return
	 */
	public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }
	
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	/**
	 * 判断两日期是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
	
	public static Date maxDate(Date date1,Date date2) {
		if (date1 == null && date2 != null) {
			return date2;
		} else if (date1 != null && date2 == null) {
			return date1;
		} else if (date1 == null && date2 == null) {
			return null;
		} else {
			if (date1.before(date2)) {
				return date2;
			} else {
				return date1;
			}
		}
	}
	
	private static void modify(Calendar val, int field, int modType) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }
        
        if (field == Calendar.MILLISECOND) {
            return;
        }

        Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;

        // truncate milliseconds
        int millisecs = val.get(Calendar.MILLISECOND);
        if (MODIFY_TRUNCATE == modType || millisecs < 500) {
            time = time - millisecs;
        }
        if (field == Calendar.SECOND) {
            done = true;
        }

        // truncate seconds
        int seconds = val.get(Calendar.SECOND);
        if (!done && (MODIFY_TRUNCATE == modType || seconds < 30)) {
            time = time - (seconds * 1000L);
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }

        // truncate minutes
        int minutes = val.get(Calendar.MINUTE);
        if (!done && (MODIFY_TRUNCATE == modType || minutes < 30)) {
            time = time - (minutes * 60000L);
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        // ----------------- Fix for LANG-59 ----------------------- END ----------------

        boolean roundUp = false;
        for (int[] aField : fields) {
            for (int element : aField) {
                if (element == field) {
                    //This is our field... we stop looping
                    if (modType == MODIFY_CEILING || (modType == MODIFY_ROUND && roundUp)) {
                        if (field == DateUtil.SEMI_MONTH) {
                            //This is a special case that's hard to generalize
                            //If the date is 1, we round up to 16, otherwise
                            //  we subtract 15 days and add 1 month
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
// ----------------- Fix for LANG-440 ---------------------- START ---------------
                        } else if (field == Calendar.AM_PM) {
                            // This is a special case
                            // If the time is 0, we round up to 12, otherwise
                            //  we subtract 12 hours and add 1 day
                            if (val.get(Calendar.HOUR_OF_DAY) == 0) {
                                val.add(Calendar.HOUR_OF_DAY, 12);
                            } else {
                                val.add(Calendar.HOUR_OF_DAY, -12);
                                val.add(Calendar.DATE, 1);
                            }
// ----------------- Fix for LANG-440 ---------------------- END ---------------
                        } else {
                            //We need at add one to this field since the
                            //  last number causes us to round up
                            val.add(aField[0], 1);
                        }
                    }
                    return;
                }
            }
            //We have various fields that are not easy roundings
            int offset = 0;
            boolean offsetSet = false;
            //These are special types of fields that require different rounding rules
            switch (field) {
                case DateUtil.SEMI_MONTH:
                    if (aField[0] == Calendar.DATE) {
                        //If we're going to drop the DATE field's value,
                        //  we want to do this our own way.
                        //We need to subtrace 1 since the date has a minimum of 1
                        offset = val.get(Calendar.DATE) - 1;
                        //If we're above 15 days adjustment, that means we're in the
                        //  bottom half of the month and should stay accordingly.
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        //Record whether we're in the top or bottom half of that range
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (aField[0] == Calendar.HOUR_OF_DAY) {
                        //If we're going to drop the HOUR field's value,
                        //  we want to do this our own way.
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset >= 6;
                        offsetSet = true;
                    }
                    break;
            }
            if (!offsetSet) {
                int min = val.getActualMinimum(aField[0]);
                int max = val.getActualMaximum(aField[0]);
                //Calculate the offset from the minimum allowed value
                offset = val.get(aField[0]) - min;
                //Set roundUp if this is more than half way between the minimum and maximum
                roundUp = offset > ((max - min) / 2);
            }
            //We need to remove this field
            if (offset != 0) {
                val.set(aField[0], val.get(aField[0]) - offset);
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");

    }
	/**
	 * 截取时间
	 * @param date 要截取的时间
	 * @param field 截取的时间域  (Calendar.DAY_OF_MONTH,Calendar.HOUR ... )
	 * @return 截取后的时间
	 */
	public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, MODIFY_TRUNCATE);
        return gval.getTime();
    }
	/**
	 * 截取时间后进行比较大小
	 * @param date1
	 * @param date2
	 * @param field 要截取的域 (Calendar.DAY_OF_MONTH,Calendar.HOUR ... )
	 * @return 负整数(第1个日期比第2个早),零(两日期相等),正整数(第1个比第2个晚)
	 */
	public static int truncatedCompareTo(Date date1, Date date2, int field) {
        Date truncatedDate1 = truncate(date1, field);
        Date truncatedDate2 = truncate(date2, field);
        return truncatedDate1.compareTo(truncatedDate2);
    }
	/**
	 * 截取时间后比较两日期是否相等
	 * @param date1
	 * @param date2
	 * @param field 要截取的域 (Calendar.DAY_OF_MONTH,Calendar.HOUR ... )
	 * @return true(截取后的两日期相等),false(截取后的两日期不相等)
	 */
	public static boolean truncatedEquals(Date date1, Date date2, int field) {
        return truncatedCompareTo(date1, date2, field) == 0;
    }
	
	/**
	 * 获取date月中最后一天
	 * @param date
	 * @return
	 */
	public static Date lastDayOfMonth(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.roll(Calendar.DAY_OF_MONTH, -1); 
		return cal.getTime(); 
	} 
	
	/**
	 * 获取date月中第一天
	 * @param date
	 * @return
	 */
	public static Date firstDayOfMonth(Date date) { 
		String dateStr = toDateString(date);
		dateStr = dateStr.substring(0, 8) + "01";
		return toShortDate(dateStr); 
	} 
	
	/**
	 * 取得系统时间前一天的日期
	 * @return
	 */
	public static Date beforeOneDayOfSysdate(){
		Calendar c = Calendar.getInstance();    
		c.add(Calendar.DAY_OF_MONTH, -1);   
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
		String mDateTime = formatter.format(c.getTime());   
		String strStart = mDateTime.substring(0, 10);//2007-10-29 09:30  
		return toShortDate(strStart);
	}
	
	/** 
	* 字符串转换成日期 
	* @param str 
	* @return date 
	*/ 
	public static Date StrToDate(String str) {
		Date date = null;
		try {
			date = FMT_DATETIME.parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date;
	} 
	
	/**
	 * 返回该日期的星期
	 * @param date
	 * @return
	 */
	public static String getWeekDay(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
		return simpleDateFormat.format(date);
	}
	/**
	 * 返回该日期是当前星期第几天
	 * @param date
	 * @return  1,2,3,4,5,6,7 分别对应周一,周二至周日
	 */ 
	public static int getDayOfWeed(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int a = cal.get(Calendar.DAY_OF_WEEK)-1;
		return a==0?7:a;
	}
	
	
	/**
	 * 获取两个时间间隔内的小时数
	 * 一天按8小时算 上班时间为 9-12 , 13-18
	 * d1 < d2
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getHoursBetweenDates(Date d1 , Date d2){
		if(d2.getTime()<=d1.getTime()){return 0 ;}
		long subDays = (d2.getTime()-d1.getTime())/(24*60*60*1000);
		long h1 = d1.getHours() ;
		long h2 = d2.getHours() ;
		if (subDays==0) {
			if(h1<h2){
				return getHours(h1,h2) ;
			}else {
				return getHours(h1,24)+getHours(0,h2) ;
			}	
		}else {
			if (h1<=h2) {
				return getHours(h1, 24) + getHours(0, h2) + (subDays - 1) * 8;
			}else {
				return getHours(h1, 24) + getHours(0, h2) + subDays * 8;
			}
		}
	}
    public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) {
    	if(date == null) return null;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
		}
		return gc;
	}
	
	private static long getHours(long h1,long h2){
		if (h2<=h1) {
			return 0 ;
		}
		if (h2 > 12) {
			if (h1>=13) {
				if (h2<=18) {
					return h2-h1 ;
				}else {
					if (h1<18) {
						return 18 -h1 ;
					}else {
						return 0 ;
					}
				}
			}else if (h1<13) {
				if (h1<=9) {
					if (h2<=18) {
						return 3 + h2 - 13;
					}else {
						return 8 ;
					}
				}else {
					if (h2<18) {
						return 12 - h1 +h2 -13 ;
					}else {
						return 12-h1 +5 ;
					}
				}
			}else {
				return 0 ;
			}
		}else {
			if (h2<=9) {
				return 0 ;
			}else {
				if (h1<9) {
					return h2 - 9 ;
				}else {
					return h2 - h1 ;
				}
			}	
		}
	} 

 	public static Date[] splitDate(Date startDate, Date endDate, int days, int timeType) {
 		if(startDate == null || endDate == null) return new Date[0];
 		if (startDate.after(endDate)) {
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}

 		long daysMill = days;
		switch (timeType) {
		case Calendar.DAY_OF_MONTH: daysMill = days * 3600 * 24; break;
		case Calendar.HOUR: daysMill = days * 3600; break;
		case Calendar.MINUTE: daysMill = days * 60; break;
		case Calendar.SECOND: daysMill = days; break;
		default: daysMill = days * 3600 * 24; break;
		}
		long dis = (endDate.getTime() - startDate.getTime()) / 1000;
		Long num = (dis + daysMill - 1) / daysMill;
		Date[] lRet = new Date[(++num).intValue()];
		lRet[0] = startDate;
		
		for (int i = 1; i < lRet.length - 1; i++) {
			long temp = startDate.getTime() / 1000 + daysMill * i;
			lRet[i] = new Date(temp * 1000);
		}
		lRet[num.intValue() - 1] = endDate;
		return lRet;
 	}

	public static void main(String arg[]){
		
			System.err.println(getPreviousMonthFirst());
	}
	
	// 上月第一天
	 public static String getPreviousMonthFirst() {
	  String str = "";
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	  Calendar lastDate = Calendar.getInstance();
	  lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
	  lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
	  // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

	  str = sdf.format(lastDate.getTime());
	  return str;
	 }

	 // 获取当月第一天
	 public static String getFirstDayOfMonth() {
	  String str = "";
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	  Calendar lastDate = Calendar.getInstance();
	  lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
	  str = sdf.format(lastDate.getTime());
	  return str;
	 }


	 // 获取当天时间
	 public static String getNowDate() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}
	
	// 获取当天时间
	 public static String getNowDateTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}
	 
	 //获取本周第一天
	 public static String getNowWeekBegin() {
		 int mondayPlus;
		 Calendar cd = Calendar.getInstance();
		 // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		 int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		 if (dayOfWeek == 1) {
		 mondayPlus = 0;
		 } else {
		 mondayPlus = 1 - dayOfWeek;
		 }
		 GregorianCalendar currentDate = new GregorianCalendar();
		 currentDate.add(GregorianCalendar.DATE, mondayPlus);
		 Date monday = currentDate.getTime();
		 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
		  String hehe = dateFormat.format(monday);
		  return hehe;
	 }




	 



}
