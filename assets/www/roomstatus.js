var resulta;
var name_val = getUrlVars()["name"];
var starttime;
var endtime;
var attendees;
var suggestion_result;
var start;

function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}
 function init_status()
  {
  console.log("inside the init_status function");
  document.addEventListener("deviceready", ready, true); 
  //showLoading();
  
  }
  
  function generateDialogUrl() {
  
  tail = [];
  for (var p in suggestion_result) {
    if (suggestion_result.hasOwnProperty(p) && suggestion_result[p] !="222" && suggestion_result[p] !="444" && suggestion_result[p] !="220") {
      tail.push(p + "=" + encodeURIComponent(suggestion_result[p]));
    }
  }
  return  tail.join("&")
}
  
  function suggest()
  {
  //alert("in suggest fuction");
  for(var key in suggestion_result)
		{
			if(suggestion_result.hasOwnProperty(key))
			{
				var val = suggestion_result[key];
				//alert("the key is-->"+key+" the values is-->"+val);
			}
		}
	
	queryString = generateDialogUrl();
		console.log("the query string is.."+queryString);
	if(queryString == "") 
	{
	alert("sorry no suggestions available...!!");
	document.getElementById("btn_suggest").style.display='none';
	}
	else
    navigator.app.loadUrl("file:///android_asset/www/suggestions.html?"+queryString+"&start="+encodeURIComponent(start));
  
  
  }
  function clone(obj){
    if(obj == null || typeof(obj) != 'object')
        return obj;

    var temp = obj.constructor(); // changed

    for(var key in obj)
        temp[key] = clone(obj[key]);
    return temp;
}
  
 function fetchSuggestions()
 {
 	//alert("fetch suggestions called");
 	//Form an array of room names and call suggest function with this as argument..
 	room_number_parts = name_val.split(".");
 	room_number = room_number_parts[2].split("@");
 	var room_numbers_array = new Array();
 	//console.log("%%%%"+"new array created.");
 	rm = parseInt(room_number[0]);
 	rm = rm-2;
 	room_numbers_array[0] = room_number_parts[0]+"."+room_number_parts[1]+"."+rm+"@netapp.com";
 	rm = rm + 1;
 	room_numbers_array[1] = room_number_parts[0]+"."+room_number_parts[1]+"."+rm+"@netapp.com";
 	rm = rm + 2;
 	room_numbers_array[2] = room_number_parts[0]+"."+room_number_parts[1]+"."+rm+"@netapp.com";
 	rm = rm + 1;
 	room_numbers_array[3] = room_number_parts[0]+"."+room_number_parts[1]+"."+rm+"@netapp.com";
 	//console.log("%%%%"+"new array created. and assigned values");
 	//console.log("Room numbers"+"Displaying the room numbers....."+room_numbers_array[0]+room_numbers_array[1]+room_numbers_array[2]+room_numbers_array[3]);
 	
 	window.plugins.BusyFreeStatus.suggest(room_numbers_array,
	function(r){
		//alert(r+"success");
		delete r.status;
		start = r.startDateTime;
		delete r.startDateTime;
		
		for(var key in r)
		{
			if(r.hasOwnProperty(key))
			{
				var val = r[key];
				//alert("the key is-->"+key+" the values is-->"+val);
			}
		}
		// Now the object with the room status is ready.. now show the suggestions button..	
		document.getElementById("loadText").style.display = 'none';
		document.getElementById("btn_suggest").style.display='block';
		suggestion_result = clone(r);
	},
	function(e) {
	alert(e);
	});
 	//with the json object obtained.. pass this to suggestions.html and display info there.. 
    
 
 }
  function initialization(r){
  	resulta = r;
  	//console.log("in the initialization function-- before opening new html page "+result);
   	navigator.app.loadUrl("file:///android_asset/www/roomStatus.html");
   
  }
  
  var ready = function()
  {
  	// showLoading();
  	 init();
	//console.log("inside the ready function.. the result is "+result);
  	var name_val = getUrlVars()["name"];
	//console.log("the result is got using the barcode script "+name_val);
	
	
	window.plugins.BusyFreeStatus.show(name_val,
	function(r){
	
	
	var roomName1 = name_val.split("-");
	var roomName = roomName1[1]+".";
	var roomName2 = roomName1[2].split(".");
	roomName += roomName2[1]+".";
	var roomName3 = roomName2[2].split("@");
	roomName += roomName3[0];
	
	var newhtml = "Meeting Room : "+"<p style='color:#CC9966;display:inline'>"+roomName+"</p>";
	document.getElementById("name").innerHTML = newhtml;
		var type = r.status;
	//console.log("the val is "+r.room_name+"  "+r.status+" the type is...  "+typeof type);
	//document.getElementById("busyFree").innerHTML = "Status : "+r.status;
	//document.getElementById("starttime").innerHTML = "Start Time : "+r.starttime;
	//document.getElementById("endtime").innerHTML = "End time : "+r.endtime;
	 var strDate = r.startDateTime;
	 //console.log("The value of date received is.."+strDate);
   var dateParts = strDate.split(".");
  var setStartTime;
   if( parseInt(dateParts[4] ) == 0 ) 
   {
   		//console.log("its zero......!!!!!");
   		dateParts[4] = "00";
   }
   	else
   	{
   		dateParts[4] = 30;
   	}
	if (dateParts[3] < 12)
	 setStartTime = "Start Time :"+ dateParts[3] + ":" + dateParts[4] + " AM";
	else if(dateParts[3] == 12)
	 setStartTime = "Start Time :"+ dateParts[3] + ":" + dateParts[4] + " PM";
	else
	setStartTime = "Start Time : "+ dateParts[3] % 12 + ":" + ( dateParts[4]) + " PM";
	starttime = r.startDateTime;
	
	
	var startTime_new = setStartTime;
	var endTime = startTime_new.split(":");
	// endTime[1] refers to hours
	//  endTime[2] refers to minutes and AM/PM
	var minutes = endTime[2].split(" ");
	// minutes[0] refers to number of minutes 
	// minutes[1] refers to AM or PM
	
	var sTime = endTime[1]+":"+endTime[2];
	//console.log("the split values for calculating the endtime are "+endTime[1]+"hours "+minutes[0]+" minutes "+minutes[1]);
	var endHours = parseInt(endTime[1]);
	var endMinutes = parseInt(minutes[0]);
	var timeFrame = minutes[1];
	
	var endHours1,endMinutes1,timeFrame1,endHours2,endMinutes2,timeFrame2,endHours3,endMinutes3,timeFrame3;
	function alertDismissed() {
        // do something
    }
	if(endMinutes == 0)
	{
		//for first half an hour slot..
		endHours1 = endHours;
		endMinutes1 = 30;
		timeFrame1 = timeFrame;
		
		//for the 1 hour slot
		endHours2 = endHours+1;
		endMinutes2 = endMinutes;
		if(endHours2 >= 12)
		{
			timeFrame2 = "PM";
			if(endHours2 > 12)
				endHours2 = endHours2 % 12;
		}
		else
		    timeFrame2 = timeFrame;
		    
		//for 1 and half an hour slot..
		endHours3 = endHours+1;
		endMinutes3 = 30;
		if(endHours3 >= 12)
		{
			timeFrame3 = "PM";
			if(endHours3 > 12)
				endHours3 = endHours3 % 12;
		}
		else
		    timeFrame3 = timeFrame;
	
	}
	else if(endMinutes == 30 )
	{
		//for the first half an hour slot
		endHours1 = endHours+1;
		endMinutes1 = 0;
		if(endHours1 >= 12)
		{
			timeFrame1 = "PM";
			if(endHours1 > 12)
				endHours1 = endHours1 % 12;
		}
		else
		    timeFrame1 = timeFrame;
		    
		//for the 1 hour slot
		endHours2 = endHours+1;
		endMinutes2 = endMinutes;
		if(endHours2 >= 12)
		{
			timeFrame2 = "PM";
			if(endHours2 > 12)
				endHours2 = endHours2 % 12;
		}
		else
		    timeFrame2 = timeFrame;
		    
		//for 1 and half an hour
		endHours3 = endHours+2;
		endMinutes3 = 0;
		if(endHours3 >= 12)
		{
			timeFrame3 = "PM";
			if(endHours3 > 12)
				endHours3 = endHours3 % 12;
		}
		else
		    timeFrame3 = timeFrame;
		
	
	}
	if (endMinutes1 == 0)
		endMinutes1 = "00";
	if (endMinutes2 == 0)
		endMinutes2 = "00";
	if (endMinutes3 == 0)
		endMinutes3 = "00";
	
	attendees = name_val;
	var st = r.status; 
	var flag = 0;
	var merged_status = r.merged_stat;
	
	if(merged_status =='022' || merged_status =='020')
	{
		//display only first half n hour slot..
		 var newhtml = "Status :"+"<p style='color:#009933'>Available</p>"+"Book for";
	    document.getElementById("busyFree").innerHTML = newhtml;
		document.getElementById("first").style.display='inline';
		document.getElementById("first_text").innerHTML = "30 minutes "+"[ "+sTime+" - "+endHours1+":"+endMinutes1+" "+timeFrame1+" ]<br>";
		flag=1;
		//alert("Inside the status 020 or 022");
	}
	else if(merged_status == '000')
	{
	    var newhtml = "Status :"+"<p style='color:#009933'>Available</p>"+"Book for";
	    document.getElementById("busyFree").innerHTML = newhtml;
	    document.getElementById("first").style.display='inline';
	    document.getElementById("first_text").innerHTML = "30 minutes "+"[ "+sTime+" - "+endHours1+":"+endMinutes1+" "+timeFrame1+" ]";
		  document.getElementById("second").style.display='inline';
	     document.getElementById("second_text").innerHTML = "60 minutes "+"[ "+sTime+" - "+endHours2+":"+endMinutes2+" "+timeFrame2+" ]";
	    document.getElementById("third").style.display='inline'; 
	     document.getElementById("third_text").innerHTML = "90 minutes "+"[ "+sTime+" - "+endHours3+":"+endMinutes3+" "+timeFrame3+" ]<br><br><br>";
	    flag=1;
	   // alert("Inside the status 000");
	}
	else if(merged_status == '002')
	{
		 var newhtml = "Status :"+"<p style='color:#009933'>Available</p>"+"Book for";
	    document.getElementById("busyFree").innerHTML = newhtml;
		document.getElementById("first").style.display='inline'; 
	    document.getElementById("second").style.display='inline';
	     document.getElementById("first_text").innerHTML = "30 minutes "+"[ "+sTime+" - "+endHours1+":"+endMinutes1+" "+timeFrame1+" ]";
	      document.getElementById("second_text").innerHTML = "60 minutes "+"[ "+sTime+" - "+endHours2+":"+endMinutes2+" "+timeFrame2+" ]<br><br>";
	     
	    flag=1;
	  //  alert("Inside the status 002");
	
	}
	else if(merged_status == '202')
	{
		document.getElementById("busyFree").innerHTML = "Status :<br>"
		 var oldhtml = document.getElementById("busyFree").innerHTML ;
		 var newhtml = oldhtml+"<p style='color:red'>Currently Busy.. But..</p><br>"+"<br> Suggestions :";
		 document.getElementById("busyFree").innerHTML =  newhtml;   
		// document.getElementById("availability").innerHTML = "Currently Busy ";
		// change the starttime and endtime accordingly
		// starttime + half an hour and endtime is starttime+hour
		
		 document.getElementById("fourth_text").innerHTML = "30 minutes "+"[ "+endHours1+":"+endMinutes1+" "+timeFrame1+" - "+endHours2+":"+endMinutes2+" "+timeFrame2+" ]<br>";	
	   document.getElementById("fourth").style.display='inline';
	    document.getElementById("fourth").checked = true;
	    flag=3;
	  //  alert("Inside the status 202 0r 200");
	    
	
	}
	else if( merged_status == '200')
	{
		document.getElementById("busyFree").innerHTML = "Status :<br>"
		 var oldhtml = document.getElementById("busyFree").innerHTML ;
		 var newhtml = oldhtml+"<p style='color:red'>Currently Busy.. But..</p><br>"+"<br> Suggestions :";
		 document.getElementById("busyFree").innerHTML =  newhtml; 
		// starttime + half an hour and endtime is starttime+hour
		
		 document.getElementById("fourth_text").innerHTML = "30 minutes "+"[ "+endHours1+":"+endMinutes1+" "+timeFrame1+" - "+endHours2+":"+endMinutes2+" "+timeFrame2+" ]";	
	     document.getElementById("fourth").style.display='inline';
	    document.getElementById("fourth").checked = true;
	    
	     document.getElementById("fifth_text").innerHTML = "60 minutes "+"[ "+endHours1+":"+endMinutes1+" "+timeFrame1+" - "+endHours3+":"+endMinutes3+" "+timeFrame3+" ]<br>";	
	     document.getElementById("fifth").style.display='inline';
	    
	    
	    flag=3;
	  //  alert("Inside the status 202 0r 200");
	    
	
	}
	else if(merged_status == '220')
	{
	 var newhtml = "Status :<br>"+"<p style='color:#B80000 '>Busy for next 60 minutes</p><br>"
	 document.getElementById("busyFree").innerHTML = newhtml;
	 
	  flag=2;
	  document.getElementById("loadText").innerHTML = "Loading suggestions..";
	   
	 // alert("Inside the status 220");
	  fetchSuggestions();
	  
	
	}
	else if(merged_status == '222')
	{
	   var newhtml = "Status :<br>"+"<p style='color:#B80000 '>Busy for next 90 minutes</p><br>"
	   document.getElementById("busyFree").innerHTML = newhtml;
	   document.getElementById("loadText").innerHTML = "Loading suggestions..";
	   flag=2;
	  // alert("Inside the status 222");
		fetchSuggestions();	
	}
	else 
	{
	   document.getElementById("busyFree").innerHTML = "Unable to fetch the availability status";
	   flag=2;
	}
	
	
	console.log("the variable stored is..."+st+"name is"+name_val+"flag is.."+flag);
	if(flag == 1 || flag == 3 ) {
		
		document.getElementById("btn_large").style.display='inline';
		 document.getElementById("btn_back").style.display='inline';
		 document.getElementById('myCanvas').style.display='none';
		 document.getElementById('load').style.display='none';
		// hideLoading();
	}
	
	else if(flag==2)
	{
	   document.getElementById("btn_back").innerHTML = 'Back';
	   document.getElementById("btn_back").style.display='block';
	   //hideLoading();
	   document.getElementById('myCanvas').style.display='none';
		document.getElementById('load').style.display='none'; 
	   
	}
	//fetchSuggestions();
			
	},function(e){
	//hideLoading(); 
	document.getElementById('myCanvas').style.display='none';
	document.getElementById('load').style.display='none'; 
	console.log("Inside status callback" + e);
	if(e.status == null) {
	 navigator.notification.alert(
            'Lost connection to the server',  // message
            alertDismissed1,         // callback
            'OOPS!!!',            // title
            'Bye Bye'                  // buttonName
        );
	
	navigator.app.loadUrl("file:///android_asset/www/login.html");
	}
	else  if(e.status == 'Invalid room number')
	{
			 navigator.notification.alert(
		      e.status,  // message
            alertDismissed,         // callback
            'OOPS!!!',            // title
            'Try Again'                  // buttonName
        );
	
	 navigator.app.loadUrl("file:///android_asset/www/index.html");
		
	}
	else
	{
	 //alert("Unsuccessful");
	  navigator.notification.alert(
            e.status,  // message
            alertDismissed,         // callback
            'OOPS!!!',            // title
            'Try Again'                  // buttonName
        );
	
	 navigator.app.loadUrl("file:///android_asset/www/login.html");
	 }
	});
  	
  	
  }
  
  function alertDismissed() {
        // do something
    }
   function alertDismissed1() {
   		navigator.app.exitApp();
       // navigator.app.loadUrl("file:///android_asset/www/login.html");
    }
  function book()
  {
  	endtime = "s0";
	if(document.getElementById("first").checked)
	endtime = "s1";
	else if(document.getElementById("second").checked)
	endtime = "s2";
	else if(document.getElementById("third").checked)
	endtime = "s3";
	else if(document.getElementById("fourth").checked)
	endtime = "s4";
	else if(document.getElementById("fifth").checked)
	endtime = "s5";
  	if(endtime == 's0') {
  	alert("Please select some time slot before you book");
  	}
  	else
  	{
  	//alert("your time slot selected is.."+endtime);
  	navigator.notification.activityStart("Booking...");
  	window.plugins.Meeting.schedule(name_val,starttime,endtime,attendees,
  	function(success) {
  	navigator.notification.activityStop();
  	navigator.notification.alert(
            'Conference room booked',  // message
            alertDismissed,         // callback
            'SUCCEESS',            // title
            'OK'                  // buttonName
        );
  	 
  	navigator.app.loadUrl("file:///android_asset/www/index.html");
  	},function(fail) {
  	//console.log("error in booking the room");
  	navigator.notification.alert(
            'Booking failed..',  // message
            alertDismissed,         // callback
            'OOPS!!!',            // title
            'Try Again'                  // buttonName
        );
  	 navigator.notification.activityStop();
  	navigator.app.loadUrl("file:///android_asset/www/index.html");
  	});
  	}
  }
  
        
  function status(result) {
  res= "cr-NB-1.1.325@netapp.com";
  navigator.app.loadUrl("file:///android_asset/www/roomStatus.html?name="+result);
  
  name_val = getUrlVars()["name"];
  console.log("the name_val is.."+name_val);
 }
    
    
  
 
    