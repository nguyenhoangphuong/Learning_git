
var target = UIATarget.localTarget();
var host = UIATarget.localTarget().host();

//-------------------variable ----------------------

var path = "/Users/MISFIT/Documents/integrated_SOAPUI/"; //you need config it to your root project


var scriptPath = "/bin/sh";

var args = [path+"runnerSOAPUI.sh","Flow_Control", "All_Card_SLL", path+"project/MFOTD-soapui-project.xml"];

var timeout = 50; // seconds
//--------------------------------------



//-------------------execute------------------------
var result = host.performTaskWithPathArgumentsTimeout(scriptPath,args,timeout);

//----------------------------



//-------------------log----------------------

UIALogger.logDebug("exitCode: " + result.exitCode);

UIALogger.logDebug("stdout: " + result.stdout);

UIALogger.logDebug("stderr: " + result.stderr); 
//-----------------------------------------------