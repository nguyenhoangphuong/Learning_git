package com.misfit.ta.backend.aut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultLogger {

    private static ResultLogger logger;
    private static File file;
    private static FileWriter writer;

    public static ResultLogger getLogger(String testname) {
        if (logger == null) {
            logger = new ResultLogger(testname);
        }

        return logger;
    }

    protected ResultLogger(String testname) {
        file = new File("logs/result_" + testname + ".log");
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
        }
    }
    
    public void log(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
        }
        
    }
    
    public static void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

}
