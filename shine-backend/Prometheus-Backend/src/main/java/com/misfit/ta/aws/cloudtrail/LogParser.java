package com.misfit.ta.aws.cloudtrail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.misfit.ta.aws.cloudtrail.Records;
import com.misfit.ta.aws.cloudtrail.filterset.Item1;
import com.misfit.ta.backend.aut.ResultLogger;

public class LogParser {

    Records records;
    Map users = new HashMap<String, Long>();
   
    private static ResultLogger logger;
    private static int count = 0;

    public static void main(String[] args) {
        
        LogParser test = new LogParser();
        test.runTest();

    }
    
    private void runTest() {
        
        records = new Records();
        
        logger = ResultLogger.getLogger("Analysis_Terminate_" + new Date(System.currentTimeMillis()) + ".cvs");
        logger.log("Count\t" + "Accesskey\t" + "Username\t" + "Time\t" + "Name\t" + "sourceIP\t" + "instanceId\t"

        );
        
//        String path = "/Users/thinh/Downloads/logs/04/25";
        String path = "/Users/thinh/Downloads/logs/us-east-1/2014/04/11";
        ArrayList<File> files  = new ArrayList<File>();
        listFiles(path, files);
        System.out.println("LOG [Test.runTest]: total files: " + files.size());
        
        for (File file: files) {
            if (file == null || !file.getName().endsWith("json")) {
                continue;
            }
            try {
                System.out.println("LOG [Test.runTest]: processing file: " + file);
                readLogRecords(file);
//                findDataDb();
//                findXlargeIncident();
//                checkPeopleIPs();
                findTerminalOperation();
                

                Object[] keys = users.keySet().toArray();
                
                
                for (Object key : keys) {
                    System.out.println("LOG [Test.runTest]: " + (String) key + "\t"+ users.get(key));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        logger.log("\n\n\nSummary\nAccount\tAccess times]\tPercentage");
        Object[] keys = users.keySet().toArray();
        long sum  =0;
        for (Object key : keys) {
            sum += ((Long)users.get(key)).longValue();
        }
        
        for (Object key : keys) {
            logger.log((String) key + "\t"+ users.get(key) + "\t" + ((Long)users.get(key)*100/sum) );
        }

        
    }

    private void readLogRecords(File file) throws IOException {
        byte[] encoded = Files.toByteArray(file);
        records = new Gson().fromJson(new String(encoded), Records.class);
    }

    private void findDataDb() {
        //i-51a09133
        for (Record record : records.getRecords()) {
            try {
                List<Item> items = record.getRequestParameters().getInstancesSet().getItems();
                for (Item item: items) {
                    
                    String result = match(item.getInstanceId());
                    if (result != null) {
                        logInstance(record, result);
                    }
                }
            }
            catch (Exception e) {
                
            }
            try {
                for (Item item : record.getRequestParameters().getFilterSet().getItems()) {
                    if (item.getName().equals("resource-id")) {
                        for (Item1 item1: item.getValueSet().getItems() ) {
                            String result = match(item1.getValue());
                            if (result != null) {
                                logInstance(record, result);
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                
            }
            
        }
    }
    
    private void checkPeopleIPs() {
        for (Record record : records.getRecords()) {
            
            String username = record.getUserIdentity().getUserName();
            if (username == null) {
                continue;
            }
            if (username.contains("hoan") || username.contains("thinh") || username.contains("tung")
                    || username.contains("quan") || username.contains("chan") || username.equals("winston")
                    || username.equals("binh") || username.equals("viet")) {
                logger.log(record.getEventTime() + "\t" + record.getEventName()+"\t"+username + "\t" + record.getSourceIPAddress());
            }
        }
    }
    
    private void findXlargeIncident() {
        //i-51a09133
        for (Record record : records.getRecords()) {
            try {
                if (record.getRequestParameters().getLaunchSpecification().getInstanceType().contains("xlarge")) {
                    logger.log(count + "\t" + record.getUserIdentity().getAccessKeyId() + "\t"
                            + record.getUserIdentity().getUserName() + "\t" + record.getEventTime() + "\t" + record.getEventName()
                            + "\t" + record.getSourceIPAddress() + "\t" + record.getRequestParameters().getLaunchSpecification().getInstanceType()
                            + "\t" + record.getRequestParameters().getInstanceCount());
                }
            }
            catch (Exception e) {
                
            }
        }
    }
    
    private void findTerminalOperation() {
        for (Record record: records.getRecords()) {
            if (record.getEventName().equals("TerminateInstances")) {
                logger.log(record.getUserIdentity().getAccessKeyId() + "\t"
                        + record.getUserIdentity().getUserName() + "\t" + record.getEventTime() + "\t" + record.getEventName()
                        + "\t" + record.getSourceIPAddress() + "\t" + record.getRequestParameters().getInstancesSet().getItems().get(0).getInstanceId());
            }
        }
    }

    private void logInstance(Record record, String instanceId) {
        Long count = (Long) users.get(record.getUserIdentity().getUserName());
        if ( count == null) {
            count = new Long(0);
        }
        users.put(record.getUserIdentity().getUserName(), new Long(count+1));
        logger.log(count + "\t" + record.getUserIdentity().getAccessKeyId() + "\t"
                + record.getUserIdentity().getUserName() + "\t" + record.getEventTime() + "\t" + record.getEventName()
                + "\t" + record.getSourceIPAddress() + "\t" + instanceId);
    }
    
    public void listFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }
    }
    
    private static String[] instances = {"i-51a09133",
        "i-5de63232", 
        "i-d28cbdb9",
        "snap-59342386",
        "snap-7ecfe0a1",
        "snap-51c0938e",
        "snap-2ad097f5",
        "snap-f1bed42e",
        "snap-d5059b0a",
        "snap-e76fe238",
        "snap-4b59e894",
        "snap-379b3fe8",
        "snap-f9955d26",
        "snap-7821dda7",
        "snap-2e6787f1",
        "snap-e4051238",
        "snap-f5fac129",
        "snap-a8775974",
        "snap-125a08ce",
        "snap-884f0e54",
        "snap-34f481e8",
        "snap-0320b8df",
        "snap-329e11ee",
        "snap-a0eb597c",
        "snap-563a9d8a",
        "snap-5a04ce86",
        "snap-0a33ccd6",
        "snap-d4d63508",
        "snap-97d5c14a",
        "snap-c7271f1a",
        "snap-9e406d43",
        "snap-e77f2e3a",
        "snap-2fcbb1f2",
        "snap-f53b5228",
        "snap-06fc6edb",
        "snap-20c95bfd",
        "snap-7c9d1ba1",
        "snap-068229db",
        "snap-35b41fe8",
        "snap-1515cac8",
        "snap-82b9795f",
        "snap-1dde2ac0",
        "snap-afdbc275",
        "snap-052138df",
        "snap-5cd9d486",
        "snap-8bd4e251",
        "snap-48fda792",
        "snap-37723ced",
        "snap-2efa88f4",
        "snap-32ddb9e8",
        "snap-6fc0a4b5",
        "snap-bfea6265",
        "snap-43e75499",
        
        

        
        
    
    };
    
    public String match(String keyword) {
       for (String str: instances) {
           if (str.equalsIgnoreCase(keyword)) {
               return str;
           }
       }
       return null;
    }

}
