package com.phonegap.example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.moyosoft.exchange.ExchangeServiceException;
import com.moyosoft.exchange.calendar.ExchangeAttendee;
import com.moyosoft.exchange.calendar.ExchangeCalendarItem;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

/**
 * This is called when a meeting has to be set up
 * 
 */
public class MeetingPlugin extends Plugin {

	private static final String ACTION = "schedule";
	/**
     * Executes the request and returns PluginResult.
     *
     * @param arg0          The action to execute.
     * @param arg1          JSONArray of arguments for the plugin.
     * @param arg2    		The arg2 is used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		// TODO Auto-generated method stub
		PluginResult result = null;
		if (ACTION.equals(arg0)) {
			try 
			{
				if (!arg1.isNull(0))
				{
					//Log.d("in execute method of meeting plugin", "arguments are "+arg1.getString(0)+"  "+arg1.getString(1)+"  "+arg1.getString(2));
					JSONObject userinfo =  scheduleMeeting(arg1); //should contain a list of attendees
					result = new PluginResult(Status.OK, userinfo);
					//Log.d("in execute method", "the object is "+userinfo+" the result is "+result);

				} 
				
			} catch (JSONException jsonEx)
			{
				//Log.d("busyFreePlugin", "Got JSON Exception " + jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION);
				
			} catch (Exception e) {
				//Log.d("In exception"," ----");
				result = new PluginResult(Status.ERROR);
				e.printStackTrace();
			}
		} 
		
		return result;
			
		
	}
	/**
	 * Schedule a meeting for the conference room
	 * @param arg1				Conference room name
	 * @return					JSON object with the status and a message
	 * @throws JSONException
	 * @throws ExchangeServiceException
	 * @throws ParseException
	 */
	private JSONObject scheduleMeeting(JSONArray arg1) throws JSONException, ExchangeServiceException, ParseException {
		JSONObject meetingInfo = new JSONObject();
		     
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String Tzonename = TimeZone.getDefault().getDisplayName();
		
        
        Calendar c = Calendar.getInstance();
	    System.out.println("current: "+c.getTime());

	    TimeZone z = c.getTimeZone();
	    int offset = z.getRawOffset();
	    int offsetHrs = offset / 1000 / 60 / 60;
	    int offsetMins = offset / 1000 / 60 % 60;

	    System.out.println("offset: " + offsetHrs);
	    System.out.println("offset: " + offsetMins);

	    c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
	    c.add(Calendar.MINUTE, (-offsetMins));
        
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
        startDate.setMinutes(startDate.getMinutes()-offsetMins);
        startDate.setHours(startDate.getHours()-offsetHrs);
        endDate.setMinutes(endDate.getMinutes()-offsetMins);
        endDate.setHours(endDate.getHours()-offsetHrs);
        System.out.println("todays date is "+dateFormat1.format(startDate)+" Minutes is "+startDate.getMinutes()+" Hours is "+startDate.getHours());
        if(startDate.getMinutes() < 30)
        {
        	startDate.setMinutes(0);
        	startDate.setSeconds(0);
        	endDate.setMinutes(30);
        	endDate.setSeconds(0);
        	
        }	
        else
        {
        	startDate.setSeconds(0);
        	startDate.setMinutes(30);
        	endDate.setMinutes(0);
        	endDate.setSeconds(0);
        	endDate.setHours(endDate.getHours()+1);
        	
        }	
        System.out.println("the modified start date after setting the minutes is "+dateFormat1.format(startDate)+" the end date is "+dateFormat1.format(endDate));
        
			
	//Log.d("####","the name of the timezone on  the system is.. "+Tzonename);
	TimeZone firstTime = TimeZone.getTimeZone("GMT");
	//Log.d("####","the timezone returned using the ist is "+firstTime);
	//TimeZone tZone = TimeZone.getDefault();
	//Log.d("##","the new time zone returned is.. "+tZone);
	
	
	dateFormat.setTimeZone(firstTime);  //this is affecting... not setting this results in zeros
	com.phonegap.example.AuthenticationPlugin.exchange.setTimeZone(firstTime);
    
	com.phonegap.example.AuthenticationPlugin.exchange.setTimeZone(firstTime);
    	
	String d = arg1.getString(1).toString();
	Log.d("the value stored in string d is..","+++"+d);
	String delimiter = "\\.";
	String s[] = d.split(delimiter); 
	//Log.d(" "," "+s);
    //for (String element : s) {
   //     Log.d("%%%%%%%%%%%%%%%%%%","Hi "+element);
  //  }
	
    SimpleDateFormat reqDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    Date newStartDate = new Date(); 
    Date newEndDate = new Date();
    
    newStartDate.setYear(Integer.parseInt(s[0].trim())-1900);
    newStartDate.setMonth(Integer.parseInt(s[1].trim())-1);
    newStartDate.setDate(Integer.parseInt(s[2].trim()));
    newStartDate.setHours(Integer.parseInt(s[3].trim()));
    newStartDate.setMinutes(Integer.parseInt(s[4].trim()));
    newStartDate.setSeconds(0);
    //convert it to gmt before we parse and send to exchange.. 
    newStartDate.setMinutes(newStartDate.getMinutes()-offsetMins);
    newStartDate.setHours(newStartDate.getHours()-offsetHrs);
    
    newEndDate.setYear(Integer.parseInt(s[0].trim())-1900);
    newEndDate.setMonth(Integer.parseInt(s[1].trim())-1);
    newEndDate.setDate(Integer.parseInt(s[2].trim()));
    newEndDate.setSeconds(0);
    
    String num_slots = arg1.getString(2).toString();
    // setting up endtime based upon the radio button being selected..
    if(num_slots.equalsIgnoreCase("s1")) //half an hour slot..
    {
    	newEndDate.setHours(newStartDate.getHours());
    	newEndDate.setMinutes(newStartDate.getMinutes()+30);
    }
    else if(num_slots.equalsIgnoreCase("s2")) //one hour slot..
    {
    	newEndDate.setHours(newStartDate.getHours()+1);
    	newEndDate.setMinutes(newStartDate.getMinutes());
    }
    else if(num_slots.equalsIgnoreCase("s3")) //one and half an hour slot..
    {
    	newEndDate.setHours(newStartDate.getHours()+1);
    	newEndDate.setMinutes(newStartDate.getMinutes()+30);
    }
    else if(num_slots.equalsIgnoreCase("s4")) //next half an hour slot.. modifying starttime as well
    {
    	newStartDate.setMinutes(newStartDate.getMinutes()+30);
    	newEndDate.setHours(newStartDate.getHours());
    	newEndDate.setMinutes(newStartDate.getMinutes()+30);
    }
    else if(num_slots.equalsIgnoreCase("s5")) //next one hour slot.. modifying starttime as well
    {
    	newStartDate.setMinutes(newStartDate.getMinutes()+30);
    	newEndDate.setHours(newStartDate.getHours()+1);
    	newEndDate.setMinutes(newStartDate.getMinutes());
    }
    newEndDate.setHours(newEndDate.getHours());
    Log.d("the start date set is", "--"+dateFormat.parse(reqDateFormat.format(newStartDate))+"the enddate is"+dateFormat.parse(reqDateFormat.format(newEndDate)));
    Log.d("hi","Printitng after dat");
    Date startTime=null,endTime=null;
	try {
		startTime = dateFormat.parse(reqDateFormat.format(newStartDate));
		Log.d("Date------------","parsed start date is "+startTime);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	try {
		endTime =dateFormat.parse(reqDateFormat.format(newEndDate));
		Log.d("Date-----------","parsed end date is "+endTime);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	TimeZone firstTime1 = TimeZone.getTimeZone("GMT");
    ExchangeCalendarItem meeting = com.phonegap.example.AuthenticationPlugin.exchange.createCalendarItem();
		try {
			meeting.setSubject("Instant Meeting");
			meeting.setStart(startTime);
			meeting.setEnd(endTime);
			meeting.setStartTimeZone(firstTime1);
			// Add the user2 as the required attendee to send him a request:
			Log.d("Meeting plugin"," The meeting request sent for the conference room "+arg1.getString(0));
			meeting.addRequiredAttendee(arg1.getString(0));
			meeting.save();
   
			
			
		} catch (ExchangeServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.List<ExchangeAttendee> list = meeting.getRequiredAttendees();
		ExchangeAttendee attendee = null;
		int i;
		for(i=0;i<list.size();i++)
		{
			attendee = list.get(i);
			System.out.println("attendees are"+attendee.getMailbox().getName());
		}
		attendee = list.get(0);	
		Log.d("+++++++++++++++++++++","attendees are"+attendee.getMailbox().getName());
		return meetingInfo;
	}
	
}