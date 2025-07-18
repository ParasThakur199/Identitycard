package com.idcard.CommonData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class Juleandate {
	

	public static String juliandate()
	{
		 String julianDate=null;	
		try {
			
			 SimpleDateFormat julianDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms");
			  Date date = new Date();
			  julianDate = julianDateFormat.format(date);			 
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return julianDate;
		
	}
	
	public static String juliandateToTime(Date date)
	{
		 String julianDate=null;	
		try {
			
			 SimpleDateFormat julianDateFormat = new SimpleDateFormat("HH:mm");
			  julianDate = julianDateFormat.format(date);			 
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return julianDate;
		
	}

	public static Date getCurrentDateTime()
	{
		Date parsedDate=null;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        Calendar cal = Calendar.getInstance();
	        String formattedDate = dateFormat.format(cal.getTime()); 
	        try {
	            parsedDate = dateFormat.parse(formattedDate); 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return parsedDate;
	}
	public static String getDatetoString(Date date)
	{ 
		 String formattedDate="";
		 if(date!=null)
		    {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	        formattedDate = dateFormat.format(date);
		    }
		return formattedDate;
	}
	
	public static String getDatetoStringWithTime(Date date)
	{ 
		 String formattedDate="";
		 if(date!=null)
		    {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS"); 
	        formattedDate = dateFormat.format(date);
		    }
		return formattedDate;
	}
	public static Date getStringtoDate(String date)
	{  
	    Date date1=null;
	    if(date!=null && date!="")
	    {
		try {
			date=date.replaceAll("-", "/");
			String[] parts = date.split("/");
			if(parts[0].length()==2)
			{
				 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
				    date1=(sdf2.parse(sdf2.format(sdf.parse(date))));
			}
			else {
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				 date1 = dateFormat.parse(date);
			} 

		} catch (ParseException e) { 
		e.printStackTrace();
		}  
	    }
		return date1;
	} 
	public static String juliandate2() {
		 String julianDate=null;	
			try {
				 SimpleDateFormat julianDateFormat = new SimpleDateFormat("dd/MM/yyyy ");
				  Date date = new Date();
				  julianDate = julianDateFormat.format(date);			 
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return julianDate;
	}
	
	public static Date futuredate(Date date) {
		 Date futureDate = null;
			
			try {
				Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        calendar.add(Calendar.DAY_OF_YEAR, 1);
		         futureDate = calendar.getTime();			 
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return futureDate;
	}

	public static String time24HourTotime12Hour(String time24Hour)  {
        LocalTime time = LocalTime.parse(time24Hour, DateTimeFormatter.ofPattern("HH:mm")); 
        String time12Hour = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
        return time12Hour; 
	}
	public static Date bindDate_time(Date date,String time)
	{
		if(time==null || time.isEmpty() || time.isBlank()) {
			time="00:00 AM";
		}
		  SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	        try {
	            // Parse the time string into a Date object (only time part is considered)
	            Date timePart = timeFormat.parse(time); 
	            // Use Calendar to combine date and time
	            Calendar dateCal = Calendar.getInstance();
	            dateCal.setTime(date); // Set the original date 
	            Calendar timeCal = Calendar.getInstance();
	            timeCal.setTime(timePart); // Set the parsed time 
	            // Set hours, minutes, and seconds from time part to date
	            dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
	            dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
	            dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
	            dateCal.set(Calendar.MILLISECOND, 0); // Clear milliseconds 
	            // Return the combined date and time
	            return dateCal.getTime(); 
	        }catch (Exception e) {
				e.printStackTrace();
			}
			return date;
	}
	
	
}
