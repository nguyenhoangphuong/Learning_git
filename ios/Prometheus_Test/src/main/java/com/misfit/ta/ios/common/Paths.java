package com.misfit.ta.ios.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

public class Paths {
    private static final Logger logger = Util.setupLogger(Paths.class);

    private static final String currentUser = System.getProperty("user.name");
    private static String path;

    static {

        String searchPath = "/Users/" + currentUser + "/Library/Application Support/iPhone Simulator/";
        String cmd[] = { "find", searchPath, "-name", "Spotify*.app" };
        try {
            String line;
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            // make sure latest and greatest version is tested!
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    path = line.replaceAll("Spotify.*?.app", "Library/");
                }
            }
            Assert.assertTrue(path != null, "Path to Spotify folder has not been defined!");
            logger.debug("Path to cache: " + path);
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    public static String getSimulatorCrashLogFolder() {
        return "/Users/" + currentUser + "/Library/Logs/DiagnosticReports";
    }

    static String getSpotifyAppSupportFolder() {
        return path + "Application Support";
    }

    static String getSpotifyPreferencesFolder() {
        return path + "Preferences";
    }

    static String getSpotifyCacheFolder() {
        return path + "Caches";
    }

    static String getSimulatorApp() {
        return "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/Applications/iPhone Simulator.app";
    }

    public static String getSpotifyApp() {
        return "/Users/" + currentUser
                + "/Library/Application Support/iPhone Simulator/5.1/Applications/Release-iphonesimulator/Spotify.app";
    }
}
