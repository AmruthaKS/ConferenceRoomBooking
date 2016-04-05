var Authentication = function() {
};
Authentication.prototype.login = function(uname,pword,successCallback,failureCallback) {
 return PhoneGap.exec(    successCallback,    //Success callback from the plugin
      failureCallback,     //Error callback from the plugin
      'AuthenticationPlugin',  //Tell PhoneGap to run "BusyFreeStatusPlugin" Plugin
      'login',              //Tell plugin, which action we want to perform
      [uname,pword]);        //Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
                   PhoneGap.addPlugin("Authentication", new Authentication());
               });