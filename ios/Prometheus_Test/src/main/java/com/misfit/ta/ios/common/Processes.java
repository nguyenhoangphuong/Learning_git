package com.misfit.ta.ios.common;

public class Processes {

    // private static final Logger logger = Util.setupLogger(Processes.class);
    // // private static App app = null;
    //
    // public static void startSimulator() {
    // // logger.debug("Will start iOS Simulator");
    // //
    // // String family = Settings.getTarget();
    // // family = family.replace("Sim", "");
    // // String[] cmd = {"defaults", "write", "com.apple.iphonesimulator",
    // "SimulateDevice", family};
    // // runCmd(cmd);
    // //
    // // app = new App(Paths.getSimulatorApp());
    // // app.open();
    // }
    //
    // public static void quitSimulator() {
    // // logger.debug("Will quit iPhone Simulator");
    // // app.close();
    // }
    //
    // public static void killSimulator() {
    // if (ProcessFinder.isProcessRunning("iPhoneSimulator")) {
    // ProcessFinder.kill("iPhoneSimulator");
    // }
    // }
    //
    // public static void startSpotify() {
    // logger.debug("Will start the Spotify client");
    // String target = Settings.getTarget();
    // String appPath = Settings.getAppPath();
    // int noOfTries = 0;
    // boolean connected = false;
    // while (!connected && noOfTries < 10) {
    // logger.debug("Startup attempt: " + noOfTries + 1);
    // if (target.contains("Sim")) {
    // String clientLog = "logs/client.log";
    // String family = target.contains("iPhone") ? "iphone" : "ipad";
    // String[] cmd = {"/usr/local/bin/ios-sim", "launch", appPath, "--family",
    // family, "--stderr", clientLog, "--exit"};
    // runCmd(cmd);
    // ShortcutsTyper.delayTime(2000);
    // } else {
    // String[] cmd =
    // {"/Applications/Xcode.app/Contents/Developer/usr/bin/instruments", "-w",
    // Settings.getDeviceId(), "-t",
    // "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate",
    // appPath};
    // runCmd(cmd);
    // ShortcutsTyper.delayTime(10000);
    // }
    // connected = NuRemoteClient.init();
    // noOfTries++;
    // }
    // TRS trs = TRS.getInstance();
    // trs.addTestData("iOS init", "noOfTries = " + noOfTries);
    // }
    //
    // public static void killSpotifyOnDevice() {
    // String deviceId = Settings.getDeviceId();
    // ProcessFinder.kill(deviceId);
    // killHard(deviceId);
    // }
    //
    // private static void killHard(String process) {
    // String str = "kill -9";
    // List<String> processes = listRunningProcesses("ps -ax");
    // Iterator<String> it = processes.iterator();
    // while (it.hasNext()) {
    // String result = it.next();
    // if (result.contains(process)) {
    // String[] line = result.split(" ");
    // int i = 0;
    // while (line[i].isEmpty()) {
    // i++;
    // }
    // str = str.concat(" " + line[i]);
    // }
    // }
    // runCmd(str.split(" "));
    // ShortcutsTyper.delayTime(5000);
    // }
    //
    // private static List<String> listRunningProcesses(String cmd) {
    // List<String> processes = new ArrayList<String>();
    // try {
    // String line;
    // Process proc = Runtime.getRuntime().exec(cmd);
    // BufferedReader input = new BufferedReader(new
    // InputStreamReader(proc.getInputStream()));
    // while ((line = input.readLine()) != null) {
    // logger.debug(line);
    // if (!line.trim().equals("")) {
    // if (line.indexOf("\"") != -1) {
    // // keep only the process name
    // line = line.substring(1);
    // processes.add(line.substring(0, line.indexOf("\"")));
    // } else {
    // processes.add(line);
    // }
    // System.out.println(line);
    // }
    // }
    // input.close();
    // } catch (Exception err) {
    // err.printStackTrace();
    // }
    // return processes;
    // }
    //
    //
    // private static void runCmd(String[] cmd) {
    // try {
    // String line;
    // Process proc = Runtime.getRuntime().exec(cmd);
    // BufferedReader input = new BufferedReader(new
    // InputStreamReader(proc.getInputStream()));
    // if (input.ready()) {
    // while ((line = input.readLine()) != null) {
    // logger.debug(line);
    // }
    // }
    // input.close();
    // } catch (Exception err) {
    // err.printStackTrace();
    // }
    // }
}
