package com.misfit.ta.android.aut;

import java.io.IOException;

import org.graphwalker.conditions.EdgeCoverage;
import org.graphwalker.exceptions.StopConditionException;
import org.graphwalker.generators.NonOptimizedShortestPath;
import org.graphwalker.multipleModels.ModelHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.android.modelapi.signin.InvalidSignInAPI;
import com.misfit.ta.android.modelapi.signin.SuccessfulSignInAPI;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class SigninTest extends AutomationTest {

	@Test(groups = { "Android", "Prometheus", "signin", "invalidSignIn" })
    public void invalidSignInTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("invalidSignin", new InvalidSignInAPI(this, Files.getFile("model/signin/InvalidSignIn.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("invalidSignin");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    
    @Test(groups = { "Android", "Prometheus", "signin", "succesfulSignIn" })
    public void successfulSignInTest() throws InterruptedException, StopConditionException, IOException {
        ModelHandler model = getModelhandler();
        model.add("successfulSignin", new SuccessfulSignInAPI(this, Files.getFile("model/signin/SuccessfulSignIn.graphml"), true, new NonOptimizedShortestPath(new EdgeCoverage(1.0)), false));
        model.execute("successfulSignin");
        Assert.assertTrue(getModelhandler().isAllModelsDone(), "Not all models are done");
        String actualResult = getModelhandler().getStatistics();
        System.out.println(actualResult);
    }
    

}