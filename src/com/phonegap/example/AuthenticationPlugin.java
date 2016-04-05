package com.phonegap.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.moyosoft.exchange.Exchange;
import com.moyosoft.exchange.ExchangeServiceException;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;


/**
 * This is used to authenticate the user
 * with the credentials he has entered with the microsoft exchange server
 * 
 */
public class AuthenticationPlugin extends Plugin {
	public static Exchange exchange = null;
	private static final String LOGIN_ACTION = "login";
	
	/**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArray of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
	
	@Override
	public PluginResult execute(String arg0, JSONArray arg1, String arg2) {
		PluginResult result = null;
		if (LOGIN_ACTION.equals(arg0)) {
			try 
			{
				if (!arg1.isNull(0))
				{
					//Log.d("in execute method of login plugin", "arguments are "+arg1.getString(0)+"  "+arg1.getString(1));
					JSONObject userinfo =  Login_create_object(arg1.getString(0),arg1.getString(1)); //should contain a list of attendees
					if(userinfo.getString("status").equals("1") )
						result = new PluginResult(Status.OK, userinfo);
					else
						result = new PluginResult(Status.JSON_EXCEPTION,userinfo);
					//Log.d("in execute method", "the object is "+userinfo+" the result is "+result);

				} 
				
			} catch (JSONException jsonEx)
			{
				//Log.d("login plugin", "Got JSON Exception " + jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION);
				
			} catch (Exception e) {
				//Log.d("In exception login plugin"," ----");
				//e.printStackTrace();
			}
		} 
		
		return result;
			
	}
	/**
	 * Authentication of the user followed by creation of exchange object
	 * @param uname
	 * @param pword
	 * @return A JSON object with the status and a message
	 * @throws JSONException
	 */
	private JSONObject Login_create_object(String uname,String pword)  throws JSONException {
		JSONObject userInfo = new JSONObject();
				
		 try {
	        	//com.moyosoft.exchange.Exchange.Settings.AutoSetTimeZones = true; //this is nt affecting
				exchange = new Exchange("email.netapp.com",uname,pword,"NETAPP",true);
				userInfo.put("status","1");
				
			} catch (ExchangeServiceException e) {
				// TODO Auto-generated catch blockS
				//Log.d("-----","Inside the exchange service exception login plugin cant create an instance of exchange");
				e.printStackTrace();
				userInfo.put("status", "0");
				if(e.getMessage().matches(".*HTTP error: The host is unknown.*"))
				userInfo.put("cause","Couldn't find the server");
				else if(e.getMessage().matches(".*HTTP error: .* timed out"))
					userInfo.put("cause","Lost connection with the server..Slow internet connection");
				else
					userInfo.put("cause","Bad username/password..");
				}
		return userInfo;
	}
}