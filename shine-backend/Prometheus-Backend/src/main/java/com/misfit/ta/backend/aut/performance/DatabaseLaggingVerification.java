package com.misfit.ta.backend.aut.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.misfit.ta.Settings;
import com.misfit.ta.utils.Files;

public class DatabaseLaggingVerification {

    protected int NUMBER_OF_ITEMS_PER_DAY = 1;
    protected int NUMBER_OF_DAYS = 1;
    protected int NUMBER_OF_USERS = 20;
    protected int NUMBER_OF_THREADS = 2;
    String FILE = "user_token.csv";
    ArrayList<UserToken> users = new ArrayList<UserToken>();

    public DatabaseLaggingVerification() {

        try {
            File file = Files.getFile(FILE);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                if (items.size() > 0) {
                    UserToken user = new UserToken();
                    user.setId(items.get(0));
                    user.setToken(items.get(1));
                    users.add(user);
                }
            }
            br.close();
            Collections.reverse(users);
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("LOG [DatabaseLaggingVerification.DatabaseLaggingVerification]: number of Users: " + users.size());
    }

    public void doLongQuery() {
//
//        Random rand = new Random(users.size());
//        long boundary = users.size() - 0 + 1 + 0;
//        rand = new Random();
        
        
      
        int userCount = 1;
        
        Random rand = new Random();
        
        

        NUMBER_OF_ITEMS_PER_DAY = Settings.getInt("NUMBER_OF_ITEMS_PER_DAY");
        NUMBER_OF_DAYS = Settings.getInt("NUMBER_OF_DAYS");
        NUMBER_OF_USERS = Settings.getInt("NUMBER_OF_USERS");
        NUMBER_OF_THREADS = Settings.getInt("NUMBER_OF_THREADS");

        Collection<Future<?>> futures = new LinkedList<Future<?>>();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        while (userCount < NUMBER_OF_USERS) {
            
            for (int threads = 0; threads < Math.min(NUMBER_OF_THREADS, NUMBER_OF_USERS - userCount); threads++) {
                int randomNum = rand.nextInt((users.size() - 1) + 1) + 1;
                UserToken userToken = users.get(randomNum);
                DatabaseLaggingVerificationThread test = new DatabaseLaggingVerificationThread(userToken);
                futures.add(executor.submit(test));
                userCount++;
               
            }

        }
        executor.shutdown();

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        DatabaseLaggingVerification data = new DatabaseLaggingVerification();
        data.doLongQuery();
    }
}
