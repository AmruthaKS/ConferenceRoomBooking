var BusyFreeStatus = function() {
};

BusyFreeStatus.prototype.show = function(room,successCallback, failureCallback) {
 return PhoneGap.exec(    successCallback,    //Success callback from the plugin
      failureCallback,     //Error callback from the plugin
      'BusyFreeStatusPlugin',  //Tell PhoneGap to run "BusyFreeStatusPlugin" Plugin
      'show',              //Tell plugin, which action we want to perform
      [room]);        //Passing list of args to the plugin
};
BusyFreeStatus.prototype.suggest = function(rooms,successCallback, failureCallback) {
 return PhoneGap.exec(    successCallback,    //Success callback from the plugin
      failureCallback,     //Error callback from the plugin
      'BusyFreeStatusPlugin',  //Tell PhoneGap to run "BusyFreeStatusPlugin" Plugin
      'suggest',              //Tell plugin, which action we want to perform
      [rooms]);        //Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
                   PhoneGap.addPlugin("BusyFreeStatus", new BusyFreeStatus());
               });