package com.misfit.ta.backend.aut.performance;

public class BackendDatabaseSeedLaunching {

    public static void main(String[] args) {
        BackendDatabaseSeeding test = new BackendDatabaseSeeding();
        
        // first registrations
        test.setParameters(1000, 200, 1, 1);
        long now = System.currentTimeMillis();
        test.signupOneMillionUsers(true);
        
        System.out.println("LOG [BackendDatabaseSeedLaunching.main]:  time ran (s) " + (System.currentTimeMillis() - now) / 1000);
        pause(120000);

        
        // more traffic
        test.setParameters(2000, 500, 1, 3);
        now = System.currentTimeMillis();
        test.signupOneMillionUsers(false);
        System.out.println("LOG [BackendDatabaseSeedLaunching.main]:  more traffic time ran (s): " + (System.currentTimeMillis() - now) / 1000);
        pause(120000);
        
        // slow down traffic
        test.setParameters(1000, 200, 1, 1);
        now = System.currentTimeMillis();
        test.signupOneMillionUsers(true);
        System.out.println("LOG [BackendDatabaseSeedLaunching.main]:  slow down traffic time ran (s): " + (System.currentTimeMillis() - now) / 1000);

        
    }
    
    private static void pause(long millis) {
    	try {
    		System.out.println("----------- SLEEPING !!! GO AWAY !!! -----------");
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			System.out.println("Oooops something went wrong when i was sleeping!!!!");
		}
    }

}
