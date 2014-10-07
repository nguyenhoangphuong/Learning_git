package com.misfit.ta.backend.aut.performance;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.misfit.ta.Settings;

public class MassSignIn {

    protected int NUMBER_OF_ITEMS_PER_DAY = 1;
    protected int NUMBER_OF_DAYS = 1;
    protected int NUMBER_OF_USERS = 20;
    protected int NUMBER_OF_THREADS = 2;
    
    public MassSignIn() {
    }

    public void doSignIn() {
        int userCount = 0;
        

        NUMBER_OF_ITEMS_PER_DAY = Settings.getInt("NUMBER_OF_ITEMS_PER_DAY");
        NUMBER_OF_DAYS = Settings.getInt("NUMBER_OF_DAYS");
        NUMBER_OF_USERS = Settings.getInt("NUMBER_OF_USERS");
        NUMBER_OF_THREADS = Settings.getInt("NUMBER_OF_THREADS");

        Collection<Future<?>> futures = new LinkedList<Future<?>>();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        while (userCount < NUMBER_OF_USERS) {
            
            for (int threads = 0; threads < Math.min(NUMBER_OF_THREADS, NUMBER_OF_USERS - userCount); threads++) {
                MassSignInThread test = new MassSignInThread();
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
        MassSignIn data = new MassSignIn();
        data.doSignIn();
    }
}
