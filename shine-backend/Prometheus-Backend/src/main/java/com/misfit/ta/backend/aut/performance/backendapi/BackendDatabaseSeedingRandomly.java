package com.misfit.ta.backend.aut.performance.backendapi;


public class BackendDatabaseSeedingRandomly {

    public static void main(String[] args) {
        BackendDatabaseSeeding test = new BackendDatabaseSeeding();
        test.signupOneMillionUsers(true);
    }

}
