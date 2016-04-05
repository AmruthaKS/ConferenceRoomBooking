/**
 * 
 */
package com.phonegap.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.moyosoft.exchange.ExchangeServiceException;
import com.moyosoft.exchange.calendar.UsersAvailability;
import com.moyosoft.exchange.item.ExchangeItem;
import com.moyosoft.exchange.mail.ExchangeMail;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import com.phonegap.example.AuthenticationPlugin;


/**
 * This calls out the busyfree status plugin to get the busy/free status of the rooms
 *
 */
public class BusyFreeStatusPlugin extends Plugin {

	/* (non-Javadoc)
	 * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	private static final String SHOW_ACTION = "show";
	/**
     * Executes the request and returns PluginResult.
     *
     * @param arg0        The action to execute.
     * @param arg1          JSONArray of arguments for the plugin.
     * @param arg2    		The arg2 is used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
	
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		// TODO Auto-generated method stub
		//Log.d("BusyFreeStatusPlugin", "Plugin Called"+arg0+"\t"+arg1+"\t"+arg2);
		PluginResult result = null;
		JSONObject userinfo = null;
		JSONObject roomStatusInfo = null;
		if (SHOW_ACTION.equals(arg0)) {
			try 
			{
				if (!arg1.isNull(0))
				{
					//Log.d("in execute method", "before the function is called "+arg1.getString(0));
					 userinfo =  viewStatus(arg1.getString(0));
					if(userinfo.getString("status").equalsIgnoreCase("success"))
						result = new PluginResult(Status.OK, userinfo);
					else
						result = new PluginResult(Status.JSON_EXCEPTION,userinfo);	
					//Log.d("in execute method", "the object is "+userinfo+" the result is "+result);

				} 
				
			} catch (JSONException jsonEx)
			{
				try {
					userinfo.put("status","Connection to the server is lost..");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("busyFreePlugin", "Got JSON Exception " + jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION,userinfo);
				
			} catch (Exception e) {
				Log.d("In exception line 66"," ----");
				try {
					userinfo.put("status","Connection to the server is lost..");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				result = new PluginResult(Status.JSON_EXCEPTION,userinfo);
				e.printStackTrace();
			}
		} 
		else if(arg0.equals("suggest"))
		{
			try 
			{
				if (!arg1.isNull(0))
				{
					Log.d("in execute method++++", "suggest option being selected"+arg1.getString(0));
					String a = arg1.getString(0);
					 Log.d("a is--"," "+a);
					 roomStatusInfo =  viewSuggestions(arg1);
					 
					 
					if(roomStatusInfo.getString("status").equalsIgnoreCase("success"))
						result = new PluginResult(Status.OK, roomStatusInfo);
					else
						result = new PluginResult(Status.JSON_EXCEPTION,roomStatusInfo);	
					//Log.d("in execute method", "the object is "+userinfo+" the result is "+result);

				} 
				
			} catch (JSONException jsonEx)
			{
				try {
					roomStatusInfo.put("status","Connection to the server is lost..");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("busyFreePlugin", "Got JSON Exception " + jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION,roomStatusInfo);
				
			} catch (Exception e) {
				Log.d("In exception line 66"," ----");
				try {
					roomStatusInfo.put("status","Connection to the server is lost..");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				result = new PluginResult(Status.JSON_EXCEPTION,roomStatusInfo);
				e.printStackTrace();
			}
		}
		
		return result;
	}
	/**
	 * Get the suggestions of nearby conference rooms
	 * @param rooms				the array of room names
	 * @return					JSON object corresponding to the rooms availability status of the different conference rooms and status messages
	 * @throws JSONException
	 * @throws ParseException
	 */
	private JSONObject viewSuggestions(JSONArray rooms) throws JSONException, ParseException
	{
		JSONObject roomStatusInfo = new JSONObject();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String b = rooms.getString(0);
		String aa = b.replace("[", " ");
		String ab = aa.replace("]"," ");
		String a1 = ab.replace("\"", " ");
		String a = a1.trim();
		Log.d("+++","after replacing unwanted characters-->"+a);
		String rooms_array[] = a.split(",");
		Calendar now = Calendar.getInstance();
		TimeZone timeZone = now.getTimeZone();
		Log.d("Timezone offset-- suggest","The offset obtained is "+timeZone.getRawOffset());
		Log.d("rooms array"," "+rooms_array[1]+rooms_array[2]+rooms_array[3]+rooms_array[0]);
		 Calendar c = Calendar.getInstance();
		    System.out.println("current: "+c.getTime());

		    TimeZone z = c.getTimeZone();
		    int offset = z.getRawOffset();
		   // int off = z.getOffset(1, now.getTime().getYear(),now.getTime().getMonth(),now.getTime().getDate() ,now.getTime().getDay(), (int) now.getTimeInMillis());
		    int offsetHrs = offset / 1000 / 60 / 60;
		    int offsetMins = offset / 1000 / 60 % 60;

		    System.out.println("offset: " + offsetHrs);
		    System.out.println("offset: " + offsetMins);

		    c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
		    c.add(Calendar.MINUTE, (-offsetMins));
		    
		    System.out.println("GMT Time: "+c.getTime());

		    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date startDate = new Date();
	        Date endDate = new Date();
	        Log.d("Printing date","date in date constructor"+startDate+" The date is "+dateFormat1.format(startDate)+"and parsed date is.. "+dateFormat1.parse(dateFormat1.format(endDate)));
	        if(startDate.getMinutes() <= 20)
	        {
	        	startDate.setMinutes(0);
	        	startDate.setSeconds(0);
	        	
	        	
	        }	
	        else if(startDate.getMinutes() > 20 && startDate.getMinutes() <= 50)
	        {
	        	startDate.setSeconds(0);
	        	startDate.setMinutes(30);
	        		
	        	
	        }	
	        else
	        {
	        	startDate.setMinutes(0);
	        	startDate.setSeconds(0);
	        	startDate.setHours(startDate.getHours()+1);
	        		        	
	        }
	        endDate.setHours(startDate.getHours()+1);
	        endDate.setMinutes(startDate.getMinutes()+30);
	        endDate.setSeconds(0);
	       // System.out.println("the modified start date after setting the minutes is "+dateFormat1.format(startDate)+" the end date is "+dateFormat1.format(endDate));
	        startDate.setMinutes(startDate.getMinutes()-offsetMins);
	        startDate.setHours(startDate.getHours()-offsetHrs);
	        endDate.setMinutes(endDate.getMinutes()-offsetMins);
	        endDate.setHours(endDate.getHours()-offsetHrs);
	        //System.out.println("(GMT conv)todays date is "+dateFormat1.format(startDate)+" Minutes is "+startDate.getMinutes()+" Hours is "+startDate.getHours());
	       
	       
	        
				
		//Log.d("####","the name of the timezone on  the system is.. "+Tzonename);
		TimeZone firstTime = TimeZone.getTimeZone("GMT");
		//Log.d("####","the timezone returned using the ist is "+firstTime);
		//TimeZone tZone = TimeZone.getDefault();
		//Log.d("##","the new time zone returned is.. "+tZone);
		
		
		dateFormat.setTimeZone(firstTime);  //this is affecting... not setting this results in zeros
		
        
		
        Date startTime=null,endTime=null;
		try {
			com.phonegap.example.AuthenticationPlugin.exchange.setTimeZone(firstTime);
			startTime = dateFormat.parse(dateFormat1.format(startDate));
			Log.d("***","starttime--"+startTime);
			//Log.d("Date","parsed start date is "+startDate.getHours()+" "+(startDate.getYear()+1900)+" " +(startDate.getMonth()+1) +" "+startDate.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		 catch (NullPointerException e) {
		  Log.d("---","Exchange object not created..n is null");
		  roomStatusInfo.put("status", "failure");
		 }
        
		try {
			endTime = dateFormat.parse(dateFormat1.format(endDate));
			Log.d("***","endtime--"+endTime);
			//Log.d("Date","parsed end date is "+endTime.toLocaleString()+" "+endTime.toGMTString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Log.d("----","Before calling users availability");
		UsersAvailability Useravail=null;
		
		JSONObject roomStatusInfo1 = new JSONObject();
		 String merged_stat = null;
		 List<String> merged_stat_rooms = null;
		  int i,no=1;
		 for(i = 0; i< rooms_array.length;i++)
		 {
			 try {
		   
				 Useravail =  com.phonegap.example.AuthenticationPlugin.exchange.getUsersAvailability(startTime, endTime,30);
		   //Log.d("^^^","user availability success");
				 Useravail.setTimeZone(firstTime);
				 //Log.d("GGGGGGGGGGGGGGGG","the first room is-->"+rooms_array[0]+"-"+rooms_array[1]+"-"+rooms_array[2]);
				 Useravail.addRoom(rooms_array[i].trim());
		       Log.d("^^^",rooms_array[i].trim()+"^^addRoom success");
			   //Useravail.addRequiredAttendee("Amrutha.S@netapp.com");
			   //Useravail.addRoom("cr-NB-1.1.326@netapp.com");
		  // Log.d("^^^","user addRequiredAttendee success");
		   
		  
		   Log.d("++++","the availability status 1  is "+Useravail.getAvailabilities().get(0).getFreeBusy());
		   Log.d("++++","the availability status 2 is "+Useravail.getAttendeesFreeBusy());
		   merged_stat = Useravail.getAvailabilities().get(0).getFreeBusy();
		   merged_stat_rooms = Useravail.getAttendeesFreeBusy();
		   roomStatusInfo1.put(rooms_array[i].trim(),merged_stat);
		   
		   
		  
			 } 
	   catch(NullPointerException e ){
		   Log.d("****","Null pointer exception");
		   Log.d("**","Exchange Object is not created..or user session expired.. ");
		   roomStatusInfo.put("status", "User session expired");
	   	}
	   
	   
	   catch (ExchangeServiceException e) {
		// TODO Auto-generated catch block
		Log.d("***","exception in retrieving the availability"+e.getMessage());
		e.printStackTrace();
		if(e.getMessage().matches(".*ErrorMailRecipientNotFound.*"))
			roomStatusInfo.put("status", "Invalid room number");
		else if(e.getMessage().matches(".*HTTP error:.*timed out"))
			roomStatusInfo.put("status","Lost connection to the server");
		else if(e.getMessage().matches(".*HTTP error:.*server failed.*")) 
			roomStatusInfo.put("status","Lost connection to the server");
		else
			roomStatusInfo.put("status", "Failed to fetch the availability status");
		
		//userInfo.put("status","Failed");
		Log.d("----","val is"+roomStatusInfo.getString("status"));
		//return roomStatusInfo;
	   }
	  
 }
	   String startDateTime = " ";
	   startDateTime += startTime.getYear()+1900;
	   startDateTime += ".";
	   startDateTime += startTime.getMonth()+1;
	   startDateTime += ".";
	   startDateTime += startTime.getDate();
	   startDateTime += ".";
	   startDateTime += startTime.getHours();
	   startDateTime += ".";
	   startDateTime += startTime.getMinutes();
	   
	   roomStatusInfo.put("room_names", rooms);
	   roomStatusInfo1.put("status","success");
	   roomStatusInfo.put("starttime", startTime.toLocaleString());
	   roomStatusInfo.put("endtime",endTime.toLocaleString());
	   roomStatusInfo1.put("startDateTime", startDateTime);
	   roomStatusInfo.put("merged_stat", merged_stat);
	   roomStatusInfo.put("merged_room_status", merged_stat_rooms);
	  Log.d("------","Reached the end.. printing the userinfo json object "+roomStatusInfo);
	  	Log.d("000000","printing the new one-->"+roomStatusInfo1);		
	  return roomStatusInfo1;
		
		
	}
	/**
	 * Gets the busy/free status of the conference room
	 * @param user					name of the room
	 * @return						JSON object with room status info and status messages
	 * @throws JSONException
	 * @throws ParseException
	 * @throws ExchangeServiceException
	 */
	private JSONObject viewStatus(String user) throws JSONException, ParseException, ExchangeServiceException
	{
		
		JSONObject userInfo = new JSONObject();
		                
        /* timeZone
         *  set the timezone to gmt
         *  get the current time zone n modify the time to GMT
         *  then it will bw parsed n formatted as per the current time zone..
         * 
         */
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String Tzonename = TimeZone.getDefault().getDisplayName();
		
		Calendar now = Calendar.getInstance();
		TimeZone timeZone = now.getTimeZone();
		Log.d("Timezone offset","The offset obtained is "+timeZone.getRawOffset());
		
		 Calendar c = Calendar.getInstance();
		    System.out.println("current: "+c.getTime());

		    TimeZone z = c.getTimeZone();
		    int offset = z.getRawOffset();
		   // int off = z.getOffset(1, now.getTime().getYear(),now.getTime().getMonth(),now.getTime().getDate() ,now.getTime().getDay(), (int) now.getTimeInMillis());
		    int offsetHrs = offset / 1000 / 60 / 60;
		    int offsetMins = offset / 1000 / 60 % 60;

		    System.out.println("offset: " + offsetHrs);
		    System.out.println("offset: " + offsetMins);

		    c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
		    c.add(Calendar.MINUTE, (-offsetMins));
		    
		    System.out.println("GMT Time: "+c.getTime());

		    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date startDate = new Date();
	        Date endDate = new Date();
	        Log.d("Printing date","date in date constructor"+startDate+" The date is "+dateFormat1.format(startDate)+"and parsed date is.. "+dateFormat1.parse(dateFormat1.format(endDate)));
	        if(startDate.getMinutes() <= 20)
	        {
	        	startDate.setMinutes(0);
	        	startDate.setSeconds(0);
	        	
	        	
	        }	
	        else if(startDate.getMinutes() > 20 && startDate.getMinutes() <= 50)
	        {
	        	startDate.setSeconds(0);
	        	startDate.setMinutes(30);
	        		
	        	
	        }	
	        else
	        {
	        	startDate.setMinutes(0);
	        	startDate.setSeconds(0);
	        	startDate.setHours(startDate.getHours()+1);
	        		        	
	        }
	        endDate.setHours(startDate.getHours()+1);
	        endDate.setMinutes(startDate.getMinutes()+30);
	        endDate.setSeconds(0);
	       // System.out.println("the modified start date after setting the minutes is "+dateFormat1.format(startDate)+" the end date is "+dateFormat1.format(endDate));
	        startDate.setMinutes(startDate.getMinutes()-offsetMins);
	        startDate.setHours(startDate.getHours()-offsetHrs);
	        endDate.setMinutes(endDate.getMinutes()-offsetMins);
	        endDate.setHours(endDate.getHours()-offsetHrs);
	        //System.out.println("(GMT conv)todays date is "+dateFormat1.format(startDate)+" Minutes is "+startDate.getMinutes()+" Hours is "+startDate.getHours());
	    	
		//Log.d("####","the name of the timezone on  the system is.. "+Tzonename);
		TimeZone firstTime = TimeZone.getTimeZone("GMT");
		//Log.d("####","the timezone returned using the ist is "+firstTime);
		//TimeZone tZone = TimeZone.getDefault();
		//Log.d("##","the new time zone returned is.. "+tZone);
		
		
		dateFormat.setTimeZone(firstTime);  //this is affecting... not setting this results in zeros
		
        
		
        Date startTime=null,endTime=null;
		try {
			com.phonegap.example.AuthenticationPlugin.exchange.setTimeZone(firstTime);
			startTime = dateFormat.parse(dateFormat1.format(startDate));
			Log.d("***","starttime--"+startTime);
			//Log.d("Date","parsed start date is "+startDate.getHours()+" "+(startDate.getYear()+1900)+" " +(startDate.getMonth()+1) +" "+startDate.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		 catch (NullPointerException e) {
		  Log.d("---","Exchange object not created..n is null");
		  userInfo.put("status", "failure");
		 }
        
		try {
			endTime = dateFormat.parse(dateFormat1.format(endDate));
			Log.d("***","endtime--"+endTime);
			//Log.d("Date","parsed end date is "+endTime.toLocaleString()+" "+endTime.toGMTString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Log.d("----","Before calling users availability");
		UsersAvailability Useravail=null;
		
		
		 String merged_stat = null;
	   try {
		   
		   Useravail = com.phonegap.example.AuthenticationPlugin.exchange.getUsersAvailability(startTime, endTime,30);
		   //Log.d("^^^","user availability success");
		   Useravail.setTimeZone(firstTime);
		   Useravail.addRoom(user);
		  // Log.d("^^^",user + " addRoom success");
		  // Useravail.addRequiredAttendee("Amrutha.S@netapp.com");
		  // Log.d("^^^","user addRequiredAttendee success");
		   
		  
		   Log.d("++++","the availability status 1  is "+Useravail.getAvailabilities().get(0).getFreeBusy());
		   Log.d("++++","the availability status 2 is "+Useravail.getAttendeesFreeBusy());
		   merged_stat = Useravail.getAvailabilities().get(0).getFreeBusy();
		  
	} 
	   catch(NullPointerException e ){
		   Log.d("****","Null pointer exception");
		   Log.d("**","Exchange Object is not created..or user session expired.. ");
		   userInfo.put("status", "User session expired");
	   }
	   
	   
	   catch (ExchangeServiceException e) {
		// TODO Auto-generated catch block
		Log.d("***","exception in retrieving the availability"+e.getMessage());
		e.printStackTrace();
		if(e.getMessage().matches(".*ErrorMailRecipientNotFound.*"))
			userInfo.put("status", "Invalid room number");
		else if(e.getMessage().matches(".*HTTP error:.*timed out"))
			userInfo.put("status","Lost connection to the server");
		else if(e.getMessage().matches(".*HTTP error:.*server failed.*")) 
			userInfo.put("status","Lost connection to the server");
		else
			userInfo.put("status", "Failed to fetch the availability status");
		
		//userInfo.put("status","Failed");
		Log.d("----","val is"+userInfo.getString("status"));
		return userInfo;
	}
	  
	   String startDateTime = " ";
	   startDateTime += startTime.getYear()+1900;
	   startDateTime += ".";
	   startDateTime += startTime.getMonth()+1;
	   startDateTime += ".";
	   startDateTime += startTime.getDate();
	   startDateTime += ".";
	   startDateTime += startTime.getHours();
	   startDateTime += ".";
	   startDateTime += startTime.getMinutes();
	   
	   userInfo.put("room_name", user);
	   userInfo.put("status","success");
	  userInfo.put("starttime", startTime.toLocaleString());
	  userInfo.put("endtime",endTime.toLocaleString());
	  userInfo.put("startDateTime", startDateTime);
	  userInfo.put("merged_stat", merged_stat);
	  Log.d("------","Reached the end.. printing the userinfo json object "+userInfo);
	 
	  			
	  return userInfo;
	}

}
