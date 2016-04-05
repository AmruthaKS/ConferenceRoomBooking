var Meeting = function() {
};

Meeting.prototype.schedule = function(room,starttime,endtime,attendees,successCallback,failureCallback) {
 return PhoneGap.exec(    successCallback,    //Success callback from the plugin
      failureCallback,     //Error callback from the plugin
      'MeetingPlugin',  //Tell PhoneGap to run "BusyFreeStatusPlugin" Plugin
      'schedule',              //Tell plugin, which action we want to perform
      [room,starttime,endtime,attendees]);        //Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
                   PhoneGap.addPlugin("Meeting", new Meeting());
               });