package com.misfit.ta.backend.aut.performance;

public class BackendDatabaseSeedLaunching {

    public static void main(String[] args) {
        BackendDatabaseSeeding test = new BackendDatabaseSeeding();
        long now = System.currentTimeMillis();
        // first registrations
        test.setParameters(1000, 200, 1, 1);
        test.signupOneMillionUsers(true);
        
        System.out.println("LOG [BackendDatabaseSeedLaunching.main]:  time ran" + (System.currentTimeMillis() - now) / 1000);
        
//        // moarrr traffic
//        test.setParameters(10000, 1000, 1, 1);
//        test.signupOneMillionUsers(true);
//        
//     // slow down traffic
//        test.setParameters(1000, 200, 1, 1);
//        test.signupOneMillionUsers(true);

    }

}
